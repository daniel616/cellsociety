package Simulations;

import java.util.ArrayList;	
import java.util.List;
import java.util.Map;
import java.util.Random;

import BackEndGrid.BackEndGrid;
import BackEndGrid.SquareEightNeighborGrid;
import Cells.Cell;
import Cells.SegregationCell;

/**
 * @author Daniel
 * Shows how individuals interact if they prefer to be with some minimum number of entities of their own kind. 
 * SegregationCells teleport to empty space if there are not a large enough fraction of people around them of their own kind.
 * Eventually, reaches equilibrium in segregated state.
 */
public class Segregation extends Simulation {
	private final String EMPTY = "empty"; // duplicated code
	private final String TYPE1 = "type1";
	private final String TYPE2 = "type2";
	private double satisfactionRequirement;

	public Segregation(Map<String,Double> parameters,Map<int[],String> cells, String simTile) {
		super(parameters,cells, simTile);
		this.satisfactionRequirement = parameters.get("satisfactionRequirement");
	}

	/**
	 * For each nonempty cell, calculate whether it's satisfied, and teleport to an empty spot if not.
	 */
	@Override
	public void update() {
		List<SegregationCell> occupiedCells = new ArrayList<>();
		List<SegregationCell> emptyCells = new ArrayList<>();
		for (int i = 0; i < getMyGrid().getRows(); i++) {
			for (int j = 0; j < getMyGrid().getColumns(); j++) {
				SegregationCell cell = (SegregationCell) getMyGrid().tryGetCell(i, j);
				if (cell.getState().equals(EMPTY)) {
					emptyCells.add(cell);
				} else {
					occupiedCells.add(cell);
				}
			}
		}

		for (SegregationCell cell : occupiedCells) {
			List<Cell> cellNeighbors = getMyGrid().getNeighbors(cell.getRow(), cell.getCol());
			List<Cell> emptyNeighbors = getStateSpecificSubset(cellNeighbors, EMPTY);
			List<Cell> similarNeighbors = getStateSpecificSubset(cellNeighbors, cell.getState());
			double occupiedNeighbors = cellNeighbors.size() - emptyNeighbors.size();
			double numberOfSimilarNeighbors = similarNeighbors.size();
			double similarityIndex = numberOfSimilarNeighbors / occupiedNeighbors;
				if (similarityIndex < satisfactionRequirement) {
				Random rn = new Random();
				int index = rn.nextInt(emptyCells.size());
				getMyGrid().switchCell(cell, emptyCells.get(index));
			}
		}
		
	}

	/**
	 * Populate simulation with differnt types
	 */
	@Override
	public void initiateSimulation() {
		for (int[] coordinates : getMyCells().keySet()) {
			String cellType = getMyCells().get(coordinates);
			SegregationCell cell = null;
			if (cellType.equals(TYPE1)) {
				cell = new SegregationCell(TYPE1);
			} else if (cellType.equals(TYPE2)) {
				cell = new SegregationCell(TYPE2);
			} else if (cellType.equals(EMPTY)) {
				cell = new SegregationCell(EMPTY);
			}
			getMyGrid().setCell(coordinates[0], coordinates[1], cell);
			cell.setRow(coordinates[0]);
			cell.setCol(coordinates[1]);
		}
	}
	
	
	public void setSatisfactionRequirement(double satisfactionRequirement) {
		this.satisfactionRequirement = satisfactionRequirement;
		getMyParameters().put("satisfactionRequirement", satisfactionRequirement);
	}

	/**
	 * Change parameters to match input parameters
	 */
	@Override
	public void updateParameters(Map<String,Double> parameters) {
		setSatisfactionRequirement(parameters.get("satisfactionRequirement"));
	}

	@Override
	public List<Integer> getSimState() {
		List<Integer> state = new ArrayList<Integer>();
		return state;
	}

	@Override
	public BackEndGrid getSquareGrid() {
		return new SquareEightNeighborGrid(getGridSize());
	}

}