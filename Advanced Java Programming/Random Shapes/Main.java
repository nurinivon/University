/*
 * Nuri Nivon 2021
 * The following program is a graphic interface for a game.
 * In the interface the is a button and a canvas.
 * Every time the user clicks the button a random shapes will be shown on the canvas. 
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/*
 * Main class is the is main class of the game and it creates the interface for the game.
 * The interface is based on the file "App.fxml"
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
