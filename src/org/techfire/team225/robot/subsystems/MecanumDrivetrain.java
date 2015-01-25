package org.techfire.team225.robot.subsystems;

import org.techfire.team225.robot.PortMap;
import org.techfire.team225.robot.commands.drivetrain.MecanumDrive;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class MecanumDrivetrain extends Subsystem {
	
	public Gyro gyro = new Gyro(PortMap.GYRO);
	public DigitalInput photoLeft = new DigitalInput(PortMap.PHOTO_SENSOR_LEFT);
    public DigitalInput photoRight = new DigitalInput(PortMap.PHOTO_SENSOR_RIGHT);
    
    public Encoder encoderFL = new Encoder(PortMap.ENCODER_LEFT_FORWARD_A, PortMap.ENCODER_LEFT_FORWARD_B);
    public Encoder encoderFR = new Encoder(PortMap.ENCODER_RIGHT_FORWARD_A, PortMap.ENCODER_RIGHT_FORWARD_B);
    public Encoder encoderBL = new Encoder(PortMap.ENCODER_LEFT_BACK_A, PortMap.ENCODER_LEFT_BACK_B);
    public Encoder encoderBR = new Encoder(PortMap.ENCODER_RIGHT_BACK_A, PortMap.ENCODER_RIGHT_BACK_B);
    
	Victor[] victorLeft = new Victor[2];
	Victor[] victorRight = new Victor[2];
	
	public MecanumDrivetrain() {
		victorLeft[0] = new Victor(PortMap.LEFT_FORWARD_MOTOR);
		victorLeft[1] = new Victor(PortMap.LEFT_BACK_MOTOR);
		victorRight[0] = new Victor(PortMap.RIGHT_FORWARD_MOTOR);
		victorRight[1] = new Victor(PortMap.RIGHT_BACK_MOTOR);
	}
	
	public void setMotorSpeeds(double xIn, double yIn, double rotation, boolean fieldCentric) {
		double x;
		double y;
		
		if (fieldCentric) {
			double gyroAngle = gyro.getAngle();
			gyroAngle = Math.toRadians(gyroAngle);
			x = xIn * Math.cos(gyroAngle) - yIn * Math.sin(gyroAngle);
			y = xIn * Math.sin(gyroAngle) + yIn * Math.cos(gyroAngle);
		} else {
			x = xIn;
			y = yIn;
		}
		
		victorLeft[0].set(x + y + rotation);
        victorRight[0].set(-(-x + y - rotation));
        victorLeft[1].set(-x + y + rotation);
        victorRight[1].set(-(x + y - rotation));
	}
	
	public boolean getRightEye() {
		return photoRight.get();
	}
	
	public boolean getLeftEye() {
		return photoLeft.get();
	}
	
	public Integer[] getEncoders() {
		return new Integer[] {
				encoderFL.get(),
				encoderFR.get(),
				encoderBL.get(),
				encoderBR.get()
		};
	}

	@Override
	protected void initDefaultCommand()  {
		setDefaultCommand(new MecanumDrive());
	}
}
