/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class LinearDrive extends Command {

  double target;
  double error;
  double accum = 0;
  double preverror;
  double Kp=0.0007;
  double Ki=0.001;
  double Kd=0.0009;
  double Izone = 100; 
  double maxAccum = 50;
  double maxAllowableError = 20;
  double errordot;

  public LinearDrive(double target) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.chassis);
    this.target = target;

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

    target += (Robot.chassis.getWheelEncoderValue()[0][0] + Robot.chassis.getWheelEncoderValue()[0][0])/2.0;
    error = target - (Robot.chassis.getWheelEncoderValue()[0][0] + Robot.chassis.getWheelEncoderValue()[0][0])/2.0;
    preverror = error;
    accum = 0;

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    error = target - (Robot.chassis.getWheelEncoderValue()[0][0] + Robot.chassis.getWheelEncoderValue()[0][0])/2.0;
    errordot = error - preverror;
    SmartDashboard.putNumber("EncoderValue", (Robot.chassis.getWheelEncoderValue()[0][0] + Robot.chassis.getWheelEncoderValue()[0][0])/2.0);
    double Poutput = Kp * error;
    double Ioutput = Ki * accum;
    double Doutput = Kd * errordot;
    preverror = error;
    SmartDashboard.putNumber("accum", accum);

    Poutput = range(Poutput,-0.5,0.5);
    Doutput = range(Doutput,-0.2,0.2);
    double output = Poutput + Ioutput + Doutput;
    SmartDashboard.putNumber("Doutput", Doutput);

    if(Math.abs(error)<Izone){
      accum += error * 0.02;
      accum = range (accum,-maxAccum,maxAccum);
    }
    else{
      accum = 0;
    }

    
    Robot.chassis.arcadeDrive(output, 0);
  }

  double range(double val,double min,double max){

    if(val>max){
      return max;
    }
    else if(val<min){
      return min;
    }
    else{
      return val;
    }

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {

    return Math.abs(error) < maxAllowableError && Math.abs(errordot) < maxAllowableError;

  }

  // Called once after isFinished returns true
  @Override
  protected void end() {

    Robot.chassis.arcadeDrive(0,0);

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}