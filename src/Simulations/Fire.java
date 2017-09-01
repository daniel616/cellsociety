package Simulations;

import java.util.ArrayList;	
import java.util.List;
import java.util.Map;

import BackEndGrid.BackEndGrid;
import BackEndGrid.SquareFourNeighborGrid;
import Cells.Cell;
import Cells.FireCell;

/**
 * @author sarahzhou
 *	Spreading of Fire Simulation that extends the basic abstract Simulation class
 */
public class Fire extends Simulation {

	private double probCatch;
	
	private static final String EMPTY = "empty";
	private static final String TREE = "tree";
	private static final String BURNING = "burning";
	
	private List<Cell> burningList;
	private List<Cell> emptyList;
	
	private int numBurning;
	private int numTrees;

	/**
	 * Constructor that initializes number of number of burning trees and non-burning trees, list of burning trees and empty trees, and sets probCatch to the value given by the Parameter map
	 * in addition to what was set by the Simulation superclass constructor.
	 * @param parameters
	 * @param cells
	 * @param simTile
	 */
	public Fire(Map<String,Double> parameters, Map<int[],String> cells, String simTile) {
		super(parameters,cells,simTile);
		probCatch = parameters.get("probCatch");
		burningList = new ArrayList<Cell>();
		emptyList = new ArrayList<Cell>();
		numTrees = getGridSize()*getGridSize()-3*getGridSize()-1;
		numBurning = 1;
	}

	@Override
	public void update() {
		if (!burningTreesLeft()) {
			return;
		}
		for (int i = 0; i<getGridSize();i++) {
			for (int j = 0; j<getGridSize();j++) {
				applyRules(i,j);
			}
		}
		updateTrees();
	}
	
	/**
	 * Returns boolean indicating whether there are any burning trees left in the grid
	 * @return boolean
	 */
	private boolean burningTreesLeft() {
		int gridSize = getGridSize();
		for (int i=0; i<gridSize;i++) {
			for (int j = 0; j<gridSize;j++) {
				if (getMyGrid().tryGetCell(i, j).getState().equals(BURNING)) {
					return true;
				}
			}
	}
		return false;
	}

	/**
	 * Apply rules of Fire simulation to the cell
	 * @param row
	 * @param col
	 */
	private void applyRules(int row, int col) {
		Cell cell = getMyGrid().tryGetCell(row, col);
		if (cell.getState().equals(TREE)) {
			if (existsBurningNeighbor(getMyGrid().getNeighbors(row,col))) {
				calculateNewStateOfTree((FireCell) cell);
			}
		} if (cell.getState().equals(BURNING)) {
			emptyList.add(cell);
		}
	}

	/**
	 * Returns boolean indicating whether there exists a burning neighbor
	 * @param neighbors: list of neighbors
	 * @return 
	 */
	private boolean existsBurningNeighbor(List<Cell> neighbors) {
		for (Cell neighborCell : neighbors) {
			if (neighborCell.getState().equals(BURNING)) {
				return true;
			} 
		}
		return false;
	}

	/**
	 * Calculates whether or not a tree catches on fire based on probCatch
	 * @param cell
	 */
	private void calculateNewStateOfTree(FireCell cell) {
		double random = Math.random();
		if (random<probCatch) {
			burningList.add(cell);
		}
	}
	
	/**
	 * Update the states of the trees
	 */
	private void updateTrees() {
		for (Cell cell : burningList) {
			cell.updateState(BURNING);
			updateCellInMap(cell);
		}
		for (Cell cell : emptyList) {
			cell.updateState(EMPTY);
			updateCellInMap(cell);
		}
		numBurning += burningList.size()-emptyList.size();
		numTrees-=burningList.size();
		burningList.clear();
		emptyList.clear();
	}
	
	public void initiateSimulation() {
		for (int[] coordinates : getMyCells().keySet()) {
			String cellType = getMyCells().get(coordinates);
			FireCell cell = new FireCell(cellType);
			getMyGrid().setCell(coordinates[0],coordinates[1],cell);
		}
	}
	
	public void setProbCatch(double probCatch) {
		this.probCatch=probCatch;
		getMyParameters().put("probCatch", probCatch);
	}

	public int getNumBurning() {
		return numBurning;
	}
	
	public int getNumTrees() {
		return numTrees;
	}

	@Override
	public void updateParameters(Map<String,Double> parameters) {
		setProbCatch(parameters.get("probCatch"));
	}
	
	public List<Integer> getSimState() {
		List<Integer> state = new ArrayList<Integer>();
		state.add(getNumBurning());
		state.add(getNumTrees());
		return state;
	}

	@Override
	public BackEndGrid getSquareGrid() {
		// TODO Auto-generated method stub
		return new SquareFourNeighborGrid(getGridSize());
	}	

}