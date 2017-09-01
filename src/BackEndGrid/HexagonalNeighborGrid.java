package BackEndGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Cells.Cell;

/**
 * 
 * @author Daniel
 * Gets neighbors in a a hexagonal configuration. 
 */
public class HexagonalNeighborGrid extends BackEndGrid{
	public HexagonalNeighborGrid(int size) {
		super(size);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	@Override
	public List<Cell> getNeighbors(int row, int column) {
		// TODO Auto-generated method stub
		List<Cell> neighbors=new ArrayList<Cell>();
		neighbors.add(tryGetCell(row-2,column));
		neighbors.add(tryGetCell(row+2,column));
		if(row%2==0){
			neighbors.add(tryGetCell(row-1,column-1));
			neighbors.add(tryGetCell(row+1,column-1));	
			neighbors.add(tryGetCell(row-1,column));
			neighbors.add(tryGetCell(row+1,column));
		}
		if(row%2==1){
			neighbors.add(tryGetCell(row-1,column));
			neighbors.add(tryGetCell(row+1,column));
			neighbors.add(tryGetCell(row-1,column+1));
			neighbors.add(tryGetCell(row+1,column+1));
		}
		neighbors.removeAll(Collections.singleton(null));
		return neighbors;
	}
}
