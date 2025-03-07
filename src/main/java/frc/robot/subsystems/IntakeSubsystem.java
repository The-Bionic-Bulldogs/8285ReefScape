
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
public class IntakeSubsystem extends SubsystemBase {
	private static IntakeSubsystem instance;
  private SparkMax m_motor;
  //private and public variables defined here


  /**
	 * Returns the instance of the IntakeSubsystem subsystem.
	 * The purpose of this is to only create an instance if one does not already exist.
	 * @return IntakeSubsystem instance
	 */
  public static IntakeSubsystem getInstance() {
		if (instance == null)
			instance = new IntakeSubsystem();
		return instance;
	}
  
  public IntakeSubsystem() {
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
      .idleMode(IdleMode.kCoast)
      .inverted(Constants.intake.kInverted);

      m_motor = new SparkMax(Constants.intake.kMotorId, MotorType.kBrushed); //775 is brushed motor
      m_motor.configure(defaultConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    //m_motor.setInverted(false);
  }
  
  @Override
  public void periodic() {
  }

  //#region local controls (private)
  private void stopIntake() {
    m_motor.set(0.0);
  }

  private void startIntake(boolean inverted) {
    m_motor.set((inverted) ? Constants.intake.kRevSpeed : Constants.intake.kFwdSpeed);
  }
  //#endregion local controls (private)

  //#region public commands
  public Command stopCommand() {
    return runOnce(() -> stopIntake());
  }
  public Command fwdCommand() {
    return runOnce(() -> startIntake(false));
  }
  public Command revCommand() {
    return runOnce(() -> startIntake(true));
  }
  //#endregion public commands
}
