package Simulations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import BackEndGrid.BackEndGrid;
import BackEndGrid.SquareEightNeighborGrid;
import Cells.Cell;
import Cells.GameOfLifeCell;

/**
 * @author sarahzhou
 * Conway's Game of Life simulation that extends basical abstract Simulation class
 */
/**
 * @author sarahzhou
 *
 */
public class GameOfLife extends Simulation{
	
	private static final String ALIVE = "alive";
	private static final String DEAD = "dead";

	private int numDead; 
	
	/**
	 * In addition to Simulation superclass's constructor, also intitializes number of dead cells
	 * @param parameters
	 * @param cells
	 * @param simTile
	 */
	public GameOfLife(Map<String,Double> parameters,Map<int[],String> cells, String simTile) {
		super(parameters,cells,simTile);
		numDead = 0;
	}
	
	@Override
	public void update(){
		for (int i=0; i<getGridSize();i++) {
			for (int j = 0; j<getGridSize();j++) {
				applyRulesToCell(i,j);
			}
		}
	}
	
	
	/**
	 * Apply Game of Life rules to cell
	 * @param row
	 * @param col
	 */
	private void applyRulesToCell(int row, int col) {
		Cell cell = getMyGrid().tryGetCell(row, col);
		List<Cell> neighbors = getMyGrid().getNeighbors(row, col);
		int numAliveNeighbors=0;
		for (Cell gameCell : neighbors) {
			if (gameCell.getState().equals(ALIVE)) {
				numAliveNeighbors++;
			}
		}
		if (cell.getState().equals(ALIVE)) {
			if (numAliveNeighbors<2||numAliveNeighbors>3) {
				cell.updateState(DEAD);
				numDead++;
				return;
			}
		} else if (cell.getState().equals(DEAD)) {
			if (numAliveNeighbors==3) {
				cell.updateState(ALIVE);
				numDead--;
				return;
			}
		}
	}

	@Override
	public void initiateSimulation() {
		for (int[] coordinates : getMyCells().keySet()) {
			String cellType = getMyCells().get(coordinates);
			if (cellType.equals(DEAD)) numDead++;
			GameOfLifeCell cell = new GameOfLifeCell(cellType);
			getMyGrid().setCell(coordinates[0],coordinates[1],cell);
		}
	}

	public int getNumDead() {
		return numDead;
	}
	
	public int getNumAlive() {
		return getGridSize()*getGridSize()-numDead;
	}

	@Override
	public void updateParameters(Map<String,Double> parameters) {
		
	}

	@Override
	public List<Integer> getSimState() {
		List<Integer> state = new ArrayList<Integer>();
		state.add(getNumDead());
		state.add(getNumAlive());
		return state;
	}

	@Override
	public BackEndGrid getSquareGrid() {
		return new SquareEightNeighborGrid(getGridSize());
	}

}