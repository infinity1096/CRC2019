/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.trajectory;

import java.util.ArrayList;
import java.util.List;


import frc.lib.Util;
import frc.lib.geometry.PoseWithCurvature;
import frc.lib.geometry.StampedState;
import frc.lib.physics.DifferentialDrive;
import frc.lib.physics.DifferentialDrive.ChassisState;
import frc.lib.physics.DifferentialDrive.WheelState;
import frc.lib.trajectory.constraints.IConstraints;
import frc.lib.trajectory.constraints.IConstraints.MinMax;

/**
 * Add your docs here.
 */
public class TrajectoryGenerator {

    private DistanceView disView;
    private List<IConstraints<PoseWithCurvature>> constraints;
    private List<StampedState<PoseWithCurvature>> generatedList_;
    private double[][] leftList;
    private double[][] rightList;
    private DifferentialDrive drive;
    private double maxSpeed;
    private double maxAccel;
    private double startSpeed;
    private double endSpeed;
    private double dl;
    private int num;

    public TrajectoryGenerator(){

    }

    public TrajectoryGenerator(
            DistanceView disView,
            List<IConstraints<PoseWithCurvature>> constraints,
            DifferentialDrive drive,
            double maxSpeed,
            double maxAccel,
            double startSpeed,
            double endSpeed,
            double dl){
        this.disView = disView;
        this.constraints = constraints;
        this.drive = drive;
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;
        this.startSpeed = startSpeed;
        this.endSpeed = endSpeed;
        this.dl = dl;
    }

    public List<StampedState<PoseWithCurvature>> generate(){
        //calc the number of states needed given dx

        int num =  (int)Math.ceil(disView.getLastPoint() / dl) + 1;
        this.num = num;
        List<PoseWithCurvature> states = new ArrayList<PoseWithCurvature>(num);
        for (int i = 0; i < num; i++){
            states.add(disView.sample(Math.min(i * dl,disView.getLastPoint())));
        }
        List<ConstrainedState> constrainedStates = new ArrayList<ConstrainedState>(num);

        //begin FORWARD pass

        //initialize "predecessor"
        ConstrainedState predecessor = new ConstrainedState();
        predecessor.state = states.get(0);
        predecessor.distance = 0.0;
        predecessor.velocityLimit = new MinMax(-startSpeed,startSpeed);
        predecessor.accelLimit = new MinMax(-maxAccel,maxAccel);

        for (int i = 0; i < num; ++i){
            //create a new "constrainted state" in the list
            constrainedStates.add(new ConstrainedState());
            ConstrainedState constrainedState = constrainedStates.get(i); // passed by refrences
            //update state and distance
            constrainedState.state = states.get(i);
            double ds = constrainedState.state.distance(predecessor.state);
            constrainedState.distance = predecessor.distance + ds;

            while(true){

            //enforce golbal velocity constraint
            constrainedState.velocityLimit = constrainedState.velocityLimit.intersect(
                    new MinMax(-maxSpeed,maxSpeed));
            //enforce max reachable velocity constraint
            // v[t+1]^2 = v[t]^2 + 2*a[t]*dx
            constrainedState.velocityLimit = constrainedState.velocityLimit.intersect(
                new MinMax(-Math.sqrt(predecessor.velocityLimit.max * predecessor.velocityLimit.max +
                2 *predecessor.accelLimit.max * ds),
                        Math.sqrt(predecessor.velocityLimit.max * predecessor.velocityLimit.max +
                                2 *predecessor.accelLimit.max * ds)));
                
            //enforce global acceleration constraint
            constrainedState.accelLimit = constrainedState.accelLimit.intersect(new MinMax(-maxAccel,maxAccel));

            //enforce all velocity constraints in "constraints"
            for (IConstraints<PoseWithCurvature> constraint : constraints){
                constrainedState.velocityLimit = 
                constrainedState.velocityLimit.intersect(constraint.getMaxSpeed(constrainedState.state));
            }

            //enforce all acceleration constraints in "constraints"
            for (IConstraints<PoseWithCurvature> constraint : constraints){
                ChassisState chassisState = new ChassisState();
                chassisState.linear = constrainedState.velocityLimit.max;
                chassisState.angular = constrainedState.velocityLimit.max * predecessor.state.curvature();

                constrainedState.accelLimit = 
                constrainedState.accelLimit.intersect(constraint.getMaxAcceleration(
                        chassisState, 
                        predecessor.state));
            }

            if (ds < Util.kEpsilon) {
                break;
            }

            final double actual_acceleration = (constrainedState.velocityLimit.max * constrainedState.velocityLimit.max
                    - predecessor.velocityLimit.max * predecessor.velocityLimit.max) / (2.0 * ds);

            if (constrainedState.accelLimit.max < actual_acceleration - Util.kEpsilon) {
                predecessor.accelLimit.max  = constrainedState.accelLimit.max;
            } else {
                if (actual_acceleration > predecessor.accelLimit.min + Util.kEpsilon) {
                    predecessor.accelLimit.max = actual_acceleration;
                }
                // If actual acceleration is less than predecessor min accel, we will repair during the backward
                // pass.
                break;
            }
            }
            predecessor = constrainedState;
        }


        
        //Backward Pass
        //initialize "successor"
        ConstrainedState successor = constrainedStates.get(num - 1);
        successor.velocityLimit = new MinMax(-endSpeed- Util.kEpsilon,endSpeed);
        successor.accelLimit = new MinMax(-maxAccel,maxAccel);

        for (int i = num-1; i >=0; --i){
            ConstrainedState constrainedState = constrainedStates.get(i);
            double ds = constrainedState.distance - successor.distance;
            while(true){
            //enforce golbal velocity constraint
            constrainedState.velocityLimit = constrainedState.velocityLimit.intersect(
                    new MinMax(-maxSpeed,maxSpeed)
            );
            
            //enforce max reachable velocity constraint
            // v[t]^2 = v[t+1]^2 - 2*a[t+1]*dx
            double vel = Math.sqrt(successor.velocityLimit.max*successor.velocityLimit.max +
                    2 * successor.accelLimit.min * ds);
            constrainedState.velocityLimit = constrainedState.velocityLimit.intersect(
                    new MinMax(-vel , vel)
            );   

            //enforce global acceleration constraint
            constrainedState.accelLimit = constrainedState.accelLimit.intersect(
                    new MinMax(-maxAccel,maxAccel)   
            );

            //enforce all velocity constraints in "constraints"

            for (IConstraints<PoseWithCurvature> constraint : constraints){
                constrainedState.velocityLimit.intersect(
                    constraint.getMaxSpeed(constrainedState.state));
            }

            //enforce all acceleration constraints in "constraints"
            for (IConstraints<PoseWithCurvature> constraint : constraints){
                ChassisState chassisState = new ChassisState();
                chassisState.linear = constrainedState.velocityLimit.max;
                chassisState.angular = constrainedState.velocityLimit.max * successor.state.curvature();

                constrainedState.accelLimit = 
                constrainedState.accelLimit.intersect(constraint.getMaxAcceleration(
                        chassisState, 
                        successor.state));
            }



            if (Math.abs(ds) < Util.kEpsilon) {
                break;
            }

            //find actual acceleration

            double actualAcceleration = -(successor.velocityLimit.max() * successor.velocityLimit.max() - 
            constrainedState.velocityLimit.max() * constrainedState.velocityLimit.max()) / (ds * 2.0);

            if ( constrainedState.accelLimit.min > actualAcceleration + Util.kEpsilon){
                successor.accelLimit.min = constrainedState.accelLimit.min;
            } else{
                successor.accelLimit.min = actualAcceleration;
                break;
            }
        }
        
        successor = constrainedState;
        }
        
        /*
                for (int i = 0; i < num; i++){
            System.out.println(constrainedStates.get(i).velocityLimit);
        }*/
        

        //Integration Step

        List<StampedState<PoseWithCurvature>> generatedPoints = new ArrayList<StampedState<PoseWithCurvature>>(num);
        double t = 0;
        double s = 0;
        double v = startSpeed;

        for (int i = 0; i < num; ++i){
            ConstrainedState constrainedState = constrainedStates.get(i);
            double ds = constrainedState.distance - s;
            double dt = 0d;
            //calculate dt
            if (i > 0){
                double accel = (constrainedState.velocityLimit.max * constrainedState.velocityLimit.max - v * v) / (2.0 * ds);
                if (Math.abs(accel) > Util.kEpsilon){
                    dt = (constrainedState.velocityLimit.max - v) / accel;
                }else if (Math.abs(v) > Util.kEpsilon){
                    dt = ds / v;
                }else{
                    throw new RuntimeException();
                }

            }
            t += dt;
            s = constrainedState.distance;
            v = constrainedState.velocityLimit.max;
            generatedPoints.add(new StampedState<PoseWithCurvature>(constrainedState.state,t,v,s));
        }
        generatedList_ = generatedPoints;
        toWheelSpeed();
        return generatedPoints;
    }

