package UI;

import java.io.File;
import java.util.Map;

import javax.xml.transform.TransformerException;

import Utils.ParameterParser;
import Utils.StateSaver;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Class used to initialize the UI display to allow reading in a simulation from
 * a file. Locks out altering controls and specifications of the simulation to
 * prevent tampering with the file's original parameters.
 * 
 * @author Moses Wayne
 *
 */

public class GUIFromFile extends UserInterface {
	private String mySimType;
	private Map<String, Double> mySimParameters;
	private Map<int[], String> mySimCells;
	private FileChooser fileBrowse;
	private String mySimTile;

	public GUIFromFile(Stage mainStage, String resources, Scene previousScene, String css) {
		super(mainStage, resources, previousScene, css);
		fileBrowse = new FileChooser();
	}

	/**
	 * opens file dialog to choose file to be read in, catches general
	 * exceptions in the reading and parsing of the file
	 */
	private void openFileBrowser() {
		File readFile = fileBrowse.showOpenDialog(getStage());
		try {
			ParameterParser myParameterParser = new ParameterParser(readFile);
			mySimType = myParameterParser.getSimType();
			mySimParameters = myParameterParser.getParameters();
			mySimCells = myParameterParser.getCells();
			mySimTile = myParameterParser.getLatticeType();
			getNewSimulation();
		} catch (Exception e) {
			Alert fileAlert = new Alert(AlertType.ERROR);
			fileAlert.setHeaderText("File Error");
			fileAlert.setContentText("The file has invalid inputs");
			fileAlert.showAndWait();
		}
	}

	/**
	 * see {@link UserInterface#extendToolBar(Pane)} 
	 */
	@Override
	public void extendToolBar(Pane toolBar) {
		Button openFileButton = new Button(getResources("OpenFile"));
		openFileButton.setOnAction(e -> openFileBrowser());
		toolBar.getChildren().add(openFileButton);
	}

	/**
	 * see {@link UserInterface#getNewSimulation()}
	 */
	@Override
	public void getNewSimulation() {
		InitiateCS myInitializer = new InitiateCS(mySimType, mySimParameters, mySimCells, GRID_SIZE_RATIO * getWidth(),
				GRID_SIZE_RATIO * getHeight(), mySimTile, DEFAULT_COLOR);
		setCellSociety(myInitializer.getCellSociety());
		setCenterNode(myInitializer.getGridNode());
	}

	/**
	 * see {@link UserInterface#xmlSaver()}
	 */
	@Override
	public void xmlSaver() {
		try {
			StateSaver myState = new StateSaver("empty", mySimType, mySimParameters, mySimCells);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}