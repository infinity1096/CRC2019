/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.test;

import java.util.List;

import frc.lib.geometry.Pose2d;
import frc.lib.geometry.PoseWithCurvature;
import frc.lib.geometry.Rotation2d;
import frc.lib.geometry.Translation2d;
import frc.lib.path.Curve;
import frc.lib.path.Line;
import frc.lib.path.LineCurveGenerator;

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
    
    for (int i = 0; i < wayPoints.size();++i){
        System.out.println(wayPoints.get(i).toString());
    }

    }
}
