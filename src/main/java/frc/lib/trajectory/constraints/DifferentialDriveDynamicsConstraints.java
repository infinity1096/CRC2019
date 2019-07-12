/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.trajectory.constraints;


import frc.lib.geometry.PoseWithCurvature;
import frc.lib.physics.DifferentialDrive;
import frc.lib.physics.DifferentialDrive.ChassisState;

/**
 * Add your docs here.
 */
public class DifferentialDriveDynamicsConstraints implements IConstraints<PoseWithCurvature> {

    double maxVoltage;
    DifferentialDrive drive;

    public DifferentialDriveDynamicsConstraints(DifferentialDrive drive,double maxVoltage){
        this.drive = drive;
        this.maxVoltage = maxVoltage;
    }

    @Override
    public MinMax getMaxSpeed(PoseWithCurvature state) {
        return new MinMax(-drive.getMaxVelocity(state.curvature(), maxVoltage),
                drive.getMaxVelocity(state.curvature(), maxVoltage));
    }

    @Override
    public MinMax getMaxAcceleration(ChassisState chassisState,PoseWithCurvature state) {

        return drive.getMaxAccelcration(chassisState, state.curvature(), maxVoltage);
    }

}
