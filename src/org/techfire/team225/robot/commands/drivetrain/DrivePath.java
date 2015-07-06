package org.techfire.team225.robot.commands.drivetrain;

import org.techfire.team225.robot.CommandBase;

import edu.wpi.first.wpilibj.Timer;

public class DrivePath extends CommandBase
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
    	return "Point: "+pos+","+vel;
    }
  }

  double target, vcruise, maxAccel;
  double startT = 0;
  double tToCruise, dRamping, dCruising, tCruising;
  double theta;
  PathPoint finalPos;
  public DrivePath(double target, double vcruise, double maxAccel, double theta)
  {
    requires(drivetrain);
    this.target = target;
    this.vcruise = vcruise;
    this.maxAccel = maxAccel;
    this.theta = theta;

  }

  public void initialize()
  {
    
    tToCruise = vcruise/maxAccel;
    dRamping = (0.5)*maxAccel*Math.pow(tToCruise,2);
    dCruising = target-(dRamping*2);

    if (dCruising < 0) {
        // I am overshooting target by accelerating to vcruise
        tToCruise = Math.sqrt(2 * maxAccel * (target / 2) - 1) / maxAccel;
        if (tToCruise < 0)
            tToCruise = -tToCruise;
        dCruising = 0;
        dRamping = target/2;
        vcruise = maxAccel * tToCruise; // how fast do we get to?
    }
    tCruising = dCruising / vcruise;
    System.out.println("tCruising "+tCruising);
    System.out.println("tRamping "+tToCruise);
    System.out.println("dCruising"+dCruising);
    System.out.println("dRamping"+dRamping);
    startT = Timer.getFPGATimestamp();
    finalPos = calcAt(((tToCruise*2)+tCruising));
  }

  public void execute()
  {
	double t = Timer.getFPGATimestamp()-startT;
	if ( t > ((tToCruise*2)+tCruising) )
		t = ((tToCruise*2)+tCruising);
	
    PathPoint p = calcAt(t);
    System.out.println(p);
    double pow = (((p.pos-0.3)-drivetrain.getFeetDistance())*0.5) + (p.vel/9.8) + (p.acc*0.01);
    drivetrain.setMotorSpeeds(0, -pow, 0, 1, false);
  }

  public PathPoint calcAt(double t)
  {
	  System.out.println("T: "+t);
    PathPoint point = new PathPoint();
    if ( t <= tToCruise )
    {
      point.t = t;
      point.vel = maxAccel * t;
      point.pos = (0.5) * maxAccel * Math.pow(t, 2);
      point.acc = maxAccel;
    }
    else if ( t <= (tToCruise+tCruising) )
    {
      double i = t - tToCruise;
      point.t = t;
      point.pos = dRamping + (vcruise * i);
      point.vel = vcruise;
      point.acc = 0;
    }
    else if ( t <= ((tToCruise*2)+tCruising) )
    {
      double i = t - tToCruise - tCruising;
      point.t = t;
      point.pos = (dCruising + dRamping) + (vcruise * i) + (0.5) * -maxAccel * Math.pow(i, 2);
      point.vel = vcruise + ((-maxAccel) * i);
      point.acc = -maxAccel;
    }

    point.theta = theta;
    return point;
  }

@Override
protected boolean isFinished() {
	return false;
	//return Math.abs((finalPos.pos-0.25)-drivetrain.getFeetDistance()) < 0.01;
	
}

@Override
protected void end() {
	// TODO Auto-generated method stub
	System.out.println("I'm dead!");
	drivetrain.setMotorSpeeds(0, 0, 0, 0, false);
}

public String toString()
{
	return "Path: T"+((tToCruise*2)+tCruising);
}

}