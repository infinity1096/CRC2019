/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.ChassisA;
import frc.robot.commands.TakeIn;
import frc.robot.commands.Intake.MoveIntake;
import frc.robot.commands.Intake.PanelReady;
import frc.robot.commands.lift.MoveToDown;
import frc.robot.commands.lift.MoveToMid;
import frc.robot.commands.lift.MoveToUp;
import frc.robot.commands.paneltaker.ExtendTaker;
import frc.robot.commands.paneltaker.NipPanel;
import frc.robot.commands.paneltaker.TurnHolder;
import frc.robot.commands.TakeIn;
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
  // Button button = new JoystickButton(stick, buttonNumber);

  public Joystick stick = new Joystick(0);
  public Joystick stick2 = new Joystick(1);
  

  public JoystickButton panelReady = new JoystickButton(stick2, 6);
  public JoystickButton moveIntake = new JoystickButton(stick2, 1);
  public JoystickButton holderFront = new JoystickButton(stick2, 8);
  public JoystickButton holderUp = new JoystickButton(stick2, 10);
  public JoystickButton holderBack = new JoystickButton(stick2,12);
  //DANGEROUS!!!
  public JoystickButton liftUp = new JoystickButton(stick2,7);
  public JoystickButton liftMid = new JoystickButton(stick2,9);
  public JoystickButton liftDown = new JoystickButton(stick2,11);
  public JoystickButton nip = new JoystickButton(stick2,5);
  public JoystickButton extend = new JoystickButton(stick2,3);
  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  public OI() {
    panelReady.whenPressed(new PanelReady());
    moveIntake.whenPressed(new MoveIntake());
    holderFront.whenPressed(new TurnHolder(-90));
    holderUp.whenPressed(new TurnHolder(0));
    holderBack.whenPressed(new TurnHolder(90));
    liftUp.whenPressed(new MoveToUp());
    liftMid.whenPressed(new MoveToMid());
    liftDown.whenPressed(new MoveToDown());
    nip.whenPressed(new NipPanel());
    extend.whenPressed(new ExtendTaker());
    

    //button2.whenPressed(new HolderToBack());
  }
  // Run the command while the button )is being held down and interrupt it once
  // the button is released.
    //button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
    //button.whenReleased(new ExampleCommand());
}
