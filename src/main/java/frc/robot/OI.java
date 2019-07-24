/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.Intake.MoveIntake;
import frc.robot.commands.Intake.PanelReady;
import frc.robot.commands.Intake.TakeIn;
import frc.robot.commands.lift.MoveToDown;
import frc.robot.commands.lift.MoveToMid;
import frc.robot.commands.lift.MoveToUp;
import frc.robot.commands.paneltaker.ExtendTaker;
import frc.robot.commands.paneltaker.NipPanel;
import frc.robot.commands.paneltaker.TurnHolder;
import frc.robot.commands.commandgroup.FlipToHigh;
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
  public JoystickButton button_MoveToUp= new JoystickButton(stick2, 8);
  public JoystickButton button_MoveToMid = new JoystickButton(stick2, 10);
  public JoystickButton button_MoveToDown = new JoystickButton(stick2, 12);
  public JoystickButton button_panelForward = new JoystickButton(stick2,7);
  public JoystickButton button_panelUpward = new JoystickButton(stick2,9);
  public JoystickButton button_panelBackward = new JoystickButton(stick2,11);
  public JoystickButton button_ExtendPanel = new JoystickButton(stick2,5);
  public JoystickButton button_NipPanel = new JoystickButton(stick2,3);
  public JoystickButton button_PanelReady = new JoystickButton(stick2,6);

  public JoystickButton button_Shoot = new JoystickButton(stick2, 1);
  public JoystickButton button_TakeIn = new JoystickButton(stick2, 2);
  public JoystickButton button_MoveIntake = new JoystickButton(stick2, 4);
  public boolean robotstate=false;

  //public JoystickButton button_Switch = new JoystickButton(stick2, 4);
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
    button_MoveToUp.whenPressed(new MoveToUp());
    button_MoveToMid.whenPressed(new MoveToMid());
    button_MoveToDown.whenPressed(new MoveToDown());
    button_panelForward.whenPressed(new TurnHolder(90));
    button_panelUpward.whenPressed(new TurnHolder(0));
    button_panelBackward.whenPressed(new TurnHolder(-90));
    button_ExtendPanel.whenPressed(new ExtendTaker());
    button_NipPanel.whenPressed(new NipPanel());
    button_PanelReady.whenPressed(new PanelReady());
    button_TakeIn.whenPressed(new TakeIn());
    button_MoveIntake.whenPressed(new MoveIntake());
    //button_Shoot.whenPressed(new );
  }
   

  // the button is released.
    //button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
    //button.whenReleased(new ExampleCommand());
}
