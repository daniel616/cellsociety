package Simulations;

import java.util.List;
import java.util.Map;

import BackEndGrid.BackEndGrid;
import Cells.AntForageCell;
import Cells.Cell;

/**
 * @author sarahzhou
 *	Attempt at Ant Foraging simulation
 */
public class AntForage extends Simulation{
	
	private double evaporationRatio;
	private double diffusionRatio;
	private double antsBornPerTimeStep;
	private double maxAntsPerLoc;
	private double lifeTimePerAnt;
	private double maxAntsInSimulation;
	private double numInitialAntsAtNest;
	
	private static final String EMPTY = "empty";
	private static final String FOOD = "food";
	private static final String NEST = "nest";
	
	private static final int MAX_HORMONE_LEVEL = 100;
	

	public class Ant {
		private boolean hasFood;
		private int daysLived;
		public Ant() {
			hasFood =false;
			daysLived = 0;
		}
		public boolean hasFood() {
			return hasFood;
		}
		
		public void dropFood() {
			this.hasFood =false;
		}
		
		public void grabFood() {
			this.hasFood =true;
		}
		
		public int getDaysLived() {
			return daysLived;
		}
		
		public void incrementDaysLived() {
			daysLived++;
		}
	}
	
	public AntForage(Map<String,Double> parameters, Map<int[],String> cells, String simTile) {
		super(parameters, cells, simTile);
		evaporationRatio = parameters.get("evaporationRatio");
		diffusionRatio = parameters.get("diffusionRatio");
		antsBornPerTimeStep = parameters.get("antsBornPerTimeStep");
		maxAntsPerLoc = parameters.get("maxAntsPerLoc");
		lifeTimePerAnt = parameters.get("lifeTimePerAnt");
		maxAntsInSimulation = parameters.get("maxAntsInSimulation");
		numInitialAntsAtNest = parameters.get("numInitialAntsAtNest");
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		for (int i=0;i<getMyGrid().getRows();i++) {
			for (int j = 0; j<getMyGrid().getColumns();j++) {
				
				AntForageCell cell = (AntForageCell) getMyGrid().tryGetCell(i, j);
				
				if (cell.getState().equals(NEST)) {
					if (cell.getNumAnts()>0) dropPheromonesAtNest(cell);
					for (int k =0;k<antsBornPerTimeStep;k++) {
						if (cell.getNumAnts()<maxAntsPerLoc) {
							cell.addAnt(new Ant());
						}
					}
					
					for (Ant ant :cell.getAntList()) {
						moveAnt(ant, i, j);
					}
				} 
				
				else if (cell.getState().equals(FOOD)) {
					if (cell.getNumAnts()>0) dropPheromonesAtFood(cell);
					for (Ant ant :cell.getAntList()) {
						if (ant.getDaysLived()==lifeTimePerAnt) cell.deleteAnt(ant);
						else {
							ant.grabFood();
							ant.incrementDaysLived();
						}
						moveAnt(ant,i,j);
						
					}
				} 
				
				else if (cell.getState().equals(EMPTY)) {
					for (Ant ant : cell.getAntList()) {
						if (ant.getDaysLived()==lifeTimePerAnt) {
							cell.deleteAnt(ant);
							continue;
						}
						moveAnt(ant,i,j);
					}
				}
				cell.updateAntLists();
				diffuseAndEvaporate(i,j);
				
			}
		}
		
		updateAllCells();
	}
	private void moveAnt(Ant ant,int row, int col) {
		AntForageCell cell = (AntForageCell) getMyGrid().tryGetCell(row, col);
			AntForageCell nestMaxNeighbor = getMaxHormoneNeighbor(row,col,NEST);
			AntForageCell foodMaxNeighbor = getMaxHormoneNeighbor(row,col,FOOD);
			AntForageCell toGo;
			if (ant.hasFood()) {
				toGo = nestMaxNeighbor;
				dropPheromones(cell,foodMaxNeighbor.getFoodPheromoneLevel(),FOOD);
				System.out.println("food pher"+foodMaxNeighbor.getFoodPheromoneLevel());
				if (toGo.getState().equals(NEST)) {
					ant.dropFood();
				}
			} else {
				toGo = foodMaxNeighbor;
				dropPheromones(cell,nestMaxNeighbor.getNestPheromoneLevel(),NEST);
				System.out.println("nest pher"+nestMaxNeighbor.getNestPheromoneLevel());
			}
			if (toGo.getNumAnts()<maxAntsPerLoc) {
				toGo.addAnt(ant);
				cell.deleteAnt(ant);
			}
			ant.incrementDaysLived();
		
	}
	
	private AntForageCell getMaxHormoneNeighbor(int row, int col,String pheromoneType) {
		List<Cell> neighbors = getMyGrid().getNeighbors(row, col);
		double maxFoodPheromoneLevel = 0;
		AntForageCell max=null;
		for (Cell neighbor : neighbors) {
			AntForageCell neighborCell = (AntForageCell) neighbor;
			double level =0;
			if (pheromoneType.equals(FOOD)) {
				level = neighborCell.getFoodPheromoneLevel();
			} else if (pheromoneType.equals(NEST)) {
				level = neighborCell.getNestPheromoneLevel();
			}
			if (level>maxFoodPheromoneLevel) {
				maxFoodPheromoneLevel=level;
				max = neighborCell;
			}
		}
		if (max==null) {
			double rand = Math.random()*neighbors.size();
			max = (AntForageCell) neighbors.get((int)rand);
		}
		return max;
		
	}
	
