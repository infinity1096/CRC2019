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
public class Translation2d implements ITranslation{

    protected double x;
    protected double y;

    public static Translation2d kIdentity = new Translation2d();

    public Translation2d(){
        x = 0;
        y = 0;
    }

    public Translation2d(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Translation2d(Translation2d other){
        this.x = other.x;
        this.y = other.y;
    }

    public Translation2d(Translation2d start,Translation2d end){
        this.x = end.x - start.x;
        this.y = end.y - start.y;
    }

    public Translation2d scale(double s){
        return new Translation2d(s*x,s*y);
    }

    public Translation2d inverse(){
        return scale(-1);
    }

    
    public Translation2d add(Translation2d other){
        return new Translation2d(
                this.x + other.x,
                this.y + other.y);
    }

    public double dot(Translation2d other){
        return this.x * other.x + this.y * other.y;
    }

    public double cross(Translation2d other){
        return this.x * other.y - this.y * other.x;
    }

    public double norm(){
        return Math.hypot(x,y);
    }

    public double norm2(){
        return x*x + y*y;
    }

    public Translation2d interpolate(Translation2d other, double p){
        if (p >= 1){
            return other;
        }else if (p <= 0){
            return this;
        }else{
            return extrapolate(other, p);
        }
    }

    public Translation2d extrapolate(Translation2d other, double p){
        return new Translation2d(p * (other.x - x),p* (other.y-y));
    }
    
    public Translation2d rotateBy(Rotation2d rotation){
        //Rotation Matrix: [cos -sin]
        //                 [sin  cos]
        return new Translation2d(
            rotation.cos_angle * x - rotation.sin_angle * y,
            rotation.sin_angle * x + rotation.cos_angle * y);
    }

    @Override
    public Translation2d getTranslation2d() {
        return this;
    }



}
