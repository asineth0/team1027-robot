package frc.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
  private float xikaraX = 0;
  private float xikaraY = 0;
  private float xikaraZ = 0;

  @Override
  public void robotInit() {
    ctlXbox = new Joystick(0);
    motorDriveFL = new WrappedMotor(0, WrappedMotorType.TalonFX, 1);
    motorDriveBL = new WrappedMotor(2, WrappedMotorType.TalonFX, 1);
    motorDriveFR = new WrappedMotor(1, WrappedMotorType.TalonFX, -1);
    motorDriveBR = new WrappedMotor(3, WrappedMotorType.TalonFX, -1);
    limitFilterLX = new SlewRateLimiter(0.8);
    limitFilterLY = new SlewRateLimiter(0.8);
    limitFilterRX = new SlewRateLimiter(0.8);
    limitFilterRY = new SlewRateLimiter(0.8);

    new Thread(() -> {
      for (;;) {
        try (Socket xikaraSock = new Socket("10.10.27.100", 8081)) {
          Util.log("xikara OK v2");
          BufferedReader xikaraReader = new BufferedReader(new InputStreamReader(xikaraSock.getInputStream()));

          for (;;) {
            String line = xikaraReader.readLine();
            xikaraX = Float.parseFloat(line.split("\"x\":")[1].split(",")[0]);
            xikaraY = Float.parseFloat(line.split("\"y\":")[1].split(",")[0]);
            xikaraZ = Float.parseFloat(line.split("\"z\":")[1].split(",")[0]);
            SmartDashboard.putNumber(("xikara_x"), xikaraX);
            SmartDashboard.putNumber(("xikara_y"), xikaraY);
            SmartDashboard.putNumber(("xikara_z"), xikaraZ);
            Util.log(String.format("xikara msg: x=%f y=%f z=%f", xikaraX, xikaraY, xikaraZ));
          }

        } catch (Exception e) {
          Util.log("xikara disconnected - retrying in 1s");
          try {
            Thread.sleep(1000);
          } catch (Exception e2) {
            //
          }
        }
      }
    }).start(); // xikara XC thread
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
    double speed = Math.min((xikaraY / 10) * 0.15, 0.15);

    if (Math.abs(xikaraZ) <= 1) {
      speed = 0;
    }

    motorDriveFL.set(speed);
    motorDriveFR.set(speed);
    motorDriveBL.set(speed);
    motorDriveBR.set(speed);
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
    motorDriveFL.set((lx + ly + rx) * scale);
    motorDriveFR.set((-lx + ly - rx) * scale);
    motorDriveBL.set((-lx + ly + rx) * scale);
    motorDriveBR.set((lx + ly - rx) * scale);

    Util.log(String.format("teleop: fl=%f fr=%f bl=%f br=%f -> xx=%f xy=%f xz=%f",
        motorDriveFL.get(),
        motorDriveFR.get(),
        motorDriveBL.get(),
        motorDriveBR.get(),
        xikaraX,
        xikaraY,
        xikaraZ));
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
