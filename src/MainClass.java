import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class MainClass {

	private static MovePilot pilot;
	private static Arbitrator arby;

	public static void main(String[] args) {
		EV3 brick = LocalEV3.get();
		try(EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(brick.getPort("B")))
		{
			try(EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(brick.getPort("C")))
			{
				setupPilot(leftMotor, rightMotor);
				Behavior [] bArray = {};
				arby = new Arbitrator(bArray);
				arby.go();
			}
		}
	}
	
	private static void setupPilot(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
		Wheel wheel1 = WheeledChassis.modelWheel(leftMotor, 2.7).offset(-7.9);
		Wheel wheel2 = WheeledChassis.modelWheel(rightMotor, 2.7).offset(7.9);

		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		double speed = 5;
		pilot.setLinearAcceleration(speed);
		pilot.setAngularAcceleration(speed);
	}

}
