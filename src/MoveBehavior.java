import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class MoveBehavior implements Behavior {

	
	private boolean supressed;
	private MovePilot pilot;

	public MoveBehavior(MovePilot pilot)
	{
		this.pilot = pilot;
	}
	
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		supressed = false;
		pilot.forward();
		while(!supressed)
		{
		}
		pilot.stop();;
	}

	@Override
	public void suppress() {
		supressed = true;
	}
}
