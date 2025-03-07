// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.LifterSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SliderSubsystem;
import frc.robot.subsystems.TipperSubsystem;

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
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // You dont need these for competition
        // joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        // joystick.b().whileTrue(drivetrain.applyRequest(() ->
        //     point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        // ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        // joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        // joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        // joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        // joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // This resets the robot so the whatever direction it is aiming is considered zero (away from the driver)
        // reset the field-centric heading on back button press
        joystick.back().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        // These are your bindings for your subsystems
        //left and right bumpers for intake/outtake
        joystick.leftBumper().onTrue(intake.fwdCommand()).onFalse(intake.stopCommand());
        joystick.rightBumper().onTrue(intake.revCommand()).onFalse(intake.stopCommand());
        //pov up and down for lifter
        joystick.povUp().onTrue(lifter.fwdCommand()).onFalse(lifter.stopCommand());
        joystick.povDown().onTrue(lifter.revCommand()).onFalse(lifter.stopCommand());
        //pov left and right for tipper
        joystick.povLeft().onTrue(tipper.fwdCommand()).onFalse(tipper.stopCommand());
        joystick.povRight().onTrue(tipper.revCommand()).onFalse(tipper.stopCommand());
        //x and y for slider
        joystick.x().onTrue(slider.fwdCommand()).onFalse(slider.stopCommand());
        joystick.y().onTrue(slider.revCommand()).onFalse(slider.stopCommand());
        //a and b for the elevator
        joystick.a().onTrue(elevator.fwdCommand()).onFalse(elevator.stopCommand());
        joystick.b().onTrue(elevator.revCommand()).onFalse(elevator.stopCommand());


        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
