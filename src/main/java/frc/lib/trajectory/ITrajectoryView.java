/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.trajectory;

import frc.lib.geometry.PoseWithCurvature;

/**
 * Add your docs here.
 */
public interface ITrajectoryView {

    PoseWithCurvature sample(double x);

    PoseWithCurvature getFirstPoint();

    PoseWithCurvature getLastPoint();
    
}
