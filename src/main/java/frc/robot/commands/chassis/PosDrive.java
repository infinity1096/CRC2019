/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class PosDrive extends Command {

  private double[] target = { 0, 0, 0 };
  private double[] RobotPos = { 0, 0, 0 };
  private double kp = RobotMap.CHASSIS_POSDRIVE_KP;
  private double ka = RobotMap.CHASSIS_POSDRIVE_KA;
  private double kb = RobotMap.CHASSIS_POSDRIVE_KB;
  private double kd = RobotMap.CHASSIS_POSDRIVE_KD;
  private double ki_turn = RobotMap.CHASSIS_POSDRIVE_TURN_I;
  private double ki_move = RobotMap.CHASSIS_POSDRIVE_MOVE_I;
  private double activation_range = RobotMap.CHASSIS_POSDRIVE_I_ACTIVIATE_RANGE;
  private double ki_turn_accum = 0;
  private double ki_move_accum = 0;

  private double timeout = 100;

  private Timer timer = new Timer();


  public PosDrive(double targetx, double targety, double targettheta,double timeout) {
    this.target = new double[] { targetx, targety, targettheta};
    requires(Robot.chassis);
    this.timeout = timeout;
  }

  public PosDrive(double targetx, double targety, double targettheta) {
    this.target = new double[] { targetx, targety, targettheta};
    requires(Robot.chassis);
    this.timeout = 100;
  }

  public PosDrive(double[] target) {
    this.target = target.clone();
    requires(Robot.chassis);
    this.timeout = 100;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    this.timer.reset();
    this.timer.start();
    this.RobotPos[0] = Robot.odometry.get()[0];
    this.RobotPos[1] = Robot.odometry.get()[1];
    double heading;
    heading = Math.toRadians(Robot.gyro.getYaw()) + Math.PI / 2;
    heading = Math.atan2(Math.sin(heading), Math.cos(heading));
    this.RobotPos[2] = heading;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    this.RobotPos[0] = Robot.odometry.get()[0];
    this.RobotPos[1] = Robot.odometry.get()[1];
    double heading;
    heading = Math.toRadians(-Robot.gyro.getYaw()) + Math.PI / 2;
    heading = Math.atan2(Math.sin(heading), Math.cos(heading));
    this.RobotPos[2] = heading;
    double p = Math.hypot(target[0] - RobotPos[0], target[1] - RobotPos[1]) / 1000.0;

    double aph;
    double beta;
    double p_output;
    double omega_output;
    double[] u = {0,0};
      aph = Math.atan2((target[1] - RobotPos[1]), (target[0] - RobotPos[0])) - (RobotPos[2]);
      aph = Math.atan2(Math.sin(aph), Math.cos(aph));
      if (-Math.PI / 2 < aph && aph <= Math.PI / 2) {
        aph = Math.atan2((target[1] - RobotPos[1]), (target[0] - RobotPos[0])) - (RobotPos[2]);
        aph = Math.atan2(Math.sin(aph), Math.cos(aph));// restrain angle to [-pi,pi]
        beta = -RobotPos[2] - aph + target[2];
        beta = Math.atan2(Math.sin(beta), Math.cos(beta));
        // compute control outputs
        p_output = kp * p;
        omega_output = ka * aph + kb * beta;
        // do simulation - Xd = Bu ~ Xt = Xt-1 + Xd * dt
        u = new double[] { p_output, omega_output };
      } else {// in case the goal is behind the robot
        double Xv_ = RobotPos[2] + Math.PI;
        Xv_ = Math.atan2(Math.sin(Xv_), Math.cos(Xv_));
        aph = Math.atan2((target[1] - RobotPos[1]), (target[0] - RobotPos[0])) - (Xv_);
        aph = Math.atan2(Math.sin(aph), Math.cos(aph));// restrain angle to [-pi,pi]
        beta = -RobotPos[2] - aph + (target[2]);
        beta = Math.atan2(Math.sin(beta), Math.cos(beta));
        // compute control outputs
        p_output = kp * p;
        omega_output = ka * aph + kb * beta;
        // do simulation - Xd = Bu ~ Xt = Xt-1 + Xd * dt
        u = new double[] { -p_output, omega_output };
      }
      //Apply Kd - not sure it actually works, thus DEPRECATED
      //u[1] = u[1] + kd * Math.signum(u[1]) * -Robot.gyro.getRate();
      if (Math.abs(p) < activation_range){
        u[0] = deadzone2(u[0],0.12);
        if (Math.abs(p) < 0.05){
        double delta_angle = target[2] - RobotPos[2];
        delta_angle = Math.atan2(Math.sin(delta_angle), Math.cos(delta_angle));
        u[1] = ka * delta_angle;

        //overcome friction
        u[1] = range1(u[1],-0.06,0.06);

        u[0] = 0;
      }
      }
      //Limit omega output
      if (Math.abs(u[1]) > RobotMap.CHASSIS_POSDRIVE_P_OMEGA_LIMIT){
        u[1] = Math.signum(u[1]) * RobotMap.CHASSIS_POSDRIVE_P_OMEGA_LIMIT;
      }

      //Limit Kp output - indespensible, or the robot wouls turn crazy
      if (Math.abs(u[0]) > RobotMap.CHASSIS_POSDRIVE_P_LIMIT){
        u[0] = Math.signum(u[0]) * RobotMap.CHASSIS_POSDRIVE_P_LIMIT;
      }
      
      SmartDashboard.putNumber("KD output", kd * Math.signum(u[1]) * Robot.gyro.getRate());
      SmartDashboard.putNumber("u1", u[1]);
      u[0] = deadzone2(u[0],0.05);
      u[1] = deadzone2(u[1],0.02);
      Robot.chassis.arcadeDrive(u);
  }
  public double range(double value, double max, double min){
    double finalValue = 0.0;
    if(value > max ){
      finalValue = max;
    }

    else if(value < min){
      finalValue = min;
    }
    else{
      finalValue = value;
    }
   
    return finalValue;
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

  public double deadzone2(double value, double deadzone){
    double finalValue = 0.0;
    if(Math.abs(value) < deadzone ){
      finalValue = Math.signum(value) *deadzone;
    }else{
      finalValue = value;
    }
    return finalValue;
  }
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    this.RobotPos[0] = Robot.odometry.get()[0];
    this.RobotPos[1] = Robot.odometry.get()[1];
    double heading;
    heading = Math.toRadians(-Robot.gyro.getYaw()) + Math.PI / 2;
    heading = Math.atan2(Math.sin(heading), Math.cos(heading));
    this.RobotPos[2] = heading;
    double p = Math.hypot(target[0] - RobotPos[0], target[1] - RobotPos[1])/1000.0;
    return (Math.abs(p) < 0.05 && Math.abs(heading - target[2]) < 0.05) || timer.get() > this.timeout;
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
}
