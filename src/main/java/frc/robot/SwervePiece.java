package frc.robot;


import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.PIDController;

public class SwervePiece {
    PIDController turningMotorPID = new PIDController(1.25, 0,0);
    public final CANSparkFlex driveMotor;
    public final CANSparkFlex turningMotor;
    public final CANcoder turningEncoder;
    public final RelativeEncoder driveEncoder;
    public static final float FULL_REVOLUTION = 1F;//150F/7F; //original: 21.68F

    /**
     * Constructs a SwerveModule with a drive motor, turning motor, drive encoder and turning encoder.
     *
     * @param driveMotorChannel PWM output for the drive motor.
     * @param turningMotorChannel PWM output for the turning motor.
     */
    public SwervePiece(int driveMotorChannel, int turningMotorChannel, int canCoderID, boolean inverted, boolean invertedDriving){
        driveMotor = new CANSparkFlex(driveMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        turningMotor = new CANSparkFlex(turningMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        turningEncoder = new CANcoder(canCoderID);
        driveEncoder = driveMotor.getEncoder();
    }

    /**
     * Updates the SwervePiece's turning motor and driving motor to move in the direction xPull and yPull requests.
     * @param desiredPosition The desired number along FULL_ROTATION the encoder modulus FULL_ROTATION wants to be at
     * @param drivingSpeed Speed the robot drives
     */
    public void update(double desiredPosition, double drivingSpeed) {
        double motorPos = turningEncoder.getPosition().getValue() % FULL_REVOLUTION;

        double rotationAngle = shortestRotation(desiredPosition, motorPos);
        double velocity = turningMotorPID.calculate(rotationAngle, 0);
        turningMotor.set(-velocity);

    }
    public static double shortestRotation(double targetDegrees, double currentDegrees) {
        double difference = currentDegrees - targetDegrees;
        if (difference > 180) {
            difference -= 360;
        }
        if (difference < -180){
            difference += 360;
        }
        return difference;
    }
}
