package frc.robot;


import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.PIDController;

public class SwervePiece {
    PIDController turningMotorPID = new PIDController(1.25, 0,0);
    public final CANSparkFlex driveMotor;
    public final CANSparkFlex turningMotor;
//    public final RelativeEncoder turningEncoder;
    public final CANcoder turningEncoder;
    public final RelativeEncoder driveEncoder;
    public boolean inverted;
    public boolean drivingMotorInverted;
    public int timeWhileFarAwayFromTargetWhileStopped = 0;
    public int ticksDrivingInWrongDirection = 0;
//    public final float MAX_WHEEL_ROTATION = 5.42F;
    public static final float FULL_REVOLUTION = 1F;//150F/7F; //original: 21.68F

    /**
     * Constructs a SwerveModule with a drive motor, turning motor, drive encoder and turning encoder.
     *
     * @param driveMotorChannel PWM output for the drive motor.
     * @param turningMotorChannel PWM output for the turning motor.
     */
    public SwervePiece(int driveMotorChannel, int turningMotorChannel, int canCoderID, boolean inverted, boolean invertedDriving){
        driveMotor = new CANSparkFlex(driveMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
//        driveEncoder = driveMotor.getEncoder();
        turningMotor = new CANSparkFlex(turningMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        turningEncoder = new CANcoder(canCoderID);
        this.inverted = inverted;
        this.drivingMotorInverted = invertedDriving;
        driveEncoder = driveMotor.getEncoder();
    }

    /**
     * Updates the SwervePiece's turning motor and driving motor to move in the direction xPull and yPull requests.
     * @param desiredPosition The desired number along FULL_ROTATION the encoder modulus FULL_ROTATION wants to be at
     * @param drivingSpeed Speed the robot drives
     */
    public void update(double desiredPosition, double drivingSpeed) {
        double motorPos = turningEncoder.getPosition().getValue() % FULL_REVOLUTION;
        double velocity = turningMotorPID.calculate(motorPos, desiredPosition);
        turningMotor.set(-velocity);

        double shortestDistanceFromTarget = calculateShortestDistanceFromTarget(motorPos, desiredPosition);
        if(shortestDistanceFromTarget < 0.5){ //DON'T DRIVE IF THE WHEEL IS FACING THE WRONG DIRECTION
            driveMotor.setIdleMode(CANSparkBase.IdleMode.kCoast);
            driveMotor.set(drivingSpeed);
        } else {
            driveMotor.set(0);
            driveMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);
        }
    }
    /**
     * This calculates the shortest distance the wheel needs to turn to reach its destination.
     * Although seemingly unneeded with the PID controller, I have left this code in for stopping the wheels when they are in the wrong direction (until a native solution is found)
    */
    private double calculateShortestDistanceFromTarget(double motorPos, double desiredPosition) {
        double turningMotorPos = Math.abs(motorPos); //the current position of the wheel turning motor
        double distanceClockwise = ((turningMotorPos - desiredPosition) + FULL_REVOLUTION) % FULL_REVOLUTION; //distance the controller position and motor position is
        double distanceCounterClockwise = FULL_REVOLUTION - distanceClockwise;

        return Math.min(distanceClockwise, distanceCounterClockwise);
    }
}
