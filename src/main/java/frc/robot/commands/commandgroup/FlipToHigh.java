package frc.robot.commands.commandgroup;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.lift.MoveToDown;
import frc.robot.commands.lift.MoveToUp;
import frc.robot.commands.Intake.PanelReady;
import frc.robot.commands.paneltaker.TurnHolder;
public class FlipToHigh extends CommandGroup{
    public FlipToHigh(){
        addSequential(new PanelReady());
        addParallel(new MoveToUp());
        addSequential(new TurnHolder(90));
        addSequential(new MoveToDown());
    }
}