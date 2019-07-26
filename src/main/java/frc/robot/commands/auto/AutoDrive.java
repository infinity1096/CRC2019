/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.lift.MoveToMid;

public class AutoDrive extends CommandGroup {
  /**
   * Add your docs here.
   */
  public AutoDrive() {


    
    addSequential(new LinearDrive(1000));
    addSequential(new RotateTo(Math.PI*2/3));
    addSequential(new LinearDrive(1000));
    addSequential(new RotateTo(Math.PI*4/3));
    addSequential(new LinearDrive(1000));
    addSequential(new RotateTo(Math.PI*6/3));
      /*
      addSequential(new LinearDrive(200));       //start from the right to the rocket
      addSequential(new RotateTo(-Math.PI*1/2));
      addSequential(n
      ew LinearDrive(920));
      addSequential(new RotateTo(-Math.PI*4/18));
      addSequential(new LinearDrive(1644));
  */
  /*
      addSequential(new LinearDrive(2082.4));     //start from the left to the rocket
      addSequential(new RotateTo(Math.PI*1/2));
      addSequential(new LinearDrive(2192));
      addSequential(new RotateTo(Math.PI*4/18));
      addSequential(new LinearDrive(1644));
  
      addSequential(new LinearDrive(2082.4));     //start from the right 
      addSequential(new RotateTo(Math.PI*1/2));
      addSequential(new LinearDrive(876.8));
      addSequential(new RotateTo(-Math.PI*1/2));
      addSequential(new LinearDrive(21105.6));
  
      addSequential(new LinearDrive(2082.4));     //start from the left
      addSequential(new RotateTo(-Math.PI*1/2));
      addSequential(new LinearDrive(876.8));
      addSequential(new RotateTo(Math.PI*1/2));
      addSequential(new LinearDrive(21105.6));
      */
    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
  }
}
