package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
// import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.cameraserver.CameraServer;

// import javax.swing.text.StyledEditorKit.UnderlineAction;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorSensorV3;

public class Robot extends TimedRobot {
  protected static final int CTL_XBOX_BUTTON_A = 1;
  protected static final int CTL_XBOX_BUTTON_B = 2;
  protected static final int CTL_XBOX_BUTTON_X = 3;
  protected static final int CTL_XBOX_BUTTON_Y = 4;
  protected static final int CTL_XBOX_BUTTON_LB = 5; // L & R bumpers.
  protected static final int CTL_XBOX_BUTTON_RB = 6;
  protected static final int CTL_XBOX_AXIS_LX = 0; // L and R here refer to placement on the controller.
  protected static final int CTL_XBOX_AXIS_LY = 1;
  protected static final int CTL_XBOX_AXIS_RX = 4;
  protected static final int CTL_XBOX_AXIS_RY = 5;
  protected static final int CTL_XBOX_AXIS_TRIGGER_L = 2;
  protected static final int CTL_XBOX_AXIS_TRIGGER_R = 3;
  protected static final int CTL_XBOX_BUTTON_START = 8;

  WrappedMotor motor_ball_suck;
  WrappedMotor motor_ball_shoot;
  WrappedMotor motor_climb_1;
  WrappedMotor motor_climb_2;
  WrappedMotor motor_drive_bl;
  WrappedMotor motor_drive_br;
  WrappedMotor motor_drive_fl;
  WrappedMotor motor_drive_fr;
  Joystick ctl_xbox;
  DoubleSolenoid piston_1;
  DoubleSolenoid piston_2;
  int piston_1_active = 0;
  int piston_2_active = 0;
  int climb_enabled = 0;

  @Override
  public void robotInit() {
    motor_ball_suck = new WrappedMotor(WrappedMotorType.TALON_SRX, 8, 1);
    motor_ball_shoot = new WrappedMotor(WrappedMotorType.TALON_SRX, 9, 1);
    motor_climb_1 = new WrappedMotor(WrappedMotorType.TALON_FX, 10, 1);
    motor_climb_2 = new WrappedMotor(WrappedMotorType.TALON_FX, 12, 1);
    motor_drive_bl = new WrappedMotor(WrappedMotorType.TALON_FX, 2, 1);
    motor_drive_br = new WrappedMotor(WrappedMotorType.TALON_FX, 3, -1);
    motor_drive_fl = new WrappedMotor(WrappedMotorType.TALON_FX, 0, 1);
    motor_drive_fr = new WrappedMotor(WrappedMotorType.TALON_FX, 1, -1);
    ctl_xbox = new Joystick(0);
    piston_1 = new DoubleSolenoid(50, PneumaticsModuleType.CTREPCM, 0, 1);
    piston_2 = new DoubleSolenoid(50, PneumaticsModuleType.CTREPCM, 4, 3);
  } // RB = suck, A = spit, LB = blow(unsuck)

  @Override
  public void teleopInit() {
    //
  }

