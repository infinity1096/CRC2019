/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.geometry;

/**
 * Add your docs here.
 */
public class Pose2d {

    //pose consists two parts: rotation(direction) and translation
    protected Rotation2d rotation;
    protected Translation2d translation;

    public Rotation2d getRotation(){
        return this.rotation;
    }

    public Translation2d getTranslation(){
        return this.translation;
    }

    


}
