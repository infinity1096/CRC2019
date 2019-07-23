/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TakeIn extends Command {
  
  boolean is_triged = false;
  double time = 999;
  Timer timer = new Timer();
  
  public void SetTimer() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.intake);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.intake.intakeDown();
    timer.reset();
    timer.start();
    is_triged = false;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.intake.IntakeOpen();
    Robot.intake.takeIn();

    if ((!is_triged) && !Robot.intake.getDigitital()){
      time = timer.get();
      is_triged = true;
    }
  
}

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return timer.get() > time + 1;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.intake.hold();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.intake.hold();
  }
}