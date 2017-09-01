package UI;

import java.util.ResourceBundle;

import CellSociety.CellSocietyView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Abstract class that divides UIs between what can be changed and what can be
 * read
 * 
 * @author Moses Wayne
 *
 */
public abstract class UserInterface {
	public static final int BUTTON_SPACING = 10;
	public static final Color DEFAULT_COLOR = Color.WHITE;
	public static final double GRID_SIZE_RATIO = .65;

	private CellSocietyView myCellSociety;
	private Stage myStage;
	private BorderPane myScreen;
	private Button startButton;
	private Button stepButton;
	private Button stopButton;
	private Button resetButton;
	private Button graphButton;
	private Slider mySpeedSlider;
	private boolean runSimulation;
	private Scene myScene;
	private Scene backScene;
	private ResourceBundle myResources;
	private double myWidth;
	private double myHeight;
	private String cssResource;

	public UserInterface(Stage mainStage, String resources, Scene previousScene, String css) {
		cssResource = css;
		myResources = ResourceBundle.getBundle(resources);
		backScene = previousScene;
		myStage = mainStage;
		myScreen = new BorderPane();
		myScreen.setTop(setUpToolBar());
		myScreen.setLeft(setControlPanel());
		enableButtons();
	}

	/**
	 * sets up the tool bar of the simulation
	 * 
	 * @return the toolbar
	 */
	private Node setUpToolBar() {
		HBox toolBar = new HBox();
		toolBar.setAlignment(Pos.TOP_RIGHT);
		Button returnToMainMenu = new Button(myResources.getString("ReturnMainMenu"));
		returnToMainMenu.setOnAction(e -> goBackToMenu());
		Button xmlSaver = new Button(myResources.getString("xmlSave"));
		xmlSaver.setOnAction(e -> xmlSaver());
		toolBar.getChildren().addAll(returnToMainMenu, xmlSaver);
		extendToolBar(toolBar);
		return toolBar;
	}

	/**
	 * allows UI specific functionality in the toolbar, such as choosing the
	 * simulation tiles
	 * 
	 * @param toolBar
	 *            the toolbar to be added to
	 */
	public abstract void extendToolBar(Pane toolBar);

	/**
	 * sets up the start stop step functions
	 * 
	 * @return controlpanel to be added to the gridpane
	 */
	private Node setControlPanel() {
		VBox controlPanel = new VBox();
		controlPanel.setAlignment(Pos.CENTER_LEFT);
		controlPanel.setSpacing(BUTTON_SPACING);
		initiateControlButtons();
		Text sliderLabel = new Text(myResources.getString("SpeedLabel"));
		controlPanel.getChildren().addAll(startButton, stepButton, stopButton, resetButton, sliderLabel, mySpeedSlider,
				graphButton);
		return controlPanel;
	}

