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
  double Kp = 0.0007;
  double Ki = 0.001;
  double Kd = 0.0009;
  double Izone = 100; 
  double maxAccum = 50;
  double maxAllowableError = 20;
  double errordot;

  //angle
  double heading;
  double angle_p = 0.5,angle_i = 0.7,angle_d = 0.16;
  double angle_izone = 0.16,angle_maxaccum = 0.3456,angle_accum = 0;
  double angle_error,angle_preverror,angle_expaverage = 0,angle_expcoeff = 0.2;


  public LinearDrive(double target,double heading) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.chassis);
    this.target = target;
    this.heading = heading;

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

    target += (Robot.chassis.getWheelEncoderValue()[0][0] + Robot.chassis.getWheelEncoderValue()[0][1])/2.0;
    error = target - (Robot.chassis.getWheelEncoderValue()[0][0] + Robot.chassis.getWheelEncoderValue()[0][1])/2.0;
    preverror = error;
    accum = 0;

    angle_error = heading -(- Math.toRadians(Robot.gyro.getAngle()) + Math.PI/2);
    angle_error = Math.atan2(Math.sin(angle_error),Math.cos(angle_error));
    angle_preverror = angle_error;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    error = target - (Robot.chassis.getWheelEncoderValue()[0][0] + Robot.chassis.getWheelEncoderValue()[0][1])/2.0;
    errordot = error - preverror;
    SmartDashboard.putNumber("EncoderValue", (Robot.chassis.getWheelEncoderValue()[0][0] + Robot.chassis.getWheelEncoderValue()[0][1])/2.0);
    
    double Poutput = Kp * error;
    double Ioutput = Ki * accum;
    double Doutput = Kd * errordot;
    preverror = error;

    Poutput = range(Poutput,-0.5,0.5);
    Doutput = range(Doutput,-0.2,0.2);
    double output = Poutput + Ioutput + Doutput;


    if(Math.abs(error)<Izone){
      accum += error * 0.02;
      accum = range (accum,-maxAccum,maxAccum);
    }
    else{
      accum = 0;
    }

    //angle 
    angle_error = heading -(- Math.toRadians(Robot.gyro.getAngle()) + Math.PI/2);
    angle_error = Math.atan2(Math.sin(angle_error),Math.cos(angle_error));

    System.out.println(angle_error);

    angle_accum += angle_error * 0.02;

    double angle_errordot = angle_error - angle_preverror;
    angle_expaverage = angle_expcoeff * angle_expaverage + (1 - angle_expcoeff) * angle_errordot;

    double angle_poutput,angle_ioutput,angle_doutput;
    angle_poutput = angle_error * angle_p;
    angle_ioutput = angle_accum * angle_i;
    angle_doutput = angle_expaverage * angle_d;
    
    if(Math.abs(angle_expaverage) > 6){
      angle_poutput /= 1.4;
    }
    //limit all
    angle_poutput = range(angle_poutput,-0.5,0.5);
    angle_doutput = range(angle_doutput,-0.2,0.2);
    angle_ioutput = range1(angle_ioutput,-0.06,0.06);
    double angle_output = Poutput + Ioutput + Doutput;
  
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
