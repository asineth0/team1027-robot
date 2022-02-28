package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
/*import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;*/
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class Robot extends TimedRobot {
  TalonSRX fl_motor;
  TalonSRX fr_motor;
  TalonSRX bl_motor;
  TalonSRX br_motor;
  Joystick ctl;
  Joystick joy;

  TalonSRX crMotor;
  TalonSRX shooterOne;
  TalonSRX shooterTwo;

  TalonSRX shooterMotorOne;
  TalonSRX shooterMotorTwo;
  ADXRS450_Gyro gyro;
  // AnalogInput aimingPot;
  AnalogPotentiometer aimingAngle;

  @Override
  public void robotInit() {
    crMotor = new TalonSRX(20); //temp port number
    shooterOne = new TalonSRX(20); //placeholder
    shooterTwo = new TalonSRX(20); //placeholder
    shooterMotorOne = new TalonSRX(20); //placeholder
    shooterMotorTwo = new TalonSRX(20); //placeholder
    fl_motor = new TalonSRX(3);
    fr_motor = new TalonSRX(5); // on god on god
    bl_motor = new TalonSRX(2);
    br_motor = new TalonSRX(4);
    ctl = new Joystick(2);
    joy = new Joystick(1); //temp uwu
    //color_sensor = new ColorSensorV3(I2C.Port.kOnboard);
    gyro = new ADXRS450_Gyro();
    // aimingPot = new AnalogInput(20); //placeholder uwu
    // aimingPot.setAverageBits(2);
    // aimingAngle = new AnalogPotentiometer(aimingPot, 300, 0);
  }

  // Method to return the distance to the retroreflective tape using pixel width from vision processing.
  public double getDistance(int pixels) {
    return (127*4.2)/(0.0028*pixels);
  }
  
  // Method to set the speed of the rotation shooting hum.
  public void rotation(float speed) { 
    crMotor.set(ControlMode.PercentOutput, speed);
  }

//method to wind up shooter wheels
  public void shoot() {
   shooterOne.set(ControlMode.PercentOutput, 1);
   shooterOne.set(ControlMode.PercentOutput, 1);
    //if needed set to -1 for shooter
  }

  //method to control aim motor 
  public void setAim(float speed) {
    shooterMotorOne.set(ControlMode.PercentOutput, speed);
    shooterMotorTwo.set(ControlMode.PercentOutput, -speed);
  }

//method to control shooting angle 
  public void manAim() {
    double zAxis = -joy.getZ() * 360;
    double x = aimingAngle.get();
    float speed = (float)(zAxis - x) / 90;

    if (zAxis - x >= 5) {
        setAim(speed); //placehold
    }  
    else if (zAxis - x <= -5) {
        setAim(-speed); //placehold
    } 
    else {
        setAim(0); //stops motor in desired angle 
    }
  }


  public void drive_tank() {
    double in_sp = 0.5;
    fl_motor.set(ControlMode.PercentOutput, -in_sp * ctl.getRawAxis(1));
    fr_motor.set(ControlMode.PercentOutput, in_sp * ctl.getRawAxis(3));
    bl_motor.set(ControlMode.PercentOutput, -in_sp * ctl.getRawAxis(1));
    br_motor.set(ControlMode.PercentOutput, in_sp * ctl.getRawAxis(3));
  }

  public void drive_adv() {
    double in_x = ctl.getRawAxis(0); // left <-> right
    double in_y = -(ctl.getRawAxis(1)); // down <-> up, fuck you logitech.
    double in_rot = ctl.getRawAxis(2);
    double in_sp = Math.min(1, Math.max(0, -ctl.getRawAxis(3) + 0.5));

    bl_motor.set(ControlMode.PercentOutput, in_sp * ((in_y - in_x) + in_rot));
    fl_motor.set(ControlMode.PercentOutput, in_sp * ((in_y + in_x) + in_rot));
    br_motor.set(ControlMode.PercentOutput, in_sp * (-(in_y + in_x) + in_rot));
    fr_motor.set(ControlMode.PercentOutput, in_sp * (-(in_y - in_x) + in_rot));
  }

  @Override
  public void teleopPeriodic() {

    // drive_tank();
    drive_adv();
    SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
    if(ctl.getRawButton(1)) {
      gyro.calibrate();
    }
    if(ctl.getRawButton(2)) {
      gyro.reset();
    }
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }
}
