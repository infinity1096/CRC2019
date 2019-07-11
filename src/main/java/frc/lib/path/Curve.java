/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.path;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import frc.lib.geometry.Pose2d;
import frc.lib.geometry.PoseWithCurvature;
import frc.lib.geometry.Rotation2d;
import frc.lib.geometry.Translation2d;

/**
 * Add your docs here.
 */
public class Curve {

    protected Pose2d start;
    protected Pose2d end;

    public Curve(){
        
    }
    
    @Deprecated //two vectors and their direction over-defined the arc.
                //User MUST ensure both pose belongs to the arc. 
    public Curve(Pose2d start, Pose2d end){
        this.start = start;
        this.end = end;
    }


    public List<PoseWithCurvature> interpolate(double dtheta){

        RealVector theta1 = new ArrayRealVector(start.RottoArray());
        RealVector theta2 = new ArrayRealVector(end.RottoArray());

        RealVector Pos1 = new ArrayRealVector(start.TranstoArray());
        RealVector Pos2 = new ArrayRealVector(end.TranstoArray());

        RealMatrix matrix = new Array2DRowRealMatrix(2,2);
        matrix.setColumnVector(0, theta1);
        matrix.setColumnVector(1, theta2);

        RealMatrix matrix2 = new Array2DRowRealMatrix(1,2);
        matrix2.setEntry(0, 0, theta1.dotProduct(Pos1));
        matrix2.setEntry(0, 1, theta2.dotProduct(Pos2));

        RealMatrix mat1inv = new LUDecomposition(matrix).getSolver().getInverse();
        
        double[] center = mat1inv.preMultiply(matrix2).getRow(0); //solve for center of circle
        double r = Math.hypot(center[0]-start.TranstoArray()[0], center[1]-start.TranstoArray()[1]);
        
        Translation2d center_to_startpos = new Translation2d(start.getTranslation().x() - center[0], 
                                                            start.getTranslation().y() - center[1]);
        Translation2d center_ = new Translation2d(center[0],center[1]);

        int num = (int) (Math.ceil(Math.abs(end.getRotation().angleDiff(start.getRotation())) / dtheta) + 1);

        List<PoseWithCurvature> list = new ArrayList<>(num);

        double curve = 1/r;

        if (!(end.getRotation().angleDiff(start.getRotation()) > 0)){
            curve = -1/r;
        }

        
        for (int i = 0; i <= num; ++i){
            Rotation2d rot = start.getRotation().interpolate(end.getRotation(), (double)i/(double)num);
            PoseWithCurvature pose_ =  new PoseWithCurvature(
                center_to_startpos.rotateBy(rot.rotateBy(start.getRotation().inverse())).add(center_),
                rot
            );
            pose_.curvature(curve);
            list.add(pose_);
        }
        return list;
    }


}
