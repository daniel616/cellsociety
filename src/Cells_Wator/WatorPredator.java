package Cells_Wator;

import javafx.scene.paint.Color;
/**
 * 
 * @author Daniel
 * Tracks how hungry it gets, and reproduces.
 */
public class WatorPredator extends WatorCreature{

	private final Color PREDATOR_COLOR=Color.RED;
	private int timeSinceAte=0;
	
	public WatorPredator(){
		setColor(PREDATOR_COLOR);
		setState("WatorPredator");
	}
	
	public WatorPredator makeChild(){
		return new WatorPredator();
	}
	
	public void incrementTimeSinceAte(){
		timeSinceAte++;
	}
	
	public void resetTimeSinceAte(){
		timeSinceAte=0;
	}
	
	public int getTimeSinceAte(){
		return timeSinceAte;
	}

}
