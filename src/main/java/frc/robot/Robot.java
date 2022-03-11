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
import edu.wpi.first.cameraserver.CameraServer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorSensorV3;

public class Robot extends TimedRobot {
  protected static final int CTL_XBOX_BUTTON_A = 0;
  protected static final int CTL_XBOX_BUTTON_B = 1;
  protected static final int CTL_XBOX_BUTTON_X = 2;
  protected static final int CTL_XBOX_BUTTON_Y = 3;
  protected static final int CTL_XBOX_BUTTON_LB = 4; // L & R bumpers.
  protected static final int CTL_XBOX_BUTTON_RB = 5;
  protected static final int CTL_XBOX_AXIS_LX = 0; // L and R here refer to placement on the controller.
  protected static final int CTL_XBOX_AXIS_LY = 1;
  protected static final int CTL_XBOX_AXIS_RX = 4;
  protected static final int CTL_XBOX_AXIS_RY = 5;
  protected static final int CTL_STICK_AXIS_X = 0;
  protected static final int CTL_STICK_AXIS_Y = 1;
  protected static final int CTL_STICK_AXIS_KNOB = 2;
  protected static final int CTL_STICK_TRIGGER = 1;
  protected static final int CTL_STICK_BUTTON_2 = 2;
  protected static final int CTL_STICK_BUTTON_3 = 3;
  protected static final int CTL_STICK_BUTTON_4 = 4;
  protected static final int CTL_STICK_BUTTON_5 = 5;
  protected static final int CTL_STICK_BUTTON_6 = 6;
  protected static final int CTL_STICK_BUTTON_7 = 7;
  protected static final int CTL_STICK_BUTTON_8 = 8;
  protected static final int CTL_STICK_BUTTON_9 = 9;
  protected static final int CTL_STICK_BUTTON_10 = 10;
  protected static final int CTL_STICK_BUTTON_11 = 11;

  TalonSRX motor_drive_fl;
  TalonSRX motor_drive_fr;
  TalonSRX motor_drive_bl;
  TalonSRX motor_drive_br;
  TalonSRX motor_turret_shoot_1;
  TalonSRX motor_turret_shoot_2;
  TalonSRX motor_climb_1;
  TalonSRX motor_climb_2;

  VictorSPX motor_turret_rotate;
  VictorSPX motor_turret_aim_1;
  VictorSPX motor_turret_aim_2;
  VictorSPX motor_cargo;

  Joystick ctl_xbox;
  Joystick ctl_stick;

  ColorSensorV3 sensor_color;

  AnalogPotentiometer pot_turret_aim;

  DigitalInput shoot_aim_reset_left;
  DigitalInput shoot_aim_reset_right;
  DigitalInput hub_limit_left;
  DigitalInput hub_limit_right;
  DigitalInput cargo_check;

  DoubleSolenoid piston_one;
  DoubleSolenoid piston_two;
  Solenoid led_ring;

  Timer auto_timer;

  int mode_drive_tank = 0;
  int mode_drive_mecanum = 0;
  int mode_ball_shoot = 0;
  int mode_climb = 0;

  @Override
  public void robotInit() {
    motor_drive_fl = new TalonSRX(0);
    motor_drive_fr = new TalonSRX(1);
    motor_drive_bl = new TalonSRX(2);
    motor_drive_br = new TalonSRX(3);
    motor_turret_shoot_1 = new TalonSRX(7);
    motor_turret_shoot_2 = new TalonSRX(8);
    motor_climb_1 = new TalonSRX(10);
    motor_climb_2 = new TalonSRX(11);

    motor_turret_rotate = new VictorSPX(4);
    motor_turret_aim_1 = new VictorSPX(5);
    motor_turret_aim_2 = new VictorSPX(6);
    motor_cargo = new VictorSPX(9);

    ctl_xbox = new Joystick(2);
    ctl_stick = new Joystick(0);

    // sensor_color = new ColorSensorV3(I2C.Port.kOnboard);

    pot_turret_aim = new AnalogPotentiometer(0);

    shoot_aim_reset_left = new DigitalInput(0);
    shoot_aim_reset_right = new DigitalInput(1);
    hub_limit_left = new DigitalInput(2);
    hub_limit_right = new DigitalInput(3);
    cargo_check = new DigitalInput(4);

    piston_one = new DoubleSolenoid(50, PneumaticsModuleType.CTREPCM, 0, 1);
    piston_two = new DoubleSolenoid(50, PneumaticsModuleType.CTREPCM, 2, 3);
    led_ring = new Solenoid(50, PneumaticsModuleType.CTREPCM, 4);
    led_ring.set(true);

    auto_timer = new Timer();
    CameraServer.startAutomaticCapture();

  }

