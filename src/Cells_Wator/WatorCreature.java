package Cells_Wator;

import Cells.Cell;

/**
<<<<<<< HEAD
 * 
=======
>>>>>>> 569237353f264f65a1b45f9535ed7c7a66acfc12
 * @author Daniel
 * A superclass for WatorPredator and WatorPrey. Allows them to share reproduction functionalities.
 */
public abstract class WatorCreature extends Cell{
	private int timeSinceBirth=0;//time since it last made a child or was born
	private int gestationPeriod;//time required to make a child

	@Override
	public void updateState(String newState) {
		// TODO Auto-generated method stub
		
	}
	
	public void resetTimeSinceBirth(){
		timeSinceBirth=0;
	}
	
	public int getTimeSinceBirth(){
		return timeSinceBirth;
	}
	
	public void incrementTimeSinceBirth(){
		timeSinceBirth++;
	}
	/**
	 * 
	 * @return
	 * Made abstract so that Prey and Predator can add instances of their own class.
	 */
	public abstract WatorCreature makeChild();
}
