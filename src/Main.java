import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;

import java.util.Random;
import java.util.random.*;

import javafx.stage.Stage;


/**
 * launching application which runs the timer
 *
 * @author William Randle
 */
public class Main extends Application {

    private static Stage scene;

    public static MainMenuController timerController;

    public static Random random;




    /**
     * Load the menus for navigation, and launch the user selection menu.
     *
     * @param primaryStage Stage javafx shows things on.
     */
    public void start(Stage primaryStage) {
        random = new Random();

        SolveList.loadSolves();
        Menu.loadMenus();


        Main.scene = primaryStage;

        setMainMenu();
        scene.show();


    }






    public static void setMainMenu() {
        scene.setScene(Menu.mainMenu);
    }




    /**
     * Closes the javafx window.
     */
    public static void quit() {

        scene.close();
    }


    /**
     * Launches the application.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


}
