package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.RobotMap;

public class PanelTaker extends Subsystem{

    Solenoid takerExtender;
    Solenoid takerNipper;

    public static boolean isExtended = false;
    public static boolean isNipped = false;

    public PanelTaker()
    {
        takerExtender = new Solenoid(RobotMap.TAKER_EXTENDER_PORT);
        takerNipper = new Solenoid(RobotMap.TAKER_NIPPER_PORT);
    }


    public void extend()
    {
        if(!isExtended)
            takerExtender.set(true);
        else
            takerExtender.set(false);
        isExtended = !isExtended;
    }

    public void forceShrink()
    {
        takerExtender.set(false);
        isExtended = false;
    }

    public void nip()
    {
        if(!isNipped)
            takerNipper.set(true);
        else
            takerNipper.set(false);
        isNipped = !isNipped;
    }

    public void forceRelease()
    {
        takerNipper.set(false);
        isNipped = false;
    }

    public void realExtend(){
        takerExtender.set(true);
        isExtended = true;
    }
    public void realRetract(){
        takerExtender.set(false);
        isExtended = false;
    }




    /**
     * @return the isExtended
     */
    public boolean isExtended() {
        return isExtended;
    }
    
     /**
     * @return the isNipped
     */
    public static boolean isNipped() {
        return isNipped;
    }

    @Override
    protected void initDefaultCommand() {
        //setDefaultCommand(new TurnHolder(0));
    }

}

