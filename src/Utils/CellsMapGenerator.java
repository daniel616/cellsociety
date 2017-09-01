package Utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * generates a randomized map based on cell parameter specifications
 * @author Moses Wayne
 */
public class CellsMapGenerator {
	private static final String GEN_STUB = "Generate";
	
	private static final String EMPTY = "empty";
	private static final String TREE = "tree";
	private static final String BURNING = "burning";
	private static final String PROBABILITY_BURNING = "probBurning";
	
	private static final String ALIVE = "alive";
	private static final String DEAD = "dead";
	
	private static final String TYPE_1 = "type1";
	private static final String TYPE_2 = "type2";
	private static final String PROBABILITY_TYPE = "redBlueRatio";
	
	private static final String PREDATOR = "WatorPredator";
	private static final String PREY = "WatorPrey";
	private static final String WATOR_EMPTY = "WatorEmpty";
	private static final String PROBABILITY_PREDATOR = "predPreyRatio";
	
	private static final String ANT_NEST = "nest";
	private static final String ANT_FOOD = "food";
	
	private int gridSize;
	private Map<int[],String> myCells;
	private Map<String,Double> cellParameters;
	
	public CellsMapGenerator(Map<String,Double> params) {
		cellParameters = params;
		myCells = new HashMap<int[],String>();
	}
	
	/**
	 * reflection used to generate the cell map based on the string passed in
	 * @param simType String designating the simulation type
	 * @return cell map to be initialized
	 */
	public Map<int[],String> generateMap(String simType) {
		gridSize = cellParameters.get("size").intValue();
		
		try{
			Method constructMap = this.getClass().getMethod(simType+GEN_STUB);
			constructMap.setAccessible(true);
			constructMap.invoke(this, (Object[]) null);
			return myCells;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * generates a fire simulation map to be initialized
	 */
	public void fireGenerate() {
		myCells = new HashMap<int[],String>();
		
		for (int i=0; i<gridSize;i++) {
			for (int j = 0; j<gridSize;j++) {
				int[] coordinates = {i,j};
				if (isBorderCell(i,j)) {
					myCells.put(coordinates,EMPTY);
				} else {
					double rand = Math.random();
					if (rand>cellParameters.get(PROBABILITY_BURNING)) {
						myCells.put(coordinates, TREE);
					} else {
						myCells.put(coordinates, BURNING);
					}
				}
			}
		}
	}
	
	/**
	 * generates a Game of Life simulation map to be initialized
	 */
	public void gameoflifeGenerate() {	
		myCells = new HashMap<int[],String>();
		double probDead = cellParameters.get(EMPTY);
		for (int i=0; i<gridSize;i++) {
			for (int j = 0; j<gridSize;j++) {
				int[] coordinates = {i,j};
				double rand = Math.random();
				if (rand<probDead) {
					myCells.put(coordinates,DEAD);
				} else {
					myCells.put(coordinates, ALIVE);
				}
			}
		}
	}
	
	/**
	 * generates a segregation simulation map to be initialized
	 */
	public void segregationGenerate() {
		myCells = new HashMap<int[],String>();
		double probEmpty = cellParameters.get(EMPTY);
		double probType = ((1-probEmpty)*cellParameters.get(PROBABILITY_TYPE))+probEmpty;
		for (int i=0; i<gridSize;i++) {
			for (int j = 0; j<gridSize;j++) {
				int[] coordinates = {i,j};
				double rand = Math.random();
				if (rand<probEmpty) {
					myCells.put(coordinates, EMPTY);
				} else if (rand<probType){
					myCells.put(coordinates, TYPE_1);
				} else {
					myCells.put(coordinates, TYPE_2);
				}
			}
		}
	}
	
	/**
	 * generates a predator prey simulation map to be initialized
	 */
	public void predatorpreyGenerate() {
		myCells = new HashMap<int[],String>();
		double probEmpty = cellParameters.get(EMPTY);
		double predPreyRatio = ((1-probEmpty)*cellParameters.get(PROBABILITY_PREDATOR))+probEmpty;
		for (int i=0; i<gridSize;i++) {
			for (int j = 0; j<gridSize;j++) {
				int[] coordinates = {i,j};
				double rand = Math.random();
				if (rand<probEmpty) {
					myCells.put(coordinates, WATOR_EMPTY);
				} else if (rand<predPreyRatio){
					myCells.put(coordinates, PREDATOR);
				} else {
					myCells.put(coordinates, PREY);
				}
			}
		} 
	}
	
	/**
	 * generates an ant forage simulation map to be initialized
	 * NOTE: the ant forage simulation was not implemented fully
	 */
	public void antforageGenerate() {
		myCells = new HashMap<int[],String>();
		for (int i = 0; i<gridSize; i++) {
			for (int j = 0; j<gridSize; j++) {
				int[] coordinates = {i,j};
				myCells.put(coordinates, EMPTY);
			}
		}
		int[] coord = {(int) (Math.random()*gridSize),(int) (Math.random()*gridSize)};
		myCells.put(coord, ANT_NEST);
		while (myCells.get(coord).equals(ANT_NEST)){
			int[] newCoord = {(int) (Math.random()*gridSize),(int) (Math.random()*gridSize)};
			coord = newCoord;
		}
		myCells.put(coord, ANT_FOOD);
	}
	
	/**
	 * determines if a cell is a border cell 
	 */
	private boolean isBorderCell(int row, int col) {
		int gridEdge = gridSize -1;
		if (row==0 || col==gridEdge || row==0 || col==gridEdge) return true;
		else return false;
	}

}
