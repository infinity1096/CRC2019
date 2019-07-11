
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
  public class Intake extends Subsystem {
    TalonSRX  talonf = new TalonSRX(11);
    TalonSRX  talonf2 = new TalonSRX(12);
    
  public void takeIn (){
    talonf.set(ControlMode.PercentOutput, -1);
    talonf2.set(ControlMode.PercentOutput, 1);
  }
  public void hold(){
  
    talonf2.set(ControlMode.PercentOutput, 0.1);
    talonf.set(ControlMode.PercentOutput, 0);
    
  }
  public void shoot(){
  
    talonf2.set(ControlMode.PercentOutput, -1);
  }

  

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}