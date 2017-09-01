package SugarClasses;

import Cells.Cell;
import javafx.scene.paint.Color;
/*
public class SugarCell extends Cell{
	private int timeSinceGrowth;
	private int sugarCount;
	private SugarPerson myOccupant;
	
	public SugarCell(int maxSugar, int sugarRegen, int sugarGrowthInterval, int sugarCount, SugarPerson myOccupant){
		this.maxSugar=maxSugar;
		this.sugarRegen=sugarRegen;
		this.sugarGrowthInterval=sugarGrowthInterval;
		this.sugarCount=sugarCount;
		this.myOccupant=myOccupant;
	}
	
	public SugarCell(int maxSugar, int sugarRegen, int sugarGrowthInterval, int sugarCount){
		this(maxSugar,sugarRegen,sugarGrowthInterval,sugarCount,null);
	}
	
	public void adjustColor(){
		Color color = new Color(sugarCount)
	}
	
	public void periodicGrowSugar(){
		if(timeSinceGrowth==sugarGrowthInterval){
			sugarCount+=sugarRegen;
			if(sugarCount>maxSugar) sugarCount=maxSugar;
			timeSinceGrowth=0;
		}else{
			timeSinceGrowth++;
		}
	}
	
	public SugarPerson getSugarPerson(){
		return myOccupant;
	}
	
	public boolean isOccupied(){
		if(myOccupant==null) return false;
		return true;
	}
	
	public void resetSugarCount(){
		sugarCount=0;
	}
	
	public void setPerson(SugarPerson person){
		myOccupant=person;
		person.setRow(getRow());
		person.setColumn(getCol());
	}
	
	public int getSugar(){
		return sugarCount;
	}

	@Override
	public void updateState(String newState) {
		// TODO Auto-generated method stub
		
	}

	@Deprecated
	public Cell getEmptyCell() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
*/