/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.geometry;

import frc.lib.Util;

/**
 * Add your docs here.
 */
public class Rotation2d implements IRotation {

    protected double cos_angle;
    protected double sin_angle;

    public static Rotation2d kIdentity = new Rotation2d();

    public Rotation2d (){
        this.cos_angle = 1;
        this.sin_angle = 0;
    }

    public Rotation2d (double x, double y, boolean normalize){
        if (normalize){
            double mag = Math.hypot(x, y);
            if (mag > Util.kEpsilon){
                this.cos_angle /= mag;
                this.sin_angle /= mag;
            }else{
                this.cos_angle = 1;
                this.sin_angle = 0;
            }
        }else{
            this.cos_angle = x;
            this.sin_angle = y;
        }
    }

    public Rotation2d (Rotation2d other){
        this.cos_angle = other.cos_angle;
        this.sin_angle = other.sin_angle;
    }

    public Rotation2d (Translation2d translation){
        this(translation.x,translation.y,true);
    }

    public Rotation2d fromRad(double Rad){
        return new Rotation2d(
                Math.cos(Rad),
                Math.sin(Rad),
                false);
    }

    public Rotation2d fromDeg(double Deg){
        return fromRad(Math.toRadians(Deg));
    }

    public double sin(){
        return this.sin_angle;
    }

    public double cos(){
        return this.cos_angle;
    }

    public double tan(){
        if (Math.abs(cos_angle) < Util.kEpsilon){
            if (sin_angle >= 0){
                return Double.POSITIVE_INFINITY;
            }else{
                return Double.NEGATIVE_INFINITY;
            }
        }else{
            return sin_angle / cos_angle;
        }
    }

    public double getRad(){
        return Math.atan2(sin_angle, cos_angle);
    }

    public Rotation2d RotateBy(Rotation2d other){
        //Rotation Matrix: [cos -sin]
        //                 [sin  cos]
        return new Rotation2d(
                cos_angle * other.cos_angle - sin_angle * other.sin_angle,
                sin_angle * other.cos_angle + cos_angle * other.sin_angle,
                true);
    }

    public Rotation2d inverse(){
        return new Rotation2d(cos_angle,-sin_angle,false);
    }

    public Rotation2d getRotation(){
        return this;
    }


}
