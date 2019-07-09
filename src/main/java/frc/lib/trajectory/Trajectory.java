/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.trajectory;

import java.util.ArrayList;
import java.util.List;

import frc.lib.geometry.PoseWithCurvature;

/**
 * Add your docs here.
 */
public class Trajectory {

    protected List<PoseWithCurvature> points;

    public Trajectory (List<PoseWithCurvature> list){
        points = new ArrayList<PoseWithCurvature>(list.size());
        for (int i = 0; i < list.size(); ++i){
            points.add(list.get(i));
        }
    }
    
    public PoseWithCurvature getFirstPoint() {
        return points.get(0);
    }

    public PoseWithCurvature getLastPoint() {
        return points.get(points.size()-1);
    }

    public PoseWithCurvature getState(int index){
        return this.points.get(index);
    }

    public PoseWithCurvature interpolate(double index){
        if (index < 0){
            return getFirstPoint();
        } else if (index > points.size()){
            return getLastPoint();
        } else {
            int whole = (int)Math.floor(index);
            double frac = index - whole;

            return points.get(whole).intrapolate(
                    points.get(whole+1), frac);
        }
    }

}
