/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.test;

import frc.lib.physics.DCMotorTransmission;
import frc.lib.physics.DifferentialDrive;
import frc.lib.physics.DifferentialDrive.DriveDynamics;
import frc.lib.physics.DifferentialDrive.WheelState;

/**
 * Add your docs here.
 */
public class testDC {

    public static void main(String[] args){
        DCMotorTransmission left,right;
        left = new DCMotorTransmission(7.407, 24.7844, 1.055);
        right = new DCMotorTransmission(7.407, 24.7844, 1.055);

        DifferentialDrive drive = new DifferentialDrive(60,10,12,0.10,0.90,left, right);
        
        DriveDynamics dynamics = new DriveDynamics();

        dynamics.wheelVelocity = new WheelState(10,20);

        dynamics.voltage = new WheelState(5,2);

        System.out.print(dynamics.toString());

        drive.solveForwardDynamics(dynamics);

        System.out.print(dynamics.toString());

        dynamics.voltage = new WheelState();
        dynamics.wheel_torque = new WheelState();
        //dynamics.chassisVelocity = new ChassisState();
        //dynamics.curvature = 0;

        
        drive.solveInverseDynamics(dynamics);

        System.out.print(dynamics.toString());

    }


}
