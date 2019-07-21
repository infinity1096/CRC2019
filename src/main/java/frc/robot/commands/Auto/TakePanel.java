/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class TakePanel extends Command {
  int counter = 5;
  double CV_heading = 0;
  double x=0,y=0;
  public TakePanel() {
    requires(Robot.chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    CV_heading = Math.toRadians(Robot.gyro.getAngle()) + Math.PI;
    CV_heading = Math.atan2(Math.sin(CV_heading),Math.cos(CV_heading));
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    
     x = Robot.CVtable.getEntry("CV_x").getDouble(-1);
     y = Robot.CVtable.getEntry("CV_y").getDouble(-1);
    int is_valid = (int)Robot.CVtable.getEntry("is_valid").getDouble(0);


      //do a P loop
      double linearOutput = 0;
      double turn_output = 0;
      double Kp = 1.0;
      double error = 0;
      
      counter++;
      if (counter > 5){
        CV_heading = Math.toRadians(Robot.gyro.getAngle())  -Math.atan2(960 - x,1371)/2 + Math.PI;
        CV_heading = Math.atan2(Math.sin(CV_heading),Math.cos(CV_heading));
        counter = 0;
      }

      error =  Math.toRadians(Robot.gyro.getAngle())+ Math.PI -  CV_heading;
      error = Math.atan2(Math.sin(error),Math.cos(error));

      SmartDashboard.putNumber("error", error);

      if (Math.abs(error) > 0.2){
        linearOutput = 0;
      }else{
        linearOutput = -0.2;
      }



      turn_output = Kp * error;
      turn_output = range(turn_output,-0.2,0.2);

      

      if (is_valid==1 ){
        if (y > 900){
          linearOutput = 0;
        }
        Robot.chassis.arcadeDrive(linearOutput, turn_output);
        
      }else{
        //System.out.println("Im called");
        Robot.chassis.arcadeDrive(linearOutput, -0.2 * Robot.oi.stick.getRawAxis(2));
      }
    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return y>950;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.chassis.arcadeDrive(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }

  private double range(double val,double min,double max){
    if (val > max){
        return max;
    }else if(val < min){
        return min;
    }else{
        return val;
    }
}

}
