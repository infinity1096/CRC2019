/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Odometry.Odometry;
import frc.robot.Odometry.UpdateOdometryPos;
import frc.robot.commands.lift_P;
import frc.robot.commands.lift_P1;
import frc.robot.commands.lift_down;
import frc.robot.commands.Intake.IntakeStop;
import frc.robot.commands.Intake.PanelReady;
import frc.robot.commands.Intake.Shoot;
import frc.robot.commands.Intake.TakeIn;
import frc.robot.commands.auto.AutoDrive;
import frc.robot.commands.auto.LeftRocketAuto;
import frc.robot.commands.auto.LinearDrive;
import frc.robot.commands.auto.RightRocketAuto;
import frc.robot.commands.auto.RotateTo;
import frc.robot.commands.calibration.DecreaseRotaryOffset;
import frc.robot.commands.calibration.IncreaseRotaryOffset;
import frc.robot.commands.auto.AutoDrive;
import frc.robot.commands.chassis.PosDrive;
import frc.robot.commands.lift.CalibrateLift;
import frc.robot.commands.lift.MoveToDown;
import frc.robot.commands.lift.MoveToUp;
import frc.robot.commands.paneltaker.TurnHolder;
import frc.robot.sensors.AbsoluteEncoder;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.PanelTaker;
import frc.robot.subsystems.Rotary;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {


  public static Chassis chassis = new Chassis();
  public static AbsoluteEncoder absoluteEncoder = new AbsoluteEncoder(Port.kOnboardCS0);
  public static Lift lift = new Lift();
  public static Intake intake = new Intake();
  public static PanelTaker paneltaker = new PanelTaker();
  public static Rotary rotary = new Rotary();
  public static OI oi = new OI();
  public static NetworkTable CVtable =
  NetworkTableInstance.getDefault().getTable("VisionBoard");
  public static Notifier odometry_notifier;
  public static Odometry odometry = new Odometry(0.02, chassis.getWheelEncoderValue()[0][0], chassis.getWheelEncoderValue()[0][1]);
  public static AHRS gyro = new AHRS(Port.kMXP);
  
  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();


  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    // chooser.addOption("My Auto", new MyAutoCommand());
    SmartDashboard.putData("Auto mode", m_chooser);
    CameraServer.getInstance().startAutomaticCapture();


  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
    if (odometry_notifier != null){
      this.odometry_notifier.stop();
    }
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }  

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = new LeftRocketAuto();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    gyro.reset();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
    
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }
  long count = 0;
  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    Shoot.phase = false;
    Robot.intake.hold();
    odometry_notifier = new Notifier(odometry);
    odometry_notifier.startPeriodic(0.02);
    odometry.setPos(0, 0);
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }


    Robot.lift.moveElevator(0);
    new CalibrateLift().start();
    Robot.intake.intakeUp();
    Robot.intake.IntakeOpen();
    SmartDashboard.putData(new UpdateOdometryPos());
    SmartDashboard.putData(new PosDrive(80, 600, Math.PI/2));
    SmartDashboard.putData(new LinearDrive(500, Math.PI/2));
    //emergency stop buttons
    SmartDashboard.putData(new IntakeStop());
    SmartDashboard.putData(new PanelReady());
    SmartDashboard.putData(new CalibrateLift());
    SmartDashboard.putData(new TakeIn());
    SmartDashboard.putData(new IncreaseRotaryOffset());
    SmartDashboard.putData(new DecreaseRotaryOffset());
    
    
  }

  /**
   * This function is called periodically during operator control.
   */
  double i = 0;
  @Override
  public void teleopPeriodic() {
  
    //Robot.lift.setPower(0);
 
    Scheduler.getInstance().run();
    
    //odometry
    SmartDashboard.putNumber("Odom_x",odometry.get()[0]);
    SmartDashboard.putNumber("Odom_y",odometry.get()[1]);
    //encoder
    SmartDashboard.putNumber("encL",chassis.getWheelEncoderValue()[0][0]);
    SmartDashboard.putNumber("encR",chassis.getWheelEncoderValue()[0][1]);
    SmartDashboard.putBoolean("isNippped", PanelTaker.isNipped());

    SmartDashboard.putNumber("ANGLE",rotary.get_encoder_value());
    SmartDashboard.putNumber("Rotary Offset",rotary.RotaryOffset());
    
    SmartDashboard.putBoolean("Is claw on", paneltaker.isNipped());
    //cv track

    SmartDashboard.putBoolean("Is climber limit", lift.isClimberToLimit());
    

    boolean isValid = Robot.CVtable.getEntry("isvalid").getDouble(-1)==1;
    SmartDashboard.putBoolean("Is Valid", isValid);

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
