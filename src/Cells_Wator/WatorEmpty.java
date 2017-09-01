package Cells_Wator;

import javafx.scene.paint.Color;
import Cells.Cell;
/**
 * 
 * @author Daniel
 * Saves Daniel the trouble of having to deal with null cells in his grid.
 */
public class WatorEmpty extends Cell{
	private Color WATOREMPTY_COLOR=Color.BLUE;
	
	public WatorEmpty(){
		setColor(WATOREMPTY_COLOR);
		setState("empty");
	}
	
	@Override
	public void updateState(String newState) {
		// TODO Auto-generated method stub
		
	}

}
