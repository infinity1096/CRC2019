/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.test;

import frc.lib.physics.DCMotorTransmission;
import frc.lib.physics.DifferentialDrive;
import frc.lib.physics.DifferentialDrive.ChassisState;
import frc.lib.trajectory.constraints.IConstraints.MinMax;

/**
 * Add your docs here.
 */
public class testMaxAccel {

    public static void main(String args[]){

        DCMotorTransmission left,right;
        left = new DCMotorTransmission(7.407, 24.7844, 1.055);
        right = new DCMotorTransmission(7.407, 24.7844, 1.055);

        DifferentialDrive drive = new DifferentialDrive(60,10,12,0.3,0.90,left, right);

        ChassisState velocity = new ChassisState(1, 0);

        MinMax limit = drive.getMaxAccelcration(velocity, 0, 5);

        System.out.println(limit.toString());

    }

}
