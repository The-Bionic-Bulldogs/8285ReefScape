// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.events.EventTrigger;

import edu.wpi.first.math.MathUtil;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.constants.Constants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LifterSubsystem;
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


new EventTrigger("tiltLOneScore").onTrue(tipper.revCommand());//.alongWith(elevator.revCommand())); 

new EventTrigger("adjustElevatorStation").onTrue(elevator.magicToPositionCommand(Constants.elevator.positions.L2)); //adjust elevator for station 

new EventTrigger("tiltForStation").onTrue((tipper.magicToPositionCommand(Constants.tipper.positions.STATION)));

//new EventTrigger("adjustElevatorl1").onTrue(Commands.runOnce(()->{System.out.println("adjusting elevator for L1");}));  -22.595215
new EventTrigger("outtake").onTrue(intake.fwdCommand()); //starting OUTTAKE

new EventTrigger("intake").onTrue(intake.revCommand());//starting INTAKE

new EventTrigger("stopOutIntake").onTrue(intake.stopCommand()); //stop INTAKE OR OUTTAKE

new EventTrigger("L3Pos").onTrue(elevator.magicToPositionCommand(Constants.elevator.positions.L3));

      autoChooser = AutoBuilder.buildAutoChooser("SimpleElevator");
        //autoChooser.addOption("forwardShoot", getAutonomousCommand());
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
        
        //a and b for the elevator
        oj.b().onTrue(elevator.fwdCommand()).onFalse(elevator.stopCommand());
        oj.a().onTrue(elevator.revCommand()).onFalse(elevator.stopCommand());
        // Bind R2 (right trigger) & L2 (left trigger) to set elevator to set pos
        oj.leftTrigger().onTrue(elevator.magicToPositionCommand(Constants.elevator.positions.L2)); //21.0
        oj.rightTrigger().onTrue(elevator.magicToPositionCommand(Constants.elevator.positions.L3)); //54.0
        // Bind X to set elevator to pos
        oj.x().onTrue(elevator.magicToPositionCommand(Constants.elevator.positions.DOWN)); // 0.5
        // Bind Y to set tipper to set pos
        oj.y().onTrue(tipper.magicToPositionCommand(Constants.tipper.positions.STATION)); //-19.0
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
