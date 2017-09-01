package Cells;

import javafx.scene.paint.Color;


/**
 * @author sarahzhou
 *Cell abstract class that specifies behaviors, and states for a basic CA cell
 */
public abstract class Cell {
	private String myState;
	private Color myColor; 
	private int row;
	private int col;
	private boolean makesWorldExpand=true;
	
	/**
	 * empty constructor for cells without multiple states
	 */
	public Cell() {
	}
	
	/**
	 * constructor that takes in, and sets a state
	 * @param state: cell's state
	 */
	public Cell(String state) {
		updateState(state);
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	/**
	 * Abstract method that subclasses must override to update specifics about their cells when state is updated
	 * @param newState
	 */
	public abstract void updateState(String newState);
	
	public Color getColor() {
		return myColor;
	}
	
	public String getState(){
		return myState;
	}
	
	public void setColor(Color color) {
		myColor = color;
	}
	
	public void setState(String state) {
		myState = state;
	}
}
