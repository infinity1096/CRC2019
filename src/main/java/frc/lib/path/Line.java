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
public class Line {

    protected Pose2d start;
    protected Pose2d end;

    public Line(Pose2d start,Pose2d end){
        this.start = start;
        this.end = end;
    }

    public List<PoseWithCurvature> interpolate(double dx){
       double l =  (new Translation2d(start.getTranslation(),end.getTranslation())).norm();

       int num = (int) Math.ceil(l / dx) + 1;

       System.out.println(num);

        List<PoseWithCurvature> list = new ArrayList<>(num);

        for (int i = 0; i < num+1; ++i){
            list.add(new PoseWithCurvature(start.getTranslation().interpolate(
                    end.getTranslation(), (double)i/(double)num)));
        }
        return list;
    }

}
