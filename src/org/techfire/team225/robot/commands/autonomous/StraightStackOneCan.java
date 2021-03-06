package org.techfire.team225.robot.commands.autonomous;

import org.techfire.team225.robot.commands.arm.SetArm;
import org.techfire.team225.robot.commands.arm.WaitForArm;
import org.techfire.team225.robot.commands.drivetrain.DriveYDistance;
import org.techfire.team225.robot.commands.drivetrain.ResetEncoders;
import org.techfire.team225.robot.commands.drivetrain.TurnTo;
import org.techfire.team225.robot.commands.gripper.CloseGripper;
import org.techfire.team225.robot.commands.gripper.OpenGripper;
import org.techfire.team225.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class StraightStackOneCan extends CommandGroup {
	private String name = "Straight Stack, with one can being removed";
	
	public StraightStackOneCan() {
		double turnAngle = 32;
		double slowTurnSpeed = 0.55;
		
		addSequential(new CloseGripper());
		addSequential(new WaitCommand(0.3));
		addSequential(new SetArm(Arm.firstPosition-100));
		addSequential(new DriveYDistance(470, 0));
		
		addSequential(new TurnTo(turnAngle));
		addSequential(new SetArm(Arm.firstPosition));
		addSequential(new ResetEncoders());
		addSequential(new WaitCommand(0.25));
		addSequential(new DriveYDistance(680, turnAngle, 1.0));
		
		addSequential(new SetArm(Arm.firstPosition-100));
		addSequential(new WaitCommand(0.2));
		
		addSequential(new OpenGripper());
		
		addSequential(new SetArm(Arm.floorPosition));
		addSequential(new WaitForArm());
		
		addSequential(new ResetEncoders());
		addSequential(new DriveYDistance(250, turnAngle));
		
		addSequential(new CloseGripper());
		addSequential(new WaitCommand(0.3));
		
		addSequential(new SetArm(Arm.firstPosition+50));
		addSequential(new WaitCommand(0.5));
		addSequential(new ResetEncoders());
		addSequential(new DriveYDistance(1150, turnAngle, 1.0));		
				
		addSequential(new ResetEncoders());
		
		addSequential(new SetArm(Arm.firstPosition-100));
		addSequential(new WaitCommand(0.2));
		
		addSequential(new OpenGripper());
		
		addSequential(new SetArm(Arm.floorPosition));
		addSequential(new WaitForArm());
		
		addSequential(new ResetEncoders());
		addSequential(new DriveYDistance(250, turnAngle));
		
		addSequential(new CloseGripper());
		addSequential(new WaitCommand(0.3));
		
		addSequential(new SetArm(Arm.floorPosition+150));
		addSequential(new WaitForArm());
		
		addSequential(new TurnTo(turnAngle+90, slowTurnSpeed));
		addSequential(new ResetEncoders());
		addSequential(new DriveYDistance(1200, turnAngle+90));
		addSequential(new SetArm(Arm.floorPosition));
		addSequential(new WaitForArm());
		addSequential(new OpenGripper());
	}
	
	public String toString() {
		return name;
	}
}
