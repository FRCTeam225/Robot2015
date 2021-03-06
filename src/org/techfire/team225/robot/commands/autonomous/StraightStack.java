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

public class StraightStack extends CommandGroup {
	private String name = "Straight Stack";
	
	public StraightStack() {
		double turnAngle = 34;
		double secondTurnAngle = 34;
		double slowTurnSpeed = 0.55;
		
		addSequential(new CloseGripper());
		addSequential(new WaitCommand(0.3));
		addSequential(new SetArm(Arm.firstPosition-100));
		addSequential(new DriveYDistance(530, 0));
		
		addSequential(new TurnTo(turnAngle));
		addSequential(new SetArm(Arm.firstPosition));
		addSequential(new ResetEncoders());
		addSequential(new WaitCommand(0.25));
		addSequential(new DriveYDistance(630, turnAngle, 1.0));
		
		addSequential(new SetArm(Arm.firstPosition-130));
		addSequential(new WaitForArm());
		
		addSequential(new OpenGripper());
		
		addSequential(new SetArm(Arm.floorPosition));
		addSequential(new WaitForArm());
		
		addSequential(new ResetEncoders());
		addSequential(new DriveYDistance(250, turnAngle));
		
		addSequential(new CloseGripper());
		addSequential(new WaitCommand(0.3));
		addSequential(new SetArm(Arm.firstPosition-300));
		addSequential(new WaitCommand(0.2));
		addSequential(new TurnTo(0).chainableSetTimeout(1.0));
		addSequential(new SetArm(Arm.firstPosition-200));
		addSequential(new WaitCommand(0.6));
		addSequential(new DriveYDistance(200, 0).chainableSetTimeout(2.0));
		// second turn angle?
		addSequential(new TurnTo(secondTurnAngle, 0.8));
		
		addSequential(new SetArm(Arm.firstPosition));
		addSequential(new WaitCommand(0.3));
		addSequential(new ResetEncoders());
		addSequential(new DriveYDistance(1050, secondTurnAngle, 1.0).chainableSetTimeout(2.5));		
				
		addSequential(new ResetEncoders());
		
		addSequential(new SetArm(Arm.firstPosition-130));
		addSequential(new WaitForArm());
		
		addSequential(new OpenGripper());
		
		addSequential(new SetArm(Arm.floorPosition));
		addSequential(new WaitForArm());
		
		addSequential(new ResetEncoders());
		addSequential(new DriveYDistance(250, turnAngle));
		
		addSequential(new CloseGripper());
		addSequential(new WaitCommand(0.3));
		
		addSequential(new SetArm(Arm.floorPosition+70));
		
		addSequential(new TurnTo(turnAngle+90, slowTurnSpeed).chainableSetTimeout(1.0));
		addSequential(new ResetEncoders());
		addSequential(new DriveYDistance(1400, turnAngle+90));
		addSequential(new SetArm(Arm.floorPosition));
		addSequential(new WaitForArm());
		addSequential(new OpenGripper());
	}
	
	public String toString() {
		return name;
	}
}
