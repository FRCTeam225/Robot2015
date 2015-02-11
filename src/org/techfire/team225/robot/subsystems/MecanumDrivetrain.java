
package org.techfire.team225.robot.subsystems;

import org.techfire.team225.robot.PortMap;
import org.techfire.team225.robot.SimplePID;
import org.techfire.team225.robot.commands.drivetrain.MecanumDrive;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class MecanumDrivetrain extends Subsystem {
	
	public SimplePID pidX = new SimplePID(0.05, 0.01, 0);
	public SimplePID pidY = new SimplePID(0.05, 0.01, 0);
	public SimplePID pidTheta = new SimplePID(0.1, 0, 0);

	public DigitalInput photoLeft = new DigitalInput(PortMap.get("PHOTO_SENSOR_LEFT"));
    public DigitalInput photoRight = new DigitalInput(PortMap.get("PHOTO_SENSOR_RIGHT"));
    
    public Encoder encoderL = new Encoder(PortMap.get("ENCODER_LEFT_A"), PortMap.get("ENCODER_LEFT_B"));
    public Encoder encoderR = new Encoder(PortMap.get("ENCODER_RIGHT_A"), PortMap.get("ENCODER_RIGHT_B"));
    public Encoder encoderF = new Encoder(PortMap.get("ENCODER_FOLLOW_A"), PortMap.get("ENCODER_FOLLOW_B"));
    
    public double driveScale = 0.5;
    
    //GyroProvider gyro;
    Gyro gyro;
    
	Victor[] victorLeft = new Victor[2];
	Victor[] victorRight = new Victor[2];
	
	boolean pidEnabled = false;
	
	public MecanumDrivetrain() {
		victorLeft[0] = new Victor(PortMap.get("LEFT_FORWARD_MOTOR"));
		victorLeft[1] = new Victor(PortMap.get("LEFT_BACK_MOTOR"));
		victorRight[0] = new Victor(PortMap.get("RIGHT_FORWARD_MOTOR"));
		victorRight[1] = new Victor(PortMap.get("RIGHT_BACK_MOTOR"));
		
		//gyro = new GyroProvider();
		gyro = new Gyro(PortMap.get("GYRO"));
	}
	
	public void setMotorSpeeds(double xIn, double yIn, double rotation, boolean fieldCentric) {
		double x;
		double y;
		
		double gyroAngle = getGyro();
		
		if (fieldCentric) {
			gyroAngle = Math.toRadians(gyroAngle);
			x = xIn * Math.cos(gyroAngle) - yIn * Math.sin(gyroAngle);
			y = xIn * Math.sin(gyroAngle) + yIn * Math.cos(gyroAngle);
		} else {
			x = xIn;
			y = yIn;
		}
		
		victorLeft[0].set((x + y + rotation) * driveScale);
        victorRight[0].set(-(-x + y - rotation) * driveScale);
        victorLeft[1].set((-x + y + rotation) * driveScale);
        victorRight[1].set(-(x + y - rotation) * driveScale);
	}
	
	public void setPIDEnabled(boolean state)
	{
		this.pidEnabled = state;
	}
	
	public void update()
	{
		
		double turnSpeed = -pidTheta.calculate(getGyro());
		if ( turnSpeed > 0.1 )
		setMotorSpeeds(-pidX.calculate(getFollowEncoder()), -pidY.calculate(getAverageForwardEncoders()), turnSpeed, false);
	}
	
	public boolean getRightEye() {
		return photoRight.get();
	}
	
	public boolean getLeftEye() {
		return photoLeft.get();
	}
	
	public int getLeftEncoder() {
		return encoderL.get();
	}
	
	public int getRightEncoder() {
		return -encoderR.get();
	}
	
	public int getAverageForwardEncoders() {
		return (encoderL.get() - encoderR.get())/2;
	}
	
	public void resetForwardEncoders() {
		encoderL.reset();
		encoderR.reset();
	}
	
	public void resetFollowerEncoder() {
		encoderF.reset();
	}
	
	public int getFollowEncoder() {
		return encoderF.get();
	}
	
	public double getGyro() {
		return gyro.getAngle();
	}
	
	public void resetAngle() {
		gyro.reset();
	}

	@Override
	protected void initDefaultCommand()  {
		setDefaultCommand(new MecanumDrive());
	}
}
