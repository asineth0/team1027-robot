package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

enum WrappedMotorType {
    TALON_SRX,
    TALON_FX,
    VICTOR_SPX,
}

public class WrappedMotor {
    private WrappedMotorType type;
    private int id;
    private double factor;
    private double power = 0;
    private TalonFX talon_fx;
    private TalonSRX talon_srx;
    private VictorSPX victor_spx;

    public WrappedMotor(WrappedMotorType type, int id, int factor) {
        this.type = type;
        this.id = id;
        this.factor = factor;
        
        if (this.type == WrappedMotorType.TALON_FX) {
            this.talon_fx = new TalonFX(this.id);
        }

        if (this.type == WrappedMotorType.TALON_SRX) {
            this.talon_srx = new TalonSRX(this.id);
        }

        if (this.type == WrappedMotorType.VICTOR_SPX) {
            this.victor_spx = new VictorSPX(this.id);
        }
    }

    public void set(double power) {
        this.power = power;
        
        if (this.type == WrappedMotorType.TALON_FX) {
            this.talon_fx.set(ControlMode.PercentOutput, this.power * this.factor);
        }

        if (this.type == WrappedMotorType.TALON_SRX) {
            this.talon_srx.set(ControlMode.PercentOutput, this.power * this.factor);
        }

        if (this.type == WrappedMotorType.VICTOR_SPX) {
            this.victor_spx.set(ControlMode.PercentOutput, this.power * this.factor);
        }
    }

    public double get() {
        return this.power;
    }
}