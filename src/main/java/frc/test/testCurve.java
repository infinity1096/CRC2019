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

/**
 * Add your docs here.
 */
public class testCurve {
    public static void main(String[] args){
        
    Translation2d translation1 = new Translation2d(0,0);
    Translation2d translation2 = new Translation2d(1,0);

    //System.out.println(translation1.rotateBy(Rotation2d.fromRad(Math.PI/2.0d)));

    Rotation2d rotation1 = new Rotation2d(-Math.PI/6);
    Rotation2d rotation2 = new Rotation2d(Math.PI/6);

    Pose2d pose1,pose2;

    pose1 = new Pose2d(translation1,rotation1);
    pose2 = new Pose2d(translation2,rotation2);

    Curve curve = new Curve(pose1,pose2);
    
    List<PoseWithCurvature> list = curve.interpolate(0.1);
    
    for (int i = 0; i < list.size();++i){
        System.out.println(list.get(i).toString());
    }

    }
}
