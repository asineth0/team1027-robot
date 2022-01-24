package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Robot extends TimedRobot {
  TalonSRX fl_motor;
  TalonSRX fr_motor;
  TalonSRX bl_motor;
  TalonSRX br_motor;
  Joystick ctl;

  @Override
  public void robotInit() {
    fl_motor = new TalonSRX(3);
    fr_motor = new TalonSRX(5); // on god on god
    bl_motor = new TalonSRX(2);
    br_motor = new TalonSRX(4);
    ctl = new Joystick(2);
  }

  public void drive_tank() {
    double in_sp = 0.5;
    fl_motor.set(ControlMode.PercentOutput, -in_sp * ctl.getRawAxis(1));
    fr_motor.set(ControlMode.PercentOutput, in_sp * ctl.getRawAxis(3));
    bl_motor.set(ControlMode.PercentOutput, -in_sp * ctl.getRawAxis(1));
    br_motor.set(ControlMode.PercentOutput, in_sp * ctl.getRawAxis(3));
  }

  public void ddrive_fancy() {
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
    ddrive_fancy();
  }
}
