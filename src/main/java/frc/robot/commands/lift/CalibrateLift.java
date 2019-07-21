/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class CalibrateLift extends Command {

  public double epsilon = 1;
  public double accumTurn = 4;
  public double accum = 0;
  public double lastPos = 0;

  public CalibrateLift() {
    requires(Robot.lift);  
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.lift.moveElevator(-0.2);
    lastPos = Robot.lift.getEncodervalue()[0]+epsilon;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    System.out.println(Robot.lift.getEncodervalue()[0] - lastPos);
    if (Math.abs(Robot.lift.getEncodervalue()[0] - lastPos) < epsilon){
      this.accum ++;
    }else{
      accum = 0;
    }
    lastPos = Robot.lift.getEncodervalue()[0];
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return accum > accumTurn;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.lift.resetEncoder();
    Robot.lift.moveElevator(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
