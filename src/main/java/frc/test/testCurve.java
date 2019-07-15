/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.test;

import java.util.ArrayList;
import java.util.List;


import frc.lib.geometry.Pose2d;
import frc.lib.geometry.PoseWithCurvature;
import frc.lib.geometry.Rotation2d;
import frc.lib.geometry.StampedState;
import frc.lib.geometry.Translation2d;
import frc.lib.path.Curve;
import frc.lib.path.Line;
import frc.lib.path.LineCurveGenerator;
import frc.lib.physics.DCMotorTransmission;
import frc.lib.physics.DifferentialDrive;
import frc.lib.trajectory.DistanceView;
import frc.lib.trajectory.Trajectory;
import frc.lib.trajectory.TrajectoryGenerator;
import frc.lib.trajectory.constraints.CentripetalAccelerationConstraint;
import frc.lib.trajectory.constraints.DifferentialDriveDynamicsConstraints;
import frc.lib.trajectory.constraints.IConstraints;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class testCurve {
    
    public static void main(String[] args){
        
    Translation2d translation1 = new Translation2d(0,0);
    Translation2d translation2 = new Translation2d(1,0);
    Translation2d translation3 = new Translation2d(1,1);

    Line line1 = new Line(translation1,translation2);
    Line line2 = new Line(translation2,translation3);
    
    LineCurveGenerator generator = new LineCurveGenerator(new double[][]
            {{0,0},
             {0,4}},0.4);
    generator.generate();

    List<PoseWithCurvature> wayPoints = generator.poseList;
    /*
    for (int i = 0; i < wayPoints.size();++i){
        System.out.println(wayPoints.get(i).toString());
    }
    */
    DCMotorTransmission left,right;
    left = new DCMotorTransmission(RobotMap.DRIVETRAIN_SPEED_PRE_VOLT,
                                    RobotMap.DRIVETRAIN_TORQUE_PER_VOLT,
                                    RobotMap.DRIVETRAIN_FRICTION_VOLT);
    right = new DCMotorTransmission(RobotMap.DRIVETRAIN_SPEED_PRE_VOLT,
                                    RobotMap.DRIVETRAIN_TORQUE_PER_VOLT,
                                    RobotMap.DRIVETRAIN_FRICTION_VOLT);

    
    DifferentialDrive drive = new DifferentialDrive(
            RobotMap.DRIVETRAIN_MASS,
            RobotMap.DRIVETRAIN_MOI,
            RobotMap.DRIVETRAIN_ANGULAR_DRAG,
            RobotMap.DRIVETRAIN_WHEEL_RADIUS,
            RobotMap.DRIVETRAIN_WHEELBASE_RADIUS,left, right);

    List<IConstraints<PoseWithCurvature>> constraints = new ArrayList<IConstraints<PoseWithCurvature>>(2);
    constraints.add(new CentripetalAccelerationConstraint(5));
    constraints.add(new DifferentialDriveDynamicsConstraints(drive, 12));


    TrajectoryGenerator generatorTraj = new TrajectoryGenerator(
            new DistanceView(new Trajectory(wayPoints)),
            constraints,
            drive,
            5,
            5,
            0,
            0,
            0.005);
    

    List<StampedState<PoseWithCurvature>> generatedPoints = generatorTraj.generate();
    generatorTraj.printLeft();
    }
}
