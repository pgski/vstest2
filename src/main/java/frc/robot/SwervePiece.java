package frc.robot;


import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.RelativeEncoder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SwervePiece {
    public final CANSparkFlex driveMotor;
    public final CANSparkFlex turningMotor;
    public final RelativeEncoder turningEncoder;
//    public final float MAX_WHEEL_ROTATION = 5.42F;
    public static final float FULL_REVOLUTION = 150F/7F; //original: 21.68F

    /**
     * Constructs a SwerveModule with a drive motor, turning motor, drive encoder and turning encoder.
     *
     * @param driveMotorChannel PWM output for the drive motor.
     * @param turningMotorChannel PWM output for the turning motor.
     */
    public SwervePiece(int driveMotorChannel, int turningMotorChannel){
        driveMotor = new CANSparkFlex(driveMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        turningMotor = new CANSparkFlex(turningMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        turningEncoder = turningMotor.getEncoder();
    }

    /**
     * Updates the SwervePiece's turning motor and driving motor to move in the direction xPull and yPull requests.
     *
     * @param xPull Controller x direction pull
     * @param yPull Controller y direction pull
     */
    public void update(double xPull, double yPull) {
        double turningMotorPos = turningEncoder.getPosition()%FULL_REVOLUTION; //the current position of the wheel turning motor
        double desiredPosition = (getDesiredPosition(xPull, yPull)/360D)*FULL_REVOLUTION;//(xPull < 0 ? xPull*(FULL_REVOLUTION/4) : xPull*(FULL_REVOLUTION)) + (yPull > 0 ? yPull*(FULL_REVOLUTION/2) : 0); //the position the controller wants the motor to be in
//        Logger.getGlobal().log(Level.INFO, String.valueOf(desiredPosition));

        double positionDistanceFromDesired = Math.abs(turningMotorPos-desiredPosition); //distance the controller position and motor position is
        double distanceCounterClockwise = FULL_REVOLUTION - positionDistanceFromDesired;

        byte rotationDirection = 1;
        if(distanceCounterClockwise < positionDistanceFromDesired){
            positionDistanceFromDesired = distanceCounterClockwise;
            rotationDirection = -1;
        }
//        Logger.getGlobal().log(Level.INFO, positionDistanceFromDesired + " | " + distanceCounterClockwise);

        Logger.getGlobal().log(Level.INFO, "RAW: " + turningEncoder.getPosition() + '\n' + "MODED: " + turningMotorPos + '\n' + "CONTROLLER: " + desiredPosition);

        if(positionDistanceFromDesired > 0.025) { //if we are close enough to the desired position, adjustments will be too sensitive
            double turnSpeed = positionDistanceFromDesired/(FULL_REVOLUTION/4)/16; //the turnSpeed decreases when closer to the desiredPosition to prevent overshooting
            if(positionDistanceFromDesired < 0.2) turnSpeed /= 2*(0.2/positionDistanceFromDesired); //slow down when close to target to prevent overshooting(lol use pid next time)


//            if( < positionDistanceFromDesired){ //if rotating counterclockwise is closer to 0
//                turningMotor.set(-turnSpeed);
//            } else {
//            turningMotor.set(turnSpeed * rotationDirection);
//            }
            if (turningMotorPos > desiredPosition) turningMotor.set(-turnSpeed*rotationDirection); //original: -0.2
            else if (turningMotorPos < desiredPosition) turningMotor.set(turnSpeed*rotationDirection); //original: 0.2
            else turningMotor.set(0);
        } else {
            turningMotor.set(0);
        }

//        driveMotor.set(yPull);
    }
    /**
     * @return angle in negative degrees (0 - 359.99)
    */
    public static double getDesiredPosition(double xPull, double yPull){
        double degreesRotation = (Math.atan2(yPull, xPull) * 180) / Math.PI;
        if(degreesRotation > 0){
            return -360+degreesRotation;
        }
        return degreesRotation;
    }
}


//        if(yPull < 0){ //top half
//            if(xPull < 0) { //quadrant II
//                return xPull*(FULL_REVOLUTION*0.25);
//            } else { //quadrant I
//                return (Math.max(yPull-xPull, 0)*(FULL_REVOLUTION)) + (xPull * (FULL_REVOLUTION*0.75));
//            }
//        } else {
//            if(xPull < 0){ //quadrant III
//                return ((yPull < 1) ? (Math.min(xPull+yPull, 0)*(FULL_REVOLUTION*0.25)) : 0) + (-yPull * (FULL_REVOLUTION*0.5));
//            } else { //quadrant IV
//                return (-yPull * (FULL_REVOLUTION*0.5)) + (xPull < 1 ? (Math.max(yPull-xPull, 0)*(FULL_REVOLUTION*0.75)) : 0);
//            }
//        }