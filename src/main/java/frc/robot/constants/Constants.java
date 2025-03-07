
package frc.robot.constants;

import static edu.wpi.first.units.Units.InchesPerSecond;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecondPerSecond;
import static edu.wpi.first.units.Units.Second;

import com.ctre.phoenix.motorcontrol.NeutralMode;

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
    
    public static final double kEleKp = 5.0;
    public static final double kEleKi = 0.0;
    public static final double kEleKd = 0.0;
    public static final double kMaxVelocity = Meters.of(1).per(Second).in(MetersPerSecond);
    public static final double kMaxAccel = Meters.of(2).per(Second).per(Second).in(MetersPerSecondPerSecond);
    public static final double kElekS = 0.02;
    public static final double kEleKG = 1.0;
    public static final double kElekV = 3.5;
    public static final double kElekA = 0.20;

    public class intake {
      public static final int kMotorId = 19; //id of intake motor
      public static final boolean kInverted = false; //invert motor
      public static final NeutralMode kNeutralMode = NeutralMode.Brake;
      public static final double kFwdSpeed = 1.0; //motor power for intake/outtake
      public static final double kRevSpeed = -1.0; //should be negative
    }
    public class lifter {
      public static final int kMotorId = 16; //id of climber motor
      public static final boolean kInverted = false; //invert motor
      public static final NeutralMode kNeutralMode = NeutralMode.Brake; //Brake or Coast
      public static final double kFwdSpeed = 1.0; //motor power for climb
      public static final double kRevSpeed = -1.0; //should be negative
    }
    public class tipper {
      public static final int kMotorId = 17; //id of tipper motor
      public static final boolean kInverted = false;
      public static final double kFwdSpeed = 1.0;
      public static final double kRevSpeed = -1.0; //should be negative
    }
    public class slider {
      public static final int kMotorId = 18;
      public static final boolean kInverted = false;
      public static final NeutralMode kNeutralMode = NeutralMode.Brake;
      public static final double kFwdSpeed = 0.25;
      public static final double kRevSpeed = -0.25;
    }
    public class elevator {
      public static final int kMotor1Id = 14;
      public static final boolean kInverted = false;
      public static final int kMotor2Id = 15;
      public static final boolean kInvertFromLeader = true;
      //For motion magic
      public static final double kP = 5.0;
      public static final double kI = 0.0;
      public static final double kD = 0.0;
      public static final double kS = 0.02;
      public static final double kG = 1.0;
      public static final double kV = 3.5;
      public static final double kA = 0.20;
      public static final double kMaxVelocity = Meters.of(1).per(Second).in(MetersPerSecond);
      public static final double kMaxAccel = Meters.of(2).per(Second).per(Second).in(MetersPerSecondPerSecond);
      //These will be needed for position controls later
      public class positions {
        public static final double DOWN = 0.0;
        public static final double L1 = 1.0;
        public static final double L2 = 2.0;
        public static final double L3 = 3.0;
        public static final double L4 = 4.5;
      }
    }
}
