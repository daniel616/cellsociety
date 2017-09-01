package BackEndGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Cells.Cell;
/**
 * 
 * @author Daniel
 * Returns three neighbors with getNeighbors.
 *
 */
public class TriangularNeighborGrid extends BackEndGrid{

	public TriangularNeighborGrid(int size) {
		super(size);
		// TODO Auto-generated constructor stub
	}


	@Override
	public List<Cell> getNeighbors(int row, int column) {
		// TODO Auto-generated method stub
		List<Cell> neighbors =new ArrayList<Cell>();
		neighbors.add(tryGetCell(row,column-1));
		neighbors.add(tryGetCell(row,column+1));
		if((row+column)%2==0){
			neighbors.add(tryGetCell(row-1,column));
		}else{
			neighbors.add(tryGetCell(row+1,column));
		}
		neighbors.removeAll(Collections.singleton(null));
		return neighbors;
	}

}
