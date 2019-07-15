/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

   //Software parameters 

  //Chassis - autonomous
  public static final double CHASSIS_ENCUNIT_TO_RAD_CONSTANT = Math.PI * 2.0 / 4000.0;
  public static final double DRIVETRAIN_TORQUE_PER_VOLT = 3.00733; //stall torque: 36.08
  public static final double DRIVETRAIN_FRICTION_VOLT = 0.2; 
  public static final double DRIVETRAIN_SPEED_PRE_VOLT = 6.82556; // wheel output shaft! rad / s

  public static final double DRIVETRAIN_WHEEL_RADIUS = 0.0522848; //m
  public static final double DRIVETRAIN_WHEELBASE_RADIUS = 0.3186466; //m
  public static final double DRIVETRAIN_MASS = 45.00; //kg
  public static final double DRIVETRAIN_MOI = 30.00; //kg * m
  public static final double DRIVETRAIN_ANGULAR_DRAG = 1.920; //N*m / rad/s
  



  //lift
  public static final double LIFT_CALIBRATION_CURRENT_THRESHOLD = 3.00;
  public static final double LIFT_CALIBRATION_ACCUM_THRESHOLD = 0.50;

  public static final double LIFT_OFFSET = 480.00; // mm




  //Port definition - DO NOT modify code under this line unless actual wire connection is changed

  
  //Chassis
  public static final int CHASSIS_LFMOTOR_PORT = 1;
  public static final int CHASSIS_LBMOTOR_PORT = 2;
  public static final int CHASSIS_RFMOTOR_PORT = 3;
  public static final int CHASSIS_RBMOTOR_PORT = 4;
  public static final int CHASSIS_LEFTMASTER_PORT = 5;
  public static final int CHASSIS_RIGHTMASTER_PORT = 6;

  //Lift
  public static final int LIFT_MOTOR1_PORT = 7;
  public static final int LIFT_MOTOR2_PORT = 8;
  public static final int LIFT_MOTOR3_PORT = 9;
  public static final int LIFT_MOTOR4_PORT = 10;

  //Holder
  public static final int HOLDER_MOTOR_PORT = 11;

  //Intake
  public static final int INTAKE_MOTOR_PORT = 12;

//Pneumatics
  







}
