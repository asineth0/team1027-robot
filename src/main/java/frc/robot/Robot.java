package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Robot extends TimedRobot {
  private static final int XBOX_AXIS_LX = 0;
  private static final int XBOX_AXIS_LY = 1;
  private static final int XBOX_AXIS_LT = 2;
  private static final int XBOX_AXIS_RT = 3;
  private static final int XBOX_AXIS_RX = 4;
  private static final int XBOX_AXIS_RY = 5;
  private static final int XBOX_BUTTON_A = 1;
  private static final int XBOX_BUTTON_B = 2;
  private static final int XBOX_BUTTON_X = 3;
  private static final int XBOX_BUTTON_Y = 4;
  private static final int XBOX_BUTTON_LB = 5;
  private static final int XBOX_BUTTON_RB = 6;
  private static final int XBOX_BUTTON_DUP = 6;
  private static final int XBOX_BUTTON_DDOWN = 6;
  private static final int XBOX_BUTTON_DLEFT = 6;
  private static final int XBOX_BUTTON_DRIGHT = 6;
  private Joystick xbox1;
  private Joystick xbox2;
  private WrappedMotor mDriveBL;
  private WrappedMotor mDriveBR;
  private WrappedMotor mDriveFL;
  private WrappedMotor mDriveFR;
  private WrappedMotor mArm;
  private WrappedMotor mIntake;
  private DoubleSolenoid pIntakeRachet;
  private DoubleSolenoid pIntake;
  private DoubleSolenoid pGrip;
  private Solenoid pIntakePin;
  private Timer autoTimer;

  @Override
  public void robotInit() {
    xbox1 = new Joystick(0);
    xbox2 = new Joystick(1);
    mDriveFL = new WrappedMotor(0, WrappedMotorType.TalonFX, 1);
    mDriveBL = new WrappedMotor(2, WrappedMotorType.TalonFX, 1);
    mDriveFR = new WrappedMotor(1, WrappedMotorType.TalonFX, -1);
    mDriveBR = new WrappedMotor(3, WrappedMotorType.TalonFX, -1);
    mArm = new WrappedMotor(10, WrappedMotorType.TalonSRX, -1); // +extends
    mIntake = new WrappedMotor(20, WrappedMotorType.TalonSRX, 1);
    pIntakeRachet = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 3, 4);
    pIntake = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
    pGrip = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 5, 6);
    pIntakePin = new Solenoid(PneumaticsModuleType.CTREPCM, 2);
    autoTimer = new Timer();
  }

  @Override
  public void robotPeriodic() {
    //
  }

  @Override
  public void autonomousInit() {
    autoTimer.reset();
    autoTimer.start();
  }

  @Override
  public void autonomousPeriodic() {
    if (autoTimer.get() < 1) {
      mDriveFL.set(0.2);
      mDriveFR.set(0.2);
      mDriveBL.set(0.2);
      mDriveBR.set(0.2);
    }

    if (autoTimer.get() > 1) {
      mDriveFL.set(0);
      mDriveFR.set(0);
      mDriveBL.set(0);
      mDriveBR.set(0);
    }
  }

  @Override
  public void teleopInit() {
    //
  }

  @Override
  public void teleopPeriodic() {
    // drive inputs 
    double driveLX = xbox1.getRawAxis(XBOX_AXIS_LX);
    double driveLY = xbox1.getRawAxis(XBOX_AXIS_LY) * -1;
    double driveRX = xbox1.getRawAxis(XBOX_AXIS_RX);
    driveLX = Math.abs(driveLX) > 0.1 ? driveLX : 0;
    driveLY = Math.abs(driveLY) > 0.1 ? driveLY : 0;
    driveRX = Math.abs(driveRX) > 0.1 ? driveRX : 0;

    // drive DPI
    double driveScale = 1;
    if (xbox1.getRawAxis(XBOX_AXIS_RT) > 0.25)
      driveScale *= 0.5;
    double mDriveFLVal = driveLX + driveLY + driveRX;
    double mDriveFRVal = -driveLX + driveLY - driveRX;
    double mDriveBLVal = -driveLX + driveLY + driveRX;
    double mDriveBRVal = driveLX + driveLY - driveRX;

    // drive boost
    double mDriveMaxVal = 0;
    mDriveMaxVal = Math.max(mDriveMaxVal, mDriveFLVal);
    mDriveMaxVal = Math.max(mDriveMaxVal, mDriveFRVal);
    mDriveMaxVal = Math.max(mDriveMaxVal, mDriveBLVal);
    mDriveMaxVal = Math.max(mDriveMaxVal, mDriveBRVal);
    if (xbox1.getRawButton(XBOX_BUTTON_RB) && mDriveMaxVal < 1) {
      driveScale = 1.0d / mDriveMaxVal;
    }

    // drive
    mDriveFL.set(mDriveFLVal * driveScale);
    mDriveFR.set(mDriveFRVal * driveScale);
    mDriveBL.set(mDriveBLVal * driveScale);
    mDriveBR.set(mDriveBRVal * driveScale);

    // intake wheels
    double intakeSpeed = 0;
    if (xbox2.getRawButton(XBOX_BUTTON_A))
      intakeSpeed = 1;
    if (xbox2.getRawButton(XBOX_BUTTON_B))
      intakeSpeed = -1;
    mIntake.set(intakeSpeed);

    // intake piston
    DoubleSolenoid.Value pIntakeVal = DoubleSolenoid.Value.kOff;
    if (-xbox2.getRawAxis(XBOX_AXIS_RY) > 0.25)
      pIntakeVal = DoubleSolenoid.Value.kForward;
    if (-xbox2.getRawAxis(XBOX_AXIS_RY) < -0.25)
      pIntakeVal = DoubleSolenoid.Value.kReverse;
    pIntake.set(pIntakeVal);
    if (pIntake.get() != pIntakeVal) {
      pIntake.set(pIntakeVal);
    }

    // intake pin
    if (xbox2.getRawButtonPressed(XBOX_BUTTON_Y)) {
      pIntakePin.set(!pIntakePin.get());
    }

    // arm
    double armLY = xbox2.getRawAxis(XBOX_AXIS_LY) * -1;
    armLY = Math.abs(armLY) > 0.1 ? armLY : 0;
    mArm.set(armLY);

    // grip
    DoubleSolenoid.Value pGripVal = DoubleSolenoid.Value.kOff;
    if (xbox2.getRawButton(XBOX_BUTTON_LB)) {
      pGripVal = DoubleSolenoid.Value.kReverse;
    }
    if (xbox2.getRawButton(XBOX_BUTTON_RB)) {
      pGripVal = DoubleSolenoid.Value.kForward;
    }
    if (pGrip.get() != pGripVal) {
      pGrip.set(pGripVal);
    }
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
    mArm.set(xbox1.getRawAxis(XBOX_AXIS_RY));
  }
}
