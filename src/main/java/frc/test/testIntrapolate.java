/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.test;

import frc.lib.geometry.Rotation2d;

/**
 * Add your docs here.
 */
public class testIntrapolate {

    public static void main (String args[]){
        Rotation2d rot1 = new Rotation2d(3);
        Rotation2d rot2 = new Rotation2d(-3);

        


        for (int i = 0; i < 100; i++){
            System.out.println(
                rot2.interpolate(rot1, (double)i/100.0d).toString()
            );
        }
    }




}
