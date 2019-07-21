/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.geometry;

import frc.lib.physics.DifferentialDrive.ChassisState;

/**
 * Add your docs here.
 */
public class StampedState<T extends PoseWithCurvature> {
    
    protected T state;
    protected double t; //  time assiciated with this pose, seconds
    protected double v; //  time assiciated with this pose, m/s
    protected double s; //  time assiciated with this pose, seconds

    public StampedState(T state,double t,double v,double s){
        this.state = state;
        this.t = t;
        this.v = v;
        this.s = s;
    }

    public T getState(){
        return this.state;
    }

    public double t(){
        return this.t;
    }

    public double v(){
        return this.v;
    }

    public double s(){
        return this.s;
    }

    public ChassisState toChassisState(){
        return new ChassisState(v, v *state.curvature);
    }

    public String toString(){
        String s = "";
        s += "distance:\t";
        s += s;
        s += "\ntime:\t";
        s += t;
        s += "\nvelocity:\t";
        s += v;
        s += "\n";
        return s;

    }

}
