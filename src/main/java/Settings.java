package main.java;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class Settings {

    public static MainMenuController controller;

    public static String setting = "medium"; //can be fast, medium or quality

    private static String fontSize = "-fx-font-size: 13px";

    private static String delimeter = "------------------------------------------";

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
                    controller.updateGui();
                } else if (apply.get().equals(dnf)) {
                    SolveList.setPenalty(solve, "DNF", "solves.txt");
                    controller.updateGui();
                } else if (apply.get().equals(delete)) {
                    SolveList.delete(solve, "solves.txt");
                    controller.updateGui();
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
}
