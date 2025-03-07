
package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
      .inverted(false);

      m_motor = new SparkMax(2, MotorType.kBrushed); //775 is brushed motor
      m_motor.configure(defaultConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    //m_motor.setInverted(false);
  }
  
  @Override
  public void periodic() {
  }

  public void stopIntake() {
    m_motor.set(0.0);
  }

  public void startIntake(boolean inverted) {
    m_motor.set((inverted) ? -1.0 : 1.0);
  }

  public Command stopIntakeCommand() {
    return runOnce(() -> stopIntake());
  }
  public Command fwdIntakeCommand() {
    return runOnce(() -> startIntake(false));
  }
  public Command revIntakeCommand() {
    return runOnce(() -> startIntake(true));
  }

}
