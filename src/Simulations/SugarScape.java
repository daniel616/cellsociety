package Simulations;
/*
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BackEndGrid.BackEndGrid;
import Cells.Cell;
import SugarClasses.SugarCell;
import SugarClasses.SugarPerson;

public class SugarScape extends Simulation{
	private int sugarMetabolism;
	private int vision;
	private int maxSugar;
	private int sugarRegen;
	private int sugarGrowthInterval;

	public SugarScape(Map<String, Double> parameters, Map<int[], String> cells, String simTile) {
		super(parameters, cells, simTile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		for(int i=0; i<getMyGrid().getRows();i++){
			for(int j=0;j<getMyGrid().getColumns();j++){
				SugarCell cell=(SugarCell)getMyGrid().getCell(i, j);
				cell.periodicGrowSugar();
				if(cell.isOccupied()){
					SugarPerson person=cell.getSugarPerson();
					chaseSugar(person,cell);
					handleHunger(person,(SugarCell)getMyGrid().getCell(person.getRow(), person.getColumn()));
				}
			}
		}
		// TODO Auto-generated method stub
	}
	
	public void eatSugar(SugarPerson person, SugarCell cell){
		person.adjustSugar(cell.getSugar());
		cell.resetSugarCount();
	}
	
	public void handleHunger(SugarPerson person, SugarCell personsCell){
		person.adjustSugar(-sugarMetabolism);
		if(person.getSugar()<=0){
			personsCell.setPerson(null);
		}
	}
	
	public void chaseSugar(SugarPerson person, SugarCell personsCell){
		int row=personsCell.getRow();
		int column=personsCell.getCol();
		List<Cell> neighbors=getNeighborsInSight(row,column,vision);
		SugarCell sugaryCell=mostSugaryCell(neighbors);
		personsCell.setPerson(null);
		sugaryCell.setPerson(person);
		eatSugar(person, sugaryCell);
		
	}
	public SugarCell mostSugaryCell(List<Cell> sugarCells){ 
		SugarCell mostSugar=(SugarCell)sugarCells.get(0);
		for(Cell cell:sugarCells){
			if(((SugarCell)cell).getSugar()>mostSugar.getSugar()){
				mostSugar=(SugarCell)cell;
			}
		}
		return mostSugar;
	}
	
	public List<Cell> getNeighborsInSight(int row, int column, int vision){
		List<Cell> neighbors=new ArrayList<>();
		recursiveHelper(row, column,vision,neighbors);
		
		return neighbors;
	}
	
	private void recursiveHelper(int row, int column, int vision, List<Cell> neighbors){
		if(vision>0){
			List<Cell> localNeighbors=getMyGrid().getNeighbors(row, column);
			vision--;
			for(Cell cell:localNeighbors){
				if(!((SugarCell)cell).isOccupied()){
					neighbors.add(cell);
				}
				recursiveHelper(cell.getRow(),cell.getCol(),vision,neighbors);
			}
		}
	}

	@Override
	public void initiateSimulation() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateParameters(Map<String, Double> parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BackEndGrid getSquareGrid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getSimState() {
		// TODO Auto-generated method stub
		return null;
	}
}
*/
