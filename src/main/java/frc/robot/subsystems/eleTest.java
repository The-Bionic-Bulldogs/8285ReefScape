package frc.robot.subsystems;
/* 
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import com.ctre.phoenix6.sim.TalonFXSimState;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.trajectory.TrapezoidProfile;


public class eleTest extends SubsystemBase {
private TalonFX e_motor1 = new TalonFX(Constants.e_motor1);  //use m_somename... this is called a "member variable", ie, one that is local to this class
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
    //looks like you are trying to limit the input to a max of 1, don't forget you need to handle -1 also...
    // speed = Math.max(-1.0, Math.min(speed, 1.0)); //This makes absolutely sure it is between -1.0 and 1.0
    speed = MathUtil.clamp(speed,-1.0,1.0); //This makes absolutely sure it is betwwen -1.0 and 1.0
    
    //it appears you were checking for a deadband value of 0.1.  This should be handled on the input side, rather than on the control side.
    // however, if you wanted to do it, you could do
    speed = MathUtil.applyDeadband(speed, 0.1);

    //This works, but can be dangerous if something goes wrong. See ElevatorSubsystem.java for an example of creating a follower
    //then, you can simply apply control only to the leader.
    e_motor1.set(speed);
    e_motor2.set(-speed);  //e_motor2.set(speed * -1.0); //is often easier to see that you are inverting the value, but both works

    // //your code
    // if(speed > 1){
    // speed = 1;   
    // } 
    // if(speed < 1) { //this wont work, you are saying anything less than 1 is full reverse
    // speed = -1; //TIP: dont use integers when its actually a double.  ie. use -1.0 instead of -1
    // }
    // if(Math.abs(speed) > 0.1){
    // e_motor1.set(speed);
    // e_motor2.set(-speed);
    // } else stop();
    
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
*/