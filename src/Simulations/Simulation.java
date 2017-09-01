package Simulations;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import BackEndGrid.BackEndGrid;
import BackEndGrid.HexagonalNeighborGrid;
import BackEndGrid.SquareEightNeighborGrid;
import BackEndGrid.TriangularNeighborGrid;
import Cells.Cell;

/**
 * @author sarahzhou
 *Abstract Simulation class that specifies behaviors, and states for a basic CA simulation
 */
public abstract class Simulation {
	
	private BackEndGrid myGrid;
	private int myGridSize;
	private Map<String,Double> myParameters;
	private Map<int[],String> myCells;
	
	
	/**
	 * Constructor for Simulation that initializes cell Hashmap, parameters Hashmap, and parameters Grid
	 * @param parameters: map of parameters
	 * @param cells: map of cells
	 * @param simTile: simulation lattice type
	 */
	public Simulation(Map<String,Double> parameters, Map<int[],String> cells, String simTile){
		myParameters = parameters;
		myCells = cells;
		myGridSize = myParameters.get("size").intValue();
		setMyGrid(simTile);
	}
	
	
	/**
	 * updates back end grid one timestep
	 */
	public abstract void update();
	
	/**
	 * initiates simulation cell configurations based on cell Map
	 */
	public abstract void initiateSimulation();
	
	/**
	 * @param cells
	 * @param state
	 * @return
	 */
	public List<Cell> getStateSpecificSubset(List<Cell> cells, String state){
		List<Cell> sublist=new ArrayList<Cell>();
		for(Cell cell:cells){
			if(cell.getState().equals(state)){
				sublist.add(cell);
			}
		}
		return sublist;
	}
	
	/**
	 * @param list
	 * @param className
	 * @return
	 */
	public List<Cell> getClassSpecificSubcells(List<Cell> list,String className){
		List<Cell> sublist=new ArrayList<>();
		Class<?> cls;
		try {
			cls = Class.forName(className);
			for(Cell item:list){
				if(cls.isInstance(item)){	
					sublist.add(item);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sublist;
	}

	public BackEndGrid getMyGrid() {
		return myGrid;
	}

	/**
	 * sets the correct type of back end grid based on gridTile
	 * @param gridTile: String indicating type of grid lattice structure
	 */
	public void setMyGrid(String gridTile) {
		if (gridTile.equals("hexagon")) {
			myGrid = new HexagonalNeighborGrid(myGridSize);
		} else if (gridTile.equals("triangle")) {
			myGrid = new TriangularNeighborGrid(myGridSize);
		} else {
			myGrid = new SquareEightNeighborGrid(myGridSize);
		}
	}

	public int getGridSize() {
		return myGridSize;
	}
	
	public Map<String, Double> getMyParameters() {
		return myParameters;
	}
	
	public Map<int[],String> getMyCells() {
		return myCells;
	}
	
	/**
	 * Updates the cell's state in cell Map
	 * @param cell: instance of cell to update
	 */
	public void updateCellInMap(Cell cell) {
		int[] coordinates = new int[2];
		coordinates[0] = cell.getRow();
		coordinates[1] = cell.getCol();
		myCells.put(coordinates, cell.getState());
	}
	
	public abstract BackEndGrid getSquareGrid();
	
	public abstract void updateParameters(Map<String,Double> parameters);

	public abstract List<Integer> getSimState();
	
}