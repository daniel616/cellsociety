package BackEndGrid;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Cells.Cell;
/**
 * 
 * @author Daniel
 * Base class that is used to store cell elements. Stores cells in a 2D array. 
 * Subclasses implement different getNeighbors functions.
 * Can implement toiroidal configurations.
 */
public abstract class BackEndGrid {
	private Cell[][] myCellGrid;
	private int maxColumns;
	private int maxRows;
	private final int GROWTH_RANGE=5;
	private boolean infinite=false;
	private boolean toiroidal;

	public BackEndGrid(int size) {
		myCellGrid = new Cell[size][size];
		//myFileReader = new FileReader();//commented this because it caused compile errors, but may be put back in later
		this.maxColumns=size;
		this.maxRows=size;
	}
	//returns null and no exceptions
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 * A helper method used for the getNeighbors list, that checks if a cell is reachable.
	 * If not, a null element is added to list, which is removed at the end of the getNeighbors function.
	 */
	public Cell tryGetCell(int x,int y){
		if(toiroidal) return myCellGrid[x%maxRows][y%maxColumns];
		if(x>=0&&x<maxRows&&y>=0&&y<maxColumns) return myCellGrid[x][y];
		return null;
	}
	//exceptions can occur
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 * The method that can cause exceptions with incorrect, and should be used by other classes.
	 */
	public Cell getCell(int x,int y){
		if(toiroidal) return myCellGrid[x%maxRows][y%maxColumns];
		else return myCellGrid[x][y];
	}
	
	public int getRows(){
		return maxRows;
	}
	
	public int getColumns(){
		return maxColumns;
	}

	
	public void switchCell(Cell cell1, Cell cell2){
		int cell2Row=cell2.getRow();
		int cell2Col=cell2.getCol();
		
		setCell(cell1.getRow(), cell1.getCol(), cell2);
		setCell(cell2Row, cell2Col, cell1);
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @param cell
	 * Sets a cell at given location.
	 */
	public void setCell(int row, int col, Cell cell) {
		myCellGrid[row][col] = cell;
		cell.setCol(col);
		cell.setRow(row);
	}
	
	//to be overriden depending on each simulation
	public List<Cell> getNeighbors(int row, int column){
		return null;
	}
}
