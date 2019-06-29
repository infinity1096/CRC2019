/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.physics;

import frc.lib.Util;

/**
 * Add your docs here.
 */
public class DCMotorTransmission {

    private double speed_per_volt_; // rad/s / V
    private double torque_per_volt_; // N*m / V 
    private double friction_volt_; // Voltage needed to overcome friction

    public DCMotorTransmission(final double speed_per_volt,
                        final double torque_per_volt, 
                        final double friction_volt){
        this.speed_per_volt_ = speed_per_volt;
        this.torque_per_volt_ = torque_per_volt;
        this.friction_volt_ = friction_volt;
    }

    public double speed_per_volt(){
        return this.speed_per_volt_;
    }

    public double torque_per_volt(){
        return torque_per_volt_;
    }

    public double friction_volt(){
        return friction_volt_;
    }

    public double free_speed_at_voltage(double voltage){
        if (voltage > Util.kEpsilon){
            return (Math.max(0.0d,(voltage - friction_volt_) *speed_per_volt_));
        }else if (voltage < -Util.kEpsilon){
            return (Math.min(0.0d,(voltage + friction_volt_) *speed_per_volt_));
        }else{
            return 0.0d;
        }
    }

    public double get_torque_for_voltage(double output_speed,double voltage){
        if (output_speed > Util.kEpsilon){
            //forward motion, rolling friction
            return (voltage - friction_volt_ - output_speed / speed_per_volt_) * torque_per_volt_;
        } else if (output_speed < -Util.kEpsilon){
            //reverse motion
            return (voltage + friction_volt_- output_speed / speed_per_volt_) * torque_per_volt_;
        } else if (voltage > Util.kEpsilon){
            //static, forward torque
            return Math.max(0,voltage - friction_volt_) * torque_per_volt_;
        } else if (voltage < -Util.kEpsilon){
            return Math.min(0,voltage + friction_volt_) * torque_per_volt_;
        }else{
            return 0;
        }
    }

    public double get_voltage_for_torque(double outputspeed,double torque){
        if (outputspeed > Util.kEpsilon){
            return (outputspeed / speed_per_volt_ + (torque + friction_volt_) / torque_per_volt_);
        }else if (outputspeed < -Util.kEpsilon){
            return (outputspeed / speed_per_volt_ + (torque - friction_volt_) / torque_per_volt_);
        }else if (torque > Util.kEpsilon){
            return (outputspeed / speed_per_volt_ + (torque + friction_volt_) / torque_per_volt_); 
        }else if (torque < -Util.kEpsilon){
            return (outputspeed / speed_per_volt_ + (torque - friction_volt_) / torque_per_volt_);
        }else {
            return 0;
        }
    }
}
