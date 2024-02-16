// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.hal.AccelerometerJNI;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.JNIWrapper;
import edu.wpi.first.hal.simulation.AccelerometerDataJNI;
import edu.wpi.first.wpilibj.AnalogAccelerometer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import org.jetbrains.annotations.Contract;

import javax.swing.*;


/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot
{
    private static final String DEFAULT_AUTO = "Default";
    private static final String CUSTOM_AUTO = "My Auto";
    private String autoSelected;
//    private final SendableChooser<String> chooser = new SendableChooser<>();
//    private final XboxController m_controller = new XboxController(0);
//    private final Drivetrain m_swerve = new Drivetrain();

    // Slew rate limiters to make joystick inputs more gentle; 1/3 sec from 0 to 1.
//    private final SlewRateLimiter m_xspeedLimiter = new SlewRateLimiter(3);
//    private final SlewRateLimiter m_yspeedLimiter = new SlewRateLimiter(3);
//    private final SlewRateLimiter m_rotLimiter = new SlewRateLimiter(3);
    private final CANSparkMax firstMotor = new CANSparkMax(1, CANSparkLowLevel.MotorType.kBrushless);
    private final CANSparkMax secondMotor = new CANSparkMax(2, CANSparkLowLevel.MotorType.kBrushless);
    private final CANSparkMax thirdMotor = new CANSparkMax(3, CANSparkLowLevel.MotorType.kBrushless);
//    private final DifferentialDrive robotDrive = new DifferentialDrive(firstMotor, secondMotor);
    private final Joystick stick = new Joystick(0);

    public static byte SHOOT_STICK = 1;
    public static byte PULL_STICK = 5;
    /**
     * This method is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit()
    {
//        chooser.setDefaultOption("Default Auto", DEFAULT_AUTO);
//        chooser.addOption("My Auto", CUSTOM_AUTO);
//        SmartDashboard.putData("Auto choices", chooser);
    }
    
    
    /**
     * This method is called every 20 ms, no matter the mode. Use this for items like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic methods, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {}
    
    
    /**
     * This autonomous (along with the chooser code above) shows how to select between different
     * autonomous modes using the dashboard. The sendable chooser code works with the Java
     * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all the chooser code and
     * uncomment the getString line to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional comparisons to the switch structure
     * below with additional strings. If using the SendableChooser, make sure to add them to the
     * chooser code above as well.
     */
    @Override
    public void autonomousInit()
    {
//        autoSelected = chooser.getSelected();
//        // autoSelected = SmartDashboard.getString("Auto Selector", DEFAULT_AUTO);
//        System.out.println("Auto selected: " + autoSelected);
    }
    
    
    /** This method is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic()
    {
//        driveWithJoystick(false);
//        m_swerve.updateOdometry();
//        switch (autoSelected)
//        {
//            case CUSTOM_AUTO:
//                // Put custom auto code here
//                break;
//            case DEFAULT_AUTO:
//            default:
//                // Put default auto code here
//                break;
//        }
    }
    
    
    /** This method is called once when teleop is enabled. */
    @Override
    public void teleopInit() {}
    
    
    /** This method is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
//        robotDrive.arcadeDrive(stick.getY(), stick.getX());
//        driveWithJoystick(true);
        updateSuckOrPull();
    }
    public void updateSuckOrPull(){
        double direction = stick.getRawAxis(SHOOT_STICK), speed;
        if(Math.abs(direction) > 0.1){
            firstMotor.set(direction);
            secondMotor.set(direction);
        } else {
            firstMotor.set(0);
            secondMotor.set(0);
        }


        direction = stick.getRawAxis(PULL_STICK);
        if(Math.abs(direction) > 0.1){
            thirdMotor.set(direction);
        } else {
            thirdMotor.set(0);
        }
//        new AnalogAccelerometer(5);

    }
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
    
    
    /** This method is called once when the robot is disabled. */
    @Override
    public void disabledInit() {}
    
    
    /** This method is called periodically when disabled. */
    @Override
    public void disabledPeriodic(
    ) {
    }
    
    
    /** This method is called once when test mode is enabled. */
    @Override
    public void testInit() {}
    
    
    /** This method is called periodically during test mode. */
    @Override
    public void testPeriodic() {}
    
    
    /** This method is called once when the robot is first started up. */
    @Override
    public void simulationInit() {}
    
    
    /** This method is called periodically whilst in simulation. */
    @Override
    public void simulationPeriodic() {}

//    private void driveWithJoystick(boolean fieldRelative) {
//        // Get the x speed. We are inverting this because Xbox controllers return
//        // negative values when we push forward.
//        final var xSpeed =
//                -m_xspeedLimiter.calculate(MathUtil.applyDeadband(m_controller.getLeftY(), 0.02))
//                        * Drivetrain.kMaxSpeed;
//
//        // Get the y speed or sideways/strafe speed. We are inverting this because
//        // we want a positive value when we pull to the left. Xbox controllers
//        // return positive values when you pull to the right by default.
//        final var ySpeed =
//                -m_yspeedLimiter.calculate(MathUtil.applyDeadband(m_controller.getLeftX(), 0.02))
//                        * Drivetrain.kMaxSpeed;
//
//        // Get the rate of angular rotation. We are inverting this because we want a
//        // positive value when we pull to the left (remember, CCW is positive in
//        // mathematics). Xbox controllers return positive values when you pull to
//        // the right by default.
//        final var rot =
//                -m_rotLimiter.calculate(MathUtil.applyDeadband(m_controller.getRightX(), 0.02))
//                        * Drivetrain.kMaxAngularSpeed;
//
//        m_swerve.drive(xSpeed, ySpeed, rot, fieldRelative, getPeriod());
//    }
}