	/**
	 * initiates the buttons and sliders used by the control panel
	 */
	private void initiateControlButtons() {
		startButton = initiateButton(myResources.getString("StartButton"), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				runSimulation = true;
				enableButtons();
				myCellSociety.startOp();
			}
		});
		stopButton = initiateButton(myResources.getString("StopButton"), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				runSimulation = false;
				enableButtons();
				myCellSociety.stopOp();
			}
		});
		stepButton = initiateButton(myResources.getString("StepButton"), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				myCellSociety.getNextFrame();
			}
		});
		resetButton = initiateButton(myResources.getString("ResetButton"), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				runSimulation = false;
				myCellSociety.stopOp();
				getNewSimulation();
				enableButtons();
			}
		});
		mySpeedSlider = new Slider(0, 5, 1);
		mySpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				myCellSociety.setRate(newValue.doubleValue());
			}

		});
		mySpeedSlider.setOrientation(Orientation.VERTICAL);
		graphButton = new Button(myResources.getString("graph"));
		graphButton.setOnAction(e -> graphCommand());
	}

	/**
	 * sets a generic method to prevent repeated code in the making of buttons
	 * 
	 * @param resourceString
	 *            string read from the resource file
	 * @param handler
	 *            the specific event handler for a simulation
	 * @return
	 */
	private Button initiateButton(String resourceString, EventHandler<ActionEvent> handler) {
		Button controlButton = new Button(resourceString);
		controlButton.setOnAction(handler);
		controlButton.setMaxWidth(Double.MAX_VALUE);
		return controlButton;
	}

	/**
	 * event handler for the functionality of graphing the simulation's history
	 */
	private void graphCommand() {
		NumberAxis x = new NumberAxis(0, myCellSociety.getSimHistory().get(0).size(), 1);
		NumberAxis y = new NumberAxis(0, myCellSociety.getMaxSimVal(), 1);
		ScatterChart<Number, Number> myScatter = new ScatterChart<Number, Number>(x, y);
		for (int i = 0; i < myCellSociety.getSimHistory().size(); i++) {
			XYChart.Series mySeries = new XYChart.Series();
			for (int j = 0; j < myCellSociety.getSimHistory().get(i).size(); j++) {
				mySeries.getData().add(new XYChart.Data(j, myCellSociety.getSimHistory().get(i).get(j)));
			}
			myScatter.getData().add(mySeries);
		}
		Group graphGroup = new Group();
		graphGroup.getChildren().add(myScatter);
		Scene graphScene = new Scene(graphGroup);
		Stage popUp = new Stage();
		popUp.setScene(graphScene);
		popUp.show();
	}

	/**
	 * abstract method to save the state of the current simulation
	 */
	public abstract void xmlSaver();

	/**
	 * abstract method to implement different ways of initializing a new simualtion
	 */
	public abstract void getNewSimulation();

	/**
	 * getter method to access myCellSociety
	 * @return myCellSociety
	 */
	protected CellSocietyView getCellSociety() {
		return myCellSociety;
	}

	/**
	 * setter method for the subclasses to alter the parent class' cellsociety
	 * @param newView new simulation instance to use
	 */
	public void setCellSociety(CellSocietyView newView) {
		myCellSociety = newView;
		enableButtons();
	}

	/**
	 * sets the center node of the gridpane
	 * @param gridNode node to be set
	 */
	public void setCenterNode(Node gridNode) {
		myScreen.setCenter(gridNode);
	}

	/**
	 * sets the bottom node of the gridpane
	 * @param gridNode node to be set
	 */
	public void setBottomPane(Node gridNode) {
		myScreen.setBottom(gridNode);
	}

	/**
	 * getter method to retrieve the stage of the program
	 * @return myStage
	 */
	public Stage getStage() {
		return myStage;
	}

	/**
	 * getter method to return the resource variation of the string of an object
	 * @param resourceKey
	 * @return
	 */
	public String getResources(String resourceKey) {
		return myResources.getString(resourceKey);
	}

	public double getWidth() {
		return myWidth;
	}

	public double getHeight() {
		return myHeight;
	}

	/**
	 * allows the setting of the the scene to the stage
	 * @param screenWidth default screen width from Main
	 * @param screenHeight default screen height from Main
	 */
	public void setUIScreen(double screenWidth, double screenHeight) {
		myWidth = screenWidth;
		myHeight = screenHeight;
		myScene = new Scene(myScreen, myWidth, myHeight);
		myScene.getStylesheets().add(cssResource);
		myStage.setScene(myScene);
	}

	/**
	 * returns to the main menu
	 */
	private void goBackToMenu() {
		myStage.setScene(backScene);
	}

	/**
	 * button enabler to prevent pressing buttons prior to the start of the simualtion
	 */
	private void enableButtons() {
		startButton.setDisable(myCellSociety == null || runSimulation);
		stepButton.setDisable(myCellSociety == null);
		stopButton.setDisable(myCellSociety == null || !runSimulation);
		resetButton.setDisable(myCellSociety == null);
		graphButton.setDisable(myCellSociety == null);
	}
}
