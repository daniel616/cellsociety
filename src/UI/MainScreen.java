package UI;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * class to initialize a selection screen to determine which UI to launch
 * 
 * @author Moses Wayne
 *
 */
public class MainScreen {
	public static int BUTTON_SPACING = 10;
	public static final String RESOURCE_TAG = "resources/";

	private static final String usedResource = "English";
	private static final String MAIN_CSS = "main_screen.css";
	private static final String UI_CSS = "gui_screen.css";

	private Stage myStage;
	private ResourceBundle myR;
	private String[] mySimulations;
	private ArrayList<Button> myButtons;
	private Scene myScene;
	private HBox myHBox;
	private VBox myScreen;
	private Label myTitle;
	private double myHeight;
	private double myWidth;

	public MainScreen(Stage initStage, String[] possibleSimulations, double height, double width) {
		myStage = initStage;
		mySimulations = possibleSimulations;
		myR = ResourceBundle.getBundle(RESOURCE_TAG+usedResource);
		myScreen = new VBox();
		myHBox = new HBox(BUTTON_SPACING);
		myHBox.setAlignment(Pos.BOTTOM_CENTER);
		myTitle = new Label();
		myScreen.getChildren().addAll(myTitle, myHBox);
		myHeight = height;
		myWidth = width;
		myScene = new Scene(myScreen, myHeight, myWidth);
		myScene.getStylesheets().add(RESOURCE_TAG + MAIN_CSS);
		setUpMainScreen();
	}

	/**
	 * sets the scene of the MainScreen onto the stage
	 */
	public void setUpMainScreen() {
		myTitle.setText(myR.getString("title"));
		myTitle.setWrapText(true);
		myStage.setScene(myScene);
		this.getButtons();
		setUpHBox(0, myButtons.size()/2);
		setUpHBox(myButtons.size()/2, myButtons.size());
	}
	
	private void setUpHBox(int start, int end) {
		VBox myVBox = new VBox();
		for (int i = start; i< end; i++) {
			myVBox.getChildren().add(myButtons.get(i));
		}
		myHBox.getChildren().add(myVBox);
	}

	/**
	 * initializes buttons to be added
	 */
	private void getButtons() {
		myButtons = new ArrayList<>();
		for (String currentSimulation : mySimulations) {
			Button currentButton = new Button(currentSimulation);
			currentButton.setOnAction(e -> openUserSim(currentSimulation));
			myButtons.add(currentButton);
		}
		Button fileButton = new Button(myR.getString("fromFile"));
		fileButton.setOnAction(e -> openFileSim());
		myButtons.add(fileButton);
	}

	/**
	 * button option to access UI to open a simulation from file
	 */
	private void openFileSim() {
		UserInterface myUI = new GUIFromFile(myStage, RESOURCE_TAG + usedResource, myScene, RESOURCE_TAG + UI_CSS);
		myUI.setUIScreen(myWidth, myHeight);
	}

	/**
	 * button option to access UI to open a simulation from user controlled parameters
	 * @param simType
	 */
	private void openUserSim(String simType) {
		String mySim = simType.toLowerCase().replaceAll("\\s+", "");
		UserInterface myUI = new GUIFromUser(myStage, RESOURCE_TAG + usedResource, myScene, RESOURCE_TAG + UI_CSS,
				mySim);
		myUI.setUIScreen(myWidth, myHeight);
		myUI.getNewSimulation();
	}
}