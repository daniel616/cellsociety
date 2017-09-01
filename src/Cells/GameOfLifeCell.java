package Cells;

import javafx.scene.paint.Color;

/**
 * @author sarahzhou
 * Cell for Game of Life simulation that extends basic abstract CA cell
 */
public class GameOfLifeCell extends Cell {
	private static final String ALIVE = "alive";
	private static final String DEAD = "dead";
	
	public GameOfLifeCell(String state) {
		super(state);
	}
	
	@Override
	public void updateState(String newState) {
		setState(newState);
		if (newState.equals(ALIVE)) {
			setColor(Color.BLACK);
		} else if (newState.equals(DEAD)) {
			setColor(Color.GREEN);
		}
	}
}
