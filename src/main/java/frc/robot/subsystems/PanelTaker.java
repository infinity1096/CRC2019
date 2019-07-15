package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.paneltaker.TurnHolder;
import frc.robot.sensors.AbsoluteEncoder;

public class PanelTaker extends Subsystem{

    Solenoid takerExtender;
    Solenoid takerNipper;
    TalonSRX holderTurner;

    private boolean isExtended = false;
    private boolean isNipped = false;

    public PanelTaker()
    {
        takerExtender = new Solenoid(RobotMap.TAKER_EXTENDER_PORT);
        takerNipper = new Solenoid(RobotMap.TAKER_NIPPER_PORT);
        holderTurner = new TalonSRX(RobotMap.HOLDER_MOTOR_PORT);
        holderTurner.setInverted(true);
    }

    public void turn(double power)
    {
        holderTurner.set(ControlMode.PercentOutput,power);
    }

    public void extend()
    {
        if(!isExtended)
            takerExtender.set(true);
        else
            takerExtender.set(false);
        isExtended = !isExtended;
    }

    public double get_encoder_value()
    {
        return Robot.absoluteEncoder.getDeg() * 2.0 / 3.0 - 120;
    }

    public void nip()
    {
        if(!isNipped)
            takerNipper.set(true);
        else
            takerNipper.set(false);
        isNipped = !isNipped;
    }

    @Override
    protected void initDefaultCommand() {
        //setDefaultCommand(new TurnHolder(0));
    }

}

