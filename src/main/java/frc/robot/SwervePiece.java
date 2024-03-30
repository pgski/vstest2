package frc.robot;


import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.PIDController;

public class SwervePiece {

    private final int distanceBetweenLeftRightWheels = 10;
    private final int distanceBetweenFrontBackWheels = 20;
    private final int swerveID = 0;
    PIDController turningMotorPID = new PIDController(1.25, 0,0);
    public static final float FULL_REVOLUTION = 150F/7F;
    private final CANSparkFlex driveMotor;
    private final CANSparkFlex turningMotor;
    private final RelativeEncoder driveEncoder;
    private final RelativeEncoder turningEncoder;

    public SwervePiece(int driveMotorChannel, int turningMotorChannel, int swerveID){
        driveMotor = new CANSparkFlex(driveMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        turningMotor = new CANSparkFlex(turningMotorChannel, CANSparkLowLevel.MotorType.kBrushless);
        turningEncoder = turningMotor.getEncoder();
        driveEncoder = driveMotor.getEncoder();
        swerveID = this.swerveID;
    }

    //gets the radius of the circle to calculate turn angle
    //runs the module

    public void update(double desiredPosition, double drivingSpeed) {
        //turningMotor
        double motorPos = turningEncoder.getPosition() % FULL_REVOLUTION;
        double distanceToAngle = distanceToAngle(desiredPosition, motorPos);
        turningEncoder.setPosition(3);
        double velocity = turningMotorPID.calculate(distanceToAngle, 0);
        turningMotor.set(-velocity);

        //drivingMotor
        driveMotor(distanceToAngle, drivingSpeed);
    }
    //only drives motor when close to desired position

    private void driveMotor(double distanceToAngle, double drivingSpeed) {
        if (Math.abs(distanceToAngle) > 5) {
            driveMotor.set(0);
        } else {
            driveMotor.set(drivingSpeed);
        }
    }
    //finds the rotation need the set angle + the closest rotation

    private static double distanceToAngle(double targetDegrees, double currentDegrees) {
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
