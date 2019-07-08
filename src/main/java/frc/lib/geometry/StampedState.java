/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.geometry;

/**
 * Add your docs here.
 */
public class StampedState<T> {
    
    protected T state;
    protected double time_stamp; //  time assiciated with this pose, seconds

    public StampedState(T state,double time_stamp){
        this.state = state;
        this.time_stamp = time_stamp;
    }

    public T getState(){
        return this.state;
    }

    public double getTime(){
        return this.time_stamp;
    }

}
