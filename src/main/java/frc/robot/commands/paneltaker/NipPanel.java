package frc.robot.commands.paneltaker;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.PanelTaker;

public class NipPanel extends Command
{   
    public NipPanel()
    {
        requires(Robot.paneltaker);
        //requires(Robot.intake);
    }

    @Override
    protected void initialize() {
        Robot.paneltaker.nip();
        Robot.intake.intakeDown();
        Robot.intake.intakeClose();
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