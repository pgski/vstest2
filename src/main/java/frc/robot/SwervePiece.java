package frc.robot;


import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.RelativeEncoder;

import java.util.logging.Level;
import java.util.logging.Logger;

import static frc.robot.Robot.clamp;

public class SwervePiece {
    public final CANSparkFlex driveMotor;
    public final CANSparkFlex turningMotor;
    public final RelativeEncoder turningEncoder;
    public boolean inverted;
//    public final float MAX_WHEEL_ROTATION = 5.42F;
    public static final float FULL_REVOLUTION = 150F/7F; //original: 21.68F

    /**
     * Constructs a SwerveModule with a drive motor, turning motor, drive encoder and turning encoder.
     *
     * @param driveMotorChannel PWM output for the drive motor.
     * @param turningMotorChannel PWM output for the turning motor.
     */
    public SwervePiece(int driveMotorChannel, int turningMotorChannel, boolean inverted){
        driveMotor = new CANSparkFlex(driveMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        turningMotor = new CANSparkFlex(turningMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        turningEncoder = turningMotor.getEncoder();
        this.inverted = inverted;
    }

    /**
     * Updates the SwervePiece's turning motor and driving motor to move in the direction xPull and yPull requests.
     *
     * @param drivingSpeed Speed the robot drives
     */
    public void update(double desiredPosition, double drivingSpeed) {
        double turningMotorPos = Math.abs(turningEncoder.getPosition() % FULL_REVOLUTION); //the current position of the wheel turning motor
        double distanceClockwise = ((turningMotorPos - desiredPosition) + FULL_REVOLUTION) % FULL_REVOLUTION; //distance the controller position and motor position is
        double distanceCounterClockwise = FULL_REVOLUTION - distanceClockwise;

        double distanceFromDesired; //this is whichever direction will be closest to desired
        byte rotationDirection; //whether to turn LEFT or RIGHT
        if (distanceCounterClockwise < distanceClockwise) {
            distanceFromDesired = distanceCounterClockwise;
            rotationDirection = -1;
        } else {
            distanceFromDesired = distanceClockwise;
            rotationDirection = 1;
        }
        if(inverted) rotationDirection = (byte)-rotationDirection;

        if (distanceFromDesired > 0.025) { //if we are close enough to the desired position, adjustments will be too sensitive
            double turnSpeed = (1 * (distanceFromDesired/(FULL_REVOLUTION*0.5))) * rotationDirection; //the turnSpeed decreases when closer to the desiredPosition to prevent overshooting
            if (distanceFromDesired < 0.2) turnSpeed /= 2 * (0.2 / distanceFromDesired); //slow down when close to target to prevent overshooting(lol use pid next time)
            Logger.getGlobal().log(Level.INFO, "TURNSPEED: " + turnSpeed + "\nDISTANCE: " + distanceFromDesired);


            turnSpeed = clamp(turnSpeed, -1, 1);

            turningMotor.setIdleMode(CANSparkBase.IdleMode.kCoast);
            turningMotor.set(turnSpeed);
        } else {
            turningMotor.set(0);
            turningMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);
        }

        if(distanceFromDesired < 0.5){ //DON'T DRIVE IF THE WHEEL IS FACING THE WRONG DIRECTION
            driveMotor.setIdleMode(CANSparkBase.IdleMode.kCoast);
            driveMotor.set(drivingSpeed);
        } else {
            driveMotor.set(0);
//            driveMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);
        }
    }

    public void turn(int rotationDirection, double speed) {

    }
}
