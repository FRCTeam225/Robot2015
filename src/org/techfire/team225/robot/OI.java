package org.techfire.team225.robot;

import org.techfire.team225.robot.commands.arm.LegalTilt;
import org.techfire.team225.robot.commands.arm.OverridePot;
import org.techfire.team225.robot.commands.arm.PickupContainer;
import org.techfire.team225.robot.commands.arm.SetArmTilt;
import org.techfire.team225.robot.commands.arm.SetPostContainer;
import org.techfire.team225.robot.commands.arm.SetPreContainer;
import org.techfire.team225.robot.commands.automation.AutoLift;
import org.techfire.team225.robot.commands.automation.AutoPullOut;
import org.techfire.team225.robot.commands.automation.AutoLiftHigh;
import org.techfire.team225.robot.commands.chokehold.SetChokehold;
import org.techfire.team225.robot.commands.drivetrain.CenterGyro;
import org.techfire.team225.robot.commands.gripper.CloseGripper;
import org.techfire.team225.robot.commands.gripper.HalfGripper;
import org.techfire.team225.robot.commands.gripper.OpenGripper;
import org.techfire.team225.robot.commands.gripper.AlternateHalfGripper;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    
	public static Joystick driver = new Joystick(0);
	public static Joystick operator = new Joystick(1);
	public static double neutralChokeholdSpeed = 0;
	
	public static void init() {
		// driver
		new JoystickButton(driver, 8).whenPressed(new CenterGyro());
		new JoystickButton(driver, 7).whenPressed(new OverridePot());
		
		// operator
		new JoystickButton(operator, 6).whenPressed(new SetPreContainer());
		new JoystickButton(operator, 5).whenPressed(new SetPostContainer());
		new JoystickButton(operator, 1).whenPressed(new OpenGripper());
		new JoystickButton(operator, 4).whenPressed(new CloseGripper());
		new JoystickButton(operator, 3).whenPressed(new HalfGripper());
		new JoystickButton(operator, 2).whenPressed(new AlternateHalfGripper());
		//new JoystickButton(operator, 9).whenPressed(new LegalTilt(false));
		new JoystickButton(operator, 10).whenPressed(new PickupContainer());
		//new JoystickButton(operator, 10).whenPressed(new LegalTilt(true));
	}
	
	public static boolean getDriverPreciseMode() {
		return driver.getRawButton(8);
	}
	
	public static double getDriverStrafeThrottle() {
		return driver.getRawAxis(0);
	}
	
	public static double getDriverForwardThrottle() {
		return driver.getRawAxis(1);
	}
	
	public static double getDriverRotation() {
		return driver.getRawAxis(4);
	}
	
	public static double getDriverDPadRightLeft() {
		return 0;
	}
	
	public static double getArmThrottle() {
		return operator.getRawAxis(1);
	}
	
	public static double getChokeholdThrottle() {
		if (driver.getRawButton(6)) {
			return 1.0;
		} else if (driver.getRawButton(5)) {
			return -0.5;
		} else {
			return neutralChokeholdSpeed;
		}
	}
	
	public static double getNeutralChokeholdSpeed() {
		return neutralChokeholdSpeed;
	}
	
	public static void setNeutralChokeholdSpeed(double newNeutralChokeholdSpeed) {
		neutralChokeholdSpeed = newNeutralChokeholdSpeed;
	}
}

