/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class Chassis extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  CANSparkMax talonlf = new CANSparkMax(RobotMap.CHASSIS_LFMOTOR_PORT,MotorType.kBrushless);
  CANSparkMax talonlb = new CANSparkMax(RobotMap.CHASSIS_LBMOTOR_PORT,MotorType.kBrushless);
  CANSparkMax talonrf = new CANSparkMax(RobotMap.CHASSIS_RFMOTOR_PORT,MotorType.kBrushless);
  CANSparkMax talonrb = new CANSparkMax(RobotMap.CHASSIS_RBMOTOR_PORT,MotorType.kBrushless);

  TalonSRX leftMaster = new TalonSRX(RobotMap.CHASSIS_LEFTMASTER_PORT);
  TalonSRX rightMaster = new TalonSRX(RobotMap.CHASSIS_RIGHTMASTER_PORT);

  Notifier notifier = new Notifier(() ->{
    double left = leftMaster.getMotorOutputVoltage()/12.0d;
    double right = rightMaster.getMotorOutputVoltage()/12.0d;
    talonlf.set(left);
    talonlb.set(left);
    talonrf.set(right);
    talonrb.set(right);
  });

public Chassis(){
  //rightMaster.setInverted(true);
}

public void start(){
  notifier.startPeriodic(0.01);
}

public void stop(){
  notifier.stop();
}

public void arcadeDrive(double v,double omega){
  double left = v-omega/2;
  double right = v+omega/2;
  leftMaster.set(ControlMode.PercentOutput,left);
  rightMaster.set(ControlMode.PercentOutput,-right);
}

public void tankDrive(double left,double right){
  leftMaster.set(ControlMode.PercentOutput,left);
  rightMaster.set(ControlMode.PercentOutput,-right);
}

  @Override
 
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
     //setDefaultCommand(new MySpecialCommand());


	  
  }
}