  public void drive_tank() {
    double in_sp = 0.5;
    motor_drive_fl.set(ControlMode.PercentOutput, -in_sp * ctl_xbox.getRawAxis(1));
    motor_drive_fr.set(ControlMode.PercentOutput, in_sp * ctl_xbox.getRawAxis(3));
    motor_drive_bl.set(ControlMode.PercentOutput, -in_sp * ctl_xbox.getRawAxis(1));
    motor_drive_br.set(ControlMode.PercentOutput, in_sp * ctl_xbox.getRawAxis(3));
  }

  public void drive_mecanum() {
    double in_x = ctl_xbox.getRawAxis(0); // left <-> right
    double in_y = -(ctl_xbox.getRawAxis(1)); // down <-> up, fuck you logitech.
    double in_rot = ctl_xbox.getRawAxis(2);
    double in_sp = Math.min(1, Math.max(0, -ctl_xbox.getRawAxis(3) + 0.5));

    motor_drive_bl.set(ControlMode.PercentOutput, in_sp * ((in_y - in_x) + in_rot));
    motor_drive_fl.set(ControlMode.PercentOutput, in_sp * ((in_y + in_x) + in_rot));
    motor_drive_br.set(ControlMode.PercentOutput, in_sp * (-(in_y + in_x) + in_rot));
    motor_drive_fr.set(ControlMode.PercentOutput, in_sp * (-(in_y - in_x) + in_rot));
  }

