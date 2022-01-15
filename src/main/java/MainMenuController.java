

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * This class handles interactions with the end game screen.
 * @author William Randle
 */
public class MainMenuController implements Initializable {



    private static ArrayList<String> availableChars = new ArrayList<>();
    private static ArrayList<String> additionalScramble = new ArrayList<>();

    private static String fontSize = "-fx-font-size: 13px";

    private static String delimeter = "----------------------------------------------------";

    private boolean updatedMonthly = false;
    private boolean updatedDaily = false;
    private boolean updatedWeekly = false;
    private boolean updatedAll = false;

    public static double timeAddon = 0;//(1.0/Menu.FPS);

    public static int scrambleLength = 25;

    public Timeline timer;

    public static String currentScramble = "";


    private static long timeAtStart;
    public static boolean running = false;

    private static int scrollH = 0;

    private static int scroll = 0;

    private static int scrollAll = 0;
    private static String display_type = "daily";

    private static String allTimeDisplay= "pbs";

    private static double lastTime;

    @FXML
    private Button vButton;

    @FXML
    private Button rButton;

    @FXML
    private Button lButton;

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

    private ArrayList<VBox> panels = new ArrayList<>();

    private int scrollMonthly = 0;

    private boolean scrollV = false;

     private LineChart<Number,Number> lineChart;


    private int lowerH = -1;
    private int higherH = 0;

    public static final int VISIBILE_LIMIT = 20;

    private static final double SECONDS_AMOUNT = 1000000000.0;

    private static final int dp = 3;

