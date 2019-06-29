/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.physics;

import java.text.DecimalFormat;

import frc.lib.Util;

/**
 * Add your docs here.
 */
public class DifferentialDrive {


    //THE FOLLOWING IDEAS FROM FRC TEAM 254
    // All units must be SI!

    // Equivalent mass when accelerating purely linearly, in kg.
    // This is "equivalent" in that it also absorbs the effects of drivetrain inertia.
    // Measure by doing drivetrain acceleration characterization in a straight line.
    protected final double mass_;

    // Equivalent moment of inertia when accelerating purely angularly, in kg*m^2.
    // This is "equivalent" in that it also absorbs the effects of drivetrain inertia.
    // Measure by doing drivetrain acceleration characterization while turning in place.
    protected final double moi_;

    // Drag torque (proportional to angular velocity) that resists turning, in N*m/rad/s
    // Empirical testing of our drivebase showed that there was an unexplained loss in torque ~proportional to angular
    // velocity, likely due to scrub of wheels.
    // NOTE: this may not be a purely linear term, and we have done limited testing, but this factor helps our model to
    // better match reality.  For future seasons, we should investigate what's going on here...
    protected final double angular_drag_;

    // Self-explanatory.  Measure by rolling the robot a known distance and counting encoder ticks.
    protected final double wheel_radius_;  // m

    // "Effective" kinematic wheelbase radius.  Might be larger than theoretical to compensate for skid steer.  Measure
    // by turning the robot in place several times and figuring out what the equivalent wheelbase radius is.
    protected final double effective_wheelbase_radius_;  // m

    private final DCMotorTransmission leftTransmission;
    private final DCMotorTransmission rightTransmission;

    public DifferentialDrive(double mass,
                            double moi,
                            double angular_drag,
                            double wheel_radius,
                            double effective_wheelbase_radius,
                            DCMotorTransmission leftTransmission,
                            DCMotorTransmission rightTransmission){
        mass_ = mass;
        moi_ = moi;
        angular_drag_ = angular_drag;
        wheel_radius_ = wheel_radius;
        effective_wheelbase_radius_ = effective_wheelbase_radius;
        this.leftTransmission = leftTransmission;
        this.rightTransmission = rightTransmission;
    }

    public double mass(){
        return mass_;
    }

    public double moi(){
        return moi_;
    }

    public double angular_drag(){
        return angular_drag_;
    }

    public double wheel_radius(){
        return wheel_radius_;
    }

    public double effective_wheelbase_radius(){
        return effective_wheelbase_radius_;
    }

    public DCMotorTransmission left_transmission(){
        return leftTransmission;
    }

    public DCMotorTransmission right_transmission(){
        return rightTransmission;
    }

    public ChassisState solveForwardKinematics(WheelState wheelState){
        ChassisState state = new ChassisState();

        state.linear = wheel_radius_ * (wheelState.left+wheelState.left)/2.0f;
        state.angular = wheel_radius_ * (wheelState.right-wheelState.left)/
                                        (2.0f*effective_wheelbase_radius_);

        return state;
    }

    public WheelState solveInverseKinematics(ChassisState chassisState){
        WheelState state = new WheelState();

        state.left = (chassisState.linear - chassisState.angular*effective_wheelbase_radius_)/ wheel_radius_;
        state.right = (chassisState.linear + chassisState.angular*effective_wheelbase_radius_)/ wheel_radius_;

        return state;
    }

    public void solveForwardDynamics(DriveDynamics dynamics){
        final boolean is_left_stationary = (leftTransmission.friction_volt()> 
        dynamics.voltage.left) && Util.epsilonEqual(dynamics.wheelVelocity.left,0);
        final boolean is_right_stationary = (rightTransmission.friction_volt()> 
        dynamics.voltage.right) && Util.epsilonEqual(dynamics.wheelVelocity.right,0);
        
        if (is_left_stationary && is_right_stationary){
            dynamics.wheel_torque.left = dynamics.wheel_torque.right = 0;
            dynamics.wheelAcceleration.left = dynamics.wheelAcceleration.right = 0;
            dynamics.chassisAcceleration.linear = dynamics.chassisAcceleration.angular = 0;
            dynamics.dcurvature = 0;
            return;
        }

        //calculate torque on both wheel
        dynamics.wheel_torque.left = leftTransmission.get_torque_for_voltage(
            dynamics.wheelVelocity.left, 
            dynamics.voltage.left);
        dynamics.wheel_torque.right = rightTransmission.get_torque_for_voltage(
            dynamics.wheelVelocity.right, 
            dynamics.voltage.right);

        //calculate acceleration(s)

        //linear acceleration: a = F / m = (Tl + Tr) / wr / m
        dynamics.chassisAcceleration.linear = (dynamics.wheel_torque.left +
                                                dynamics.wheel_torque.right)/
                                                wheel_radius_ / mass_;
        
        //angular acceleration: a = T / I = ((Tr - Tl) / wr * wb - omega * k)/ I
        dynamics.chassisAcceleration.angular = 
        ((dynamics.wheel_torque.left - dynamics.wheel_torque.right) /
        wheel_radius_ * effective_wheelbase_radius_ - angular_drag_ 
        * dynamics.chassisVelocity.angular)/ moi_;
        
        //angular acceleration = accel * k + dk/dx * v^2
        //dk/dx = (ang_acc - accel * k)/v^2

        dynamics.dcurvature = (dynamics.chassisAcceleration.angular 
            - dynamics.chassisAcceleration.linear 
            * dynamics.curvature) / dynamics.chassisVelocity.linear
            /dynamics.chassisVelocity.linear;


        dynamics.wheel_torque.left = dynamics.chassisAcceleration.linear
        - dynamics.chassisAcceleration.angular * effective_wheelbase_radius_;

        dynamics.wheel_torque.right = dynamics.chassisAcceleration.linear
        + dynamics.chassisAcceleration.angular * effective_wheelbase_radius_;
    }



    

    public static class WheelState{
        
        //depends on situation, may be interpreted as velocity or acceleration
        public double left;
        public double right;

        public WheelState(double left,double right){
            this.left = left;
            this.right = right;
        }

        public WheelState(){}

        public String toString(){
            DecimalFormat fmt = new DecimalFormat("0.000");
            return "(" + fmt.format(left) + "," + fmt.format(right) + ")";
        }
    }

    public static class ChassisState{

        public double linear;
        public double angular;

        public ChassisState(double linear,double angular){
            this.linear = linear;
            this.angular = angular;
        }

        public ChassisState(){}

        public String toString(){
            DecimalFormat fmt = new DecimalFormat("0.000");
            return "(" + fmt.format(linear) + "," + fmt.format(angular) + ")";
        }

    }

    public static class DriveDynamics{
        public double curvature = 0.0;
        public double dcurvature = 0.0;
        public ChassisState chassisVelocity;
        public ChassisState chassisAcceleration;
        public WheelState wheelVelocity;
        public WheelState wheelAcceleration;
        public WheelState voltage;
        public WheelState wheel_torque;



    }



}
