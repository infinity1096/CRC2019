/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.lib.physics;

import java.text.DecimalFormat;
import java.util.Arrays;

import frc.lib.Util;
import frc.lib.trajectory.constraints.IConstraints.MinMax;

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

    //tested
    public ChassisState solveForwardKinematics(WheelState wheelState){
        ChassisState state = new ChassisState();

        state.linear = wheel_radius_ * (wheelState.left+wheelState.right)/2.0f;
        state.angular = wheel_radius_ * (wheelState.right-wheelState.left)/
                                        (2.0f*effective_wheelbase_radius_);

        return state;
    }

    //tested
    public WheelState solveInverseKinematics(ChassisState chassisState){
        WheelState state = new WheelState();

        state.left = (chassisState.linear - chassisState.angular*effective_wheelbase_radius_)/ wheel_radius_;
        state.right = (chassisState.linear + chassisState.angular*effective_wheelbase_radius_)/ wheel_radius_;

        return state;
    }

    /**
     * 
     * @param dynamics : need speed and votage
     */ 
    //tested - 6/30/2019
    public void solveForwardDynamics(DriveDynamics dynamics){
        dynamics.chassisVelocity = solveForwardKinematics(dynamics.wheelVelocity);
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
        ((dynamics.wheel_torque.right - dynamics.wheel_torque.left) /
        wheel_radius_ * effective_wheelbase_radius_ - angular_drag_ 
        * dynamics.chassisVelocity.angular)/ moi_;

        

        //curvature = omega / v

        dynamics.curvature = dynamics.chassisVelocity.angular / dynamics.chassisVelocity.linear;
        
        //angular acceleration = accel * k + dk/dx * v^2
        //dk/dx = (ang_acc - accel * k)/v^2

        dynamics.dcurvature = (dynamics.chassisAcceleration.angular 
            - dynamics.chassisAcceleration.linear 
            * dynamics.curvature) / dynamics.chassisVelocity.linear
            /dynamics.chassisVelocity.linear;

        
        
        dynamics.wheelAcceleration.left = dynamics.chassisAcceleration.linear
        - dynamics.chassisAcceleration.angular * effective_wheelbase_radius_;

        dynamics.wheelAcceleration.right = dynamics.chassisAcceleration.linear
        + dynamics.chassisAcceleration.angular * effective_wheelbase_radius_;
    }

    //tested 
    public DriveDynamics solveInverseDynamics(final ChassisState chassis_velocity, final ChassisState
            chassis_acceleration) {
        
        DriveDynamics dynamics = new DriveDynamics();

        dynamics.chassisVelocity = chassis_velocity;
        dynamics.chassisAcceleration = chassis_acceleration;

        dynamics.curvature = dynamics.chassisVelocity.angular / dynamics.chassisVelocity.linear;//pass

        dynamics.wheelAcceleration = solveInverseKinematics(chassis_acceleration);//pass
        dynamics.wheelVelocity = solveInverseKinematics(chassis_velocity);//pass

        dynamics.dcurvature = (dynamics.chassisAcceleration.angular - dynamics.chassisAcceleration.linear*dynamics.curvature)
        / (dynamics.chassisVelocity.linear * dynamics.chassisVelocity.linear);//pass

        solveInverseDynamics(dynamics);

        return dynamics;
    }

    //tested
    public void solveInverseDynamics(DriveDynamics dynamics) {
        dynamics.wheel_torque.left = wheel_radius_ / 2*( (-dynamics.chassisAcceleration.angular *moi_ - dynamics.chassisVelocity.angular *
        angular_drag_)/ effective_wheelbase_radius_ + mass_ *dynamics.chassisAcceleration.linear);
        
        dynamics.wheel_torque.right = wheel_radius_/2*( (dynamics.chassisAcceleration.angular *moi_ + dynamics.chassisVelocity.angular *
        angular_drag_)/ effective_wheelbase_radius_ + mass_ *dynamics.chassisAcceleration.linear);

        dynamics.voltage.left = leftTransmission.get_voltage_for_torque(dynamics.wheelVelocity.left, dynamics.wheel_torque.left);
        dynamics.voltage.right = rightTransmission.get_voltage_for_torque(dynamics.wheelVelocity.right, dynamics.wheel_torque.right);
    }

    public double getMaxVelocity(double curvature,double voltage){
        
        //1. get max speed for left and right drivetrain
        double Vl_max = leftTransmission.free_speed_at_voltage(voltage);
        double Vr_max = rightTransmission.free_speed_at_voltage(voltage);

        //2. suppose left drivetrain is now at full speed, calculate the speed of the right drive train
        //  using the curvature constraint: (Vr - Vl) / (2Wb*(Vr + Vl)) = k
        // -> Vr = (1 + k * Wb) / (1 - k * Wb) * Vl
        double Vr_if_Vl_max = (1 + curvature * effective_wheelbase_radius_)
                /(1 - curvature * effective_wheelbase_radius_) * Vl_max;

        if (Math.abs(Vr_if_Vl_max) < Vr_max + Util.kEpsilon){
            //left side is constraint
            return (Vr_if_Vl_max + Vl_max) / 2.0d * wheel_radius_;
        }else{
            //right side is constraint
            double Vl_if_Vr_max = (1 - curvature * effective_wheelbase_radius_)
            /(1 + curvature * effective_wheelbase_radius_) * Vr_max;
            return (Vl_if_Vr_max + Vr_max) / 2.0d * wheel_radius_;
        }
        
    }

    public MinMax getMaxAccelcration(ChassisState chassisVelocity,double curvature, double maxVoltage){

        WheelState wheelVelocity = solveInverseKinematics(chassisVelocity);
        MinMax minmaxAccel = new MinMax();
        MinMax leftTorque, rightTorque;

        leftTorque = new MinMax(
                leftTransmission.get_torque_for_voltage(
                        wheelVelocity.left, -maxVoltage),
                leftTransmission.get_torque_for_voltage(
                        wheelVelocity.left, maxVoltage));
        
        rightTorque = new MinMax(
                rightTransmission.get_torque_for_voltage(
                        wheelVelocity.right, -maxVoltage),
                rightTransmission.get_torque_for_voltage(
                        wheelVelocity.right, maxVoltage));



        // we know that (Tl + Tr) / Wr = m*a
        // and          (Tr - Tl) / Wr - f * omega = I * k
        // we have 3 unknowns: Tl, Tr, a and two equations
        // so that we could get the relationship between Tl and Tr
        // by eliminating a:
        // Tl = (m - I*k/Wb) / (m + I*k/Wb) * Tr - f*omega*Wr/Wb/(m + I*k/Wb)
        // Tl = C1 * Tr + C0

        // and we have torque constraints:
        // right_min_torque <= Tr <= right_max_torque
        // left_min_torque <= Tl = C1 * Tr + C0 <= left_max_torque
        // (left_min_torque - C0) / C1 <> Tr <> (left_max_torque - C0) / C1 
        // (Direction of the more / less sign depends on the sign of C1!) 

        //when we have the limits of Tr, we can calculate the limits of a

        double angularTerm = moi_ * curvature / effective_wheelbase_radius_;
        double C0, C1;

        C1 = (mass_ - angularTerm) / (mass_ + angularTerm);
        C0 = -angular_drag_ *chassisVelocity.angular * wheel_radius_ / effective_wheelbase_radius_
        / (mass_ + angularTerm);

        minmaxAccel = minmaxAccel.intersect(rightTorque);

        minmaxAccel = minmaxAccel.intersect(leftTorque.sclarAdd(-C0).scale(1/C1)); // <- limit of Tr

        // re-scale to acceleration
        minmaxAccel = minmaxAccel.scale(C1 + 1).sclarAdd(C0).scale(1/(wheel_radius_ * mass_));
        return minmaxAccel;
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
        public ChassisState chassisVelocity = new ChassisState();
        public ChassisState chassisAcceleration = new ChassisState();
        public WheelState wheelVelocity = new WheelState();
        public WheelState wheelAcceleration = new WheelState();
        public WheelState voltage = new WheelState();
        public WheelState wheel_torque = new WheelState();

        @Override
        public String toString(){
            String s ;
            s = "Dynamics:\n";
            s += "\tVelocity: " + wheelVelocity.toString() + "\n";
            s += "\tAcceleration: " + wheelAcceleration.toString() + "\n";
            s += "\tTorque: " + wheel_torque.toString() + "\n";
            s += "\tVoltage: " + voltage.toString() + "\n";
            s += "\tChassis Velocity: " + chassisVelocity.toString() + "\n";
            s += "\tChassis Acceleration: " + chassisAcceleration.toString() + "\n";
            s += "\tChassis curvature: " + curvature + "\n";
            s += "\tChassis dcurvature: " + dcurvature+ "\n";     
            return s;       
        }

    }



}
