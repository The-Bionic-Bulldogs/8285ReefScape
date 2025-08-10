package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.signals.InvertedValue;
import edu.wpi.first.wpilibj.drive.RobotDriveBase.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.units.measure.Angle;

/**
 * This subsystem handles managing the Template.
 * It is responsible for doing some stuff.
 */
public class ElevatorSubsystem extends SubsystemBase {
	private static ElevatorSubsystem instance;
  private TalonFX m_motor;
  private TalonFX m_follower;

  private final MotionMagicVoltage m_positionRequest = new MotionMagicVoltage(0);

  //private and public variables defined here
  private PIDController m_pidController = new PIDController(
    Constants.elevator.kP,
    Constants.elevator.kI,
    Constants.elevator.kD
  );

  /**
	 * Returns the instance of the ElevatorSubsystem subsystem.
	 * The purpose of this is to only create an instance if one does not already exist.
	 * @return ElevatorSubsystem instance
	 */
  public static ElevatorSubsystem getInstance() {
		if (instance == null)
			instance = new ElevatorSubsystem();
		return instance;
	}
  
  public ElevatorSubsystem() {
    //initialize values for private and public variables, etc.
    init();
  }

  
  /**
   * The init method resets and operational state of the subsystem
   */
  public void init() {
    // set initial stuff, etc.
    TalonFXConfiguration fxConfig = new TalonFXConfiguration();
    Slot0Configs clConfigs = new Slot0Configs()
      .withKP(Constants.elevator.kP)
      .withKI(Constants.elevator.kI)
      .withKD(Constants.elevator.kD)
      .withKS(Constants.elevator.kS)
      .withKV(Constants.elevator.kV)
      .withKA(Constants.elevator.kA)
      .withKG(Constants.elevator.kG);
    fxConfig.Slot0 = clConfigs;

    var motionConfig = new MotionMagicConfigs();
    motionConfig.MotionMagicCruiseVelocity = 20.0 / 2.0; //20
    motionConfig.MotionMagicAcceleration = 291;
    fxConfig.MotionMagic = motionConfig;

    SoftwareLimitSwitchConfigs softLimitSwitchConfigs = new SoftwareLimitSwitchConfigs()
      .withReverseSoftLimitEnable(Constants.elevator.kSoftReverseLimitEnable)
      .withReverseSoftLimitThreshold(Constants.elevator.kSoftReverseLimit)
      .withForwardSoftLimitEnable(Constants.elevator.kSoftForwardLimitEnable)
      .withForwardSoftLimitThreshold(Constants.elevator.kSoftForwardLimit);
    fxConfig.SoftwareLimitSwitch = softLimitSwitchConfigs;
    MotorOutputConfigs mOutputConfigs = new MotorOutputConfigs()
      .withNeutralMode(Constants.elevator.kNeutralMode)
      .withInverted((Constants.elevator.kInverted) ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive);
    fxConfig.MotorOutput = mOutputConfigs;
    m_motor = new TalonFX(Constants.elevator.kMotor1Id);
    m_motor.getConfigurator().apply(fxConfig);

    m_follower = new TalonFX(Constants.elevator.kMotor2Id);
    m_follower.getConfigurator().apply(fxConfig);
    m_follower.setControl(new Follower(m_motor.getDeviceID(), Constants.elevator.kInvertFromLeader));
  }
  
  @Override
  public void periodic() {
  }

  //#region local controls (private)
  private void stop() {
    m_motor.set(0.0);
  }

  private void start(boolean inverted) {
    m_motor.set((inverted) ? Constants.elevator.kRevSpeed : Constants.elevator.kFwdSpeed);
  }
  //#endregion local controls (private)

  //#region public commands
  public Command stopCommand() {
    return runOnce(() -> stop());
  }
  public Command fwdCommand() {
    return runOnce(() -> start(false));
  }
  public Command revCommand() {
    return runOnce(() -> start(true));
  }
  //#endregion public commands

  /**
   * Returns the elevator position in rotations
   */
  
/* 
  public double getElevatorPosition() {
    // Get the position in rotations (not Angle, not degrees)
    return m_motor.getPosition().getValueAsDouble();// This returns the value in rotations
  }


  public void setElevatorPositionWPILib(double setpoint) {
    double error = getElevatorPosition() - setpoint;

    // Add a deadband to prevent oscillations
    if (Math.abs(error) < 0.5) { // Increased deadband to 0.5 rotations
        m_motor.set(0); // Stop the motor if within the deadband
        return;
    }

    // Calculate PID output with feedforward
    double feedforward = Constants.elevator.kG; // Gravity compensation
    double output = m_pidController.calculate(getElevatorPosition(), setpoint) + feedforward;

    // Limit motor output
    output = Math.max(-0.2, Math.min(0.2, output)); // Limit output between -0.2 and 0.2
    m_motor.set(output);
}
*/


public Command magicToPositionCommand(double setpoint) {
  return runOnce(() -> m_motor.setControl(m_positionRequest.withPosition(setpoint))); 
}
}



/*public Command wpilibPIDToPositionCommand(double setpoint) {
    return run(() -> setElevatorPositionWPILib(setpoint))
      .until(() -> Math.abs(getElevatorPosition() - setpoint) < 1.2) // Increased tolerance to 1.2 rotations
      .andThen(() -> m_motor.set(0)); 
}
  }
*/ 
