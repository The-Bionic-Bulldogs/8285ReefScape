
package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

/**
 * This subsystem handles managing the Template.
 * It is responsible for doing some stuff.
 */
public class TipperSubsystem extends SubsystemBase {
	private static TipperSubsystem instance;
  //private and public variables defined here
  private TalonFXS m_motor;

  /**
	 * Returns the instance of the TipperSubsystem subsystem.
	 * The purpose of this is to only create an instance if one does not already exist.
	 * @return TipperSubsystem instance
	 */
  public static TipperSubsystem getInstance() {
		if (instance == null)
			instance = new TipperSubsystem();
		return instance;
	}
  
  public TipperSubsystem() {
    //initialize values for private and public variables, etc.
    init();
  }
  
  /**
   * The init method resets and operational state of the subsystem
   */
  public void init() {
    // set initial stuff, etc.
    TalonFXSConfiguration fxConfig = new TalonFXSConfiguration();
    Slot0Configs clConfigs = new Slot0Configs()
      .withKP(Constants.tipper.kP)
      .withKI(Constants.tipper.kI)
      .withKD(Constants.tipper.kD)
      .withKS(Constants.tipper.kS)
      .withKV(Constants.tipper.kV)
      .withKA(Constants.tipper.kA)
      .withKG(Constants.tipper.kG);
    fxConfig.Slot0 = clConfigs;
    SoftwareLimitSwitchConfigs softLimitSwitchConfigs = new SoftwareLimitSwitchConfigs()
      .withReverseSoftLimitEnable(Constants.tipper.kSoftReverseLimitEnable)
      .withReverseSoftLimitThreshold(Constants.tipper.kSoftReverseLimit)
      .withForwardSoftLimitEnable(Constants.tipper.kSoftForwardLimitEnable)
      .withForwardSoftLimitThreshold(Constants.tipper.kSoftForwardLimit);
    fxConfig.SoftwareLimitSwitch = softLimitSwitchConfigs;
    MotorOutputConfigs mOutputConfigs = new MotorOutputConfigs()
      .withNeutralMode(Constants.tipper.kNeutralMode)
      .withInverted((Constants.tipper.kInverted) ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive);
    fxConfig.MotorOutput = mOutputConfigs;
    fxConfig.Commutation.MotorArrangement = MotorArrangementValue.Minion_JST;
    m_motor = new TalonFXS(Constants.tipper.kMotorId);
    m_motor.getConfigurator().apply(fxConfig);
  }
  
  @Override
  public void periodic() {
  }

  //#region local controls (private)
  private void stop() {
    m_motor.set(0.0);
  }
  private void start(boolean inverted) {
    m_motor.set((inverted) ? Constants.tipper.kRevSpeed : Constants.tipper.kFwdSpeed);
  }
  //#endregion local controls

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

}
