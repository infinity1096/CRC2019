package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class LockClimber extends Command
{
    public LockClimber()
    {
        requires(Robot.lift);
    }

    @Override
    protected void initialize()
    {
        Robot.lift.lockClimer();
    }

    @Override
    protected void execute()
    {

    }

    @Override
    protected boolean isFinished()
    {
        return true;
    }

    @Override
    protected void end()
    {

    }

    @Override
    protected void interrupted()
    {

    }
}