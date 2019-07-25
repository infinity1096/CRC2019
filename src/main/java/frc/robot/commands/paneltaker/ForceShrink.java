package frc.robot.commands.paneltaker;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ForceShrink extends Command
{
   
    public ForceShrink()
    {
        requires(Robot.paneltaker);
    }

    @Override
    protected void initialize() {
        Robot.paneltaker.forceShrink();
        
    }

    @Override
    protected void execute(){

    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end()
    {
        
    }

}