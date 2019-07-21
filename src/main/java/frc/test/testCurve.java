/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.test;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;

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
            {{0,2,4,6},
             {0,2,2,4}},0.4);
    generator.generate();

    List<PoseWithCurvature> wayPoints = generator.poseList;
    /*
    for (int i = 0; i < wayPoints.size();++i){
        System.out.println(wayPoints.get(i).toString());
    }
    */
    DCMotorTransmission left,right;
    left = new DCMotorTransmission(2.4, 24.7844, 1.055);
    right = new DCMotorTransmission(2.4, 24.7844, 1.055);

    DifferentialDrive drive = new DifferentialDrive(60,10,12,0.3,0.90,left, right);

    List<IConstraints<PoseWithCurvature>> constraints = new ArrayList<IConstraints<PoseWithCurvature>>(2);
    constraints.add(new CentripetalAccelerationConstraint(5));
    constraints.add(new DifferentialDriveDynamicsConstraints(drive, 12));


    TrajectoryGenerator generatorTraj = new TrajectoryGenerator(
            new DistanceView(new Trajectory(wayPoints)),
            constraints,
            drive,
            5,
            20,
            0,
            0,
            0.005);
    /*
    BufferedTrajectoryPointStream leftTraj = new BufferedTrajectoryPointStream();
    BufferedTrajectoryPointStream rightTraj = new BufferedTrajectoryPointStream();
    

    leftTraj.Write(generatorTraj.getLeftTrajPts());
    rightTraj.Write(generatorTraj.getRightTrajPts());
    
    System.out.println(leftTraj.toString());
    */
    

    List<StampedState<PoseWithCurvature>> generatedPoints = generatorTraj.generate();
    
    /*
    for (int i = 0; i < generatedPoints.size();i++){
        System.out.println(generatedPoints.get(i));
    }*/

    }
}