    private void updatestreakList(VBox times) {

        ArrayList<Solve> solves = SolveList.solveLastN(SolveList.getMonthDensity(), scrollMonthly ,VISIBILE_LIMIT+scrollMonthly);

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        while (times.getChildren().size() > 0) {
            times.getChildren().remove(0);
        }



        Button title = new Button();
        title.setText("last " + scrollMonthly + " - " + (VISIBILE_LIMIT+scrollMonthly) + " / " + SolveList.getMonthlyD());
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        times.getChildren().add(title);


        times.getChildren().add(new Text(delimeter));

        Button scrollUp = new Button();
        scrollUp.setText("^");

        scrollUp.setFocusTraversable(false);
        scrollUp.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                scrollMonthly--;
                if (scrollMonthly < 0) {
                    scrollMonthly = 0;
                }
                updatestreakList(times);

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
                scrollMonthly++;
                if (scrollMonthly > SolveList.getMonthlyD()-VISIBILE_LIMIT) {
                    scrollMonthly = SolveList.getMonthlyD()-VISIBILE_LIMIT;
                }
                updatestreakList(times);
            }

        });
        times.getChildren().add(scrollDown);

    }



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
        if (!scrollV) {
            long currentTime = (System.nanoTime() - timeAtStart);
            long millisTime = System.currentTimeMillis();
            System.out.println(currentTime / SECONDS_AMOUNT);
            if (event.getCode() == KeyCode.SPACE) {
                if (running) {
                    timerEnd(currentTime, millisTime);
                } else {
                    timerStart();
                }
            }
        }

    }

    private void timerEnd(long currentTime, long millisTime) {
        timer.stop();
        updateScramble();
        lastTime = currentTime/SECONDS_AMOUNT- timeAddon;
        Solve newSolve = new Solve( currentScramble, millisTime, currentTime/SECONDS_AMOUNT- timeAddon, Solve.NO_PENALTY);
        SolveList.addSolve( newSolve);
        updateTimerText(currentTime- (long)Math.floor(timeAddon*SECONDS_AMOUNT));
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

        updateLeaderboards();



    }

    @FXML
    private void scrollRight() {



        scrollH--;
        fixScrollH();
        showLeaderboard();
        updateGui();
    }

    @FXML
    private void scrollDown() {



        scrollV = !scrollV;

        if (scrollV) {
            vButton.setText("^");
        } else {
            vButton.setText("V");
        }
        showLeaderboard();
        updateGui();
    }

    private void fixScrollH() {



        if (scrollH > higherH) {
            scrollH = higherH;
        }

        if (scrollH < lowerH) {
            scrollH = lowerH;
        }
    }


    @FXML
    private void scrollLeft() {



        scrollH++;
        fixScrollH();

        showLeaderboard();
        updateGui();
    }



    private void showLeaderboard() {
        if (scrollH == 0) {
            pbs.setVisible(true);
            times.setVisible(true);
            daily.setVisible(true);
            allTime.setVisible(true);
        } else if (scrollH == 1) {
            daily.setVisible(true);
        }
    }



    public void updateLeaderboards() {
        if (!scrollV) {


            if (scrollH >= 0 && scrollH <= 3) {
                if (Settings.setting.equals("quality")) {
                    getAllTimeBoard(panels.get(scrollH));

                }
            }

            if (scrollH >= -1 && scrollH <= 2) {
                getTimeLeaderboard(panels.get(scrollH + 1));
            }

            if (scrollH >= -2 && scrollH <= 1) {
                updateLast(panels.get(scrollH + 2));
            }

            if (scrollH >= -3 && scrollH <= 0) {
                Settings.updatePbs(panels.get(scrollH + 3));
            }
            if (scrollH >= -4 && scrollH <= -1) {
                updatestreakList(panels.get(scrollH + 4));
            }
        } else {

                resetBoard(panels.get(0));
                resetBoard(panels.get(1));
                resetBoard(panels.get(2));

                timeGraph((panels.get(3)));

        }




    }



    private void timeGraph(VBox times) {


        resetBoard(times);


        times.getChildren().add(new Text(delimeter + delimeter + delimeter + delimeter));

        times.getChildren().add(lineChart);

    }


    private void loadGraph() {
        ArrayList<Solve> pbSingles = SolveList.getPbs();
        ArrayList<Solve> pbao5 = SolveList.getPbsAo5();
        ArrayList<Solve> pbao12 = SolveList.getPbsAo12();
        ArrayList<Solve> pbao50 = SolveList.getPbsAo50();
        ArrayList<Solve> weeks = SolveList.getWeeks();

        long firstTime = SolveList.getFirstSolve().getTimeInMillis();





        NumberAxis xaxis = new NumberAxis();
        NumberAxis yaxis = new NumberAxis();

        yaxis.setLabel("time in seconds");
        xaxis.setLabel("date of completion");

        xaxis.setForceZeroInRange(false);
        xaxis.setTickLabelFormatter(new StringConverter<Number>() {

            @Override
            public String toString(Number month) {

                Calendar calender = Calendar.getInstance();
                calender.setTimeInMillis((long) Math.floor((double)month));
                return Solve.getDateFormat(calender);
            }

            @Override
            public Number fromString(String string) {
                return 0;
            }

        });
        lineChart =
                new LineChart<>(xaxis,yaxis);





        lineChart.setTitle("cubing times");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("per week");
        for (Solve day : weeks) {

            series.getData().add(new XYChart.Data(day.getCalendar().getTimeInMillis(), day.getTime()));
        }
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("pb ao5");
        for (Solve day : pbao5) {

            series2.getData().add(new XYChart.Data(day.getCalendar().getTimeInMillis(), day.getTime()));
        }

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("pb singles");
        for (Solve day : pbSingles) {

            series3.getData().add(new XYChart.Data(day.getCalendar().getTimeInMillis(), day.getTime()));
        }




        lineChart.getData().addAll( series, series2,series3);




        times.getChildren().add(lineChart);

    }
    private void getUpButton(VBox vbox) {
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
    }


    private void getAllTimeBoard(VBox vbox) {
        resetBoard(vbox);




        switch (allTimeDisplay) {
            case "pbs" -> updateSinglePbs(vbox, "single");
            case "ao5" -> updateSinglePbs(vbox, "5");
            case "ao12" -> updateSinglePbs(vbox, "12");
            case "ao50" -> updateSinglePbs(vbox, "50");
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
                    case "ao12" -> allTimeDisplay = "ao50";
                    case "ao50" -> allTimeDisplay = "pbs";
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
            case "daily":
                updatedWeekly = false;
                updatedMonthly = false;
                updatedAll = false;
                if (SolveList.getDailySize() < VISIBILE_LIMIT) {
                    updateDaily(vbox);
                    addChangeTimeButton(vbox);
                } else {
                    if ((SolveList.getDailyIndex(VISIBILE_LIMIT).getTime() > lastTime) || !updatedDaily) {
                        updateWeekly(vbox);
                        updatedDaily = true;
                        addChangeTimeButton(vbox);

                    }
                }
                break;
            case "weekly":
                updatedMonthly = false;
                updatedDaily = false;
                updatedAll = false;
                if (SolveList.getWeeklySize() < VISIBILE_LIMIT) {
                    updateWeekly(vbox);
                    addChangeTimeButton(vbox);
                } else {
                    if ((SolveList.getWeeklyIndex(VISIBILE_LIMIT).getTime() > lastTime) || !updatedWeekly) {
                        updateWeekly(vbox);
                        updatedWeekly = true;
                        addChangeTimeButton(vbox);
                    }
                }

                break;
            case "monthly":
                updatedWeekly = false;
                updatedDaily = false;
                updatedAll = false;
                if (SolveList.getMonthlyN() < 1) {
                    SolveList.loadMonthly("solves");
                }

                if (SolveList.getMonthlyN() < 1) {

                } else {
                    if (SolveList.getMonthlyIndex(VISIBILE_LIMIT).getTime() > lastTime) {
                        SolveList.loadMonthly("solves");
                    }
                    if (SolveList.getMonthlySize() < VISIBILE_LIMIT) {
                        updateMonthly(vbox);
                        addChangeTimeButton(vbox);
                    } else {

                        if ((SolveList.getMonthlyIndex(VISIBILE_LIMIT).getTime() > lastTime) || !updatedMonthly) {
                            updateMonthly(vbox);
                            updatedMonthly = true;
                            addChangeTimeButton(vbox);
                        }
                    }
                }

                break;
            case "all":
                updatedMonthly = false;
                updatedDaily = false;
                updatedWeekly = false;
                if (SolveList.getSolveCount() < VISIBILE_LIMIT) {
                    updateAll(vbox);
                    addChangeTimeButton(vbox);
                } else {
                    if (!updatedAll) {
                        updateAll(vbox);
                        updatedAll = true;
                        addChangeTimeButton(vbox);
                    }
                }
                break;
            default:
                resetBoard(vbox);
        }




    }



    private void addChangeTimeButton(VBox vbox) {
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
        title.setText("All Time"+ ": " + SolveList.getSolveCount());
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

    private void updateSinglePbs(VBox vbox, String type) {



        ArrayList<Solve> solves = new ArrayList<>();
        if (type.equals("single")) {
            solves = SolveList.getPbs();
        } else    if (type.equals("5")) {
            solves =SolveList.getPbsAo5();
        }else    if (type.equals("12")) {
            solves =SolveList.getPbsAo12();
        }else    if (type.equals("50")) {
            solves =SolveList.getPbsAo50();
        }
        if (scrollAll > solves.size()-VISIBILE_LIMIT) {
            scrollAll = solves.size()-VISIBILE_LIMIT;
        }

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        Button title = new Button();
        title.setText(type + " Pbs All Time: " + scrollAll + " - " + (VISIBILE_LIMIT+scrollAll) + " / " + solves.size());
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        vbox.getChildren().add(title);
        vbox.getChildren().add(new Text(delimeter));
        getUpButton(vbox);

        if (solves.size() <= VISIBILE_LIMIT) {
            for (Solve solve : solves) {
                vbox.getChildren().add(getSolveButton(solve, ao5, ao12));
            }
        } else {
            if (scrollAll < 0)  {
                for (int i = 0; i < solves.size(); i++) {


                    if (i  >= VISIBILE_LIMIT) {

                    } else {
                        vbox.getChildren().add(getSolveButton(solves.get(i), ao5, ao12));
                    }
                }
            } else {
                for (int i = scrollAll; i < solves.size(); i++) {


                    if (i >= VISIBILE_LIMIT) {

                    } else {
                        vbox.getChildren().add(getSolveButton(solves.get(i), ao5, ao12));
                    }
                }
            }
        }

    }




    private void updateDaily(VBox vbox) {
        resetBoard(vbox);

        ArrayList<Solve> solves = SolveList.getDaily("solves.txt");

        ArrayList<Solve> ao5 = SolveList.getLastN("solves.txt", 0, 5);

        ArrayList<Solve> ao12 = SolveList.getLastN("solves.txt", 0, 12);

        Button title = new Button();
        title.setText("Today"+ ": " + SolveList.getDailyN());
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
        title.setText("This Week"+ ": " + SolveList.getWeeklyN());
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
        title.setText("This Month"+ ": " + SolveList.getMonthlyN());
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
        while (letters.size() < scrambleLength) {
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

    private void setSized(Node node) {
        node.prefHeight(0);
        node.prefWidth(0);
        node.minHeight(0);
        node.minWidth(0);
    }

    private void maxSize(Node node) {
        node.prefHeight(681);
        node.prefWidth(1350);
        node.minHeight(681);
        node.minWidth(1350);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        panels.add(allTime);
        panels.add(daily);
        panels.add(times);
        panels.add(pbs);

        Settings.controller = this;
        int fpsTime = 1000 / Menu.FPS;
        timer = new Timeline(new KeyFrame(Duration.millis(fpsTime), (ActionEvent event) -> {
            updateTimer();

        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        Main.timerController = this;
        loadGraph();

        initializeScrambleLegal();
        updateScramble();
         updateGui();
         rButton.setFocusTraversable(false);
         lButton.setFocusTraversable(false);
         vButton.setFocusTraversable(false);




    }
}

