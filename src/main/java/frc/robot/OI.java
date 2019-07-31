/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.auto.AutoDrive;
import frc.robot.commands.auto.LinearDrive;
import frc.robot.Odometry.UpdateOdometryPos;
import frc.robot.commands.Intake.MoveIntake;
import frc.robot.commands.Intake.PanelReady;
import frc.robot.commands.Intake.TakeIn;
import frc.robot.commands.chassis.ChangeSpeed;
import frc.robot.commands.chassis.PosDrive;
import frc.robot.commands.commandgroup.FlipPanel;
import frc.robot.commands.commandgroup.ResetPanel;
import frc.robot.commands.commandgroup.cargoReady;
import frc.robot.commands.commandgroup.takeinready;
import frc.robot.commands.lift.LockClimber;
import frc.robot.commands.lift.MoveClimber;
import frc.robot.commands.lift.MoveToDown;
import frc.robot.commands.lift.MoveToMid;
import frc.robot.commands.lift.MoveToUp;
import frc.robot.commands.lift.ReleaseClimber;
import frc.robot.commands.paneltaker.ExtendTaker;
import frc.robot.commands.paneltaker.NipPanel;
import frc.robot.commands.paneltaker.TurnHolder;
import frc.robot.commands.Intake.Shoot;
//import frc.robot.commands.commandgroup.FlipPanelToBack;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */

public class OI {
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  public Joystick stick = new Joystick(0);
  public Joystick stick2 = new Joystick(1);

  // Button button = new JoystickButton(stick, buttonNumber);
  //joystick1
  public JoystickButton button_UpdateOdometry = new JoystickButton(stick,3);
  public JoystickButton button_PosDrive = new JoystickButton(stick,5);
  public JoystickButton button_ChangeSpeed = new JoystickButton(stick,2);
  public JoystickButton button_Shoot = new JoystickButton(stick, 1);
  //public JoystickButton button_autodrive = new JoystickButton(stick,11);
  public JoystickButton button_autoclimbup = new JoystickButton(stick,4);
  
  //joystick2
  //lift
  public JoystickButton button_MoveToUp= new JoystickButton(stick2, 8);
  public JoystickButton button_MoveToMid = new JoystickButton(stick2, 10);
  public JoystickButton button_MoveToDown = new JoystickButton(stick2, 12);
  //holder
  public JoystickButton button_ExtendPanel = new JoystickButton(stick2,5);
  public JoystickButton button_NipPanel = new JoystickButton(stick2,3);
  //public JoystickButton button_FlipPanel = new JoystickButton(stick2,7);
  //public JoystickButton button_ResetPanel = new JoystickButton(stick2,11);
  public JoystickButton button_HolderUp = new JoystickButton(stick2,9);
  public JoystickButton button_HolderFront = new JoystickButton(stick2, 7);
  public JoystickButton button_HolderBack = new JoystickButton(stick2, 11);
  //intake
  public JoystickButton button_TakeIn = new JoystickButton(stick2, 2);
  public JoystickButton button_releaseClimber = new JoystickButton(stick2,6);

  public boolean robotstate=false;

  public JoystickButton button_panelready = new JoystickButton(stick2, 4);
  //public JoystickButton button_testAutoDrive = new JoystickButton(stick,9);
  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.

  // button.whenPressed(new ExampleCommand());
  public boolean Changestate(boolean changing){
    if (changing == false) return true;
    else return false;
  }
  OI(){
    //joystick1
    //button_autodrive.whenPressed(new AutoDrive());
    System.out.println(Robot.absoluteEncoder.getDeg());
    button_panelready.whenPressed(new PanelReady());
    button_UpdateOdometry.whenPressed(new UpdateOdometryPos());
    button_PosDrive.whenPressed(new PosDrive(80, 600, Math.PI/2));
    button_ChangeSpeed.whenPressed(new ChangeSpeed());
    button_Shoot.whenPressed(new Shoot());
    //button_testAutoDrive.whenPressed(new AutoDrive());
    
    //joystick2
    //lift
    button_autoclimbup.whenPressed(new MoveClimber());
    button_MoveToUp.whenPressed(new MoveToUp());
    button_MoveToMid.whenPressed(new MoveToMid());
    button_MoveToDown.whenPressed(new MoveToDown());
    //holder
    button_ExtendPanel.whenPressed(new ExtendTaker());
    button_NipPanel.whenPressed(new NipPanel());
    //button_FlipPanel.whenPressed(new FlipPanel());//lift will move to mid prior to flipping
    //button_ResetPanel.whenPressed(new ResetPanel());//holder and lift will move simultaneously do NOT move with panel
    button_HolderUp.whenPressed(new TurnHolder(0));
    button_HolderBack.whenPressed(new TurnHolder(-90));
    button_HolderFront.whenPressed(new TurnHolder(90));
    //intake
    button_TakeIn.whenPressed(new takeinready());
    
    //intake will be lifted up after cargo is taken in
    //intake will be moved down about 1s after shooting
    //button_Shoot.whenPressed(new );
    //backup release climber
    button_releaseClimber.whenPressed(new ReleaseClimber());
  }
  // the button is released.
    //button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
    //button.whenReleased(new ExampleCommand());
}
