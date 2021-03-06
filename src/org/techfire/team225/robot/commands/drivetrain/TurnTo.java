package org.techfire.team225.robot.commands.drivetrain;

import org.techfire.team225.robot.CommandBase;
import org.techfire.team225.robot.SimplePID;

public class TurnTo extends CommandBase {

	public SimplePID pidTheta = new SimplePID(0.04,0.0004,0);
	int loopsStable = 0;
	public TurnTo(double theta)
	{
		this(theta, 1.0);
	}
	
	public TurnTo(double theta, double maxSpeed)
	{
		pidTheta.setTarget(theta);
		pidTheta.setOutputConstraints(maxSpeed, -maxSpeed);
		requires(drivetrain);
	}
	
	@Override
	protected void initialize() {
		
	}

	@Override
	
	protected void execute() {
		drivetrain.setMotorSpeeds(0, 0, -pidTheta.calculate(drivetrain.getGyro()), 1, false);
		System.out.println("TurnTo: T("+pidTheta.getTarget()+") E("+pidTheta.getError()+") C("+CommandBase.drivetrain.getGyro()+")");
	}

	@Override
	protected boolean isFinished() {
		if  ( Math.abs(pidTheta.getError()) < 4 )
			loopsStable++;
		else loopsStable = 0;
		return loopsStable > 3 || isTimedOut();
	}

	@Override
	protected void end() {
		drivetrain.setMotorSpeeds(0, 0, 0, 0, false);

	}
}
