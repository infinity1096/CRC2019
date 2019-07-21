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

  //lift
  public static final double LIFT_CALIBRATION_CURRENT_THRESHOLD = 4.00;
  public static final double LIFT_CALIBRATION_ACCUM_THRESHOLD = 0.40;

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
  public static final int TAKER_EXTENDER_PORT = 1;
  public static final int TAKER_NIPPER_PORT = 5;
  public static final double HOLDER_BACK_DEGREES = 47;
  public static final double HOLDER_FRONT_DEGREES = 315;
  public static final double HOLDER_UP_DEGREES = 180;

  //Intake
  public static final int INTAKE_MOTOR_PORT = 12;

  //Aiming Line for CV
  public static final double AIMING_LINE = 999.99999;

//Pneumatics
  






}
