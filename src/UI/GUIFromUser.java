package UI;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import Utils.CellsMapGenerator;
import Utils.StateSaver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class used to initialize UI based on customizable controls controlled by the
 * user. The first simulation loaded is based on default values from the
 * sliders. Parameters of the simulation dynamically change while the grid
 * specific parameters require "Reset" to be pressed to be loaded. This includes
 * the combo box to select the tile type for the simulation.
 * 
 * @author Moses Wayne
 *
 */
public class GUIFromUser extends UserInterface {
	public static final String DEFAULT_GRID_MESSAGE = "gridSize";
	public static final double DEFAULT_GRID_SIZE = 20;
	public static final double DEFAULT_EMPTY = .3;
	public static final double MIN_GRID = 5;
	public static final double MAX_GRID = 40;
	public static final double PREDPREY_MIN = 1;
	public static final double PREDPREY_MAX = 15;
	public static final double DEFAULT_TYPE_RATIO = .2;
	public static final String CATCH_PROB_STR = "probCatch";
	public static final double DEFAULT_CATCH_PROB = .2;
	public static final double DEFAULT_BURN_PROB = .1;
	public static final String STARVE_STR = "starveTime";
	public static final double DEFAULT_STARVE = 4;
	public static final String PRED_BIRTH_STR = "predatorGestationPeriod";
	public static final double DEFAULT_PRED_BIRTH = 6;
	public static final String PREY_BIRTH_STR = "preyGestationPeriod";
	public static final double DEFAULT_PREY_BIRTH = 3;
	public static final String SATIS_STR = "satisfactionRequirement";
	public static final double DEFAULT_SATIS = .1;
	public static final String METHOD_STUB = "Controls";
	public static final String[] possibleTiles = { "square", "hexagon", "triangle" };

	private String mySimType;
	private VBox myControls;
	private HBox paramSliders;
	private HBox cellSliders;
	private Map<Slider, String> mySliderValues;
	private Map<String, Double> myParameters;
	private String simTileType;

	public GUIFromUser(Stage mainStage, String resources, Scene previousScene, String css, String simType) {
		super(mainStage, resources, previousScene, css);
		mySimType = simType;
		myControls = new VBox();
		paramSliders = new HBox();
		cellSliders = new HBox();
		mySliderValues = new HashMap<Slider, String>();
		myParameters = new HashMap<String, Double>();
		simTileType = "square";
		setUserControls(simType);
		setBottomPane(myControls);
		constructNewParamSlider(DEFAULT_GRID_SIZE, MIN_GRID, MAX_GRID, "size", cellSliders);
		paramSliders.setAlignment(Pos.BOTTOM_CENTER);
		cellSliders.setAlignment(Pos.BOTTOM_CENTER);
		myControls.getChildren().addAll(paramSliders, cellSliders);
	}

	/**
	 * see {@link UserInterface#extendToolBar(Pane)}
	 */
	@Override
	public void extendToolBar(Pane toolBar) {
		ComboBox tileSelector = new ComboBox<String>();
		tileSelector.getItems().addAll(possibleTiles);
		tileSelector.setPromptText(possibleTiles[0]);
		tileSelector.valueProperty().addListener((o, old, newTile) -> changeTileType((String) newTile));
		toolBar.getChildren().add(tileSelector);
	}

