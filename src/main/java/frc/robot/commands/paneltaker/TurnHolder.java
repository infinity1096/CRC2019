package frc.robot.commands.paneltaker;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class TurnHolder extends Command{

    private double Kp = 0.020;
    private double Kd = 0.014;
    private double Ki = 0.02;
    private double accum = 0;
    private double accumLimit = 5;
    private double Izone = 20; // DEG! 

    private double error = 0; //error, prev_error

    private double target;

    public TurnHolder(double target)
    {
        requires(Robot.rotary);
        this.target = target;
    }

    @Override
    protected void initialize() {
        error = target - Robot.rotary.get_encoder_value();
        
    }
    @Override
    protected void execute(){

        double GravComp = 0.09;

        if (Robot.paneltaker.isExtended()){
            if (Robot.paneltaker.isNipped()){
                GravComp = 0.28;
            }else{
                GravComp = 0.16;
            }
        }else{
            if (Robot.paneltaker.isNipped()){
                GravComp = 0.20;
            }else{
                GravComp = 0.09;
            }
        }

        
        double errordot = target-Robot.rotary.get_encoder_value();
        errordot = errordot - error;
        

        double comp = -GravComp * Math.sin(
                Math.toRadians(Robot.rotary.get_encoder_value()));


        double assistComp = Math.sin(Math.toRadians(Robot.rotary.get_encoder_value())) / 
        Math.sqrt(1325 - 700 * Math.cos(Math.toRadians(Robot.rotary.get_encoder_value()))) * 6.0;

        double Poutput = Kp * error;
        double Doutput = Kd * errordot;
        Poutput = range(Poutput,-0.5,0.5);
        error = target - Robot.rotary.get_encoder_value();

        if (Math.abs(error) > Izone){
            accum = 0;
        }else{
            accum += error * 0.02;
            accum = range(accum, -accumLimit, accumLimit);
        }

        double Ioutput = Ki * accum;

        System.out.println(Doutput);

        double output = Poutput + Ioutput+ Doutput + comp +assistComp;

        Robot.rotary.turn(output);
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