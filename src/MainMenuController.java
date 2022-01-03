import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * This class handles interactions with the end game screen.
 * @author William Randle
 */
public class MainMenuController implements Initializable {

    private static ArrayList<String> availableChars = new ArrayList<>();
    private static ArrayList<String> additionalScramble = new ArrayList<>();


    public Timeline timer;

    public static String currentScramble = "";


    private static long timeAtStart;
    public static boolean running = false;

    @FXML
    private VBox pbs;

    @FXML
    private VBox times;

    @FXML
    private VBox monthly;

    @FXML
    private VBox weekly;

    @FXML
    private VBox daily;

    @FXML
    private Text timeLabel;

    @FXML
    private Text scrambleLabel;

    private static final int VISIBILE_LIMIT = 20;

    private static final double SECONDS_AMOUNT = 1000000000.0;

    private static final int dp = 3;


    public void updateTimerText(long currentTime) {
        timeLabel.setText(String.format(getFormat(), currentTime/SECONDS_AMOUNT));
    }

    private static String getFormat() {

        return "%." + dp + "f";
    }


    private void updateTimer() {
        long currentTime = System.nanoTime()-timeAtStart;
        updateTimerText(currentTime);



    }

    @FXML
    public void handleKeyboard(KeyEvent event) {
        long currentTime = (System.nanoTime()-timeAtStart);
        if (event.getCode() == KeyCode.SPACE) {
            if (running) {
                Solve newSolve = new Solve(currentScramble, System.currentTimeMillis(), currentTime/SECONDS_AMOUNT, "N/A");
                SolveList.addSolve("solves.txt", newSolve);
                timer.stop();
                updateTimerText(currentTime);
                updateScramble();
                running = false;
            } else {


                timer.play();
                timeAtStart = System.nanoTime();
                running = true;
            }
        }

    }

    private void updateScramble() {
        currentScramble = generateScramble();
        scrambleLabel.setText(currentScramble);

        updateTimeList();
    }

    private void updateTimeList() {
        updateDaily();
        updateWeekly();
        updateMonthly();
       updateLast();

       updatePbs();



    }

    private boolean containsSolve(ArrayList<Solve> solves, Solve solve) {
        for (Solve solve1 : solves) {
            if (solve1.isEqual(solve)) {
                return true;
            }
        }
        return false;
    }

    private void updatePbs() {
        Button title = new Button();
        title.setText("Averages");
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        pbs.getChildren().add(title);

        pbs.getChildren().add(new Text("--------"));

        Button ao5text = new Button();
        ao5text.setText("ao5: " + String.format("%.3f"
                , SolveList.getAverage(5, "solves.txt")));
        ao5text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        pbs.getChildren().add(ao5text);


        Button ao12text = new Button();
        ao12text.setText("ao12: " + String.format("%.3f"
                , SolveList.getAverage(12, "solves.txt")));
        ao12text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        pbs.getChildren().add(ao12text);

        Button ao50text = new Button();
        ao50text.setText("ao50: " + String.format("%.3f"
                , SolveList.getAverage(50, "solves.txt")));
        ao50text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        pbs.getChildren().add(ao50text);

        pbs.getChildren().add(new Text("--------"));

        Button pbao5text = new Button();
        pbao5text.setText("PB ao5: " + String.format("%.3f"
                , SolveList.getpbAo5()));
        pbao5text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        pbs.getChildren().add(pbao5text);

        Button pbao12text = new Button();
        pbao12text.setText("PB ao12: " + String.format("%.3f"
                , SolveList.getpbAo12()));
        pbao12text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        pbs.getChildren().add(pbao12text);

        Button pbao50text = new Button();
        pbao50text.setText("PB ao50: " + String.format("%.3f"
                , SolveList.getpbAo50()));
        pbao50text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        pbs.getChildren().add(pbao50text);
    }

    private void updateLast() {
        ArrayList<Solve> solves = SolveList.getLastN("solves.txt", 25);

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 12);

        while (times.getChildren().size() > 0) {
            times.getChildren().remove(0);
        }

        Button title = new Button();
        title.setText("last " + VISIBILE_LIMIT);
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        times.getChildren().add(title);

        times.getChildren().add(new Text("-------------------------------------"));

