// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;


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
//    private final CANSparkMax firstMotor = new CANSparkMax(1, CANSparkLowLevel.MotorType.kBrushless);
//    private final CANSparkMax secondMotor = new CANSparkMax(2, CANSparkLowLevel.MotorType.kBrushless);
//    private final CANSparkMax thirdMotor = new CANSparkMax(3, CANSparkLowLevel.MotorType.kBrushless);
//    private final DifferentialDrive robotDrive = new DifferentialDrive(firstMotor, secondMotor);
    private final DriveTrain train = new DriveTrain();
    private static final Joystick rockCandyStick = new Joystick(0);
    private static final Joystick thrustMaster69Stick = new Joystick(1);
//    private static final AnalogGyro gyro = new AnalogGyro(0);
    public static final CANSparkMax liftMotor1 = new CANSparkMax(11, CANSparkMax.MotorType.kBrushless);
    public static final CANSparkMax suckMotor = new CANSparkMax(13, CANSparkBase.MotorType.kBrushless);
    public static final CANSparkMax shootMotor1 = new CANSparkMax(14, CANSparkBase.MotorType.kBrushless);
    public static final CANSparkMax shootMotor2 = new CANSparkMax(15, CANSparkBase.MotorType.kBrushless);
    public static DigitalInput tooFarForward = new DigitalInput(0);
    public static DigitalInput tooFarBack = new DigitalInput(1);

    static {
        shootMotor1.setInverted(true);
    }

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

//        updateSuckOrPull();

        double x_turningAxis = getStickAxisWithDeadZone(rockCandyStick, 4, 0.075);
        double y_turningAxis = getStickAxisWithDeadZone(rockCandyStick, 5, 0.075);
        if(Math.abs(x_turningAxis)+Math.abs(y_turningAxis) > 0) train.turn(x_turningAxis, y_turningAxis);
        else train.drive(getStickAxisWithDeadZone(thrustMaster69Stick, 0, 0.075), getStickAxisWithDeadZone(thrustMaster69Stick, 1, 0.075));

        handleLiftingAndLowering();
        handleGrabbingAndShooting();
    }
    public void handleLiftingAndLowering(){
//        double x_liftAxis = getStickAxisWithDeadZone(rockCandyStick, 0, 0.075);
        final float MAX_ROTATION_SPEED = 0.1F;
        double y_liftAxis = -getStickAxisWithDeadZone(rockCandyStick, 1, 0.075)*MAX_ROTATION_SPEED;


//        boolean canPushToGround = y_liftAxis < 0 && liftMotor1Encoder.getPosition() > -4.5 && liftMotor2Encoder.getPosition() > -10;
//        boolean canPullUp = y_liftAxis > 0 && liftMotor1Encoder.getPosition() < -1 && liftMotor2Encoder.getPosition() < -1;
        if(tooFarForward.get() && y_liftAxis > 0) return;
        else if (tooFarBack.get() && y_liftAxis < 0) return;
        if(y_liftAxis == 0) { //stop moving
            liftMotor1.set(0);
            liftMotor1.setIdleMode(CANSparkBase.IdleMode.kBrake);
        } else {
            liftMotor1.setIdleMode(CANSparkBase.IdleMode.kCoast);
            liftMotor1.set(y_liftAxis);
        }
    }
    public void handleGrabbingAndShooting(){
        double L_trigger = getStickAxisWithDeadZone(rockCandyStick, 2, 0.075);//0.0-1.0
        double R_trigger = getStickAxisWithDeadZone(rockCandyStick, 3, 0.075);//0.0-1.0
        if(L_trigger > 0){
            suckMotor.setIdleMode(CANSparkBase.IdleMode.kCoast);
            suckMotor.set(L_trigger);
        } else {
            suckMotor.set(0);
            suckMotor.setIdleMode(CANSparkBase.IdleMode.kBrake);

        }

        if(R_trigger > 0){
            shootMotor1.setIdleMode(CANSparkBase.IdleMode.kCoast);
            shootMotor2.setIdleMode(CANSparkBase.IdleMode.kCoast);
            shootMotor1.set(1.0); //always full speed when shooting
            shootMotor2.set(1.0); //always full speed when shooting
        } else {
            shootMotor1.set(0);
            shootMotor2.set(0);
            shootMotor1.setIdleMode(CANSparkBase.IdleMode.kBrake);
            shootMotor2.setIdleMode(CANSparkBase.IdleMode.kBrake);
        }
    }
    public static double getStickAxisWithDeadZone(Joystick stick, int channelId, double deadZone){
        double axisInput = stick.getRawAxis(channelId);
        if(Math.abs(axisInput) < deadZone) return 0;
        else return axisInput;
    }
    public static double lerp(double min, double max, double delta) {
        return min + delta * (max - min);
    }
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

//    public void updateSuckOrPull(){
//        double direction = stick.getRawAxis(SHOOT_STICK), speed;
//        if(Math.abs(direction) > 0.1){
//            firstMotor.set(direction);
//            secondMotor.set(direction);
//        } else {
//            firstMotor.set(0);
//            secondMotor.set(0);
//        }
//
//        direction = stick.getRawAxis(PULL_STICK);
//        if(Math.abs(direction) > 0.1){
//            thirdMotor.set(direction);
//        } else {
//            thirdMotor.set(0);
//        }
////        new AnalogAccelerometer(5);
//    }

    /** This method is called once when the robot is disabled. */
    @Override
    public void disabledInit() {}
    
    
    /** This method is called periodically when disabled. */
    @Override
    public void disabledPeriodic() {}
    
    
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
}