  @Override
  public void teleopPeriodic() {
    // int sensor_color_r = sensor_color.getRed();
    // int sensor_color_g = sensor_color.getGreen();
    // int sensor_color_b = sensor_color.getBlue();
    double pot_shoot_aim_deg = (pot_turret_aim.get() / 5.0f) * 300.0f / 2.0f;
    // potentiometer rotates at factor of 2 compared to shooter aim (gear ratio)

    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_2)) {
      if (mode_climb == 0) {
        mode_climb = 1;
      } else {
        mode_climb = 0;
      }
    }

    // driving
    if (mode_drive_tank == 1) {
      drive_tank();
    }

    if (mode_drive_mecanum == 1) {
      drive_mecanum();
    }

    // climb
    boolean piston_1_active = false;
    boolean piston_2_active = false;

    if (mode_climb == 1) {
      if (ctl_stick.getRawButton(CTL_STICK_BUTTON_8)) {
        piston_1_active = !piston_1_active;
      }

      if (ctl_stick.getRawButton(CTL_STICK_BUTTON_9)) {
        piston_2_active = !piston_2_active;
      }

      motor_climb_1.set(ControlMode.PercentOutput, ctl_stick.getRawAxis(CTL_STICK_AXIS_Y));
      motor_climb_2.set(ControlMode.PercentOutput, ctl_stick.getRawAxis(CTL_STICK_AXIS_Y));
    }

    if (piston_1_active) {
      piston_one.set(DoubleSolenoid.Value.kForward);
    }

    if (!piston_1_active) {
      piston_one.set(DoubleSolenoid.Value.kReverse);
    }

    if (piston_2_active) {
      piston_two.set(DoubleSolenoid.Value.kForward);
    }

    if (!piston_2_active) {
      piston_two.set(DoubleSolenoid.Value.kReverse);
    }

    // cargo
    if (ctl_xbox.getRawButton(CTL_XBOX_BUTTON_LB)) {
      motor_cargo.set(ControlMode.PercentOutput, -1);
    } else if (ctl_xbox.getRawButton(CTL_XBOX_BUTTON_RB)) {
      motor_cargo.set(ControlMode.PercentOutput, 1);
    } else {
      motor_cargo.set(ControlMode.PercentOutput, 0);
    }

    // turret -> aim

    if (mode_climb == 0) {
      // float aim_angle = (float) -(ctl_stick.getRawAxis(CTL_STICK_AXIS_KNOB) * 90);
      // float speed = (float) (aim_angle - pot_shoot_aim_deg) / 90;

      // if (aim_angle - pot_shoot_aim_deg >= 5) {
      //   motor_turret_aim_1.set(ControlMode.PercentOutput, speed);
      //   motor_turret_aim_2.set(ControlMode.PercentOutput, speed);
      // } else {
      //   if (aim_angle - pot_shoot_aim_deg <= -5) {
      //     motor_turret_aim_1.set(ControlMode.PercentOutput, -speed);
      //     motor_turret_aim_2.set(ControlMode.PercentOutput, -speed);
      //   } else {
      //     motor_turret_aim_1.set(ControlMode.PercentOutput, 0);
      //     motor_turret_aim_2.set(ControlMode.PercentOutput, 0);
      //   }
      // }

      if (hub_limit_left.get() && ctl_stick.getRawAxis(CTL_STICK_AXIS_X) < 0) {
        motor_turret_rotate.set(ControlMode.PercentOutput, 0);
      } else {
        if (hub_limit_right.get() && ctl_stick.getRawAxis(CTL_STICK_AXIS_X) > 0) {
          motor_turret_rotate.set(ControlMode.PercentOutput, 0);
        } else {
          motor_turret_rotate.set(ControlMode.PercentOutput, ctl_stick.getRawAxis(CTL_STICK_AXIS_X));
        }
      }
    }

    // turret -> ball shoot

    if (ctl_stick.getRawButton(CTL_STICK_TRIGGER)) {
      mode_ball_shoot = 1;
    } else {
      mode_ball_shoot = 0;
    }

    motor_turret_shoot_1.set(ControlMode.PercentOutput, mode_ball_shoot);
    motor_turret_shoot_2.set(ControlMode.PercentOutput, mode_ball_shoot);

    // SmartDashboard.putNumber("sensor_color_r", sensor_color_r);
    // SmartDashboard.putNumber("sensor_color_g", sensor_color_g);
    // SmartDashboard.putNumber("sensor_color_b", sensor_color_b);
    SmartDashboard.putNumber("pot_shoot_aim_deg", pot_shoot_aim_deg);
    SmartDashboard.putBoolean("piston_1_active", piston_1_active);
    SmartDashboard.putBoolean("piston_2_active", piston_2_active);
  }

  @Override
  public void autonomousInit() {
    auto_timer.start();
  }

  @Override
  public void autonomousPeriodic() {
    if (auto_timer.get() < 1.5) {
      motor_drive_fl.set(ControlMode.PercentOutput, -1);
      motor_drive_fr.set(ControlMode.PercentOutput, -1);
      motor_drive_bl.set(ControlMode.PercentOutput, -1);
      motor_drive_br.set(ControlMode.PercentOutput, -1);
    } else {
      motor_drive_fl.set(ControlMode.PercentOutput, 0);
      motor_drive_fr.set(ControlMode.PercentOutput, 0);
      motor_drive_bl.set(ControlMode.PercentOutput, 0);
      motor_drive_br.set(ControlMode.PercentOutput, 0);
    }
  }

  @Override
  public void testPeriodic() {
    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_8)) {
      motor_drive_fl.set(ControlMode.PercentOutput, 1);
      motor_drive_bl.set(ControlMode.PercentOutput, 1);
    } else {
      motor_drive_fl.set(ControlMode.PercentOutput, 0);
      motor_drive_bl.set(ControlMode.PercentOutput, 0);
    }

    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_9)) {
      motor_drive_fr.set(ControlMode.PercentOutput, -1);
      motor_drive_br.set(ControlMode.PercentOutput, -1);
    } else {
      motor_drive_fr.set(ControlMode.PercentOutput, 0);
      motor_drive_br.set(ControlMode.PercentOutput, 0);
    }

    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_2)) {
      motor_climb_1.set(ControlMode.PercentOutput, 1);
      motor_climb_2.set(ControlMode.PercentOutput, 1);
    } else {
      motor_climb_1.set(ControlMode.PercentOutput, 0);
      motor_climb_2.set(ControlMode.PercentOutput, 0);
    }

    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_3)) {
      motor_climb_1.set(ControlMode.PercentOutput, -1);
      motor_climb_2.set(ControlMode.PercentOutput, -1);
    } else {
      motor_climb_1.set(ControlMode.PercentOutput, 0);
      motor_climb_2.set(ControlMode.PercentOutput, 0);
    }

    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_6)) {
      motor_turret_rotate.set(ControlMode.PercentOutput, 1);
    } else {
      motor_turret_rotate.set(ControlMode.PercentOutput, 0);
    }

    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_7)) {
      motor_cargo.set(ControlMode.PercentOutput, -1);
    } else {
      motor_cargo.set(ControlMode.PercentOutput, 0);
    }

    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_10)) {
      motor_turret_shoot_1.set(ControlMode.PercentOutput, -1);
    } else {
      motor_turret_shoot_1.set(ControlMode.PercentOutput, 0);
    }

    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_11)) {
      motor_turret_shoot_2.set(ControlMode.PercentOutput, -1);
    } else {
      motor_turret_shoot_2.set(ControlMode.PercentOutput, 0);
    }

    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_4)) {
      motor_turret_aim_1.set(ControlMode.PercentOutput, 1);
    } else {
      motor_turret_aim_1.set(ControlMode.PercentOutput, 0);
    }

    if (ctl_stick.getRawButton(CTL_STICK_BUTTON_5)) {
      motor_turret_aim_2.set(ControlMode.PercentOutput, 1);
    } else {
      motor_turret_aim_2.set(ControlMode.PercentOutput, 0);
    }

    if (ctl_stick.getRawButton(CTL_STICK_TRIGGER)) {
      motor_turret_aim_1.set(ControlMode.PercentOutput, -1);
      // motor_turret_aim_2.set(ControlMode.PercentOutput, -1);
    } else {
      motor_turret_aim_1.set(ControlMode.PercentOutput, 0);
      // motor_turret_aim_2.set(ControlMode.PercentOutput, 0);
    }
  }
}
