package frc.robot;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.`

import java.util.logging.Level;
import java.util.logging.Logger;

import static frc.robot.SwervePiece.FULL_REVOLUTION;

/** Represents a swerve drive style drivetrain. */
public class DriveTrain {
    private final SwervePiece frontRight = new SwervePiece(3, 4);
    private final SwervePiece frontLeft = new SwervePiece(5, 6);
    private final SwervePiece backLeft = new SwervePiece(7, 8);
    private final SwervePiece backRight = new SwervePiece(9, 10);
    private final SwervePiece[][] swervePieces = {
            {frontLeft, frontRight},
            {backLeft, backRight}
    };

    /**
     * Method to drive the robot using joystick info.
     *
     * @param xPull Speed of the robot in the x direction (sideways).
     * @param yPull Speed of the robot in the y direction (forward).
     */
    public void drive(double xPull, double yPull) {
        double desiredPosition = (getDesiredPosition(xPull, yPull)/360D)*FULL_REVOLUTION;//(xPull < 0 ? xPull*(FULL_REVOLUTION/4) : xPull*(FULL_REVOLUTION)) + (yPull > 0 ? yPull*(FULL_REVOLUTION/2) : 0); //the position the controller wants the motor to be in
        for(SwervePiece[] swerveRow : swervePieces)
            for(SwervePiece swervePiece : swerveRow) {
                swervePiece.update(xPull, yPull, desiredPosition);
//                if(swervePiece == swervePieces[1][0]) Logger.getGlobal().log(Level.INFO, String.valueOf(swervePiece.turningEncoder.getPosition()));
            }
    }
    /**
     * Method to turn the robot using joystick info.
     *
     * @param xPull Speed of the robot in the x direction (sideways).
     * @param yPull Speed of the robot in the y direction (forward).
     */
    public void turn(double xPull, double yPull) {
        double desiredPosition = (getDesiredPosition(xPull, yPull)/360D)*FULL_REVOLUTION;//(xPull < 0 ? xPull*(FULL_REVOLUTION/4) : xPull*(FULL_REVOLUTION)) + (yPull > 0 ? yPull*(FULL_REVOLUTION/2) : 0); //the position the controller wants the motor to be in
        for(SwervePiece swervePiece : swervePieces[0]) {
            swervePiece.update(xPull, yPull, desiredPosition);
        }
        desiredPosition = ((desiredPosition-180)%360); //rotate by 180 degrees. this will make the second set of wheels drive in the opposite direction
        for(SwervePiece swervePiece : swervePieces[1]) {
            swervePiece.update(xPull, yPull, desiredPosition);
        }
    }
    /**
     * @return angle in negative degrees (0 - 359.99)
     */
    public static double getDesiredPosition(double xPull, double yPull){
        double degreesRotation = (Math.atan2(yPull, xPull) * 180) / Math.PI;
        if(degreesRotation > 0){
            return -360+degreesRotation;
        }

        Logger.getGlobal().log(Level.INFO, String.valueOf(degreesRotation));

        return (degreesRotation-90)%360;
    }
}