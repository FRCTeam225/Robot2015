package org.techfire.team225.robot.commands.drivetrain;

import org.techfire.team225.robot.CommandBase;
import org.techfire.team225.robot.SimplePID;

import edu.wpi.first.wpilibj.Timer;


public class ProfiledDriveDistance extends CommandBase
{

    class PathPoint
    {
        double t = 0;
        double pos = 0;
        double vel = 0;
        double acc = 0;
        double theta = 0;
        public String toString()
        {
            return t+","+pos+","+vel+","+acc;
        }
    }

    double target, vcruise, maxAccel;
    double startT = 0;
    double tToCruise, dRamping, dCruising, tCruising;
    double theta;
    public SimplePID pidTheta = new SimplePID(0.05, 0, 0);
    public boolean reverse = false; // fix this eventually
    public ProfiledDriveDistance(double target, double vcruise, double maxAccel, double theta)
    {
    	requires(drivetrain);
        this.target = target;
        this.vcruise = vcruise;
        this.maxAccel = maxAccel;
        this.theta = theta;
        if ( target < 0 )
        {
        	reverse = true;
        	this.target = -target;
        }
    }

    public void initialize()
    {
        tToCruise = vcruise/maxAccel;
        dRamping = (0.5)*maxAccel*Math.pow(tToCruise,2);
        dCruising = target-(dRamping*2);

        if (dCruising < 0) {
            // I am overshooting target by accelerating to vcruise
            tToCruise = Math.sqrt(2 * maxAccel * (target / 2)) / maxAccel;
            if (tToCruise < 0)
                tToCruise = -tToCruise;
            dCruising = 0;
            dRamping = target/2;
            vcruise = maxAccel * tToCruise; // how fast do we get to?
        }
        tCruising = dCruising / vcruise;
        startT = Timer.getFPGATimestamp();
        pidTheta.setTarget(theta);
       System.out.println("ProfiledDriveDistance to "+target+": Starting");
    }

    public void execute()
    {
    	double t = Timer.getFPGATimestamp()-startT;
    	PathPoint p = calcAt(t);
        
        double pow;
		if ( reverse )
			pow = (((-p.pos)-drivetrain.getFeetDistance())*0.9) - ( (p.vel/8.8) + (p.acc*0.01) );
		else
			pow = ((p.pos-drivetrain.getFeetDistance())*0.9) +  (p.vel/8.8) + (p.acc*0.01);
        drivetrain.setMotorSpeeds(0, -pow, -pidTheta.calculate(drivetrain.getGyro()), 1, false);
    }

    public PathPoint calcAt(double t)
    {
        if ( t > ((tToCruise*2)+tCruising) )
        {
        	t = ((tToCruise*2)+tCruising);
        }
        PathPoint point = new PathPoint();


        if ( t <= tToCruise )
        {
            point.t = t;
            point.vel = maxAccel * t;
            point.pos = (0.5) * maxAccel * Math.pow(t, 2);

            point.acc = maxAccel;
        }
        else if ( t < (tToCruise+tCruising) )
        {
            double i = t-tToCruise;
            point.t = t;
            point.pos = dRamping + (vcruise * i);
            point.vel = vcruise;
            point.acc = 0;
        }
        else if ( t <= ((tToCruise*2)+tCruising) )
        {
            double i = t-tToCruise-tCruising;
            point.t = t;
            point.pos = (dCruising + dRamping) + (vcruise * i) + (0.5) * -maxAccel * Math.pow(i, 2);
            point.vel = vcruise + ((-maxAccel) * i);
            point.acc = -maxAccel;
        }
        if ( t == ((tToCruise*2)+tCruising) )
        {
        	point.acc = 0;
        	point.vel = 0;
        }
        point.theta = theta;
        return point;

    }
    
    public String toString()
    {
        return "Path: T"+((tToCruise*2)+tCruising);
    }

	@Override
	protected boolean isFinished() {
		System.out.println("ProfiledDriveDistance to "+target+": err - "+(drivetrain.getFeetDistance()-(reverse?-target:target)));
		return (Math.abs(drivetrain.getFeetDistance()-(reverse?-target:target)) < 0.1) || isTimedOut();
	}

	@Override
	protected void end() {
		drivetrain.setMotorSpeeds(0, 0, 0, 0, false);
		System.out.println("ProfiledDriveDistance to "+target+": End");
		
	}
}