        int i = 0;
        for (Solve solve : solves) {
            i++;
            if (i > VISIBILE_LIMIT) {

            } else {

                times.getChildren().add(getSolveButton(solve, ao5, ao12));
            }
        }


    }

    private Button getSolveButton(Solve solve, ArrayList<Solve> ao5, ArrayList<Solve> ao12) {
        Button button = new Button();
        button.setText(solve.displayString());

        button.setTooltip(new Tooltip(solve.getScramble()));
        button.setFocusTraversable(false);
        button.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {

                ButtonType penalty = new ButtonType("+2", ButtonBar.ButtonData.OK_DONE);
                ButtonType dnf = new ButtonType("dnf", ButtonBar.ButtonData.OK_DONE);
                ButtonType delete = new ButtonType("delete", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert penalties = new Alert(Alert.AlertType.CONFIRMATION, "", penalty, dnf, delete, cancel);
                penalties.setTitle("apply penalties");
                penalties.setHeaderText("choose what you want to do with this solve");

                Optional<ButtonType> apply = penalties.showAndWait();
                if (apply.get().equals(penalty)) {
                    SolveList.setPenalty(solve, "+2", "solves.txt");
                    updateTimeList();
                } else if (apply.get().equals(dnf)) {
                    SolveList.setPenalty(solve, "DNF", "solves.txt");
                    updateTimeList();
                } else if (apply.get().equals(delete)) {
                    SolveList.delete(solve, "solves.txt");
                    updateTimeList();

                } else {

                }
            }

        });

        if (containsSolve(ao5, solve)) {


            button.setStyle("-fx-text-fill: lime;-fx-background-color:#444444;-fx-font-weight: bold;");

        } else if (containsSolve(ao12, solve)) {

            button.setStyle("-fx-text-fill: cccccc;-fx-background-color:#444444;");

        } else {

            button.setStyle("-fx-text-fill: #bbbbbb;-fx-background-color:#333333");

        }

        return button;
    }

    private void updateDaily() {
        while (daily.getChildren().size() > 0) {
            daily.getChildren().remove(0);
        }

        ArrayList<Solve> solves = SolveList.getDaily("solves.txt");

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 12);

        Button title = new Button();
        title.setText("Today");
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        daily.getChildren().add(title);
        daily.getChildren().add(new Text("-------------------------------------"));

        Button pbao5text = new Button();
        pbao5text.setText("PB ao5: " + String.format("%.3f"
                , SolveList.getPBAo5Daily()));
        pbao5text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        daily.getChildren().add(pbao5text);



        daily.getChildren().add(new Text("-------------------------------------"));

            int i = 0;
            for (Solve solve : solves) {
                i++;
                if (i > VISIBILE_LIMIT) {

                } else {
                    daily.getChildren().add(getSolveButton(solve, ao5, ao12));
                }
        }
    }

    private void updateWeekly() {
        while (weekly.getChildren().size() > 0) {
            weekly.getChildren().remove(0);
        }

        ArrayList<Solve> solves = SolveList.getWeekly("solves.txt");

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 12);

        Button title = new Button();
        title.setText("This week");
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        weekly.getChildren().add(title);
        weekly.getChildren().add(new Text("-------------------------------------"));
        int i = 0;
        for (Solve solve : solves) {
            i++;
            if (i > VISIBILE_LIMIT) {

            } else {
                weekly.getChildren().add(getSolveButton(solve, ao5, ao12));
            }
        }
    }

    private void updateMonthly() {
        while (monthly.getChildren().size() > 0) {
            monthly.getChildren().remove(0);
        }

        ArrayList<Solve> solves = SolveList.getMonthly("solves.txt");

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 12);

        Button title = new Button();
        title.setText("This Month");
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333");
        monthly.getChildren().add(title);
        monthly.getChildren().add(new Text("-------------------------------------"));
        int i = 0;
        for (Solve solve : solves) {
            i++;
            if (i > VISIBILE_LIMIT) {

            } else {
                monthly.getChildren().add(getSolveButton(solve, ao5, ao12));
            }
        }
    }



    private static String generateScramble() {
        String scramble = "";
        ArrayList<String> letters = new ArrayList<>();
        while (letters.size() < 20) {
            letters.add(getScrambleLetter(letters));
        }

        for (String letter : letters) {
            scramble = scramble + letter + additionalScramble.get(Main.random.nextInt(additionalScramble.size())) + " ";
        }

        return scramble;
    }

    private static boolean opposite(String moves1, String move2) {
        switch(moves1) {
            case "RL":
                return move2.equals("R");
            case "LR":
                return move2.equals("L");
            case "FB":
                return move2.equals("F");
            case "BF":
                return move2.equals("B");
            case "UD":
                return move2.equals("U");
            case "DU":
                return move2.equals("D");
            default:
                return false;
        }

    }



    private static String getScrambleLetter(ArrayList<String> lastMoves) {
        String newMove = "";
        if (lastMoves.size() == 0) {
            newMove = availableChars.get(Main.random.nextInt(availableChars.size()));
        } else {

            ArrayList<String> tempMoves = getAllowedMoves(lastMoves);
            newMove = tempMoves.get(Main.random.nextInt(tempMoves.size()));
        }
        return newMove;

    }

    private static ArrayList<String> getAllowedMoves(ArrayList<String> lastMoves) {
        ArrayList<String> tempMoves = new ArrayList<>();
        tempMoves.addAll(availableChars);
        if (lastMoves.size()>=1) {
            tempMoves.removeIf(s -> (s.equals(lastMoves.get(lastMoves.size() - 1))));
        }
        if (lastMoves.size()>=2) {
            tempMoves.removeIf(s -> (opposite(lastMoves.get(lastMoves.size() - 2) + lastMoves.get(lastMoves.size() - 1), s)));
        }
        return tempMoves;
    }


    private static void initializeScrambleLegal() {
        availableChars.add("R");
        availableChars.add("U");
        availableChars.add("L");
        availableChars.add("D");
        availableChars.add("B");
        availableChars.add("F");


        additionalScramble.add("'");
        additionalScramble.add("2");
        additionalScramble.add("");

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int fpsTime = 1000 / Menu.FPS;
        timer = new Timeline(new KeyFrame(Duration.millis(fpsTime), (ActionEvent event) -> {
            updateTimer();

        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        Main.timerController = this;

        initializeScrambleLegal();
        updateScramble();

    }
}

