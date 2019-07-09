/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.trajectory;

import java.awt.Point;
import java.util.List;

import frc.lib.Util;
import frc.lib.geometry.PoseWithCurvature;

/**
 * Add your docs here.
 */
public class DistanceView implements ITrajectoryView{

    protected Trajectory trajectory;
    protected double[] distance;

    public DistanceView(Trajectory trajectory){
        this.trajectory = trajectory;
        this.distance = new double[trajectory.points.size()];
        this.distance[0] = 0;
        for (int i = 1; i < trajectory.points.size(); ++i){
            distance[i] = distance[i-1] + 
                    trajectory.getState(i).distance(trajectory.getState(i-1));
        }
    }

    @Override
    public PoseWithCurvature sample(double distance) {
        for (int i = 0; i < trajectory.points.size(); ++i){
            if (this.distance[i] > distance){
                //found!
                if (Util.epsilonEqual(distance, this.distance[i])){
                    return trajectory.getState(i);
                }else{
                    return trajectory.getState(i-1).intrapolate(
                            trajectory.getState(i), 
                            (distance - this.distance[i-1]) / (this.distance[i] - this.distance[i-1]));
                }
            }
        }
        throw new RuntimeException();
    }

    @Override
    public double getFirstPoint() {
        return distance[0];
    }

    @Override
    public double getLastPoint() {
        return distance[trajectory.points.size()-1];
    }


}
