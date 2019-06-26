/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Sensors;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;

/**
 * Add your docs here.
 */
public class AbsoluteEncoder {

    private SPI spi;

    public AbsoluteEncoder(Port SPIport){
        this.spi = new SPI(SPIport);
    }

    public int get(){
        byte[] data = {0,0};
        spi.read(true, data, 2);
        return (int)(data[0] << 8 | ((data[1])&0xff))/8;
    }

    public double getRad(){
        return Math.PI / 2048.0 * get();
    }

    public double getDeg(){
        return 180.0 / 2048.0 * get();
    }




}
