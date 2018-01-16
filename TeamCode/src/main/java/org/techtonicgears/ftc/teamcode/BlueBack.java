package org.techtonicgears.ftc.teamcode;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;


@Autonomous(name = "Blueback")
public class BlueBack extends LinearOpMode {
    //Declaring glyph arm
    GlyphArm glyphArm = new GlyphArm();
    //Declaring Drive train
    DriveTrain driveTrain = new DriveTrain();
    //Declaring Jewel Arm
    JewelArm jewelArm = new JewelArm();
    //Declaring Timer
    ElapsedTime timer = new ElapsedTime();
    /* Declaring the doubles that store the value that the robot has to move
    to reach both of the two columns */
    double left = 0.5;
    double middle = 1;
    double right = 0;
    String teamColor = "blue";
    String foundColor = "";

    ModernRoboticsI2cGyro gyro;
    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        //Initializing all RobotParts and giving them a hardwareMap input
        glyphArm.init(hardwareMap);
        driveTrain.init(hardwareMap);
        jewelArm.init(hardwareMap);

        colorSensor = hardwareMap.get(ColorSensor.class, "color");
        gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");


        //Resetting the timer
        jewelArm.setJewelArm(0);


        gyro.calibrate();
        while (!isStopRequested() && gyro.isCalibrating()) {
            telemetry.addData("Calibrating", "");
            telemetry.update();
            sleep(50);
        }


        //Waiting for the start button to be pressed
        waitForStart();

        timer.reset();
        glyphArm.clawClose();

        while (opModeIsActive() && timer.seconds() < 1) {
            glyphArm.moveUpOrDown(-0.4);
        }
        timer.reset();

        colorSensor.enableLed(true);

        while (opModeIsActive() && timer.seconds() < 2) {
            glyphArm.moveUpOrDown(-0.15);
            jewelArm.setJewelArm(1);
            //returns which color the jewel is
            if (colorSensor.red() > colorSensor.blue()) {
                foundColor = "red";
            } else if (colorSensor.blue() > colorSensor.red()) {
                foundColor = "blue";
            }
        }
        timer.reset();
        if (teamColor == foundColor) {
            telemetry.addData("Blue", "");
            telemetry.update();
            while (opModeIsActive() && timer.seconds() < 0.3) {
                jewelArm.setJewelArm(1);
                //moves and pushes the other jewel
                driveTrain.move(0, 0.5, 0);
            }
            jewelArm.setJewelArm(0);
            // if the color of the jewel is not the same as the color of the team

            timer.reset();
            while (opModeIsActive() && timer.seconds() < 0.2){
                driveTrain.move(0, -0.25,0);
            }

            timer.reset();
            while (opModeIsActive() && gyro.getHeading() < 359) {
                driveTrain.move(0, -0.25, 0);
                jewelArm.setJewelArm(0);
            }


        } else if (foundColor != teamColor) {
            telemetry.addData("Red", "");
            telemetry.update();
            while (opModeIsActive() && timer.seconds() < 0.3) {
                jewelArm.setJewelArm(1);
                //moves and pushes off the same jewel it detects because colors dont match
                driveTrain.move(0, -0.5, 0);
            }

            timer.reset();
            while (opModeIsActive() && timer.seconds() < 0.2){
                driveTrain.move(0, 0.25,0);
            }

            jewelArm.setJewelArm(0);
            while (opModeIsActive() && gyro.getHeading() > 1) {
                driveTrain.move(0, 0.25, 0);
            }
        }

        gyro.resetZAxisIntegrator();

        telemetry.addData("gyro", gyro.getHeading());
        telemetry.update();

        timer.reset();
        while (opModeIsActive() && timer.seconds() < 0.2){
            driveTrain.move(0, -0.25,0);
        }

        while (opModeIsActive() && gyro.getHeading() < 87){
            driveTrain.move(0, -0.25,0);
        }

        pause(1);

        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.6){
            driveTrain.move(0.5, 0,0);
        }

        pause(0.1);

        gyro.resetZAxisIntegrator();

        timer.reset();
        while (opModeIsActive() && timer.seconds() < 0.5){
            driveTrain.move(0, 0,0.5);
        }

        pause(1);

        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1){
            driveTrain.move(0, -0.25,0);
        }

        while (opModeIsActive() && gyro.getHeading() < 90){
            driveTrain.move(0, -0.25,0);
        }

        pause(1);

        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1){
            driveTrain.move(0.25, 0,0);
        }



    }
    public void  pause(double seconds){
        timer.reset();
        while (opModeIsActive() && timer.seconds() < seconds){
            driveTrain.move(0,0,0);
            telemetry.addData("Wait",seconds);
            telemetry.update();
        }
    }
}