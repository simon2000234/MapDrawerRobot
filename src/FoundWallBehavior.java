import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import lejos.hardware.sensor.NXTTouchSensor;
import lejos.robotics.geometry.Point;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class FoundWallBehavior implements Behavior{

	private NXTTouchSensor leftSensor;
	private NXTTouchSensor rightSensor;
	private boolean foundWall = false;
	private MovePilot pilot;
	private float[] sampleLeft;
	private float[] sampleRight;
	private Random rnd;
	private boolean shouldTakeOver;
	private DataOutputStream dos;
	private PoseProvider pp;

	
	public FoundWallBehavior (NXTTouchSensor rightSensor, NXTTouchSensor leftSensor, MovePilot pilot, DataOutputStream dos, PoseProvider pp)
	{
		this.rightSensor = rightSensor;
		this.leftSensor = leftSensor;
		this.pilot = pilot;
		rnd = new Random();
		sampleRight = new float[rightSensor.sampleSize()];
		sampleLeft = new float[leftSensor.sampleSize()];
		this.dos = dos;
		this.pp = pp;
	}
	
	@Override
	public boolean takeControl() {
		if(shouldTakeOver == true)
		{
			return true;
		}
		rightSensor.fetchSample(sampleRight, 0);
		leftSensor.fetchSample(sampleLeft, 0);
		findWallToFollow(sampleRight, sampleLeft);
		if(foundWall)
		{
			System.out.println("should take over");
			shouldTakeOver = true;
		}
		return false;
	}

	@Override
	public void action() {
			try 
			{
				Point location = pp.getPose().getLocation();
				dos.writeUTF(location.getX() + "," + location.getY());
			} catch (IOException e) 
			{
				System.out.println(e.getMessage());
			}
		if(sampleRight[0] == 1 && sampleLeft[0] == 1)
		{
			if(rnd.nextInt(2) == 1)
			{
				turnLeft();
			}
			else
			{
				turnRight();
			}
		}
		else if(sampleRight[0] == 1)
		{
			turnRight();
		}
		else if(sampleLeft[0] == 1)
		{
			turnLeft();
		}
		foundWall = false;
		shouldTakeOver = false;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
	
	private void turnLeft()
	{
		pp.getPose().moveUpdate(-5);
		pilot.travel(-5);
		pilot.rotate(40, false);;
	}
	
	private void turnRight()
	{
		pp.getPose().moveUpdate(-5);
		pilot.travel(-5);
		pilot.rotate(-40, false);
	}
	
	private void findWallToFollow(float[] sampleRight, float[] sampleLeft) {
		if (sampleRight[0] == 1 && sampleLeft[0] == 1) {
			System.out.println("found 1");
			foundWall = true;
		}
		if (sampleRight[0] == 1) {
			System.out.println("found 2");
			foundWall = true;
		}
		if (sampleLeft[0] == 1) {
			System.out.println("found 3");
			foundWall = true;
		}
	}

}
