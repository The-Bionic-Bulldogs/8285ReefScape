package frc.robot.subsystems;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import com.ctre.phoenix6.sim.TalonFXSimState;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.trajectory.TrapezoidProfile;


public class eleTest extends SubsystemBase {
private TalonFX e_motor1 = new TalonFX(Constants.e_motor1);
private TalonFX e_motor2 = new TalonFX(Constants.e_motor2);

private final ProfiledPIDController  elePIDController = new ProfiledPIDController
(Constants.kEleKp, Constants.kEleKi, Constants.kEleKd,
 new TrapezoidProfile.Constraints(Constants.kMaxVelocity,  Constants.kMaxAccel));

private final ElevatorFeedforward e_ElevatorFeedforward =
 new ElevatorFeedforward(Constants.kElekS, Constants.kEleKG, Constants.kElekV, Constants.kElekA);

 private final ElevatorSim eleSim = null;



TalonFXConfiguration eleConfiguration = new TalonFXConfiguration();
//work in progress below
public void runele(double speed){
    if(speed > 1){
    speed = 1;   
    } 
    if(speed < 1) {
    speed = -1;
    }
    if(Math.abs(speed) > 0.1){
    e_motor1.set(speed);
    e_motor2.set(-speed);
    } else stop();
    
}
public void stop(){
    e_motor1.set(0);
    e_motor2.set(0); 
    System.out.println("elevator stoped");
}
public double speedGet(){
    return e_motor1.get();
}
}
