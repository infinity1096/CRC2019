/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class P_lift extends Command {

    double refrence;
    double liftpower;
  public P_lift(double power) {
    this.refrence= power;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.lift);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    liftpower = (0.6/200) * (refrence - Robot.lift.getEncodervalue()[0]) + 0.16;
    if(liftpower<0){
      liftpower = liftpower*0.6;
    }
    if(liftpower>-0.5 && liftpower<0.5){
      Robot.lift.move(liftpower);
    }
    if(liftpower<-0.5){
      Robot.lift.move(-0.5);
    }
    if(liftpower>0.5){
      Robot.lift.move(0.5);
    }
    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
