package Cells;

import javafx.scene.paint.Color;

/**
 * @author sarahzhou
 *	Cell for Fire Simulation that extends the abstract basic CA cell
 */
public class FireCell extends Cell {
	
	private static final String EMPTY = "empty";
	private static final String TREE = "tree";
	private static final String BURNING = "burning";
	
	public FireCell(String state) {
		super(state);
	}
	
	@Override
	public void updateState(String newState) {
		if (newState.equals(EMPTY)) {
			setState(EMPTY);
			setColor(Color.YELLOW);
		} else if (newState.equals(TREE)) {
			setState(TREE);
			setColor(Color.GREEN);
		} else if (newState.equals(BURNING)) {
			setState(BURNING);
			setColor(Color.RED);
		}
		
	}
}
