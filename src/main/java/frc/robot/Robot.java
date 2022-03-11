package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorSensorV3;

public class Robot extends TimedRobot {
  protected static final int MOTOR_DRIVE_FL_ID = 0;
  protected static final int MOTOR_DRIVE_FR_ID = 0; // on god on god
  protected static final int MOTOR_DRIVE_BL_ID = 0;
  protected static final int MOTOR_DRIVE_BR_ID = 0;
  protected static final int MOTOR_TURRET_SHOOT_1_ID = 0;
  protected static final int MOTOR_TURRET_SHOOT_2_ID = 0;
  protected static final int MOTOR_CLIMB_1_ID = 0;
  protected static final int MOTOR_CLIMB_2_ID = 0;
  protected static final int MOTOR_TURRET_ROTATE_ID = 0;
  protected static final int MOTOR_TURRET_AIM_1_ID = 0;
  protected static final int MOTOR_TURRET_AIM_2_ID = 0;
  protected static final int MOTOR_TURRET_SHOOT_ID = 0;
  protected static final int CTL_XBOX_ID = 0;
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
  protected static final int CTL_STICK_ID = 1;
  protected static final int CTL_STICK_AXIS_X = 0;
  protected static final int CTL_STICK_AXIS_Y = 1;
  protected static final int CTL_STICK_AXIS_KNOB = 2;
  protected static final int CTL_STICK_BUTTON_2 = 1;
  protected static final int CTL_STICK_BUTTON_3 = 2;
  protected static final int CTL_STICK_BUTTON_4 = 3;
  protected static final int CTL_STICK_BUTTON_5 = 4;
  protected static final int POT_SHOOT_AIM_ID = 0; // smoke weed every day.

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
  VictorSPX motor_turret_shoot;

  Joystick ctl_xbox;
  Joystick ctl_stick;

  ColorSensorV3 sensor_color;

  AnalogPotentiometer pot_turret_aim;

  int mode_drive_tank = 0;
  int mode_drive_mecanum = 0;
  int mode_climb = 0;
  int mode_ball_shoot = 0;

  @Override
  public void robotInit() {
    motor_drive_fl = new TalonSRX(MOTOR_DRIVE_FL_ID);
    motor_drive_fr = new TalonSRX(MOTOR_DRIVE_FR_ID);
    motor_drive_bl = new TalonSRX(MOTOR_DRIVE_BL_ID);
    motor_drive_br = new TalonSRX(MOTOR_DRIVE_BR_ID);
    motor_turret_shoot_1 = new TalonSRX(MOTOR_TURRET_SHOOT_1_ID);
    motor_turret_shoot_2 = new TalonSRX(MOTOR_TURRET_SHOOT_2_ID);
    motor_climb_1 = new TalonSRX(MOTOR_CLIMB_1_ID);
    motor_climb_2 = new TalonSRX(MOTOR_CLIMB_2_ID);

    motor_turret_rotate = new VictorSPX(MOTOR_TURRET_ROTATE_ID);
    motor_turret_aim_1 = new VictorSPX(MOTOR_TURRET_AIM_1_ID);
    motor_turret_aim_2 = new VictorSPX(MOTOR_TURRET_AIM_2_ID);
    motor_turret_shoot = new VictorSPX(MOTOR_TURRET_SHOOT_ID);

    ctl_xbox = new Joystick(CTL_XBOX_ID);
    ctl_stick = new Joystick(CTL_STICK_ID);

    sensor_color = new ColorSensorV3(I2C.Port.kOnboard);

    pot_turret_aim = new AnalogPotentiometer(POT_SHOOT_AIM_ID);
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
    int sensor_color_r = sensor_color.getRed();
    int sensor_color_g = sensor_color.getGreen();
    int sensor_color_b = sensor_color.getBlue();
    double pot_shoot_aim_deg = (pot_turret_aim.get() / 5.0f) * 300.0f;

    // driving

    if (mode_drive_tank == 1) {
      drive_tank();
    }

    if (mode_drive_mecanum == 1) {
      drive_mecanum();
    }

    if (mode_climb == 1) {
      //
    }

    // turret -> aim

    // turret -> ball shoot

    if (ctl_stick.getRawButtonPressed(CTL_STICK_BUTTON_2)) {
      if (mode_ball_shoot == 0) {
        mode_ball_shoot = 1;
      } else {
        mode_ball_shoot = 0;
      }

      motor_turret_shoot_1.set(ControlMode.PercentOutput, mode_ball_shoot);
      motor_turret_shoot_2.set(ControlMode.PercentOutput, mode_ball_shoot);
    }

    SmartDashboard.putNumber("sensor_color_r", sensor_color_r);
    SmartDashboard.putNumber("sensor_color_g", sensor_color_g);
    SmartDashboard.putNumber("sensor_color_b", sensor_color_b);
    SmartDashboard.putNumber("pot_shoot_aim_deg", pot_shoot_aim_deg);
  }
}
