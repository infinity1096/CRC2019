/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class Chassis extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  TalonSRX talonlf = new TalonSRX(5);
  TalonSRX talonlb = new TalonSRX(6);
  TalonSRX talonrf = new TalonSRX(7);
  TalonSRX talonrb = new TalonSRX(8);

void arcadeDrive(double v,double omega){
  double left = v-omega/2;
  double right = v+omega/2;


  talonlb.set(ControlMode.PercentOutput, left);
  talonlf.set(ControlMode.PercentOutput, left);
  talonrb.set(ControlMode.PercentOutput, right);
  talonrf.set(ControlMode.PercentOutput, right);
}





  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
     //setDefaultCommand(new MySpecialCommand());
  }
}
