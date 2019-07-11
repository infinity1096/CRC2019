/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.trajectory.constraints;

import frc.lib.Util;
import frc.lib.geometry.PoseWithCurvature;
import frc.lib.physics.DifferentialDrive.ChassisState;

/**
 * Add your docs here.
 */
public class CentripetalAccelerationConstraint implements IConstraints<PoseWithCurvature>{

    private double maxCentripetalAcceleration;

    public CentripetalAccelerationConstraint(double maxCentripetalAcceleration){
        this.maxCentripetalAcceleration = maxCentripetalAcceleration;
    }

    @Override
    public MinMax getMaxSpeed(PoseWithCurvature state) {
        if (Util.epsilonEqual(state.curvature(),0)){
            
            return new MinMax();
        }else{
            double curvature_ = Math.abs(state.curvature());
            return new MinMax(-Math.sqrt(maxCentripetalAcceleration / curvature_),
                Math.sqrt(maxCentripetalAcceleration / curvature_));
        }
    }

    @Override
    public MinMax getMaxAcceleration(ChassisState chassisState, PoseWithCurvature state) {
        return MinMax.kNoLimits;
    }

}
