package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.*;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			VBox rootContainer = FXMLLoader.load(getClass().getResource("/application/test.fxml"));
			Scene s = new Scene(rootContainer);
			primaryStage.setScene(s);
			// primaryStage.setTitle("Towers Of Hanoi");
			primaryStage.show();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
