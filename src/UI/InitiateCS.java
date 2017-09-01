package UI;

import java.util.Map;

import CellSociety.CellSocietyView;
import Simulations.AntForage;
import Simulations.Fire;
import Simulations.GameOfLife;
import Simulations.PredatorPrey;
import Simulations.Segregation;
import Simulations.Simulation;
import UI_FrontEndGrid.BaseFrontEndGrid;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * class to initialize a simulation based on parameters passed by the UI
 * @author moses
 *
 */
public class InitiateCS {

	private Simulation mySimulation;
	private BaseFrontEndGrid myDisplay;
	private Color myColor;
	private double myWidth;
	private double myHeight;
	private String myTileType;
	
	public InitiateCS(String sim, Map<String,Double> params, Map<int[], String> cells, double gridWidth, double gridHeight, String simTile, Color backColor) {
		myWidth = gridWidth;
		myHeight = gridHeight;
		myColor = backColor;
		myTileType = simTile;
		instantiateSimulation(sim, params, cells);
		myDisplay = new BaseFrontEndGrid(mySimulation.getMyGrid(), myWidth, myHeight, myTileType, myColor);
	}
	/**
	 * initializes simulations based on a String tag
	 * @param simType String tag to determine simulation
	 * @param myParameters Map holding the specifications of the simulation
	 * @param myCells Map holding cell locations in the simulation
	 */
	public void instantiateSimulation(String simType, Map<String,Double> myParameters, Map<int[], String> myCells) {
		simType = simType.toLowerCase();
		if (simType.equals("fire")) {
			mySimulation = new Fire(myParameters, myCells, myTileType);
		} else if (simType.equals("gameoflife")) {
			mySimulation = new GameOfLife(myParameters, myCells, myTileType);
		} else if (simType.equals("predatorprey")) {
			mySimulation = new PredatorPrey(myParameters, myCells, myTileType);
		} else if (simType.equals("segregation")) {
			mySimulation = new Segregation(myParameters, myCells, myTileType);
		} else if (simType.equals("antforage")) {
			mySimulation = new AntForage(myParameters, myCells, myTileType);
		} else {
			System.exit(1);
		}
		mySimulation.initiateSimulation();
	}
	
	/**
	 * passes back the canvas on which the front end grid is displayed
	 * @return node of the canvas from front end grid
	 */
	public Node getGridNode() {
		myDisplay.updateGrid();
		return myDisplay.returnDisplay();
	}
	
	/**
	 * passes back the specific simulation instance to the UI
	 * @return a specific simulation instance
	 */
	public CellSocietyView getCellSociety() {
		return new CellSocietyView(mySimulation, myDisplay);
	}
}
