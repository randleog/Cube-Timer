import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Menu {

    private static final int WIDTH = 1350;
    private static final int HEIGHT = 900;
    public static final int FPS = 100;

    public static Scene mainMenu;

    public static void loadMenus() {
        mainMenu = loadScene("MainMenu.fxml");
    }

    /**
     * Loads an fxml file from parsed filename string.
     *
     * @param fxmlFile String the name of the file to be loaded.
     * @return A complete scene which can be shown.
     */
    private static Scene loadScene(String fxmlFile) {
        try {
            BorderPane pane = FXMLLoader.load(Menu.class.getResource(fxmlFile));
            return new Scene(pane, WIDTH, HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
