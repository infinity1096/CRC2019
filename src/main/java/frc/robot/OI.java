/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import frc.robot.commands.lift_P;
import frc.robot.commands.lift_P1;
import frc.robot.commands.lift_down;

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
  public JoystickButton button_liftUp = new JoystickButton(stick, 1);
  public JoystickButton button_liftTo_0 = new JoystickButton(stick, 2);
  public JoystickButton button_liftDown = new JoystickButton(stick, 3);


  


  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.

  // button.whenPressed(new ExampleCommand());
  OI(){
    button_liftUp.whenPressed(new lift_P());
    button_liftTo_0.whenPressed(new lift_P1());
    button_liftDown.whenPressed(new lift_down());
  }
   

  // the button is released.
    //button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
    //button.whenReleased(new ExampleCommand());
}
