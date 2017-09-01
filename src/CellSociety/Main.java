package CellSociety;

import UI.MainScreen;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Initializer class for the application
 * @author Moses Wayne
 *
 */
public class Main extends Application {

	public static final double APPLICATION_HEIGHT = 700;
	public static final double APPLICATION_WIDTH = 700;

	private MainScreen myScreen;
	private String[] mySims = {"Ant Forage", "Fire", "Game of Life", "Predator Prey", "Segregation"};

	@Override
	public void start(Stage primaryStage) throws Exception {
		myScreen = new MainScreen(primaryStage, mySims, APPLICATION_HEIGHT, APPLICATION_WIDTH);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
