package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ChangeSpeed extends Command
{
    public ChangeSpeed()
    {
        requires(Robot.chassis);
    }

    @Override
    public void initialize()
    {
        Robot.chassis.changeSpeed();
    }

    @Override
    public void execute()
    {

    }

    @Override
    public boolean isFinished()
    {
        return true;
    }

    @Override
    public void end()
    {

    }

    @Override
    public void interrupted()
    {

    }
}