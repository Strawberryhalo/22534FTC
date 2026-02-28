package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="BasicAutoLeftEdge", group="Robot")
public class  BasicAutoLeftEdge extends LinearOpMode {

    //define each wheel/control hub as variables
    private Blinker control_hub;
    private DcMotor RBwheel;
    private DcMotor RFwheel;
    private DcMotor LBwheel;
    private DcMotor LFwheel;

    private DcMotor Flywheel;


    private ElapsedTime runtime = new ElapsedTime();

    // Calculate the COUNTS_PER_INCH for your specific drive train.
    // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
    // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
    // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
    // This is gearing DOWN for less speed and more torque.
    // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
    static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;    // HD Hex Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.5;
    static final double     TURN_SPEED               = 0.5;

    @Override
    public void runOpMode() {

        // assigns the wheel motors & control hub to the actual motors
        //(motor names are determined by what is listed in the driver hub config)
        control_hub = hardwareMap.get(Blinker.class, "Control Hub");
        RFwheel = hardwareMap.get(DcMotor.class, "RFwheel");
        LFwheel = hardwareMap.get(DcMotor.class, "LFwheel");
        RBwheel = hardwareMap.get(DcMotor.class, "RBwheel");
        LBwheel = hardwareMap.get(DcMotor.class, "LBwheel");
        Flywheel = hardwareMap.get(DcMotor.class, "Flywheel");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        LFwheel.setDirection(DcMotor.Direction.REVERSE);
        LBwheel.setDirection(DcMotor.Direction.FORWARD);
        RFwheel.setDirection(DcMotor.Direction.REVERSE);
        RBwheel.setDirection(DcMotor.Direction.REVERSE);

        LFwheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RFwheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LBwheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RBwheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        LFwheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RFwheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LBwheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RBwheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // Send telemetry message to indicate successful Encoder reset
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        encoderDrive(DRIVE_SPEED,  90,  90, 5.0);  // S1: Forward 55 Inches with 5 Sec timeout
        //encoderDrive(TURN_SPEED,   5, -5, 4.0);  // S2: Turn Left 12 Inches with 4 Sec timeout
        //Flywheel.setPower(1);
        //sleep(2000);
        //Flywheel.setPower(0);
        //encoderDrive(TURN_SPEED, 5, -5, 4.0);
        //encoderDrive(DRIVE_SPEED, 20, 20, 4.0);  // S3: Forward 20 Inches with 4 Sec timeout

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);  // pause to display final telemetry message.
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the OpMode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLFTarget;
        int newRFTarget;
        int newRBTarget;
        int newLBTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLFTarget = LFwheel.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRFTarget = RFwheel.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newLBTarget = LBwheel.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRBTarget = RBwheel.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            LFwheel.setTargetPosition(newLFTarget);
            RFwheel.setTargetPosition(newRFTarget);
            LBwheel.setTargetPosition(newLBTarget);
            RBwheel.setTargetPosition(newRBTarget);

            // Turn On RUN_TO_POSITION
            LFwheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RFwheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            LBwheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RBwheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            LFwheel.setPower(Math.abs(speed));
            RFwheel.setPower(Math.abs(speed));
            LBwheel.setPower(Math.abs(speed));
            RBwheel.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (LFwheel.isBusy() && RFwheel.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d :%7d", newLFTarget,  newRFTarget);
                telemetry.addData("Currently at",  " at %7d :%7d",
                        LFwheel.getCurrentPosition(), RFwheel.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            LFwheel.setPower(0);
            RFwheel.setPower(0);
            LBwheel.setPower(0);
            RBwheel.setPower(0);

            // Turn off RUN_TO_POSITION
            LFwheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RFwheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            LBwheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RBwheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }
    }
}

