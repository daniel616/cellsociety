package UI_FrontEndGrid;

import java.util.HashMap;

import BackEndGrid.BackEndGrid;
import Cells.Cell;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * @author Daniel
 *	Stores a node that represents the grid of Cells in the BackEndGrid that can be returned when called.
 *  Dynamically resizes to fit the dimensions of the canvas, and supports triangular, hexagonal and square grid types.
 *
 */
public class BaseFrontEndGrid { 
	private double canvasWidth;
	private double canvasHeight;
	private Canvas gridPicture;
	private BackEndGrid myGrid;
	private Color defaultColor;
	private String latticeType="triangle";
	private HashMap<String,Color> cellColors;//Colors a cell based on its getState
	private final String SQUARE_LATTICE="square";
	private final String HEXAGON_LATTICE="hexagon";
	private final String TRIANGLE_LATTICE="triangle";
	private boolean drawCellBorders;
	private double cellMaxDimension=60;
	private double edgeLength=60.0;

	/**
	 * @param myGrid
	 * @param canvasWidth
	 * @param canvasHeight
	 * @param simTile
	 * @param defaultColor
	 * Sets initial values of front end grid.
	 */
	public BaseFrontEndGrid(BackEndGrid myGrid, double canvasWidth, double canvasHeight, String simTile,
		Color defaultColor/*,HashMap<String, Color> cellColors*/) {
		this.defaultColor=defaultColor;//potential to crash and burn
		this.myGrid=myGrid;
		this.canvasHeight=canvasHeight;
		this.canvasWidth=canvasWidth;
		this.latticeType = simTile;
		gridPicture=new Canvas(canvasWidth,canvasHeight);
	}
	
