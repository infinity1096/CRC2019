/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.trajectory;

import java.util.List;

import frc.lib.geometry.PoseWithCurvature;
import frc.lib.geometry.StampedState;

/**
 * Add your docs here.
 */
public class TrajectoryGenerator {

    private DistanceView traj;

    

    public TrajectoryGenerator(){

    }

    void load(List<PoseWithCurvature> list){
        this.list = list;
    }


    List<StampedState<PoseWithCurvature>> generate(){
        




        return null;
    }


    
}