	private void diffuseAndEvaporate(int row, int col) {
		List<Cell> neighbors = getMyGrid().getNeighbors(row, col);
		AntForageCell currentCell = (AntForageCell) getMyGrid().tryGetCell(row, col);
		double currentFoodLevel = currentCell.getFoodPheromoneLevel();
		double currentNestLevel = currentCell.getNestPheromoneLevel();
		for (Cell cell : neighbors) {
			AntForageCell antCell = (AntForageCell) cell;
			antCell.setFoodPheromoneLevel(antCell.getFoodPheromoneLevel()+currentFoodLevel*diffusionRatio);
			antCell.setNestPheromoneLevel(antCell.getNestPheromoneLevel()+currentNestLevel*diffusionRatio);
		}
		
		currentCell.setFoodPheromoneLevel(currentFoodLevel*(1-evaporationRatio));
		currentCell.setNestPheromoneLevel(currentNestLevel*(1-evaporationRatio));
		
	}
	
	private void dropPheromones(AntForageCell cell, double maxHormoneLevelOfNeighbors,String pheromoneType) {
		if (pheromoneType.equals(FOOD)) {
			if (maxHormoneLevelOfNeighbors<cell.getFoodPheromoneLevel()) {
				cell.setFoodPheromoneLevel(cell.getFoodPheromoneLevel()-10);
			} else {
				double desiredHormoneLevel = maxHormoneLevelOfNeighbors - cell.getFoodPheromoneLevel()-2;
				cell.setFoodPheromoneLevel(desiredHormoneLevel);
			}
			
		} else if (pheromoneType.equals(NEST)) {
			if (maxHormoneLevelOfNeighbors<cell.getNestPheromoneLevel()) {
				cell.setNestPheromoneLevel(cell.getNestPheromoneLevel()-10);
			} else {
			double desiredHormoneLevel = maxHormoneLevelOfNeighbors - cell.getNestPheromoneLevel()-2;
			cell.setNestPheromoneLevel(desiredHormoneLevel);
		}
		}
	}
	
	private void dropPheromonesAtNest(AntForageCell cell) {
		cell.setNestPheromoneLevel(MAX_HORMONE_LEVEL);
	}
	
	private void dropPheromonesAtFood(AntForageCell cell) {
		cell.setFoodPheromoneLevel(MAX_HORMONE_LEVEL);
	}
	
	private void updateAllCells() {
		for (int i=0;i<getMyGrid().getRows();i++) {
			for (int j = 0; j<getMyGrid().getColumns();j++) {
				AntForageCell cell = (AntForageCell) getMyGrid().tryGetCell(i, j);
				cell.updateAntLists();
				cell.updateState(cell.getState());
			}
		}
	}
	
	@Override
	public void initiateSimulation() {
		for (int[] coordinates : getMyCells().keySet()) {
			String cellType = getMyCells().get(coordinates);
			AntForageCell cell = new AntForageCell(cellType);
			if (cellType.equals(NEST)) {
				for (int i =0;i<numInitialAntsAtNest;i++) {
					cell.addAnt(new Ant());
				}
			}
			getMyGrid().setCell(coordinates[0],coordinates[1],cell);
		}
		
	}

	@Override
	public void updateParameters(Map<String, Double> parameters) {
		// TODO Auto-generated method stub
		
	}

	public double getEvaporationRatio() {
		return evaporationRatio;
	}

	public void setEvaporationRatio(double evaporationRatio) {
		this.evaporationRatio = evaporationRatio;
	}

	public double getDiffusionRatio() {
		return diffusionRatio;
	}

	public void setDiffusionRatio(double diffusionRatio) {
		this.diffusionRatio = diffusionRatio;
	}

	public double getAntsBornPerTimeStep() {
		return antsBornPerTimeStep;
	}

	public void setAntsBornPerTimeStep(double antsBornPerTimeStep) {
		this.antsBornPerTimeStep = antsBornPerTimeStep;
		getMyParameters().put("antsBornPerTimeStep", antsBornPerTimeStep);
	}

	public double getMaxAntsPerLoc() {
		return maxAntsPerLoc;
	}

	public void setMaxAntsPerLoc(double maxAntsPerLoc) {
		this.maxAntsPerLoc = maxAntsPerLoc;
		getMyParameters().put("maxAntsPerLoc", maxAntsPerLoc);
	}

	public double getLifeTimePerAnt() {
		return lifeTimePerAnt;
	}

	public void setLifeTimePerAnt(double lifeTimePerAnt) {
		this.lifeTimePerAnt = lifeTimePerAnt;
		getMyParameters().put("lifeTimePerAnt", lifeTimePerAnt);
	}

	public double getMaxAntsInSimulation() {
		return maxAntsInSimulation;
	}

	public void setMaxAntsInSimulation(double maxAntsInSimulation) {
		this.maxAntsInSimulation = maxAntsInSimulation;
		getMyParameters().put("maxAntsInSimulation", maxAntsInSimulation);
	}

	@Override
	public BackEndGrid getSquareGrid() {
		return null;
	}

	@Override
	public List<Integer> getSimState() {
		return null;
	}

}
