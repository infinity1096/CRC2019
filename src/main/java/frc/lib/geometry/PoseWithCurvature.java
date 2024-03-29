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
public class PoseWithCurvature {

    protected Pose2d pose;
    protected double curvature;
    protected double dcurvature;

    public PoseWithCurvature(Pose2d pose,double curvature,double dcurvature){
        this.pose = pose;
        this.curvature = curvature;
        this.dcurvature = dcurvature;
    }

    public PoseWithCurvature(Translation2d translation, Rotation2d rotation){
        this.pose = new Pose2d(translation,rotation);
        this.curvature = 0;
        this.dcurvature = 0;
    }


    public PoseWithCurvature(Translation2d translation){
        this.pose = new Pose2d(translation);
        this.curvature = 0;
        this.dcurvature = 0;
    }

    public Pose2d pose(){
        return pose;
    }

    public double curvature(){
        return curvature;
    }

    public void curvature(double input){
        this.curvature = input;
    }

    public double dcurvature(){
        return dcurvature;
    }

    public double distance(PoseWithCurvature other){
        return Math.hypot(this.pose.translation.x() - other.pose.translation.x,
                this.pose.translation.y() - other.pose.translation.y());
    }

    public PoseWithCurvature intrapolate(PoseWithCurvature other, double x){
        return new PoseWithCurvature(pose.interpolate(other.pose, x), 
                (1-x) * curvature + x * other.curvature,
                (1-x) * dcurvature + x * other.dcurvature);
    }

    public String toString(){
        String s = pose.toString();
        s += "Curvature: ";
        s += curvature;
        s += "dCurvature: ";
        s += dcurvature;
        return s;
    }

    public String toString2(){
        String s = pose.translation.x + "\t" + pose.translation.y; 
        return s;
    }

}
