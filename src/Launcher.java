import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Name: Ahmet Bilici
 * Date: 03/30/2021
 */
public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method sets scene dimensions and starts it.
     * @param primaryStage Stage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        TicTacToePane pane = new TicTacToePane();

        Scene scene = new Scene(pane, 500, 500);

        primaryStage.setTitle("Tic Tac Toe!");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
