import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class MainClassClass {

	private static MovePilot pilot;
	private static Arbitrator arby;
	private static PoseProvider pp;

	public static void main(String[] args) {
		EV3 brick = LocalEV3.get();
		try
		(
				Socket socket = new Socket("192.168.0.115", 5000);
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());	
		)
		{
			try(EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(brick.getPort("B")))
			{
				try(EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(brick.getPort("C")))
				{
					try (NXTTouchSensor rightSensor = new NXTTouchSensor(brick.getPort("S4"))) {
						
						try (NXTTouchSensor leftSensor = new NXTTouchSensor(brick.getPort("S1"))) {
							
							setupPilot(leftMotor, rightMotor);
							pp = new OdometryPoseProvider(pilot);
							Behavior mb = new MoveBehavior(pilot);
							Behavior fwb = new FoundWallBehavior(rightSensor, leftSensor, pilot, dos, pp);
							Behavior cb = new CloseBehavior(dos);
							Behavior [] bArray = {mb, fwb, cb};
							arby = new Arbitrator(bArray);
							arby.go();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void setupPilot(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
		Wheel wheel1 = WheeledChassis.modelWheel(leftMotor, 2.8).offset(-4.1);
		Wheel wheel2 = WheeledChassis.modelWheel(rightMotor, 2.8).offset(4.1);

		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		double speedCmPrSec = 13;
		pilot.setLinearSpeed(speedCmPrSec);
		pilot.setAngularSpeed(speedCmPrSec);
	}

}