	/**
	 * sets up the bottom panel to get controls for the specific simulation.
	 * Uses reflection for extendability
	 * 
	 * @param simType
	 *            String specifying the type of simulation chosen
	 */
	public void setUserControls(String simType) {
		try {
			Method constructControls = this.getClass().getMethod(simType + METHOD_STUB);
			constructControls.setAccessible(true);
			constructControls.invoke(this, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * see {@link UserInterface#getNewSimulation()}
	 */
	@Override
	public void getNewSimulation() {
		CellsMapGenerator cmg = new CellsMapGenerator(myParameters);
		Map<int[], String> cells = cmg.generateMap(mySimType);
		InitiateCS myInitializer = new InitiateCS(mySimType, myParameters, cells, GRID_SIZE_RATIO * getWidth(),
				GRID_SIZE_RATIO * getHeight(), simTileType, DEFAULT_COLOR);
		setCellSociety(myInitializer.getCellSociety());
		setCenterNode(myInitializer.getGridNode());
	}

	/**
	 * method to construct sliders for each of the parameters in a given
	 * simulation
	 * 
	 * @param defaultValue
	 *            slider's default value
	 * @param minVal
	 *            minimum slider value
	 * @param maxVal
	 *            maximum slider value
	 * @param tagString
	 *            parameter tag that this slider changes
	 * @param box
	 *            pane to add sliders to
	 */
	private void constructNewParamSlider(double defaultValue, double minVal, double maxVal, String tagString,
			Pane box) {
		Label sliderLabel = new Label(getResources(tagString));
		Slider currSlider = new Slider(minVal, maxVal, defaultValue);
		currSlider.valueProperty().addListener(new CellListener(currSlider));
		mySliderValues.put(currSlider, tagString);
		myParameters.put(tagString, defaultValue);
		VBox labelHolder = new VBox();
		labelHolder.getChildren().addAll(sliderLabel, currSlider);
		box.getChildren().add(labelHolder);
	}

	/**
	 * method to alter the simulation's tile specification on the next launch
	 * 
	 * @param newTile
	 *            String representing tile's new specification
	 */
	private void changeTileType(String newTile) {
		simTileType = newTile;
	}

	/**
	 * method to dynamically change the simulation's parameters during the
	 * simulation
	 */
	public void updateSimulationParameters() {
		getCellSociety().updateParams(myParameters);
	}

	/**
	 * unimplemented control class because AntForager was not fully implemented.
	 * Method stub for future use.
	 */
	public void antforagerControls() {

	}

	/**
	 * sets UI controls for the fire simulation
	 */
	public void fireControls() {
		constructNewParamSlider(DEFAULT_CATCH_PROB, 0, 1, CATCH_PROB_STR, paramSliders);
		constructNewParamSlider(DEFAULT_BURN_PROB, 0, 1, "probBurning", cellSliders);
	}

	/**
	 * sets UI controls for the Game of Life simulation
	 */
	public void gameoflifeControls() {
		constructNewParamSlider(DEFAULT_EMPTY, 0, 1, "empty", cellSliders);
	}

	/**
	 * sets UI controls for the Wator simulation
	 */
	public void predatorpreyControls() {
		constructNewParamSlider(DEFAULT_STARVE, PREDPREY_MIN, PREDPREY_MAX, STARVE_STR, paramSliders);
		constructNewParamSlider(DEFAULT_PRED_BIRTH, PREDPREY_MIN, PREDPREY_MAX, PRED_BIRTH_STR, paramSliders);
		constructNewParamSlider(DEFAULT_PREY_BIRTH, PREDPREY_MIN, PREDPREY_MAX, PREY_BIRTH_STR, paramSliders);
		constructNewParamSlider(DEFAULT_TYPE_RATIO, 0, 1, "predPreyRatio", cellSliders);
		constructNewParamSlider(DEFAULT_EMPTY, 0, 1, "empty", cellSliders);
	}

	/**
	 * sets UI controls for the Segregation simulation
	 */
	public void segregationControls() {
		constructNewParamSlider(DEFAULT_SATIS, 0, 1, SATIS_STR, paramSliders);
		constructNewParamSlider(DEFAULT_TYPE_RATIO, 0, 1, "redBlueRatio", cellSliders);
		constructNewParamSlider(DEFAULT_EMPTY, 0, 1, "empty", cellSliders);
	}

	/**
	 * custom listener class to specifically change map values, kept vague to
	 * allow for use by multiple sliders
	 */
	public class CellListener implements ChangeListener<Number> {
		private Slider mySlider;

		public CellListener(Slider currSlider) {
			mySlider = currSlider;
		}

		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			myParameters.put(mySliderValues.get(mySlider), (Double) newValue);
			updateSimulationParameters();
		}
	}

	/**
	 * see {@link UserInterface#xmlSaver()}
	 */
	@Override
	public void xmlSaver() {
		try {
			StateSaver myState = new StateSaver("empty", mySimType, new HashMap<String, Double>(),
					new HashMap<int[], String>());
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
