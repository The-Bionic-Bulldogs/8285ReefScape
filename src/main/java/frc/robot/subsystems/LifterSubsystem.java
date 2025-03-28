
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
public class LifterSubsystem extends SubsystemBase {
	private static LifterSubsystem instance;
  //private and public variables defined here
  private WPI_TalonSRX m_motor;

  /**
	 * Returns the instance of the lifterSubsystem subsystem.
	 * The purpose of this is to only create an instance if one does not already exist.
	 * @return lifterSubsystem instance
	 */
  public static LifterSubsystem getInstance() {
		if (instance == null)
			instance = new LifterSubsystem();
		return instance;
	}
  
  public LifterSubsystem() {
    //initialize values for private and public variables, etc.
    init();
  }
  
  /**
   * The init method resets and operational state of the subsystem
   */
  public void init() {
    // set initial stuff, etc.
    m_motor = new WPI_TalonSRX(Constants.lifter.kMotorId); //CIM is brushed motor
    m_motor.configFactoryDefault(); //reset controller settings
    m_motor.set(ControlMode.PercentOutput,0); //stop any output
    m_motor.setNeutralMode(Constants.lifter.kNeutralMode);
    m_motor.setInverted(Constants.lifter.kInverted);
  }
  
  @Override
  public void periodic() {
  }

  //#region local controls (private)
  private void stop() {
    m_motor.set(0.0);
  }
  private void start(boolean inverted) {
    m_motor.set((inverted) ? Constants.lifter.kRevSpeed : Constants.lifter.kFwdSpeed);
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
