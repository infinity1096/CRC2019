/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.test;

import frc.lib.geometry.Pose2d;
import frc.lib.geometry.PoseWithCovariance;
import frc.lib.geometry.Rotation2d;
import frc.lib.geometry.Translation2d;

/**
 * Add your docs here.
 */
public class testPose {

    public static void main (String args[]){

        Translation2d translation1 = new Translation2d(3,4);
        Translation2d translation2 = new Translation2d(4,6);

        //System.out.println(translation1.rotateBy(Rotation2d.fromRad(Math.PI/2.0d)));

        Rotation2d rotation1 = new Rotation2d(Math.PI/4);
        Rotation2d rotation2 = new Rotation2d(0);

        PoseWithCovariance pose1,pose2;

        pose1 = new PoseWithCovariance(translation1,rotation1,new double[]{1,1,1});
        pose2 = new PoseWithCovariance(translation2,rotation2,new double[]{1,1,1});
        

        System.out.println(pose2.combine(pose1));
        


    }


}
