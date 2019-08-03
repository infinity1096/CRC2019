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
import frc.robot.subsystems.Intake;

public class Shoot extends Command {

  Timer timer = new Timer();
  public static boolean phase = false; // false = phase1 = shoot, true = phase2 = down
  public Shoot() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.intake);
    
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    timer.reset();
    timer.start();
   
      Robot.intake.intakeClose();
     
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    
    if(timer.get()<=0.3){   
      Robot.intake.shoot();
    }else {
      Robot.intake.IntakeOpen();
      Robot.intake.hold();
    }

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    
      return timer.get() > 0.5;
    
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
