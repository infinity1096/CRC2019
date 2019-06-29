/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib;

/**
 * Add your docs here.
 */
public class Util {

    public static final double kEpsilon = 1e-12;

    public static boolean epsilonEqual(double a, double b){
        return (a-b < kEpsilon) && (a-b > -kEpsilon); 
    }

}
