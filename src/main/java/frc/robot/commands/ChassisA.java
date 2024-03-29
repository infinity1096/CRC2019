/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.Robot;

/**
 * An example command. You can replace me with your own command.
 */
public class ChassisA extends Command {
  public ChassisA() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.chassis);

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
   Robot.chassis.start();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  
    double inputx = -Robot.oi.stick.getRawAxis(1)*1.0;
    double inputy = -Robot.oi.stick.getRawAxis(2)*0.5;

    if (Math.abs(Robot.oi.stick.getRawAxis(1)) < 0.13){
      inputx = 0;
    }

    if (Math.abs(Robot.oi.stick.getRawAxis(2)) < 0.05){
      inputy = 0;
    }

    inputy = Math.signum(inputy) * Math.pow(Math.abs(inputy), 1.3);

    Robot.chassis.arcadeDrive(inputx,inputy);
  

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.chassis.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
