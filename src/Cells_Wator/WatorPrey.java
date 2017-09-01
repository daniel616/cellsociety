package Cells_Wator;

import javafx.scene.paint.Color;
/**
 * 
 * @author Daniel
 * Doesn't need to store much information aside from its color.
 */
public class WatorPrey extends WatorCreature{
	private final Color PREY_COLOR=Color.YELLOW;
	
	public WatorPrey(){
		super();
		setColor(PREY_COLOR);
		setState("WatorPrey");
	}
	
	@Override
	public WatorPrey makeChild(){
		return new WatorPrey();
	}

}
