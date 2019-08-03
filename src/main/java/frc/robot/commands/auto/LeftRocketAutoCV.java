/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Odometry.UpdateOdometryPos;
import frc.robot.commands.Delay;
import frc.robot.commands.calibration.BumpBack;
import frc.robot.commands.chassis.PosDriveCV;
import frc.robot.commands.lift.CalibrateLift;
import frc.robot.commands.paneltaker.ExtendTaker;
import frc.robot.commands.paneltaker.NipPanel;
import frc.robot.commands.paneltaker.TurnHolder;

public class LeftRocketAutoCV extends CommandGroup {
  /**
   * Add your docs here.
   */
  public LeftRocketAutoCV() {
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
    addSequential(new NipPanel());
    addSequential(new CalibrateLift());
    addParallel(new TurnHolder(-90));
    //addSequential(new BumpBack());
    addSequential(new LinearDrive(2832,Math.PI/2));
    addSequential(new RotateTo(0));
    addSequential(new LinearDrive(-1860,0));
    addSequential(new RotateTo(-Math.PI*1/3));

    addSequential(new UpdateOdometryPos());
    addSequential(new PosDriveCV(230, -559, -Math.PI/3));
    
    
    
    
    
    


  }
}
