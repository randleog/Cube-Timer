import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class handles interactions with the end game screen.
 * @author William Randle
 */
public class MainMenuController implements Initializable {


    public Timeline timer;

    private static long timeAtStart;
    public static boolean running = false;

    @FXML
    private Text timeLabel;

    private static final double SECONDS_AMOUNT = 1000000000.0;

    private static final int dp = 2;

    public void updateTimerText() {
        long currentTime = System.nanoTime()-timeAtStart;
        timeLabel.setText(String.format(getFormat(), currentTime/SECONDS_AMOUNT));
    }

    private static String getFormat() {

        return "%." + dp + "f";
    }


    private void updateTimer() {
        updateTimerText();

    }

    @FXML
    public void handleKeyboard(KeyEvent event) {

        if (event.getCode() == KeyCode.SPACE) {
            if (running) {
                timer.stop();
                running = false;
            } else {


                timer.play();
                timeAtStart = System.nanoTime();
                running = true;
            }
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int fpsTime = 1000 / Menu.FPS;
        timer = new Timeline(new KeyFrame(Duration.millis(fpsTime), (ActionEvent event) -> {
            updateTimer();

        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        Main.timerController = this;
    }
}

