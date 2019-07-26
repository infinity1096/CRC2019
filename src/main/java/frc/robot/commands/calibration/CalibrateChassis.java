/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.commands.calibration;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class calibration extends CommandGroup {
  /**
   * Add your docs here.
   */
  public calibration() {
    // Add Commands here:
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    
    addSequential(new GoStraight(0.4,0));
    addSequential(new Delay(1));
    addSequential(new GoStraight(-0.4,0));
    addSequential(new Delay(1));
    addSequential(new GoStraight(0,0));
    
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
  }

  


}

