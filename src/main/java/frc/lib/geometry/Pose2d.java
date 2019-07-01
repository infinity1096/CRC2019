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

    public String toString(){
        String s = "Pose:\n";
        s += translation.toString();
        s += rotation.toString();
        return s;
    }

}
