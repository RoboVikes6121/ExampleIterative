package com.github.adambots.steamworks2017.intake;

import org.usfirst.frc.team245.robot.Actuators;
import org.usfirst.frc.team245.robot.Constants;


public class Intake {
	
	//variables for this class
	public static boolean intakeDisabled = true;		//checks if intake has been enabled yet
	private static boolean intakeButtonReleased = true;		//checks to see if the button for enabling intake has been released
	private static boolean intakeSafetyButtonReleased = true;	//checks to see if the safety has been released to prevent double counts	
	public static boolean intakeSafety = true; 		//prevents running intake out of robot
	private static int counter = Constants.COUNTER_START;//to check if the button has been pressed enough times to release safety
	private static boolean intakeInButtonReleased = true;
	public static double intakeMotorSpeed = 0;
	private static boolean intakeJamButtonReleased = true;

	private static double oldMotorSpeed = Constants.MOTOR_STOP;
	private static double newMotorSpeed = Constants.MOTOR_STOP;
	
	public static void intakeSafety(boolean intakeSafetyButton){
		if(intakeSafetyButton && intakeSafetyButtonReleased){
			counter++;
			intakeSafetyButtonReleased = false;
		}else if(!intakeSafetyButton){
			intakeSafetyButtonReleased = true;
		}
		if(counter == Constants.COUNTER_END){
			counter = Constants.COUNTER_START;
			intakeSafety = !intakeSafety;
		}
	}
	
	/*
	 * Runs intakeMotor
	 * @Param intakeButon
	 * */
	public static void intake(boolean intakeButton){
		if(!intakeButton){
			//only runs if button is released
			intakeButtonReleased = true;
		}
		if (Actuators.getFuelIntakeMotor().get() == Constants.MOTOR_STOP && intakeButton && intakeButtonReleased){
			Actuators.getFuelIntakeMotor().set(Constants.MOTOR_START_VALUE);
			intakeButtonReleased = false;
			intakeDisabled = false;
		} else if(intakeButton && intakeButtonReleased){
			Actuators.getFuelIntakeMotor().set(Constants.MOTOR_STOP);
			intakeButtonReleased = false;
			intakeDisabled = true;
		}
	}
	
	/*
	 * Changes Speed of Intake Motor
	 * @Param speed
	 */
	//TODO: Check Direction of motor
	public static void intakeSpeed(double speed){
		//increases motor speed
		if(!intakeDisabled){
			if(speed <= Constants.STICK_PRESSED_UP && Math.abs(Actuators.getFuelIntakeMotor().get()) < Constants.MAX_MOTOR_SPEED){
				//Increments motor speed by a set value while stick is more than 50% pressed
				intakeMotorSpeed = Actuators.getFuelIntakeMotor().get() + Constants.MOTOR_INCREMENT;
				Actuators.getFuelIntakeMotor().set(intakeMotorSpeed);
				oldMotorSpeed = Actuators.getFuelIntakeMotor().get();
			}//decreases motor speed
			else if(speed >= Constants.STICK_PRESSED_DOWN && Constants.MOTOR_STOP < Actuators.getFuelIntakeMotor().get()){
				//Increments motor speed by a set value while stick is more than 50% pressed
				intakeMotorSpeed = Actuators.getFuelIntakeMotor().get() - Constants.MOTOR_INCREMENT;
				Actuators.getFuelIntakeMotor().set(intakeMotorSpeed);
				oldMotorSpeed = Actuators.getFuelIntakeMotor().get();
			}
		}
	}
	
	/*
	 * Changes direction of Intake motor
	 * @Param direction
	 */
	//TODO: Check direction of motor, switch the true and false if needed
	public static void intakeDirection(double direction){
		if(!intakeDisabled){
			//when left is held, keeps motor at constant speed until released
			//needs to ramp the motor value down slowly

			if(direction <= Constants.STICK_PRESSED_LEFT){
				if(Actuators.getFuelIntakeMotor().get() > Constants.MOTOR_REVERSE){
					newMotorSpeed = Actuators.getFuelIntakeMotor().get() - Constants.MOTOR_ACCEL;
					Actuators.getFuelIntakeMotor().set(newMotorSpeed);
				}
				//TODO: Debug this - it does not set motor back to correct value
			}else if(direction > Constants.STICK_PRESSED_LEFT){	//runs this if the left stick is no longer held
				if(Actuators.getFuelIntakeMotor().get() < oldMotorSpeed){
					newMotorSpeed = Actuators.getFuelIntakeMotor().get() + Constants.MOTOR_ACCEL;
					Actuators.getFuelIntakeMotor().set(newMotorSpeed);
				}
			}
		}
	}
	
	public static void intakeJam(boolean intakeJamButton){
		if(intakeJamButton){
			Actuators.getFuelIntakeMotor().set(Constants.MIN_MOTOR_SPEED);
			Actuators.getFuelConveyorMotor().set(Constants.MIN_MOTOR_SPEED);
			intakeJamButtonReleased = false;
		}else if(!intakeJamButtonReleased){
			Actuators.getFuelIntakeMotor().set(Constants.MOTOR_STOP);
			Actuators.getFuelConveyorMotor().set(Constants.MOTOR_STOP);
			intakeJamButtonReleased = true;
		}
	}
	
	public static void intakeIn(boolean intakeButton){
		if(!intakeButton){
			intakeInButtonReleased = true;
		}
		if(intakeButton && intakeInButtonReleased && Actuators.getFuelIntakeMotor().get() > Constants.MOTOR_STOP){
			Actuators.getFuelIntakeMotor().set(Constants.MAX_MOTOR_SPEED);
		}else if(intakeButton && intakeInButtonReleased){
			Actuators.getFuelIntakeMotor().set(Constants.MOTOR_STOP);
		}
	}
}