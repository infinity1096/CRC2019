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
//import frc.robot.commands.commandgroup.takeinready;
import frc.robot.commands.paneltaker.TurnHolder;

//import frc.robot.commands.paneltaker;



public class TakeIn extends Command {
  
  boolean is_triged = false;
  double time = 999;
  Timer timer = new Timer();
  
  public TakeIn() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.intake);
    time = 999;
    is_triged = false;

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.intake.intakeDown();
    Robot.intake.IntakeOpen();
    timer.reset();
    timer.start();
    is_triged = false;
    time = 999;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.intake.IntakeOpen();
    Robot.intake.takeIn();

    if ((!is_triged) && !Robot.intake.getDigitital()){
      time = timer.get();
      is_triged = true;
      //Robot.paneltaker.TurnHolder(0);
      //Robot.paneltaker.
      //Robot.lift.
    }
  
}
  
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return timer.get() > time + 0.5 || timer.get() > 5.0;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    
    if (timer.get() < 5.0){
      Robot.intake.intakeUp();
      Robot.intake.keepBall();
    }else{
    Robot.intake.hold();
    }
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.intake.hold();
  }
}