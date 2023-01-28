package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.motorcontrol.VictorSP;

enum WrappedMotorType {
    TalonFX,
    TalonSRX,
    VictorSPX,
    VictorSP
}

public class WrappedMotor {
    private WrappedMotorType type;
    private double factor;
    private TalonFX motorTalonFX;
    private TalonSRX motorTalonSRX;
    private VictorSPX motorVictorSPX;
    private VictorSP motorVictorSP;

    public WrappedMotor(int id, WrappedMotorType type, double factor) {
        this.type = type;
        this.factor = factor;

        if (type == WrappedMotorType.TalonFX) {
            motorTalonFX = new TalonFX(id);
        }

        if (type == WrappedMotorType.TalonSRX) {
            motorTalonSRX = new TalonSRX(id);
        }

        if (type == WrappedMotorType.VictorSPX) {
            motorVictorSPX = new VictorSPX(id);
        }

        if (type == WrappedMotorType.VictorSP) {
            motorVictorSP = new VictorSP(id);
        }
    }

    public void set(double speed) {
        speed *= factor;

        if (type == WrappedMotorType.TalonFX) {
            motorTalonFX.set(ControlMode.PercentOutput, speed);
        }

        if (type == WrappedMotorType.TalonSRX) {
            motorTalonSRX.set(ControlMode.PercentOutput, speed);
        }

        if (type == WrappedMotorType.VictorSPX) {
            motorVictorSPX.set(ControlMode.PercentOutput, speed);
        }

        if (type == WrappedMotorType.VictorSP) {
            motorVictorSP.set(speed);
        }
    }
}
