package org.techfire.team225.robot.commands.gripper;

import org.techfire.team225.robot.CommandBase;

public class AlternateHalfGripper extends CommandBase {

	@Override
	protected void initialize() {
		gripper.setGripper(false, true);
	}

	@Override
	protected void execute() {		
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {		
	}
}
