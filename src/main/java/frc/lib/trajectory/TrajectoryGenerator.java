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
import frc.lib.physics.DifferentialDrive.ChassisState;
import frc.lib.trajectory.constraints.IConstraints;
import frc.lib.trajectory.constraints.IConstraints.MinMax;

/**
 * Add your docs here.
 */
public class TrajectoryGenerator {

    private DistanceView disView;
    private List<IConstraints<PoseWithCurvature>> constraints;
    private double maxSpeed;
    private double maxAccel;
    private double startSpeed;
    private double endSpeed;
    private double dl;

    public TrajectoryGenerator(){

    }

    public TrajectoryGenerator(
            DistanceView disView,
            List<IConstraints<PoseWithCurvature>> constraints,
            double maxSpeed,
            double maxAccel,
            double startSpeed,
            double endSpeed,
            double dl){
        this.disView = disView;
        this.constraints = constraints;
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;
        this.startSpeed = startSpeed;
        this.endSpeed = endSpeed;
        this.dl = dl;
    }

    public List<StampedState<PoseWithCurvature>> generate(){
        //calc the number of states needed given dx

        int num =  (int)Math.ceil(disView.getLastPoint() / dl) + 1;
        System.out.println(num);
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

            System.out.println(constrainedState.velocityLimit);
            
            predecessor = constrainedState;
        }


        return null;
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
