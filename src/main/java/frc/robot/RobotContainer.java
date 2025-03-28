// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.constants.Constants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.LifterSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SliderSubsystem;
import frc.robot.subsystems.TipperSubsystem;
import com.pathplanner.lib.auto.AutoBuilder;

public class RobotContainer {
    
    
    

    //init the subsystems
    public IntakeSubsystem intake = IntakeSubsystem.getInstance();
    public LifterSubsystem lifter = LifterSubsystem.getInstance();
    public TipperSubsystem tipper = TipperSubsystem.getInstance();
    public SliderSubsystem slider = SliderSubsystem.getInstance();
    public ElevatorSubsystem elevator = ElevatorSubsystem.getInstance();

    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.RobotCentric driveRobotCentric = new SwerveRequest.RobotCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController dj = new CommandXboxController(Constants.JoyDriverID);
    private final CommandXboxController oj = new CommandXboxController(Constants.JoyOperID);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
private SendableChooser<Command> autoChooser = new SendableChooser<>();
    public RobotContainer() {
        RobotConfig config;
        try{
          config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
          // Handle exception as needed
          e.printStackTrace();
        }
    
        // Configure AutoBuilder last
        AutoBuilder.configure(
                this::getPose, // Robot pose supplier
                this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
                this::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                (speeds, feedforwards) -> driveRobotRelative(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
                new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
                        new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                        new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
                ),
                config, // The robot configuration
                () -> {
                  // Boolean supplier that controls when the path will be mirrored for the red alliance
                  // This will flip the path being followed to the red side of the field.
                  // THE ORIGIN WILL REMAIN ON THE BLUE SIDE
    
                  var alliance = DriverStation.getAlliance();
                  if (alliance.isPresent()) {
                    return alliance.get() == DriverStation.Alliance.Red;
                  }
                  return false;
                },
                this // Reference to this subsystem to set requirements
        );
NamedCommands.registerCommand("tipDown", Commands.runOnce(()->{tipper.revCommand().withTimeout(1);}));
NamedCommands.registerCommand("stopTipDown", Commands.runOnce(()->{tipper.stopCommand();}));
NamedCommands.registerCommand("shoot", Commands.runOnce(()->{intake.revCommand().withTimeout(1);}));
NamedCommands.registerCommand("stopShoot", Commands.runOnce(()->{intake.stopCommand();}));


      // autoChooser = AutoBuilder.buildAutoChooser("forwardShoot");
        //autoChooser.addOption("NEW NAME", getNEWAutonomousCommand());
        SmartDashboard.putData("Auto Mode", autoChooser);

        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(MathUtil.applyDeadband(-dj.getLeftY(),0.1) * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(MathUtil.applyDeadband(-dj.getLeftX(),0.1) * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(MathUtil.applyDeadband(-dj.getRightX(),0.1) * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // You dont need these for competition
        // dj.a().whileTrue(drivetrain.applyRequest(() -> brake));
        // dj.b().whileTrue(drivetrain.applyRequest(() ->
        //     point.withModuleDirection(new Rotation2d(-dj.getLeftY(), -dj.getLeftX()))
        // ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        // dj.back().and(dj.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        // dj.back().and(dj.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        // dj.start().and(dj.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        // dj.start().and(dj.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // This resets the robot so the whatever direction it is aiming is considered zero (away from the driver)

        //#region Driver Joystick
        // reset the field-centric heading on back button press
        dj.back().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        // These are your bindings for your subsystems
        //#endregion Driver Joystick

        //#region Operator Joystick
        // These are your bindings for your subsystems
        //left and right bumpers for intake/outtake
        oj.leftBumper().onTrue(intake.fwdCommand()).onFalse(intake.stopCommand());
        oj.rightBumper().onTrue(intake.revCommand()).onFalse(intake.stopCommand());
        //pov up and down for lifter
        oj.povUp().onTrue(lifter.fwdCommand()).onFalse(lifter.stopCommand());
        oj.povDown().onTrue(lifter.revCommand()).onFalse(lifter.stopCommand());
        //pov left and right for tipper
        oj.povLeft().onTrue(tipper.fwdCommand()).onFalse(tipper.stopCommand());
        oj.povRight().onTrue(tipper.revCommand()).onFalse(tipper.stopCommand());
        //x and y for slider
        oj.x().onTrue(slider.fwdCommand()).onFalse(slider.stopCommand());
        oj.y().onTrue(slider.revCommand()).onFalse(slider.stopCommand());
        //a and b for the elevator
        oj.a().onTrue(elevator.fwdCommand()).onFalse(elevator.stopCommand());
        oj.b().onTrue(elevator.revCommand()).onFalse(elevator.stopCommand());
        //#endregion
        
        drivetrain.registerTelemetry(logger::telemeterize);
    }

    

    

/* 
    public Command getLineAutonomousCommand() {      
             drivetrain.applyRequest(() ->
        drive.withVelocityX(0.1 * MaxSpeed) // Drive forward with negative Y (forward)
         .withVelocityY(0) // Drive left with negative X (left)
            .withRotationalRate(0) // Drive counterclockwise with negative X (left)
    ).withTimeout(3);   tipper.revCommand().withTimeout(.5); return intake.revCommand().withTimeout(1);
    
    
            //return Commands.print("No autonomous command configured");
        } */
    
 
    
        public Command getAutonomousCommand() {
        return autoChooser.getSelected();

        //return Commands.print("No autonomous command configured");
    }
}
