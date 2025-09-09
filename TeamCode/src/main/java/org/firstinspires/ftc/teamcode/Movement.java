package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class Movement extends LinearOpMode {
    //define each wheel/control hub as variables
    //edit test
    private Blinker control_hub;
    private DcMotor RBwheel;
    private DcMotor RFwheel;
    private DcMotor LBwheel;
    private DcMotor LFwheel;
    @Override
    public void runOpMode() {
        // assigns the wheel motors & control hub to the actual motors
        //(motor names are determined by what is listed in the driver hub config)
        control_hub = hardwareMap.get(Blinker.class, "Control Hub");
        RBwheel = hardwareMap.get(DcMotor.class, "RBwheel");
        RFwheel = hardwareMap.get(DcMotor.class, "RFwheel");
        LBwheel = hardwareMap.get(DcMotor.class, "LBwheel");
        LFwheel = hardwareMap.get(DcMotor.class, "LFwheel");


        // reverse spin direction of the right wheels (commented out for now, unsure if it's needed)
        //RBwheel.setDirection(DcMotorSimple.Direction.REVERSE);
        //RFwheel.setDirection(DcMotorSimple.Direction.REVERSE);

        //x & y axis as variables
        double x;
        double y;

        //prints text to the driver hub console
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        //only runs once play button is pressed
        while (opModeIsActive()) {
            //defines x & y axis
            //(if you flip this things will break.)
            y = gamepad1.left_stick_x;
            x = gamepad1.left_stick_y;

            //sets the power of each motor according to the joystick movement
            RBwheel.setPower(y-x);
            RFwheel.setPower(y-x);
            LBwheel.setPower(y+x);
            LFwheel.setPower(y+x);

            //prints text to drivers hub console
            telemetry.addData("Status", "Running");
            telemetry.update();
        }

    }
}
