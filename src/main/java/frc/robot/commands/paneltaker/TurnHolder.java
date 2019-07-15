package frc.robot.commands.paneltaker;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class TurnHolder extends Command{

    private double Kp = 0.008;
    private double Kd = 0.007;

    private double error = 0; //error, prev_error

    private double target;

    public TurnHolder(double target)
    {
        requires(Robot.paneltaker);
        this.target = target;
    }

    @Override
    protected void initialize() {
        error = target - Robot.paneltaker.get_encoder_value();
        
    }
    @Override
    protected void execute(){
        SmartDashboard.putNumber("holder deg", Robot.absoluteEncoder.getDeg());
        
        double errordot = target-Robot.paneltaker.get_encoder_value();
        errordot = errordot - error;
        
        double comp = -0.09 * Math.sin(
                Math.toRadians(Robot.paneltaker.get_encoder_value()));

        double Poutput = Kp * error;
        double Doutput = Kd * errordot;
        Poutput = range(Poutput,-0.3,0.3);
        error = target - Robot.paneltaker.get_encoder_value();
        SmartDashboard.putNumber("Dshit", Doutput);
        double output =Poutput  + comp + Doutput;

        Robot.paneltaker.turn(output);
    }

    private double range(double val,double min,double max){
        if (val > max){
            return max;
        }else if(val < min){
            return min;
        }else{
            return val;
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
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