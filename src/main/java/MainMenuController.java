

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;


/**
 * This class handles interactions with the end game screen.
 * @author William Randle
 */
public class MainMenuController implements Initializable {



    private static ArrayList<String> availableChars = new ArrayList<>();
    private static ArrayList<String> additionalScramble = new ArrayList<>();

    private static String fontSize = "-fx-font-size: 13px";

    private static String delimeter = "------------------------------------------";


    public Timeline timer;

    public static String currentScramble = "";


    private static long timeAtStart;
    public static boolean running = false;

    private static int scroll = 0;

    private static int scrollAll = 0;
    private static String display_type = "daily";

    private static String allTimeDisplay= "pbs";

    @FXML
    private VBox pbs;

    @FXML
    private VBox times;

    @FXML
    private VBox allTime;

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
        long millisTime = System.currentTimeMillis();
        if (event.getCode() == KeyCode.SPACE) {
            if (running) {
                timerEnd(currentTime, millisTime);
            } else {
                timerStart();
            }
        }

    }

    private void timerEnd(long currentTime, long millisTime) {
        updateScramble();
        Solve newSolve = new Solve(currentScramble, millisTime, currentTime/SECONDS_AMOUNT, "N/A");
        SolveList.addSolve("solves.txt", newSolve);
        timer.stop();
        updateTimerText(currentTime);
        updateGui();
        running = false;
    }

    private void timerStart() {
        timer.play();
        timeAtStart = System.nanoTime();
        running = true;
    }

    private void updateScramble() {
        currentScramble = generateScramble();
        scrambleLabel.setText(currentScramble);

    }



    public void updateGui() {
        long time = System.currentTimeMillis();
        getTimeLeaderboard(daily);
        System.out.println("time-based: " + (System.currentTimeMillis()-time));
        time = System.currentTimeMillis();
        updateLast(times);
        System.out.println("lastN: " + (System.currentTimeMillis()-time));
        time = System.currentTimeMillis();

        if (Settings.setting.equals("quality")) {
            updatePbs(pbs);
            System.out.println("pbs: " + (System.currentTimeMillis() - time));
            time = System.currentTimeMillis();

            getAllTimeBoard(allTime);
            System.out.println("all time: " + (System.currentTimeMillis()-time));
        }


    }






    private void getAllTimeBoard(VBox vbox) {
        resetBoard(vbox);


        Button scrollUp = new Button();
        scrollUp.setText("^");

        scrollUp.setFocusTraversable(false);
        scrollUp.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                scrollAll--;
                if (scrollAll < 0) {
                    scrollAll = 0;
                }
                getAllTimeBoard(vbox);

            }

        });

        vbox.getChildren().add(scrollUp);


        switch (allTimeDisplay) {
            case "pbs" -> updateSinglePbs(vbox);
            case "ao5" -> updateAo5Pbs(vbox);
            case "ao12" -> updateAo12Pbs(vbox);
            default -> resetBoard(vbox);
        }


        Button scrollDown = new Button();
        scrollDown.setText("v");

        scrollDown.setFocusTraversable(false);
        scrollDown.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {

                scrollAll++;
                getAllTimeBoard(vbox);

            }

        });

        vbox.getChildren().add(scrollDown);


        Button allTimeChange = new Button();
        allTimeChange.setText("Change Type");

        allTimeChange.setFocusTraversable(false);
        allTimeChange.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {

                switch (allTimeDisplay) {
                    case "pbs" -> allTimeDisplay = "ao5";
                    case "ao5" -> allTimeDisplay = "ao12";
                    case "ao12" -> allTimeDisplay = "pbs";
                    default -> resetBoard(vbox);
                }
                SolveList.loadMoreStats();
                updateGui();

            }

        });
        vbox.getChildren().add(allTimeChange);
    }

    private void getTimeLeaderboard(VBox vbox) {
        switch (display_type) {
            case "daily" -> updateDaily(vbox);
            case "weekly" -> updateWeekly(vbox);
            case "monthly" -> updateMonthly(vbox);
            case "all" -> updateAll(vbox);
            default -> resetBoard(vbox);
        }

        Button scrollDown = new Button();
        scrollDown.setText("Change Time-Span");

        scrollDown.setFocusTraversable(false);
        scrollDown.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                switch (display_type) {
                    case "daily" -> display_type = "weekly";
                    case "weekly" -> display_type = "monthly";
                    case "monthly" -> display_type = "all";
                    case "all" -> display_type = "daily";
                    default -> resetBoard(vbox);
                }
                updateGui();
            }

        });
        vbox.getChildren().add(scrollDown);


    }

    private void resetBoard(VBox vbox) {
        while (vbox.getChildren().size() > 0) {
            vbox.getChildren().remove(0);
        }
    }



    private void updatePbs(VBox pbs) {
        resetBoard(pbs);

        Button title = new Button();
        title.setText("Averages");
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;-fx-font-size: 18px;");
        pbs.getChildren().add(title);

        pbs.getChildren().add(new Text(delimeter));

        Button ao5text = new Button();
        ao5text.setText("ao5: " + String.format("%.3f"
                , SolveList.getAverage(5, "solves.txt")));
        ao5text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        pbs.getChildren().add(ao5text);


        Button ao12text = new Button();
        ao12text.setText("ao12: " + String.format("%.3f"
                , SolveList.getAverage(12, "solves.txt")));
        ao12text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        pbs.getChildren().add(ao12text);

        Button ao50text = new Button();
        ao50text.setText("ao50: " + String.format("%.3f"
                , SolveList.getAverage(50, "solves.txt")));
        ao50text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        pbs.getChildren().add(ao50text);

        pbs.getChildren().add(new Text(delimeter));

        Button pbao5text = new Button();
        pbao5text.setText("PB ao5: " + String.format("%.3f"
                , SolveList.getpbAo5().getTime()));
        pbao5text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);

        Tooltip pbao5Solves= new Tooltip(SolveList.getpbAo5().toString());

        pbao5text.setTooltip(pbao5Solves);

        pbs.getChildren().add(pbao5text);

        Button pbao12text = new Button();
        pbao12text.setText("PB ao12: " + String.format("%.3f"
                , SolveList.getpbAo12().getTime()));
        pbao12text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);

        Tooltip pbao12Solves= new Tooltip(SolveList.getpbAo12().toString());

        pbao12text.setTooltip(pbao12Solves);
        pbs.getChildren().add(pbao12text);

        Button pbao50text = new Button();
        pbao50text.setText("PB ao50: " + String.format("%.3f"
                , SolveList.getpbAo50()));
        pbao50text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        pbs.getChildren().add(pbao50text);
    }

    private void updateLast(VBox times) {

        ArrayList<Solve> solves = SolveList.getLastN("solves.txt", scroll ,VISIBILE_LIMIT+scroll);

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        while (times.getChildren().size() > 0) {
            times.getChildren().remove(0);
        }



        Button title = new Button();
        title.setText("last " + scroll + " - " + (VISIBILE_LIMIT+scroll) + " / " + SolveList.getSolveCount());
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        times.getChildren().add(title);


        times.getChildren().add(new Text(delimeter));

        Button scrollUp = new Button();
        scrollUp.setText("^");

        scrollUp.setFocusTraversable(false);
        scrollUp.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                scroll--;
                if (scroll < 0) {
                    scroll = 0;
                }
                updateLast(times);

            }

        });

        times.getChildren().add(scrollUp);


        for (Solve solve : solves) {


            times.getChildren().add(getSolveButton(solve, ao5, ao12));


        }

        Button scrollDown = new Button();
        scrollDown.setText("V");

        scrollDown.setFocusTraversable(false);
        scrollDown.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                scroll++;
                if (scroll > SolveList.getSolveCount()-VISIBILE_LIMIT) {
                    scroll = SolveList.getSolveCount()-VISIBILE_LIMIT;
                }
                updateLast(times);
            }

        });
        times.getChildren().add(scrollDown);

    }

    private Button getSolveButton(Solve solve, ArrayList<Solve> ao5, ArrayList<Solve> ao12) {
        return Settings.getSolveButton(solve, ao5, ao12);

    }



    private void updateAll(VBox vbox) {
        resetBoard(vbox);

        ArrayList<Solve> solves = SolveList.getAllTime();

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        Button title = new Button();
        title.setText("All Time"+ ": " + solves.size());
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        vbox.getChildren().add(title);
        vbox.getChildren().add(new Text(delimeter));


        int i = 0;
        for (Solve solve : solves) {
            i++;
            if (i > VISIBILE_LIMIT) {

            } else {
                vbox.getChildren().add(getSolveButton(solve, ao5, ao12));
            }
        }


    }

    private void updateSinglePbs(VBox vbox) {

        ArrayList<Solve> solves = SolveList.getPbs();
        if (scrollAll > solves.size()-VISIBILE_LIMIT) {
            scrollAll = solves.size()-VISIBILE_LIMIT;
        }

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        Button title = new Button();
        title.setText("Pbs All Time: " + scrollAll + " - " + (VISIBILE_LIMIT+scrollAll) + " / " + solves.size());
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        vbox.getChildren().add(title);
        vbox.getChildren().add(new Text(delimeter));


        if (solves.size() <= VISIBILE_LIMIT) {
            for (Solve solve : solves) {
                vbox.getChildren().add(getSolveButton(solve, ao5, ao12));
            }
        } else {
            for (int i = scrollAll; i < solves.size(); i++) {
                if (i-scrollAll >= VISIBILE_LIMIT) {

                } else {
                    vbox.getChildren().add(getSolveButton(solves.get(i), ao5, ao12));
                }
            }
        }

    }



    private void updateAo5Pbs(VBox vbox) {


        ArrayList<Solve> solves = SolveList.getPbsAo5();
        if (scrollAll > solves.size()-VISIBILE_LIMIT) {
            scrollAll = solves.size()-VISIBILE_LIMIT;
        }

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        Button title = new Button();
        title.setText("ao5 All Time:"+ ": " + solves.size());
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        vbox.getChildren().add(title);
        vbox.getChildren().add(new Text(delimeter));


        int i = 0;
        for (Solve solve : solves) {
            i++;
            if (i > VISIBILE_LIMIT) {

            } else {
                vbox.getChildren().add(getSolveButton(solve, ao5, ao12));
            }
        }


    }

    private void updateAo12Pbs(VBox vbox) {


        ArrayList<Solve> solves = SolveList.getPbsAo12();
        if (scrollAll > solves.size()-VISIBILE_LIMIT) {
            scrollAll = solves.size()-VISIBILE_LIMIT;
        }

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        Button title = new Button();
        title.setText("ao12s All Time:"+ ": " + solves.size());
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        vbox.getChildren().add(title);
        vbox.getChildren().add(new Text(delimeter));


        int i = 0;
        for (Solve solve : solves) {
            i++;
            if (i > VISIBILE_LIMIT) {

            } else {
                vbox.getChildren().add(getSolveButton(solve, ao5, ao12));
            }
        }


    }

    private void updateDaily(VBox vbox) {
        resetBoard(vbox);

        ArrayList<Solve> solves = SolveList.getDaily("solves.txt");

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        Button title = new Button();
        title.setText("Today"+ ": " + solves.size());
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        vbox.getChildren().add(title);
        vbox.getChildren().add(new Text(delimeter));


            int i = 0;
            for (Solve solve : solves) {
                i++;
                if (i > VISIBILE_LIMIT) {

                } else {
                    vbox.getChildren().add(getSolveButton(solve, ao5, ao12));
                }
        }


    }

    private void updateWeekly(VBox weekly) {
        while (weekly.getChildren().size() > 0) {
            weekly.getChildren().remove(0);
        }

        ArrayList<Solve> solves = SolveList.getWeekly("solves.txt");

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        Button title = new Button();
        title.setText("This Week"+ ": " + solves.size());
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        weekly.getChildren().add(title);
        weekly.getChildren().add(new Text(delimeter));
        int i = 0;
        for (Solve solve : solves) {
            i++;
            if (i > VISIBILE_LIMIT) {

            } else {
                weekly.getChildren().add(getSolveButton(solve, ao5, ao12));
            }
        }
    }

    private void updateMonthly(VBox monthly) {
        while (monthly.getChildren().size() > 0) {
            monthly.getChildren().remove(0);
        }

        ArrayList<Solve> solves = SolveList.getMonthly("solves.txt");

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        Button title = new Button();
        title.setText("This Month"+ ": " + solves.size());
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        monthly.getChildren().add(title);
        monthly.getChildren().add(new Text(delimeter));
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
        Settings.controller = this;
        int fpsTime = 1000 / Menu.FPS;
        timer = new Timeline(new KeyFrame(Duration.millis(fpsTime), (ActionEvent event) -> {
            updateTimer();

        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        Main.timerController = this;

        initializeScrambleLegal();
        updateScramble();
        updateGui();
    }
}

