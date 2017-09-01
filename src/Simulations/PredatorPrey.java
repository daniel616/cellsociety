package Simulations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import BackEndGrid.BackEndGrid;
import BackEndGrid.SquareEightNeighborGrid;
import Cells.Cell;
import Cells_Wator.WatorCreature;
import Cells_Wator.WatorEmpty;
import Cells_Wator.WatorPredator;
import Cells_Wator.WatorPrey;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * 
 * @author Daniel
 * Simulation that displays the interaction of predators and prey. Features reproduction, starvation and predation.
 * 
 */
public class PredatorPrey extends Simulation {
	private int preyGestationPeriod;
	private int predatorGestationPeriod;
	private int starveTime;
	private int numPrey;
	private int numPredator;
	
	
	public PredatorPrey(Map<String,Double> parameters,Map<int[],String> cells, String simTile) {
		super(parameters,cells,simTile);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param creature
	 * Replaces cell at target location and leaves blank cell behind. 
	 */
	public void moveToAndReplace(int x, int y,Cell creature){//Replaces target cell and leaves blank cell behind
		getMyGrid().setCell(creature.getRow(), creature.getCol(), new WatorEmpty());
		getMyGrid().setCell(x,y,creature);
	}
	
	/**
	 * 
	 * @param creature
	 * Reproduce if it has been enough time since the last time the predator gave birth. 
	 * 
	 */
	public void handleReproduction(WatorPredator creature){//duplicate code, i know, but it removes typecasting code smell
		if(predatorGestationPeriod==creature.getTimeSinceBirth()){
			birthIfAble(creature);
		}
		else{
			creature.incrementTimeSinceBirth();
		}
	}
	
	public void handleReproduction(WatorPrey creature){//duplicate code, i know, but it removes typecasting code smell
		if(preyGestationPeriod==creature.getTimeSinceBirth()){
			birthIfAble(creature);
		}
		else{
			creature.incrementTimeSinceBirth();
		}
	}
	
	/**
	 * 
	 * @param creature
	 * Creates a new instance of the original class at an empty neighbor location if possible.
	 */
	public void birthIfAble(WatorCreature creature){
		List<Cell> cellList=getMyGrid().getNeighbors(creature.getRow(), creature.getCol());
		List<Cell> emptyCells=getClassSpecificSubcells(cellList,"Cells_Wator.WatorEmpty");		
		
		if(emptyCells.size()>0){
			Random rn = new Random();
			WatorEmpty targetCell=(WatorEmpty)emptyCells.get(rn.nextInt(emptyCells.size()));
			getMyGrid().setCell(targetCell.getRow(), targetCell.getCol(), creature.makeChild());
			creature.resetTimeSinceBirth();
		}
	}
	
	/**
	 * 
	 * @param creature
	 * Moves to a blank neighboring grid if possible.
	 */
	public void moveIfAble(WatorCreature creature){
		List<Cell> cellList=getMyGrid().getNeighbors(creature.getRow(), creature.getCol());
		List<Cell> emptyCells=getClassSpecificSubcells(cellList,"Cells_Wator.WatorEmpty");
		
		if(emptyCells.size()>0){
			Random rn = new Random();
			WatorEmpty targetCell=(WatorEmpty)emptyCells.get(rn.nextInt(emptyCells.size()));
			getMyGrid().switchCell(targetCell, creature);
		}
	}
	
	/**
	 * 
	 * @param creature
	 * Moves to and eats a neighboring prey, if one exists.
	 * Resets predator's hunger counter.
	 */
	public void eatIfAble(WatorPredator creature){
		List<Cell> cellList=getMyGrid().getNeighbors(creature.getRow(), creature.getCol());
		List<Cell> preyCells=getClassSpecificSubcells(cellList,"Cells_Wator.WatorPrey");
		
		if(preyCells.size()>0){
			Random rn = new Random();
			WatorPrey targetCell=(WatorPrey)preyCells.get(rn.nextInt(preyCells.size()));
			moveToAndReplace(targetCell.getRow(),targetCell.getCol(), creature);
			creature.resetTimeSinceAte();
		}
	}
	
	/**
	 * 
	 * @param creature
	 * Increment hunger, and remove from world if too hungry.
	 */
	public void handleHunger(WatorPredator creature){
		creature.incrementTimeSinceAte();
		if(creature.getTimeSinceAte()>=starveTime){
			getMyGrid().setCell(creature.getRow(),creature.getCol(),new WatorEmpty());
		}
	}
	
	/**
	 * Predators eat if they can, move if they can, reproduce if they can, and then starve to death if too hungry.
	 * Then, surviving prey move if they can, and reproduce if they can.
	 */
	@Override
	public void update() {
		List<Cell> allCells = getAllCells();
		List<Cell> predators=getClassSpecificSubcells(allCells,"Cells_Wator.WatorPredator");
		for(Cell predator: predators){
			
			eatIfAble((WatorPredator)predator);
			
			if(((WatorPredator)predator).getTimeSinceAte()>0){
				moveIfAble((WatorPredator)predator);			
			}
			handleReproduction((WatorPredator)predator);
			handleHunger((WatorPredator)predator);
		}
		
		List<Cell> allCellsAfterPredatorAction=getAllCells();
		List<Cell> preyCells=getClassSpecificSubcells(allCellsAfterPredatorAction,"Cells_Wator.WatorPrey");
		for(Cell prey: preyCells){
			moveIfAble((WatorPrey)prey);
			handleReproduction((WatorPrey)prey);
		}
	}

	/**
	 * @return
	 * Returns all cells in the backEndGrid as a list.
	 */
	private List<Cell> getAllCells() {
		List<Cell> allCells=new ArrayList<>();
		BackEndGrid grid=getMyGrid();
		for(int i=0; i<grid.getRows();i++){
			for(int j=0;j<grid.getColumns();j++){
				allCells.add(grid.tryGetCell(i, j));
			}
		}
		return allCells;
	}
	/**
	 * Populates simulation with objects using reflection based on a given keySet. Throws exceptions.
	 */
	@Override
	public void initiateSimulation() {
		for (int[] coordinates : getMyCells().keySet()) {
			String cellType = getMyCells().get(coordinates);	
			Class<?> cls;
			try {
				cls = Class.forName("Cells_Wator."+cellType);
				Cell cell=(Cell) cls.newInstance();
				getMyGrid().setCell(coordinates[0],coordinates[1],cell);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				Alert myAlert = new Alert(AlertType.ERROR);
				myAlert.setHeaderText("Class type unknown");
			} catch(InstantiationException e){
				Alert myAlert = new Alert(AlertType.ERROR);
				myAlert.setHeaderText("Can't initialize class with default constructor");
			}catch (IllegalAccessException e){
				Alert myAlert = new Alert(AlertType.ERROR);
				myAlert.setHeaderText("Can't apply reflection here");
			}	
		}
		// TODO Auto-generated method stub		
	}
	
	public void setPreyGestationPeriod(int preyGestationPeriod) {
		this.preyGestationPeriod=preyGestationPeriod;
	}
	
	public void setPredatorGestationPeriod(int predatorGestationPeriod) {
		this.predatorGestationPeriod=predatorGestationPeriod;
	}
	
	public void setStarveTime(int starveTime) {
		this.starveTime=starveTime;
	}

	public int getNumPrey() {
		return numPrey;
	}
	
	public int getNumPredator() {
		return numPredator;
	}
	
	/**
	 * Changes simulation parameters based on the given input.
	 */
	public void updateParameters(Map<String,Double> parameters) {
		setStarveTime(parameters.get("starveTime").intValue());
		setPredatorGestationPeriod(parameters.get("predatorGestationPeriod").intValue());
		setPreyGestationPeriod(parameters.get("preyGestationPeriod").intValue());
	}

	/**
	 * Creates a list tracking the populations of creatures within the simulation.
	 */
	@Override
	public List<Integer> getSimState() {
		List<Integer> state = new ArrayList<Integer>();
		state.add(getNumPrey());
		state.add(getNumPredator());
		return state;
	}

	
	@Override
	public BackEndGrid getSquareGrid() {
		return new SquareEightNeighborGrid(getGridSize());
	}

}