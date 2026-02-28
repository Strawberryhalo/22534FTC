
package org.firstinspires.ftc.teamcode;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class BasicAutoLeftGoal extends OpMode {


    private Blinker control_hub;
    private DcMotor RBwheel;
    private DcMotor RFwheel;
    private DcMotor LBwheel;
    private DcMotor LFwheel;
    private DcMotor Flywheel;
    private DcMotor Intake;

    private Follower follower;
    private Timer pathTimer, opModeTimer;

    public enum PathState {
        DriveStart_Shoot,
        ShootPreload,
        DriveToArtifactPickup1,



    }

    PathState pathstate;

    // poses -------------------------------------------
    private final Pose startPose = new Pose
            (21.126853377265252, 123.76935749588135, Math.toRadians(139));
    private final Pose shootPose = new Pose
            (72, 72, Math.toRadians(139));
    private final Pose artifact1Pose = new Pose
            (43.858319604612845, 36.53377265238878, Math.toRadians(-180));
    // -------------------------------------------------


    // paths -------------------------------------------
    private PathChain driveStartShoot, driveShootArtifact1;

    public void buildPaths() {
        //coords for start/end pose
        driveStartShoot = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
        driveShootArtifact1 = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, artifact1Pose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), artifact1Pose.getHeading())
                .build();
    }
    // -------------------------------------------------


    // state machine -----------------------------------
    public void statePathUpdate() {
        switch(pathstate) {
            case DriveStart_Shoot:
                follower.followPath(driveStartShoot, true);
                pathstate = PathState.ShootPreload;
                break;
            case ShootPreload:
                if (!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 5) {
                     //checks if previous path is finished
                    Flywheel.setPower(1);
                    if (pathTimer.getElapsedTimeSeconds() > 10) {
                        Flywheel.setPower(0);
                    }
                    pathstate = PathState.DriveToArtifactPickup1;
                }
                break;
            case DriveToArtifactPickup1:
                if (!follower.isBusy()) {
                    follower.followPath(driveShootArtifact1, true);
                }
                break;
        }

    }
    // -------------------------------------------------

    public void setPathstate(PathState newState) {
        pathstate = newState;
        pathTimer.resetTimer();
    }


    @Override
    public void init() {
        pathstate = PathState.DriveStart_Shoot;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        opModeTimer.resetTimer();
        follower = Constants.createFollower(hardwareMap);

        control_hub = hardwareMap.get(Blinker.class, "Control Hub");
        RFwheel = hardwareMap.get(DcMotor.class, "RFwheel");
        LFwheel = hardwareMap.get(DcMotor.class, "LFwheel");
        RBwheel = hardwareMap.get(DcMotor.class, "RBwheel");
        LBwheel = hardwareMap.get(DcMotor.class, "LBwheel");
        Flywheel = hardwareMap.get(DcMotor.class, "Flywheel");
        Intake = hardwareMap.get(DcMotor.class, "Intake");

        buildPaths();
        follower.setPose(startPose);

    }

    public void start() {
        opModeTimer.resetTimer();
        setPathstate(pathstate);

    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();



        telemetry.addData("path state: ", pathstate.toString());
        telemetry.addData("x: ", follower.getPose().getX());
        telemetry.addData("y: ", follower.getPose().getY());
        telemetry.addData("heading: ", follower.getPose().getHeading());
        telemetry.addData("Path time: ", pathTimer.getElapsedTimeSeconds());

    }

}
    