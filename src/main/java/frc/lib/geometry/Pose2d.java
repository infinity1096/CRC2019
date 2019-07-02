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
public class Pose2d {

    //pose consists two parts: rotation(direction) and translation
    protected Rotation2d rotation;
    protected Translation2d translation;

    public static double Keps = 1e-9;

    public Rotation2d getRotation(){
        return this.rotation;
    }

    public Translation2d getTranslation(){
        return this.translation;
    }

    public Pose2d(){
        this.translation = new Translation2d();
        this.rotation = new Rotation2d();
    }

    public Pose2d(double x, double y, Rotation2d rotation){
        this.translation = new Translation2d(x,y);
        this.rotation = rotation;
    }

    public Pose2d(Translation2d translation, Rotation2d rotation){
        this.translation = translation;
        this.rotation = rotation;
    }

    public Pose2d(Pose2d other){
        this.translation = other.translation;
        this.rotation = other.rotation;
    }

    public Pose2d (Translation2d translation){
        this.translation = translation;
        this.rotation = new Rotation2d();
    }

    public Pose2d (Rotation2d rotation){
        this.translation = new Translation2d();
        this.rotation = rotation;
    }

    /**
     * Obtain a new Pose2d from a (constant curvature) velocity. See:
     * https://github.com/strasdat/Sophus/blob/master/sophus/se2.hpp
     * -from 254 2018 code
     * 
     * This document may help you understand the math(Lie group) behind:
     * http://ethaneade.com/lie.pdf
     * see part: SO2, se2
     * -added by 5449
     * 
     */
    public static Pose2d exp(Twist2d twist){
        double dtheta = twist.dtheta;   
        double cos_value,sin_value;
        
        if (Math.abs(dtheta) < Keps){
            /** When dtheta is relatively small, it's better to approxmate
             *  sin/theta and (1-cos)/theta value using taylor series.
             *  the first 3 order terms would be enough(dtheta exponent shrinks quickly)
            */
            cos_value = 1 - 1/6.0d * dtheta * dtheta;
            sin_value = 0.5 * dtheta;
        } else{
            cos_value = 1 - Math.cos(dtheta) / dtheta;
            sin_value = Math.sin(dtheta) / dtheta;
        }

        return new Pose2d(
                new Translation2d(
                        cos_value * twist.dx - sin_value * twist.dy, 
                        cos_value * twist.dx + sin_value * twist.dy),
                new Rotation2d(dtheta));
    }

    //Inverse of above
    //TODO: Havent fully understood how this works
    public static Twist2d log(Pose2d transform) {
        final double dtheta = transform.getRotation().getRad();
        final double half_dtheta = 0.5 * dtheta;
        final double cos_minus_one = transform.getRotation().cos() - 1.0;
        double halftheta_by_tan_of_halfdtheta;
        if (Math.abs(cos_minus_one) < Keps) {
            halftheta_by_tan_of_halfdtheta = 1.0 - 1.0 / 12.0 * dtheta * dtheta;
        } else {
            halftheta_by_tan_of_halfdtheta = -(half_dtheta * transform.getRotation().sin()) / cos_minus_one;
        }
        final Translation2d translation_part = transform.getTranslation()
                .rotateBy(new Rotation2d(halftheta_by_tan_of_halfdtheta, -half_dtheta, false));
        return new Twist2d(translation_part.x, translation_part.y, dtheta);
    }

    public String toString(){
        String s = "Pose:\n";
        s += translation.toString();
        s += rotation.toString();
        return s;
    }

}
