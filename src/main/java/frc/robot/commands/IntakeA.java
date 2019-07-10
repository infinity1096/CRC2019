/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeA extends Command {
  public IntakeA() {
    // Use requires() here to declare subsystem dependencies
    
  public double current;
  public double currentLast;
  }}

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {


    Robot.intake.takeIn();
    Robot.intake.getCurrent();
    current = Robot.intake.getCurrent();

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

  
    
  }
    
    
  

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() { 


  
    currentLast = Robot.intake.getCurrent();

    if(currentLast>current+5){
      return true;
    }
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.intake.hold();
  }

}