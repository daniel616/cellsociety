package BackEndGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Cells.Cell;
/**
 * 
 * @author Daniel
 * Gets the eight cells adjacent to a cell in a square configuration.
 */

public class SquareEightNeighborGrid extends BackEndGrid{
	public SquareEightNeighborGrid(int size) {
		super(size);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * This function gets the four neighbors adjacent to a cell in a square configuration.
	 * Only intended as a helper, and should not be called.
	 */
	public List<Cell> getFourNeighbors(int row, int col) {
		List<Cell> neighborList = new ArrayList<Cell>();
		if (row>0) {
			neighborList.add(tryGetCell(row-1,col));
		} if (row<getRows()-1) {
			neighborList.add(tryGetCell(row+1,col));
		} if (col>0) {
			neighborList.add(tryGetCell(row,col-1));
		} if (col<getColumns()-1) {
			neighborList.add(tryGetCell(row,col+1));
		}
		neighborList.remove(null);
		return neighborList;
	}
	
	/**
	 * Returns eight neighbors using the above helper method.
	 */
	@Override
	public List<Cell> getNeighbors(int row, int col) {
		List<Cell> neighborList = new ArrayList<Cell>();
		neighborList.addAll(getFourNeighbors(row, col));
		if (row>0&&col>0) {
			neighborList.add(tryGetCell(row-1,col-1));
		} if (row>0&&col<getColumns()-1) {
			neighborList.add(tryGetCell(row-1,col+1));
		} if (row<getRows()-1&&col>0) {
			neighborList.add(tryGetCell(row+1,col-1));
		} if (row<getRows()-1&&col<getColumns()-1) {
			neighborList.add(tryGetCell(row+1,col+1));
		}
		neighborList.removeAll(Collections.singleton(null));
		return neighborList;
	}
}