    private void toWheelSpeed(){
        int num = generatedList_.size();
        double t = 0;
        double sl = 0;
        double sr = 0;
        double dt = 0;
        leftList = new double[num][3];
        rightList = new double[num][3];
        for (int i = 0; i < num; i++){
            WheelState WheelVel = drive.solveInverseKinematics(new ChassisState(
                    generatedList_.get(i).v(),generatedList_.get(i).v() * generatedList_.get(i).state.curvature()
            ));

            if (i == num-1){
                dt = dt;
            }else{
                dt = (generatedList_.get(i+1).t() - t) * 1000;
                t = generatedList_.get(i+1).t();
            }

            leftList[i][2] = dt;
            rightList[i][2] = dt;
            leftList[i][1] = WheelVel.left;
            rightList[i][1] = WheelVel.right;
            leftList[i][0] = sl;
            rightList[i][0] = sr;
            sl += WheelVel.left*dt;
            sr += WheelVel.right*dt;
        }
    }

    public double[][] leftlist(){
        return leftList;
    }

    public double[][] rightlist(){
        return rightList;
    }

    public void printLeft(){
        for (int i = 0; i < num; ++i){
            String s = "";
            s += leftList[i][0];
            s += "\t";
            s += leftList[i][1];
            s += "\t";
            s += leftList[i][2];
            System.out.println(s);
        }
    }

    public void printRight(){
        for (int i = 0; i < num; ++i){
            String s = "";
            s += rightList[i][0];
            s += "\t";
            s += rightList[i][1];
            s += "\t";
            s += rightList[i][2];
            System.out.println(s);
        }
    }



    public class ConstrainedState{
        PoseWithCurvature state;
        double distance;
        MinMax velocityLimit;
        MinMax accelLimit;

        protected ConstrainedState(){
            velocityLimit = new MinMax();
            accelLimit = new MinMax();
        }

    }
    
}
