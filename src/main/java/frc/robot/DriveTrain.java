package frc.robot;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.`

import static frc.robot.SwervePiece.FULL_REVOLUTION;

/** Represents a swerve drive style drivetrain. */
public class DriveTrain {
    private final SwervePiece frontRight = new SwervePiece(3, 4, false);
    private final SwervePiece frontLeft = new SwervePiece(5, 6, false);
    private final SwervePiece backLeft = new SwervePiece(7, 8, false);
    private final SwervePiece backRight = new SwervePiece(9, 10, false);
    public final SwervePiece[][] swervePieces = {
            {frontLeft, frontRight},
            {backLeft, backRight}
    };

    /**
     * Method to drive the robot using joystick info.
     *
     * @param xPull Speed of the robot in the x direction (sideways).
     * @param yPull Speed of the robot in the y direction (forward).
     */
    public void drive(double xPull, double yPull, boolean slow) {
        double desiredPosition = (getDesiredPosition(xPull, yPull)/360D)*FULL_REVOLUTION;//(xPull < 0 ? xPull*(FULL_REVOLUTION/4) : xPull*(FULL_REVOLUTION)) + (yPull > 0 ? yPull*(FULL_REVOLUTION/2) : 0); //the position the controller wants the motor to be in
        double drivingSpeed = (Math.max(Math.abs(xPull), Math.abs(yPull)))*((slow) ? 0.5 : 1);

        for(int i = 0, direction = 1; i < swervePieces.length; i++) {
//            direction = -direction;
            for (SwervePiece swervePiece : swervePieces[i])
                swervePiece.update(desiredPosition, drivingSpeed * direction);
        }
    }
    /**
     * Method to turn the robot using joystick info.
     *
     * @param xPull Speed of the robot in the x direction (sideways).
     */
    public void turn(double xPull, boolean slow) {
        double desiredRotation = -FULL_REVOLUTION*0.25;
        double drivingSpeed = xPull * ((slow) ? 0.5 : 1);
        for(SwervePiece swervePiece : swervePieces[0]) {
            swervePiece.update(desiredRotation, drivingSpeed);
        }
        drivingSpeed = -drivingSpeed;
        for(SwervePiece swervePiece : swervePieces[1]) {
            swervePiece.update(desiredRotation, drivingSpeed);
        }
    }
    /**
     * @return angle in negative degrees (0 - 359.99)
     */
    public static double getDesiredPosition(double xPull, double yPull){
        if(xPull == 0 && yPull == 0) return 0;
        double degreesRotation = (Math.atan2(yPull, xPull) * 180) / Math.PI;
        if(degreesRotation > 0){
            degreesRotation -= 360;
        }

        return (-(degreesRotation-90))%360;
    }
}