/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.path;

import java.util.ArrayList;
import java.util.List;

import frc.lib.geometry.Pose2d;
import frc.lib.geometry.PoseWithCurvature;
import frc.lib.geometry.Translation2d;

/**
 * Add your docs here.
 */
public class LineCurveGenerator {

    double[][] wayPoints;
    double[] radius;
    List <PoseWithCurvature> poseList = new ArrayList<PoseWithCurvature>();

    LineCurveGenerator(double[][] wayPoints){
        this.wayPoints = wayPoints;
        int num = wayPoints[0].length;
        radius = new double[num];
        for (int i = 0; i < num; ++i){
            radius[i] = 0.4;
        }
    }

    LineCurveGenerator(double[][] wayPoints,double radius){
        this.wayPoints = wayPoints;
        int num = wayPoints[0].length;
        this.radius = new double[num];
        for (int i = 0; i < num; ++i){
            this.radius[i] = radius;
        }
    }

    public void generate(){
    //TODO: FINISH THIS!!!






    }

    //TODO: TEST THIS!!!!
    public Line generate(Line a,Line b, double r){
        double a_dot_b = a.end.getRotation().dot(b.start.getRotation());
        double d = r / Math.sqrt((1-a_dot_b)/(1+a_dot_b));

        // start1 -> end1 - d*theta1 | curve 
        Line inBound,outBound;
        Curve curve = new Curve();

        Translation2d arc_cut_a = new Translation2d(
                a.start.getTranslation(),a.end.getTranslation());
        arc_cut_a.scale(-d/arc_cut_a.norm());

        Translation2d arc_cut_b = new Translation2d(
                b.start.getTranslation(),b.end.getTranslation());
        arc_cut_b.scale(d/arc_cut_b.norm());
        
        curve.start = new Pose2d(a.end.getTranslation().add(arc_cut_a),a.end.getRotation());
        inBound = new Line(a.start.getTranslation(), a.end.getTranslation().add(arc_cut_a));
        
        curve.end = new Pose2d(b.start.getTranslation().add(arc_cut_b),b.start.getRotation());
        outBound = new Line(b.start.getTranslation().add(arc_cut_b),b.end.getTranslation());

        poseList.addAll(inBound.interpolate(0.02));
        poseList.addAll(curve.interpolate(0.02));
        
        //return the outBound Line
        return outBound;
    }





}
