
package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

/**
 * This subsystem handles managing the Template.
 * It is responsible for doing some stuff.
 */
public class SliderSubsystem extends SubsystemBase {
	private static SliderSubsystem instance;
  //private and public variables defined here
  private TalonFXS m_motor;

  /**
	 * Returns the instance of the SliderSubsystem subsystem.
	 * The purpose of this is to only create an instance if one does not already exist.
	 * @return SliderSubsystem instance
	 */
  public static SliderSubsystem getInstance() {
		if (instance == null)
			instance = new SliderSubsystem();
		return instance;
	}
  
  public SliderSubsystem() {
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
      .withKP(Constants.slider.kP)
      .withKI(Constants.slider.kI)
      .withKD(Constants.slider.kD)
      .withKS(Constants.slider.kS)
      .withKV(Constants.slider.kV)
      .withKA(Constants.slider.kA)
      .withKG(Constants.slider.kG);
    fxConfig.Slot0 = clConfigs;
    SoftwareLimitSwitchConfigs softLimitSwitchConfigs = new SoftwareLimitSwitchConfigs()
      .withReverseSoftLimitEnable(Constants.slider.kSoftReverseLimitEnable)
      .withReverseSoftLimitThreshold(Constants.slider.kSoftReverseLimit)
      .withForwardSoftLimitEnable(Constants.slider.kSoftForwardLimitEnable)
      .withForwardSoftLimitThreshold(Constants.slider.kSoftForwardLimit);
    fxConfig.SoftwareLimitSwitch = softLimitSwitchConfigs;
    MotorOutputConfigs mOutputConfigs = new MotorOutputConfigs()
      .withNeutralMode(Constants.slider.kNeutralMode)
      .withInverted((Constants.slider.kInverted) ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive);
    fxConfig.MotorOutput = mOutputConfigs;
    m_motor = new TalonFXS(Constants.slider.kMotorId);
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
    m_motor.set((inverted) ? Constants.slider.kRevSpeed : Constants.slider.kFwdSpeed);
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
