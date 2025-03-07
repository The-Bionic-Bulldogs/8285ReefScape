
package frc.robot.constants;

import static edu.wpi.first.units.Units.InchesPerSecond;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecondPerSecond;
import static edu.wpi.first.units.Units.Second;

//Sometimes it is useful to comment out the following to see what variables or what controller buttons are not assigned yet
@SuppressWarnings("unused") //We silence the "unused variables" warnings in VSCode
/**
 * Constants for the Operator Interface
 */
public class Constants { 
    public static final int JoyDriverID = 0; //ID of Driver Joystick
    public static final int JoyOperID = 1; //ID of Operator Joystick
    public static final int JoyProgID = 2; //ID of Programmer Joystick
    public static final double kMinDeadband = 0.02; //Deadband for analog joystick axis minimum
    public static final double kMaxDeadband = 0.98; //Deadband for analog joystick axis minimum
    public static final int e_motor1 = 14; //first motor for elevator
    public static final int e_motor2 = 15; //second motor for elevator
    
    public static final double kEleKp = 5;
    public static final double kEleKi = 0;
    public static final double kEleKd = 0;
    public static final double kMaxVelocity = Meters.of(1).per(Second).in(MetersPerSecond);
    public static final double kMaxAccel = Meters.of(2).per(Second).per(Second).in(MetersPerSecondPerSecond);
    public static final double kElekS = .02;
    public static final double kEleKG = 1;
    public static final double kElekV = 3.5;
    public static final double kElekA = .20;


}
