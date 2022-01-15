

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class Settings {

    public static MainMenuController controller;

    public static String setting = "quality"; //can be fast, medium or quality

    private static String fontSize = "-fx-font-size: 13px";

    private static String delimeter = "----------------------------------------------------------";

    public static Button getSolveButton(Solve solve, ArrayList<Solve> ao5, ArrayList<Solve> ao12) {
        Button button= getSolveButtonRequired(solve);

        if (setting.equals("medium")) {
            button = getSolveButtonMedium(solve, ao5, ao12, button);
        } else if (setting.equals("quality")) {
            button = getSolveButtonQuality(solve, ao5, ao12, button);
        }

        return button;

    }

    private static Button getSolveButtonRequired(Solve solve) {
        Button button = new Button();
        button.setText(solve.displayString());
        Tooltip toolTip = new Tooltip(solve.getScramble());
        toolTip.setShowDelay(Duration.seconds(0.3));
    //    toolTip.setHideDelay(Duration.seconds(3));
        button.setTooltip(toolTip);
        button.setFocusTraversable(false);
        button.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {

                ButtonType penalty = new ButtonType("+2", ButtonBar.ButtonData.OK_DONE);
                ButtonType dnf = new ButtonType("dnf", ButtonBar.ButtonData.OK_DONE);
                ButtonType delete = new ButtonType("delete", ButtonBar.ButtonData.OK_DONE);
                ButtonType copy = new ButtonType("copy", ButtonBar.ButtonData.OK_DONE);

                ButtonType cancel = new ButtonType("cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert penalties = new Alert(Alert.AlertType.CONFIRMATION, "", penalty, dnf, delete,copy, cancel);
                penalties.setTitle("apply penalties");
                penalties.setHeaderText("choose what you want to do with this solve");

                Optional<ButtonType> apply = penalties.showAndWait();
                if (apply.get().equals(penalty)) {
                    SolveList.setPenalty(solve, "+2", "solves.txt");
                    controller.updateGui();
                } else if (apply.get().equals(dnf)) {
                    SolveList.setPenalty(solve, "DNF", "solves.txt");
                    controller.updateGui();
                } else if (apply.get().equals(delete)) {
                    SolveList.delete(solve, "solves.txt");
                    System.out.println("delete");
                    controller.updateGui();

                } else if (apply.get().equals(copy)) {
                    StringSelection stringSelection = new StringSelection(solve.getScramble());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);

                } else {

                }
            }

        });
        button.setStyle("-fx-text-fill: #bbbbbb;-fx-background-color:#333333;" + fontSize);
        return button;
    }

    private static Button getSolveButtonQuality(Solve solve, ArrayList<Solve> ao5, ArrayList<Solve> ao12, Button button) {

        boldifyRelevantButton(button, solve);


        if (containsSolve(ao5, solve)) {


            button.setStyle("-fx-text-fill: lime;-fx-background-color:#444444;-fx-font-weight: bold;" + fontSize);

        } else if (containsSolve(ao12, solve)) {

            button.setStyle("-fx-text-fill: #36D897;-fx-background-color:#444444;-fx-font-weight: bold;" + fontSize);

        }
        return button;
    }


    private static Button getSolveButtonMedium(Solve solve, ArrayList<Solve> ao5, ArrayList<Solve> ao12, Button button) {


        if (containsSolve(ao5, solve)) {


            button.setStyle("-fx-text-fill: lime;-fx-background-color:#444444;-fx-font-weight: bold;" + fontSize);

        } else if (containsSolve(ao12, solve)) {

            button.setStyle("-fx-text-fill: #36D897;-fx-background-color:#444444;-fx-font-weight: bold;" + fontSize);

        }
        return button;
    }


    private static void boldifyRelevantButton(Button button, Solve solve) {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        button.setStyle("-fx-text-fill: #bbbbbb;-fx-background-color:#333333;" + fontSize);
        if ((calendar.get(Calendar.DAY_OF_YEAR) == solve.getCalendar().get(Calendar.DAY_OF_YEAR))
                && (calendar.get(Calendar.YEAR) == solve.getCalendar().get(Calendar.YEAR))) {
            button.setStyle("-fx-text-fill: #cccccc;-fx-background-color:#444444;-fx-font-weight: bold;" + fontSize);

        } else if ((calendar.get(Calendar.WEEK_OF_YEAR) == solve.getCalendar().get(Calendar.WEEK_OF_YEAR))
                && (calendar.get(Calendar.YEAR) == solve.getCalendar().get(Calendar.YEAR))) {
            button.setStyle("-fx-text-fill: #cccccc;-fx-background-color:#333333;-fx-font-weight: bold;" + fontSize);

        }

    }

    private static boolean containsSolve(ArrayList<Solve> solves, Solve solve) {
        for (Solve solve1 : solves) {
            if (solve1.isEqual(solve)) {
                return true;
            }
        }
        return false;
    }

    public static void updatePbs(VBox pbs) {
        resetBoard(pbs);

        if (setting.equals("fast")) {
            updatePbsFast(pbs);
        } else if (setting.equals("medium")) {
            updatePbsMedium(pbs);
        }else if (setting.equals("quality")) {
            updatePbsQuality(pbs);
        }

    }

    private static void updatePbsFast(VBox pbs) {
        resetBoard(pbs);

        Button title = new Button();
        title.setText("Averages");
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;-fx-font-size: 18px;");
        pbs.getChildren().add(title);

        pbs.getChildren().add(new Text(delimeter));

    }

    private static void updatePbsMedium(VBox pbs) {
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

        Button total = new Button();
        total.setText("total: " + String.format("%.3f"
                , SolveList.getTotal()));
        total.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        pbs.getChildren().add(total);

        Button avg = new Button();
        avg.setText("average: " + String.format("%.3f"
                , SolveList.getAverage()));
        avg.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
        pbs.getChildren().add(avg);

    }

    private static void updatePbsQuality(VBox pbs) {
        resetBoard(pbs);

        Button title = new Button();
        title.setText("info");
        title.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);
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
                , SolveList.getpbAo5().getTime()) + " | " + Solve.getDateFormat(SolveList.getpbAo5().getCalendar()));
        pbao5text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);

        Tooltip pbao5Solves= new Tooltip(SolveList.getpbAo5().toString());

        pbao5text.setTooltip(pbao5Solves);

        pbs.getChildren().add(pbao5text);

        Button pbao12text = new Button();
        pbao12text.setText("PB ao12: " + String.format("%.3f"
                , SolveList.getpbAo12().getTime()) + " | " + Solve.getDateFormat(SolveList.getpbAo12().getCalendar()) );
        pbao12text.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);

        Tooltip pbao12Solves= new Tooltip(SolveList.getpbAo12().toString());

        pbao12text.setTooltip(pbao12Solves);
        pbs.getChildren().add(pbao12text);

        pbs.getChildren().add(styleButton("PB ao50: " + String.format("%.3f"
                , SolveList.getpbAo50().getTime()) + " | " + Solve.getDateFormat(SolveList.getpbAo50().getCalendar())));


        pbs.getChildren().add(new Text(delimeter));

        pbs.getChildren().add(styleButton("total: " + String.format("%.3f"
                , SolveList.getTotal())));



        pbs.getChildren().add(styleButton("average: " + String.format("%.3f"
                , SolveList.getAverage())));

        pbs.getChildren().add(styleButton("shortest scramble: " + SolveList.getSmallestScramble()));

        pbs.getChildren().add(new Text(delimeter));

        pbs.getChildren().add(styleButton("In day: " + SolveList.getMostInDay() + " | " + Solve.getDateFormat(SolveList.getMostDay())));

        pbs.getChildren().add(styleButton("In Hour: " + SolveList.getMostInHour() + " | " + Solve.getTimeFormat(SolveList.getMostHour())));

        pbs.getChildren().add(styleButton("longest break: " + SolveList.getLongestTimeWithout()/(1000*60*60*24) + "days on: " + Solve.getDateFormat(SolveList.getLongestTime())));

        pbs.getChildren().add(styleButton("first solve on: " + Solve.getDateFormat(SolveList.getFirstSolve())));


        pbs.getChildren().add(new Text(delimeter));

        pbs.getChildren().add(styleButton("streaks ( " + SolveList.streakCount + " solves in a day): "));

        pbs.getChildren().add(styleButton("current streak: " + SolveList.getCurrentStreak()));

        pbs.getChildren().add(styleButton("best streak: " + SolveList.getBestStreak() + " on: " + Solve.getDateFormat(SolveList.getBestStreakTime())));

        pbs.getChildren().add(styleButton("streak days: " + SolveList.getStreakDays()));

    }
    private static Button styleButton(String text) {
        Button button = new Button();
        button.setText(text);
        button.setStyle("-fx-text-fill: lime;-fx-background-color:#333333;" + fontSize);

        return button;
    }

    private static void resetBoard(VBox vbox) {
        while (vbox.getChildren().size() > 0) {
            vbox.getChildren().remove(0);
        }
    }
}
