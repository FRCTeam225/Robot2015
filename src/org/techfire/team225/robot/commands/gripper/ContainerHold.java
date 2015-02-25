package org.techfire.team225.robot.commands.gripper;

import org.techfire.team225.robot.CommandBase;

public class ContainerHold extends CommandBase {

	boolean set;
	
	public ContainerHold(boolean set) {
		this.set = set;
	}
	
	@Override
	protected void initialize() {
		gripper.setContainerHold(set);
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