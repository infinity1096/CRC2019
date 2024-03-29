/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.lift;


import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;


public class ChassisTakeover extends Command {
  public ChassisTakeover () {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.chassis);
    //requires(Robot.chassis);
  }

  //Timer timer = new Timer();

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    //Robot.lift.lockClimer();
    //timer.reset();
    //timer.start();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //Robot.lift.moveClimber(Robot.oi.stick2.getRawAxis(0));
    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    //Robot.lift.moveClimber(0);
    //Robot.chassis.arcadeDrive(0,0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
