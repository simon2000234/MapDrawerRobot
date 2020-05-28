import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;

public class CloseBehavior implements Behavior{
	private boolean supressed;
	private boolean shouldTakeOver;
	private DataOutputStream dos;


	public CloseBehavior(DataOutputStream dos)
	{
		shouldTakeOver = false;
		this.dos = dos;
	}
	@Override
	public boolean takeControl() {
		if (Button.ESCAPE.isDown())
		{
			shouldTakeOver = true;
		}
		if(shouldTakeOver)
		{
			supressed = false;
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void action() {
		if(!supressed)
		{
			try {
				dos.writeUTF("quit");
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
		
	}

	@Override
	public void suppress() {
		supressed = true;
		shouldTakeOver = false;
		
	}

}
