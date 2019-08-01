package frc.robot.commands.paneltaker;

import java.text.BreakIterator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.commands.Intake.PanelReady;
import frc.robot.subsystems.PanelTaker;
import frc.robot.commands.lift.MoveToMid;
import frc.robot.subsystems.Rotary;
import frc.robot.subsystems.Lift;
import java.lang.Math;
import frc.robot.Robot;
public class TurnHolder extends Command{

    private double Kp = 0.015;
    private double prevError = 0;
    private double errordotFused = 0;
    private double expAverageCoeff = 0.2;
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
    protected void initialize (){
        error = target - Robot.rotary.get_encoder_value();
        prevError = error;
        accum = 0;
        errordotFused = 0;
        
    }
    @Override
    protected void execute(){
        //if (((Robot.paneltaker.isNipped()) && 
        //(Math.abs(480.0-Robot.lift.getEncodervalue([0]))<=25))== false){
      //      if( (Robot.paneltaker.isNipped() &&
         //   Math.abs(Robot.lift.getEncodervalue()[0] - 480) <= 25) &&
         //   Math.abs(Robot.lift.getEncodervalue()[0]-target)>30){
       //Robot.lift.moveTo(1192);
        //    }
        double GravComp = 0.09;

        if (Robot.paneltaker.isExtended()){
            if (Robot.paneltaker.isNipped()){
                //new PanelReady();
                GravComp = 0.444;
            }else{
                GravComp = 0.16;
            }
        }else{
            if (Robot.paneltaker.isNipped()){
                //new PanelReady();
                GravComp = 0.30;
        
            }else{
                GravComp = 0.06;
            }
        }
       error = target - Robot.rotary.get_encoder_value();

        if(Robot.paneltaker.isExtended() && Robot.paneltaker.isNipped()){
            error *= 0.95;}

        double errordot = error - prevError;
        errordotFused = expAverageCoeff * errordotFused + (1-expAverageCoeff) * errordot;
        prevError = error;
        

        double comp = -GravComp * Math.sin(
                Math.toRadians(Robot.rotary.get_encoder_value()));


        double assistComp = Math.sin(Math.toRadians(Robot.rotary.get_encoder_value())) / 
        Math.sqrt(1325 - 700 * Math.cos(Math.toRadians(Robot.rotary.get_encoder_value()))) * 5.5;

            SmartDashboard.putNumber("rotery error", error);


        double Poutput = Kp * error;
        double Doutput = Kd * errordotFused;
        Poutput = range(Poutput,-0.4,0.4);

        if (Math.abs(errordotFused) > 1.5){
            Poutput /= 1.3;
        }
        

        if (Math.abs(error) > Izone){
            accum = 0;
        }else{
            accum += error * 0.02;
            accum = range(accum, -accumLimit, accumLimit);
        }

        double Ioutput = Ki * accum;


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
        SmartDashboard.putNumber("value", Robot.rotary.get_encoder_value());
        SmartDashboard.putNumber("targetval", target);
        //return Math.abs(Robot.rotary.get_encoder_value()-target) < 20;
        return false;
    }

    @Override
    protected void end()
    {

    }

    @Override
    protected void interrupted()
    {
        Robot.rotary.turn(0);
    }

}