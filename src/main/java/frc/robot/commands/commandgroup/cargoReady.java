package frc.robot.commands.commandgroup;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.lift.MoveToDown;
import frc.robot.commands.lift.MoveToMid;
import frc.robot.commands.lift.MoveToUp;
import frc.robot.Robot;
import frc.robot.commands.Intake.CargoReady;
import frc.robot.commands.Intake.PanelReady;
import frc.robot.commands.paneltaker.ExtendTaker;
import frc.robot.commands.paneltaker.ForceShrink;
import frc.robot.commands.paneltaker.TurnHolder;
public class cargoReady extends CommandGroup{
    public cargoReady(){
        addSequential(new ForceShrink());
        addSequential(new TurnHolder(0));
        addSequential(new CargoReady());
    }
}