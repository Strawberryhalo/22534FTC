package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class Basicmovement extends LinearOpMode {
    //define each wheel/control hub as variables
    private Blinker control_hub;
    private DcMotor RBwheel;
    private DcMotor RFwheel;
     private DcMotor LBwheel;
    private DcMotor LFwheel;
    private DcMotor Flywheel;
    private DcMotor Intake;

    @Override
    public void runOpMode() {
        // assigns the wheel motors & control hub to the actual motors
        //(motor names are determined by what is listed in the driver hub config)
        control_hub = hardwareMap.get(Blinker.class, "Control Hub");

        RBwheel = hardwareMap.get(DcMotor.class, "RBwheel");
        RFwheel = hardwareMap.get(DcMotor.class, "RFwheel");
        LBwheel = hardwareMap.get(DcMotor.class, "LBwheel");
        LFwheel = hardwareMap.get(DcMotor.class, "LFwheel");
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        Flywheel = hardwareMap.get(DcMotor.class, "Flywheel");


        // reverse spin direction of the right wheels (commented out for now, unsure if it's needed)
        RBwheel.setDirection(DcMotor.Direction.REVERSE);
        RFwheel.setDirection(DcMotor.Direction.REVERSE);
        LFwheel.setDirection(DcMotor.Direction.REVERSE);
        //x & y axis as variables
        double x;
        double y;
        double z;



        //prints text to the driver hub console
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        //only runs once play button is pressed
        while (opModeIsActive()) {
            //defines x & y axis
            z = gamepad1.left_stick_y;
            x = -gamepad1.left_stick_x;
            y = gamepad1.right_stick_x;

            if (gamepad1.a) {
                Flywheel.setPower(0.86);
                Intake.setPower(1);
            }

            if (gamepad1.b) {
                Intake.setPower(1);
                Flywheel.setPower(-0.25);
            } else {
                Intake.setPower(0);
            }

            if (gamepad1.y) {
                Flywheel.setPower(0.9);
            }

            if (!gamepad1.a && !gamepad1.y) {
                Flywheel.setPower(0);
            }

            //sets the power of each motor according to the joystick movement
            RBwheel.setPower(y+x);
            RFwheel.setPower(y-x);
            LBwheel.setPower(y+x);
            LFwheel.setPower(y-x);
            RBwheel.setPower(z);
            RFwheel.setPower(z);
            LBwheel.setPower(-z);
            LFwheel.setPower(-z);



            //prints text to drivers hub console
            telemetry.addData("Status", "Running");
            telemetry.update();
        }

    }
}