  @Override
  public void teleopPeriodic() {

    if (ctl_xbox.getRawButtonPressed(CTL_XBOX_BUTTON_START)) {
      if (climb_enabled == 0) {
        motor_drive_bl.set(0);
        motor_drive_br.set(0);
        motor_drive_fl.set(0);
        motor_drive_fr.set(0);
        motor_ball_shoot.set(0);
        motor_ball_suck.set(0);
        climb_enabled = 1;
      } else {
        motor_climb_1.set(0);
        motor_climb_2.set(0);
        climb_enabled = 0;
      }
    }

    if (climb_enabled == 0) {
      double in_x = ctl_xbox.getRawAxis(CTL_XBOX_AXIS_LX); // left <-> right
      double in_y = -(ctl_xbox.getRawAxis(CTL_XBOX_AXIS_LY)); // down <-> up, fuck you logitech.
      double in_rot = ctl_xbox.getRawAxis(CTL_XBOX_AXIS_RX);
      double in_sp = Math.min(1, Math.max(0, -ctl_xbox.getRawAxis(3) + 0.5));
      motor_drive_bl.set(in_sp * ((in_y - in_x) + in_rot));
      motor_drive_fl.set(in_sp * ((in_y + in_x) + in_rot));
      motor_drive_br.set(in_sp * -(-(in_y + in_x) + in_rot));
      motor_drive_fr.set(in_sp * -(-(in_y - in_x) + in_rot));
      motor_ball_suck.set(ctl_xbox.getRawButton(CTL_XBOX_BUTTON_A) ? -1 : 0);
      int motor_ball_shoot_v = 0;
      if (ctl_xbox.getRawButton(CTL_XBOX_BUTTON_RB)) {
        motor_ball_shoot_v = 1;
      }
      if (ctl_xbox.getRawButton(CTL_XBOX_BUTTON_LB)) {
        motor_ball_shoot_v = -1;
      }
      motor_ball_shoot.set(motor_ball_shoot_v);
    } else {
      motor_climb_1.set(ctl_xbox.getRawAxis(CTL_XBOX_AXIS_LY));
      motor_climb_2.set(ctl_xbox.getRawAxis(CTL_XBOX_AXIS_LY));

      if (ctl_xbox.getRawButtonPressed(CTL_XBOX_BUTTON_LB)) {
        if (piston_1_active == 0) {
          piston_1.set(DoubleSolenoid.Value.kForward);
          piston_1_active = 1;
        } else {
          piston_1.set(DoubleSolenoid.Value.kReverse);
          piston_1_active = 0;
        }
      }

      if (ctl_xbox.getRawButtonPressed(CTL_XBOX_BUTTON_RB)) {
        if (piston_2_active == 0) {
          piston_2.set(DoubleSolenoid.Value.kForward);
          piston_2_active = 1;
        } else {
          piston_2.set(DoubleSolenoid.Value.kReverse);
          piston_2_active = 0;
        }
      }
    }

    // // int sensor_color_r = sensor_color.getRed();
    // // int sensor_color_g = sensor_color.getGreen();
    // // int sensor_color_b = sensor_color.getBlue();
    // double pot_shoot_aim_deg = (pot_turret_aim.get() / 5.0f) * 300.0f / 2.0f;
    // // potentiometer rotates at factor of 2 compared to shooter aim (gear ratio)

    // if (ctl_stick.getRawButtonPressed(CTL_STICK_BUTTON_2)) {
    // if (mode_climb == 0) {
    // mode_climb = 1;
    // } else {
    // mode_climb = 0;
    // }
    // }

    // // driving + slow driving
    // if (mode_drive_tank == 1) {
    // drive_tank();
    // }

    // if (mode_drive_mecanum == 1) {
    // drive_mecanum();
    // }

    // // climb
    // if (mode_climb == 1) {
    // if (ctl_stick.getRawButtonPressed(CTL_STICK_BUTTON_8)) {
    // if (piston_1_active == 1) {
    // piston_1.set(DoubleSolenoid.Value.kForward);
    // piston_1_active = 0;
    // } else {
    // piston_1.set(DoubleSolenoid.Value.kReverse);
    // piston_1_active = 1;
    // }
    // }

    // if (ctl_stick.getRawButtonPressed(CTL_STICK_BUTTON_9)) {
    // if (piston_2_active == 1) {
    // piston_2.set(DoubleSolenoid.Value.kForward);
    // piston_2_active = 0;
    // } else {
    // piston_2.set(DoubleSolenoid.Value.kReverse);
    // piston_2_active = 1;
    // }
    // }

    // motor_climb_1.set(ControlMode.PercentOutput,
    // -ctl_stick.getRawAxis(CTL_STICK_AXIS_Y));
    // motor_climb_2.set(ControlMode.PercentOutput,
    // -ctl_stick.getRawAxis(CTL_STICK_AXIS_Y));
    // }

    // // cargo
    // if (ctl_xbox.getRawButton(CTL_XBOX_BUTTON_LB)) {
    // motor_cargo.set(ControlMode.PercentOutput, -1);
    // } else if (ctl_xbox.getRawButton(CTL_XBOX_BUTTON_RB)) {
    // motor_cargo.set(ControlMode.PercentOutput, 1);
    // } else if (ctl_xbox.getRawButton(CTL_XBOX_BUTTON_RB)) {
    // motor_cargo.set(ControlMode.PercentOutput, -1);
    // } else {
    // motor_cargo.set(ControlMode.PercentOutput, 0);
    // }

    // // turret -> aim

    // if (mode_climb == 0) {
    // // float aim_angle = (float) -(ctl_stick.getRawAxis(CTL_STICK_AXIS_KNOB) *
    // 90);
    // // float speed = (float) (aim_angle - pot_shoot_aim_deg) / 90;

    // // if (aim_angle - pot_shoot_aim_deg >= 5) {
    // // motor_turret_aim_1.set(ControlMode.PercentOutput, speed);
    // // motor_turret_aim_2.set(ControlMode.PercentOutput, speed);
    // // } else {
    // // if (aim_angle - pot_shoot_aim_deg <= -5) {
    // // motor_turret_aim_1.set(ControlMode.PercentOutput, -speed);
    // // motor_turret_aim_2.set(ControlMode.PercentOutput, -speed);
    // // } else {
    // // motor_turret_aim_1.set(ControlMode.PercentOutput, 0);
    // // motor_turret_aim_2.set(ControlMode.PercentOutput, 0);
    // // }
    // // }

    // if (ctl_stick.getRawButton(6)) {
    // motor_turret_aim_1.set(ControlMode.PercentOutput, 1);
    // motor_turret_aim_2.set(ControlMode.PercentOutput, 1);
    // } else if (ctl_stick.getRawButton(7)) {
    // motor_turret_aim_1.set(ControlMode.PercentOutput, -1);
    // motor_turret_aim_2.set(ControlMode.PercentOutput, -1);
    // } else {
    // motor_turret_aim_1.set(ControlMode.PercentOutput, 0);
    // motor_turret_aim_2.set(ControlMode.PercentOutput, 0);
    // }
    // // motor_turret_aim_1.set(ControlMode.PercentOutput,
    // // -ctl_stick.getRawAxis(CTL_STICK_AXIS_Y));
    // // motor_turret_aim_2.set(ControlMode.PercentOutput,
    // // -ctl_stick.getRawAxis(CTL_STICK_AXIS_Y));

    // // ^ new aim code

    // motor_turret_rotate.set(ControlMode.PercentOutput,
    // ctl_stick.getRawAxis(CTL_STICK_AXIS_X));
    // // if (hub_limit_left.get() && ctl_stick.getRawAxis(CTL_STICK_AXIS_X) < 0) {
    // // motor_turret_rotate.set(ControlMode.PercentOutput, 0);
    // // } else {
    // // if (hub_limit_right.get() && ctl_stick.getRawAxis(CTL_STICK_AXIS_X) > 0) {
    // // motor_turret_rotate.set(ControlMode.PercentOutput, 0);
    // // } else {
    // // motor_turret_rotate.set(ControlMode.PercentOutput,
    // // ctl_stick.getRawAxis(CTL_STICK_AXIS_X));
    // // }
    // // }
    // }

    // // turret -> ball shoot

    // if (ctl_stick.getRawButton(CTL_STICK_TRIGGER) || ctl_stick.getRawButton(0)) {
    // mode_ball_shoot = 1;
    // } else {
    // mode_ball_shoot = 0;
    // }

    // motor_turret_shoot_1.set(ControlMode.PercentOutput, -mode_ball_shoot);
    // motor_turret_shoot_2.set(ControlMode.PercentOutput, -mode_ball_shoot);
  }

  @Override
  public void autonomousInit() {
    //
  }

  @Override
  public void autonomousPeriodic() {
    //
  }
}
