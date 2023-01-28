package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// this is a comment

/*
 * this is a multiline ocmmnet
 */

public class Robot extends TimedRobot {
  private static final int CTL_XBOX_AXIS_LX = 0;
  private static final int CTL_XBOX_AXIS_LY = 1; 
  private static final int CTL_XBOX_AXIS_RX = 4;
  private static final int CTL_XBOX_AXIS_RY = 5;
  private static final int CTL_XBOX_AXIS_RT = 3;
  private static final int CTL_XBOX_AXIS_LT = 2;
  private Joystick ctlXbox;
  private WrappedMotor motorDriveBL;
  private WrappedMotor motorDriveBR;
  private WrappedMotor motorDriveFL;
  private WrappedMotor motorDriveFR;
  private SlewRateLimiter limitFilterLX;
  private SlewRateLimiter limitFilterRX;
  private SlewRateLimiter limitFilterLY;
  private SlewRateLimiter limitFilterRY;

  @Override
  public void robotInit() {
    // joystick-motor
    ctlXbox = new Joystick(0);
    motorDriveFL = new WrappedMotor(0, WrappedMotorType.TalonFX, 1);
    motorDriveBL = new WrappedMotor(2, WrappedMotorType.TalonFX, 1);
    motorDriveFR = new WrappedMotor(1, WrappedMotorType.TalonFX, -1);
    motorDriveBR = new WrappedMotor(3, WrappedMotorType.TalonFX, -1);
    limitFilterLX = new SlewRateLimiter(0.8);
    limitFilterLY = new SlewRateLimiter(0.8);
    limitFilterRX = new SlewRateLimiter(0.8);
    limitFilterRY = new SlewRateLimiter(0.8);
  }

  @Override
  public void robotPeriodic() {
    //
  }

  @Override
  public void autonomousInit() {
    //
  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {
    //
  }

  @Override
  public void teleopPeriodic() {
    // get controller inputs
    double lx = ctlXbox.getRawAxis(CTL_XBOX_AXIS_LX);
    double ly = ctlXbox.getRawAxis(CTL_XBOX_AXIS_LY) * -1;
    double rx = ctlXbox.getRawAxis(CTL_XBOX_AXIS_RX);
    double ry = ctlXbox.getRawAxis(CTL_XBOX_AXIS_RY) * -1;
    double scale = 1;

    if (ctlXbox.getRawAxis(CTL_XBOX_AXIS_RT) > 0.2) {
      scale *= 0.5;
    }

    // add 5% deadzone
    lx = Math.abs(lx) > 0.1 ? lx : 0;
    ly = Math.abs(ly) > 0.1 ? ly : 0;
    rx = Math.abs(rx) > 0.1 ? rx : 0;
    ry = Math.abs(ry) > 0.1 ? ry : 0;

    // slew rate limiter
    // lx = limitFilterLX.calculate(lx);
    // ly = limitFilterLY.calculate(ly);
    // rx = limitFilterRX.calculate(rx);
    // ry = limitFilterRY.calculate(ry);

    // tank drive:
    // motorDriveFL.set(ly);
    // motorDriveBL.set(ly);
    // motorDriveFR.set(ry);
    // motorDriveBR.set(ry);

    // mecanum drive:
    double flSpeed = (lx + ly + rx) * scale;
    double frSpeed = (-lx + ly - rx) * scale;
    double blSpeed = (-lx + ly + rx) * scale;
    double brSpeed = (lx + ly - rx) * scale;
    motorDriveFL.set(flSpeed);
    motorDriveBL.set(blSpeed);
    motorDriveFR.set(frSpeed);
    motorDriveBR.set(brSpeed);
  }

  @Override
  public void disabledInit() {
    //
  }

  @Override
  public void disabledPeriodic() {
    //
  }

  @Override
  public void testInit() {
    //
  }

  @Override
  public void testPeriodic() {
    //
  }
}
