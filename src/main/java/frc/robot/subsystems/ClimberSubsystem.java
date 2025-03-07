
package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

/**
 * This subsystem handles managing the Template.
 * It is responsible for doing some stuff.
 */
public class ClimberSubsystem extends SubsystemBase {
	private static ClimberSubsystem instance;
  //private and public variables defined here
  private SparkMax m_motor;

  /**
	 * Returns the instance of the ClimberSubsystem subsystem.
	 * The purpose of this is to only create an instance if one does not already exist.
	 * @return ClimberSubsystem instance
	 */
  public static ClimberSubsystem getInstance() {
		if (instance == null)
			instance = new ClimberSubsystem();
		return instance;
	}
  
  public ClimberSubsystem() {
    //initialize values for private and public variables, etc.
    init();
  }
  
  /**
   * The init method resets and operational state of the subsystem
   */
  public void init() {
    // set initial stuff, etc.
    SparkMaxConfig defaultConfig = new SparkMaxConfig();
    defaultConfig
      .smartCurrentLimit(30)
      .idleMode(IdleMode.kBrake)
      .inverted(Constants.climber.kInverted);

      m_motor = new SparkMax(Constants.climber.kMotorId, MotorType.kBrushed); //CIM is brushed motor
      m_motor.configure(defaultConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    //m_motor.setInverted(false);
  }
  
  @Override
  public void periodic() {
  }

  //#region local controls (private)
  private void stopClimber() {
    m_motor.set(0.0);
  }
  private void startClimber(boolean inverted) {
    m_motor.set((inverted) ? Constants.climber.kRevSpeed : Constants.climber.kFwdSpeed);
  }
  //#endregion local controls

  //#region public commands
  public Command stopCommand() {
    return runOnce(() -> stopClimber());
  }
  public Command fwdCommand() {
    return runOnce(() -> startClimber(false));
  }
  public Command revCommand() {
    return runOnce(() -> startClimber(true));
  }
  //#endregion public commands

}
