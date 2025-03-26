
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

/**
 * This subsystem handles managing the Template.
 * It is responsible for doing some stuff.
 */
public class IntakeSubsystem extends SubsystemBase {
	private static IntakeSubsystem instance;
  public WPI_TalonSRX m_motor;
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
    m_motor = new WPI_TalonSRX(Constants.intake.kMotorId); //CIM is brushed motor
    m_motor.configFactoryDefault(); //reset controller settings
    m_motor.set(ControlMode.PercentOutput,0); //stop any output
    m_motor.setNeutralMode(Constants.intake.kNeutralMode);
    m_motor.setInverted(Constants.intake.kInverted);
  }
  
  @Override
  public void periodic() {
  }

  //#region local controls (private)
  private void stop() {
    m_motor.set(0.0);
  }

  private void start(boolean inverted) {
    m_motor.set((inverted) ? Constants.intake.kRevSpeed : Constants.intake.kFwdSpeed);
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
    return  runOnce(() -> start(true)); 
  }
  //#endregion public commands
}