	/**
	 * Indirectly adjusts size of each cell drawn by changing edge dimensions.
	 * If the display would exceed canvas boundaries as it currently is, dimensions are set to the minimum values
	 * necessary to fit.
	 */
	public void adjustCellSizeToFit(){
		int rows=myGrid.getRows();
		int columns=myGrid.getColumns();
		if(latticeType.equals(SQUARE_LATTICE)){
			double x=Math.min(canvasWidth/rows, canvasWidth/columns);
			edgeLength=x;
		}else if(latticeType.equals(TRIANGLE_LATTICE)){
			double x=Math.min(2*canvasWidth/(columns+1), 2*canvasHeight/(rows*Math.sqrt(3)));
			edgeLength=x;
		}else if(latticeType.equals(HEXAGON_LATTICE)){
			double x=Math.min(2*canvasHeight/((rows+1)*Math.sqrt(3)), canvasWidth/(3*columns+2));
			edgeLength=x;
		}
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param cell
	 * @param edgeUpwards
	 * @param gc
	 * Draw a triangle, hexagon, or square depending on what kind of lattice we're in. Flip upside down depending on boolean.
	 */
	public void drawCell(double x, double y, Cell cell, boolean edgeUpwards, GraphicsContext gc){
		if(latticeType.equals(SQUARE_LATTICE)){
			gc.setFill(cell.getColor());
			gc.fillRect(x, y, x+edgeLength, y+edgeLength);
		}
		
		else if(latticeType.equals(HEXAGON_LATTICE)){
			drawHexagonCell(x, y, cell, gc);
		}
		
		else if(latticeType.equals(TRIANGLE_LATTICE)){
			drawTriangleCell(x, y, cell, edgeUpwards, gc);
		}
		/*
		else{
			System.out.print("huh");
		}*/
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param cell
	 * @param gc
	 * Draw a hexagon at given point on canvas.
	 */
	private void drawHexagonCell(double x, double y, Cell cell, GraphicsContext gc) {
		gc.setFill(cell.getColor());
		double[] xPoints=new double[6];
		double[] yPoints=new double[6];
		xPoints[0]=x+edgeLength/2;
		xPoints[1]=x+3*edgeLength/2;
		xPoints[2]=x+2*edgeLength;
		xPoints[3]=x+3*edgeLength/2;
		xPoints[4]=x+edgeLength/2;
		xPoints[5]=x;
		yPoints[0]=y;
		yPoints[1]=y;
		yPoints[2]=y+edgeLength*Math.sqrt(3)/2;
		yPoints[3]=y+edgeLength*Math.sqrt(3);
		yPoints[4]=y+edgeLength*Math.sqrt(3);
		yPoints[5]=y+edgeLength*Math.sqrt(3)/2;
		gc.fillPolygon(xPoints, yPoints, 6);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param cell
	 * @param edgeUpwards
	 * @param gc
	 * Draw a triangle at given point, with the triangle's point either facing up or down
	 */
	private void drawTriangleCell(double x, double y, Cell cell, boolean edgeUpwards, GraphicsContext gc) {
		gc.setFill(cell.getColor());
		double[] xPoints=new double[3];
		double[] yPoints=new double[3];
		if(edgeUpwards==true){
			xPoints[0]=x;
			xPoints[1]=x+edgeLength;
			xPoints[2]=x+edgeLength/2;
			yPoints[0]=y;
			yPoints[1]=y;
			yPoints[2]=y+Math.sqrt(3)*edgeLength/2;
		}
		else{
			xPoints[0]=x+edgeLength/2;
			xPoints[1]=x+edgeLength;
			xPoints[2]=x;
			yPoints[0]=y;
			yPoints[1]=y+Math.sqrt(3)*edgeLength/2;
			yPoints[2]=y+Math.sqrt(3)*edgeLength/2;
		}
		for(int i=0;i<xPoints.length;i++){
			xPoints[i]=xPoints[i]+1;
			yPoints[i]=yPoints[i]+1;
		}
		
		gc.fillPolygon(xPoints, yPoints, 3);
	}
	
	public void setDisplayType(String latticeType){
		this.latticeType=latticeType;
	}
	
	/**
	 * Display the grid on the canvas after adjusting it to fit.
	 */
	public void updateGrid(){
		adjustCellSizeToFit();
		if(latticeType.equals(SQUARE_LATTICE))updateSquareGrid();
		if(latticeType.equals(TRIANGLE_LATTICE))updateTriangleGrid();
		if(latticeType.equals(HEXAGON_LATTICE))updateHexagonGrid();
	}

	/**
	 * Draws a square grid.
	 */
	private void updateSquareGrid() {
		GraphicsContext gc=gridPicture.getGraphicsContext2D();
		gc.setFill(defaultColor);
		gc.fillRect(0, 0, canvasWidth, canvasHeight);
		
		int columns=myGrid.getColumns();
		int rows=myGrid.getRows();
		for(int i=0;i<columns;i++){
			for(int j=0;j<rows;j++){
				Color javaColor=myGrid.tryGetCell(i,j).getColor();
				gc.setFill(javaColor);
				gc.fillRect(i*edgeLength, j*edgeLength, (i+1)*edgeLength, (j+1)*edgeLength);
			}
		}
	}
	
	/**
	 * Draws a triangular grid.
	 */
	private void updateTriangleGrid(){
		GraphicsContext gc=gridPicture.getGraphicsContext2D();
		gc.setFill(defaultColor);
		gc.fillRect(0, 0, canvasWidth, canvasHeight);
		for(int i=0;i<myGrid.getRows();i++){
			for(int j=0;j<myGrid.getColumns();j++){
				Cell cell=myGrid.getCell(i, j);
				boolean edgeUpwards=false;//don't think this line is necessary but just in case
				if((i+j)%2==0) edgeUpwards=true;
				drawCell(j*0.5*edgeLength,i*edgeLength*Math.sqrt(3)/2,cell,edgeUpwards,gc);
			}
		}
	}	
	
	
	/**
	 * Draws a hexagonal grid.
	 */
	private void updateHexagonGrid(){
		GraphicsContext gc=gridPicture.getGraphicsContext2D();
		gc.setFill(defaultColor);
		gc.fillRect(0, 0, canvasWidth, canvasHeight);
		for(int i=0;i<myGrid.getRows();i++){
			for(int j=0;j<myGrid.getColumns();j++){
				Cell cell=myGrid.getCell(i, j);
				if(i%2==0){
					drawCell(j*3*edgeLength,i*(Math.sqrt(3)/2)*edgeLength,cell,false,gc);
				}
				else{
					drawCell(j*3*edgeLength+1.5*edgeLength,i*(Math.sqrt(3)/2)*edgeLength,cell,false,gc);
				}
			}
		}
	}
	
	/**
	 * Return this to some other class.
	 */
	public Node returnDisplay(){
		return gridPicture;		
	}

}