/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.geometry;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import frc.lib.Util;

/**
 * Add your docs here.
 */
public class PoseWithCovariance {
    protected Pose2d pose;

    /**
     * Cxx , Cxy , Cxt
     * Cyx , Cyy , Cyt
     * Ctx , Cty , Ctt
     */
    protected RealMatrix covariance;

    public PoseWithCovariance(){
        this.pose = new Pose2d();
        this.covariance = MatrixUtils.createRealMatrix(
            new double[][] { 
                { 0, 0, 0 }, 
                { 0, 0, 0 }, 
                { 0, 0, 0 } });
        covariance.add(MatrixUtils.createRealIdentityMatrix(3)
        ).scalarMultiply(Util.kEpsilon);
    }

    public PoseWithCovariance(Pose2d pose, RealMatrix covariance){
        this.pose = pose;
        this.covariance = covariance;
    }

    public PoseWithCovariance(Pose2d pose, double[][] covariance){
        this.pose = pose;
        this.covariance = MatrixUtils.createRealMatrix(covariance);
    }

    public PoseWithCovariance(Pose2d pose, double[] covariance){
        this.pose = pose;
        double [][] covariance_ = new double[][] { 
            { 0, 0, 0 }, 
            { 0, 0, 0 }, 
            { 0, 0, 0 } };
        for (int i = 0; i < 3; i++){
            covariance_[i][i] = covariance[i];
        }
        this.covariance = MatrixUtils.createRealMatrix(covariance_);
    }

    public PoseWithCovariance(Translation2d translation,Rotation2d rotation, double[] covariance){
        this.pose = new Pose2d(translation,rotation);
        double [][] covariance_ = new double[][] { 
            { 0, 0, 0 }, 
            { 0, 0, 0 }, 
            { 0, 0, 0 } };
        for (int i = 0; i < 3; i++){
            covariance_[i][i] = covariance[i];
        }
        this.covariance = MatrixUtils.createRealMatrix(covariance_);
    }

    //tested
    public PoseWithCovariance combine(PoseWithCovariance other){
        
        RealVector pose1 = this.getVector();
        RealVector pose2 = other.getVector();

        RealMatrix K = new LUDecomposition(this.covariance.add(other.covariance))
                .getSolver().getInverse().preMultiply(
                this.covariance
        );

        System.out.println(K);

        RealVector pose_combined = pose1.add(K.preMultiply(pose2.subtract(pose1)));
        RealMatrix covariance_ =  
                covariance.add(
                    K.preMultiply(covariance).scalarMultiply(-1)
                );
                
        return new PoseWithCovariance(
            new Pose2d(pose_combined.toArray()[0], 
                    pose_combined.toArray()[1], 
                    Rotation2d.fromRad(pose_combined.toArray()[2])),
            covariance_);
    }

    //tested
    public ArrayRealVector getVector(){
        return (new ArrayRealVector(new double[]{pose.translation.x,pose.translation.y,pose.rotation.getRad()}));
    }

    public String toString(){
        String s = pose.toString();
        s += "Covariance: ";
        s += covariance.toString();
        return s;
    }

}
