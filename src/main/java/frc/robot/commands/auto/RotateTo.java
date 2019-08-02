/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import javax.swing.text.StyleContext.SmallAttributeSet;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class RotateTo extends Command {

  double target;
  double error;
  double accum = 0;
  double preverror;
  double Kp = 0.5;
  double Ki = 0.7;
  double Kd = 0.16;
  double Izone = 0.16; 
  double maxAccum = 0.3456;
  double maxAllowableError = 0.005556;
  double errordot;

  Timer timer = new Timer();

  public RotateTo(double target) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.chassis);
    this.target = target;

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

    error = target -(- Math.toRadians(Robot.gyro.getAngle()) + Math.PI/2);
    error = Math.atan2(Math.sin(error),Math.cos(error));
    preverror = error;
    timer.reset();
    timer.start();

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    error = target - (-Math.toRadians(Robot.gyro.getAngle())+ Math.PI/2);
    error = Math.atan2(Math.sin(error),Math.cos(error));
    errordot = error - preverror;
    SmartDashboard.putNumber("errordot", errordot);
    
    SmartDashboard.putNumber("gyrovalue", -Robot.gyro.getAngle());

    double Poutput = Kp * error;
    double Ioutput = Ki * accum;
    double Doutput = Kd * errordot;
    preverror = error;

    if(Math.abs(errordot) > 6){
      Poutput /= 1.4;
    }

    Poutput = range(Poutput,-0.5,0.5);
    Doutput = range(Doutput,-0.2,0.2);
    Ioutput = range1(Ioutput,-0.06,0.06);
    double output = Poutput + Ioutput + Doutput;

    if(Math.abs(error)<Izone){
      accum += error * 0.02;
      accum = range (accum,-maxAccum,maxAccum);
    }
    else{
      accum = 0;
    }
  
    Robot.chassis.arcadeDrive(0,output);
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

    

    double range1(double val,double min1,double max1){

      if(val>0 && val<max1){
        return max1;
      }
      else if(val>max1){
        return val;
      }
      else if(val<min1){
        return val;
      }
      else{
        return min1;
      }
    }

  

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {

    return Math.abs(error) < maxAllowableError && Math.abs(errordot) < maxAllowableError || timer.get() > 3;
    
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
