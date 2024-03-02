package frc.robot;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.`

/** Represents a swerve drive style drivetrain. */
public class DriveTrain {
    private final SwervePiece frontRight = new SwervePiece(1, 2);
    private final SwervePiece frontLeft = new SwervePiece(3, 4);

    private final SwervePiece backLeft = new SwervePiece(5, 6);
    private final SwervePiece backRight = new SwervePiece(7, 8);
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
        for(SwervePiece[] swerveRow : swervePieces)
            for(SwervePiece swervePiece : swerveRow) {
                swervePiece.update(xPull, yPull);
//                if(swervePiece == swervePieces[1][0]) Logger.getGlobal().log(Level.INFO, String.valueOf(swervePiece.turningEncoder.getPosition()));
            }
    }
}