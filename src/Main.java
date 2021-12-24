import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;


/**
 * launching application which runs the timer
 *
 * @author William Randle
 */
public class Main extends Application {

    private static final int WIDTH = 1350;
    private static final int HEIGHT = 900;
    public static final int FPS = 100;

    public static Stage scene;

    public static Scene mainMenu;





    /**
     * Load the menus for navigation, and launch the user selection menu.
     *
     * @param primaryStage Stage javafx shows things on.
     */
    public void start(Stage primaryStage) {


        loadMenus();


        Main.scene = primaryStage;

        setMainMenu();
        scene.show();



    }






    public static void setMainMenu() {
        scene.setScene(mainMenu);
    }



    public void loadMenus() {
        mainMenu = loadScene("MainMenu.fxml");
    }


    /**
     * Closes the javafx window.
     */
    public static void quit() {

        scene.close();
    }

    /**
     * Loads an fxml file from parsed filename string.
     *
     * @param fxmlFile String the name of the file to be loaded.
     * @return A complete scene which can be shown.
     */
    private Scene loadScene(String fxmlFile) {
        try {
            BorderPane pane = FXMLLoader.load(getClass().getResource(fxmlFile));
            return new Scene(pane, WIDTH, HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Launches the application.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


}
