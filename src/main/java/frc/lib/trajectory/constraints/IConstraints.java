/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.trajectory.constraints;

/**
 * Add your docs here.
 */
public interface IConstraints<S> {

    public double getMaxSpeed(S state);
    public MinMaxAccelcration getMaxAcceleration(double velocity, S state);

    public static class MinMaxAccelcration{
        
        double minAcceleration;
        double maxAcceleration;

        public static MinMaxAccelcration kNoLimits = new MinMaxAccelcration();

        public MinMaxAccelcration(){
            this.minAcceleration = Double.NEGATIVE_INFINITY;
            this.maxAcceleration = Double.POSITIVE_INFINITY;
        }

        public MinMaxAccelcration(double minAcceleration,double maxAcceleration){
            this.minAcceleration = minAcceleration;
            this.maxAcceleration = maxAcceleration;
        }

        public double minAcceleration(){
            return this.minAcceleration;
        }

        public double maxAcceleration(){
            return this.maxAcceleration;
        }
    }


}
