/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Odometry;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import edu.wpi.first.wpilibj.Talon;
import frc.robot.Robot;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Odometry implements Runnable{
    
    RealVector Position = new ArrayRealVector(new double[] {0,0});
    private double dt = 0.02;
    private double encoderl_last, encoderr_last;

    public Odometry(double dt,double encoderl_last,double encoderr_last){
        this.dt = dt;
        this.encoderl_last = encoderl_last;
        this.encoderr_last = encoderr_last;
    }

    public Odometry(double encoderl_last,double encoderr_last){
        this.dt = 0.02;
        this.encoderl_last = encoderl_last;
        this.encoderr_last = encoderr_last;
    }

    @Override
    public void run(){
        double[] encoderval = Robot.chassis.getWheelEncoderValue()[0];
        double [] measurement = getMeasurement();
        //compute delta based on encoders, respect to robot
        double[] delta_encoder = {0,0};
        delta_encoder[0] = encoderval[0] - encoderl_last;
        delta_encoder[1] = encoderval[1] - encoderr_last;
        double dx = (delta_encoder[0] + delta_encoder[1])/2.0;
        RealVector delta = new ArrayRealVector(new double[] {dx,0});
        encoderl_last = encoderval[0];
        encoderr_last = encoderval[1];
        
        //compute transformation matrix
        double heading = (measurement[0] + Math.PI/2);
        heading = Math.atan2(Math.sin(heading),Math.cos(heading));
        double[][] transf_d = {{Math.cos(heading),Math.sin(heading)},{-Math.sin(heading),Math.cos(heading)}};
        RealMatrix transf = MatrixUtils.createRealMatrix(transf_d);

        //apply the transformation and update X,Y
        RealVector delta_U = transf.preMultiply(delta);
        Position = Position.add(delta_U);
    }

    public double[] get() {
        return Position.toArray();
    }

    public void setPos(double[] Position){
        this.Position = new ArrayRealVector(Position);
    }
    public void setPos(double deltaX, double deltaY){
        this.Position = new ArrayRealVector(new double[] {deltaX,deltaY});
    }

    private double[] getMeasurement() {
        double xdd,ydd,h,hd;
        h = Math.toRadians(-Robot.gyro.getYaw());
        hd = Math.toRadians(-Robot.gyro.getRate());
        xdd = Robot.gyro.getWorldLinearAccelX();
        ydd = Robot.gyro.getWorldLinearAccelY();
        return new double[] {h,hd,xdd,ydd};
    }

    private void getEncoder(){

    }

}
