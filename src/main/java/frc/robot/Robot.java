package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

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
    ctlXbox = new Joystick(0);
    motorDriveBL = new TalonFX(2);
    motorDriveBR = new TalonFX(3);
    motorDriveFL = new TalonFX(0);
    motorDriveFR = new TalonFX(1);
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
    double ly = -ctlXbox.getRawAxis(CTL_XBOX_AXIS_LY);
    double ry = -ctlXbox.getRawAxis(CTL_XBOX_AXIS_RY);

    // tank drive
    motorDriveBL.set(ControlMode.PercentOutput, ly);
    motorDriveFL.set(ControlMode.PercentOutput, ly);
    motorDriveBR.set(ControlMode.PercentOutput, ry);
    motorDriveFR.set(ControlMode.PercentOutput, ry);
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
