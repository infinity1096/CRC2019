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
    public MinMax getMaxAcceleration(double velocity, S state);

    public static class MinMax{
        
        public double min;
        public double max;

        public static MinMax kNoLimits = new MinMax();

        public MinMax(){
            this.min = Double.NEGATIVE_INFINITY;
            this.max = Double.POSITIVE_INFINITY;
        }

        public MinMax(double min,double max){
            this.min = min;
            this.max = max;
        }

        public MinMax intersect(MinMax other){
            double min, max;
            min = Math.max(this.min,other.min);
            max = Math.min(this.max,other.max);
            return new MinMax(min,max);
        }

        public MinMax sclarAdd(double sclar){
            return new MinMax(this.min + sclar,this.max + sclar);
        }

        public MinMax scale(double sclar){
            if (Math.signum(sclar) == 1) {
                return new MinMax(this.min * sclar, this.max * sclar);
            }else{
                return new MinMax(this.max * sclar, this.min * sclar);
            }
        }

        public double min(){
            return this.min;
        }

        public double max(){
            return this.max;
        }

        public String toString(){
            String s = "";
            s += this.min;
            s += "\t";
            s += this.max;
            s += "\n";
            return s;
        }

    }


}
