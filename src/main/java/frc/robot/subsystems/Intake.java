
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
  public class Intake extends Subsystem {
    CANSparkMax csm = new CANSparkMax(RobotMap.INTAKE_MOTOR_PORT,MotorType.kBrushless);
    Solenoid IntakeLifter = new Solenoid(2);
    Solenoid IntakeOpen = new Solenoid(3);
    DigitalInput d1 = new DigitalInput(0);
    
    public static boolean isDown = false;

    public Intake()
    {
      IntakeLifter.set(true);
      IntakeOpen.set(true);
    }
    
  public void takeIn (){
    System.out.println("Im called");
    csm.set(-0.35);
  
  }
  public void hold(){
  
    csm.set(0);
    
  }

  public void keepBall(){
    csm.set(-0.1);
  }

  public void shoot(){
    csm.set(1);
  }

  public double getCurrent(){
    return csm.getOutputCurrent();
  }
  
  public void intakeDown(){
     IntakeLifter.set(true);
     isDown = true;  
    }

  public void intakeUp(){
    IntakeLifter.set(false);
    isDown = false;
 }

 public boolean isDown(){
   return isDown;
 }

  public void IntakeOpen(){
    IntakeOpen.set(false);
 }

 public boolean getDigitital(){
   return d1.get();
 }

 public void intakeClose(){
  IntakeOpen.set(true);
}
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}