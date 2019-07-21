/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.geometry;

import frc.lib.Util;

/**
 * Add your docs here.
 */
public class Twist2d {

    double dx;//m
    double dy;//m
    double dtheta;//rad

    public Twist2d (double dx, double dy, double dtheta){
        this.dx = dx;
        this.dy = dy;
        this.dtheta = dtheta;
    }

    public double norm (){
        return Math.hypot(dx, dy);
    }

    public double getCurvature(){
        if(!Util.epsilonEqual(norm(),0)){
            return dtheta / norm();
        }else{
            return 0;
        }
    }

}
