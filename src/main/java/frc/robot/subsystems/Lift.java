/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Lift extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  TalonSRX Motor1 = new TalonSRX(RobotMap.LIFT_MOTOR1_PORT);
  public TalonSRX Motor2 = new TalonSRX(RobotMap.LIFT_MOTOR2_PORT);
  TalonSRX Motor3 = new TalonSRX(RobotMap.LIFT_MOTOR3_PORT);
  TalonSRX Motor4 = new TalonSRX(RobotMap.LIFT_MOTOR4_PORT);

  Solenoid ElevatorClimberChanger = new Solenoid(4);

  public Lift(){
    Motor1.follow(Motor2);
    Motor3.follow(Motor2);
    Motor4.follow(Motor2); 

    Motor2.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    Motor2.setSensorPhase(true);
    System.out.println("Im called");
  }

  public void move(double inputpower){
    ElevatorClimberChanger.set(false);
    Motor2.set(ControlMode.PercentOutput, inputpower);
  }


  public void resetencoder(){
    Motor2.setSelectedSensorPosition(0);
  }


  public double[] getEncodervalue() {
    double realliftdistance =((Motor2.getSelectedSensorPosition()) / 0.972) + 475;
    double encodervelocity =Motor2.getSelectedSensorVelocity();
    double [] encodervalue = {realliftdistance,encodervelocity};
    return encodervalue ;
  }

  public double getCurrent() {
    double current1 = Motor1.getOutputCurrent();
    double current2 = Motor2.getOutputCurrent();
    double current3 = Motor3.getOutputCurrent();
    double current4 = Motor4.getOutputCurrent();
    double current = current1 + current2 + current3 + current4;
    return current;
  }

    public boolean isDown(){
      if (getEncodervalue()[0]>-50 && getEncodervalue()[0]<50){
        return true;
      }
      else{
        return false;
      }
    }
  
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
