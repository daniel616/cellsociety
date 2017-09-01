package Cells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Simulations.AntForage.Ant;
import javafx.scene.paint.Color;

/**
 * @author sarahzhou
 *Cell for the Ant Foraging Simulation that extends basic abstract CA cell
 */
public class AntForageCell extends Cell {
	
	private static final String EMPTY = "empty";
	private static final String FOOD = "food";
	private static final String NEST = "nest";
	
	private double foodPheromoneLevel;
	private double nestPheromoneLevel;
	
	private List<Ant> ants;
	private List<Ant> antsToAdd;
	private List<Ant> antsToDelete;

	private double maxPheromoneLevel = 100;
	
	public AntForageCell(String state) {
		super(state);
		ants = new ArrayList<Ant>();
		antsToAdd = new ArrayList<Ant>();
		antsToDelete = new ArrayList<Ant>();
	}

	@Override
	public void updateState(String newState) {
		int pheromoneLevel = 255- (int) (foodPheromoneLevel+nestPheromoneLevel);
		if (newState.equals(EMPTY)) {
			setState(EMPTY);
			setColor(Color.rgb(pheromoneLevel, pheromoneLevel, pheromoneLevel));
			
		} else if (newState.equals(FOOD)) {
			setState(FOOD);
			setColor(Color.RED);
		} else if (newState.equals(NEST)) {
			setState(NEST);
			setColor(Color.GREEN);
		} 
	}

	
	public int getNumAnts() {
		return ants.size();
	}

	public void updateAntLists() {
		ants.removeAll(antsToDelete);
		ants.addAll(antsToAdd);
		antsToAdd.clear();
		antsToDelete.clear();
	}
	
	public List<Ant> getAntList() {
		return ants;
	}
	

	public void deleteAnt(Ant ant) {
		antsToDelete.add(ant);
	}
	
	public void addAnt(Ant ant) {
		antsToAdd.add(ant);
	}
	
	public double getFoodPheromoneLevel() {
		return foodPheromoneLevel;
	}

	public void setFoodPheromoneLevel(double foodPheromoneLevel) {
		if (foodPheromoneLevel>=maxPheromoneLevel) {
			this.foodPheromoneLevel = maxPheromoneLevel;
		} 
		else if (foodPheromoneLevel<0) {
			this.foodPheromoneLevel=0;
		} else {
			this.foodPheromoneLevel = foodPheromoneLevel;
		}
		
	}

	public double getNestPheromoneLevel() {
		return nestPheromoneLevel;
	}

	public void setNestPheromoneLevel(double nestPheromoneLevel) {
		if (nestPheromoneLevel>=maxPheromoneLevel) {
			this.nestPheromoneLevel = maxPheromoneLevel;
			
		} 
		else if (nestPheromoneLevel<0) {
			this.nestPheromoneLevel=0;
		} else {
			this.nestPheromoneLevel = nestPheromoneLevel;
		}
		
	}

	public double getMaxPheromoneLevel() {
		return maxPheromoneLevel;
	}

	public void setMaxPheromoneLevel(double maxPheromoneLevel) {
		this.maxPheromoneLevel = maxPheromoneLevel;
	}




}
