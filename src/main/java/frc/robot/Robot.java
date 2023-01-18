package frc.robot;

import com.ctre.phoenixpro.hardware.TalonFX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// this is a comment

/*
 * this is a multiline ocmmnet
 */

public class Robot extends TimedRobot {
  public static final int CTL_XBOX_AXIS_LY = 1; // THIS_CASE for constants (variables that will never change)
  public static final int CTL_XBOX_AXIS_RY = 5;
  public Joystick ctlXbox; // thisCase for variables that will change
  public TalonFX motorDriveBL;
  public TalonFX motorDriveBR;
  public TalonFX motorDriveFL;
  public TalonFX motorDriveFR;

  @Override
  public void robotInit() {
    //joystick-motor
    ctlXbox = new Joystick(0);
    motorDriveFL = new TalonFX(0);
    motorDriveBL = new TalonFX(2);
    motorDriveFR = new TalonFX(1);
    motorDriveBR = new TalonFX(3);
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
    // get ctrl from xbox
    double LY = ctlXbox.getRawAxis(CTL_XBOX_AXIS_LY) * -1;
    double RY = ctlXbox.getRawAxis(CTL_XBOX_AXIS_RY) * -1;
    motorDriveFL.set(LY);
    motorDriveBL.set(LY);
    motorDriveFR.set(RY);
    motorDriveBR.set(RY);
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
