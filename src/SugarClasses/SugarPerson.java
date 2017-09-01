package SugarClasses;

public class SugarPerson {
	private int sugarCount;
	private int row;
	private int column;
	
	private SugarPerson(){
		
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public void setRow(int row){
		this.row=row;
	}
	
	public void setColumn(int column){
		this.column=column;
	}
	
	public int getSugar(){
		return sugarCount;
	}
	
	public void adjustSugar(int sugar){
		sugarCount+=sugar;
	}
	
	
}