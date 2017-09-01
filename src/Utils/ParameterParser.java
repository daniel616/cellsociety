package Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Class for parsing simulation XML files, and returns cell location data and parameter data through HashMaps to pass to the Simulations
 * Generates hashmap of simulation parameters to their values and a hashmap of grid coordinates to their cell types
 * @author sarahzhou
 *
 */
/**
 * @author sarahzhou
 * @author Moses Wayne
 *
 */
public class ParameterParser {
	private String simType;
	private Map<String,Double> parameters;
	private Map<int[],String> cells;
	public static final String TAG_NAME = "simulation";
	public static final String TAG_ID = "id";
	private Document myDoc;
	private String myLatticeType;
	
	private String myTitle;
	
	/**
	 * Constructor that generates a NodeList of the parameter and cell elements and passes it into initiateParameterMap() to convert it into a Hashmap of cells and a Hashmap of Parameters
	 * @param file: name of file to parse
	 */
	public ParameterParser(File file)  {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			myDoc = dBuilder.parse(file);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		myDoc.getDocumentElement().normalize();
		NodeList nodeList = myDoc.getElementsByTagName("*");
		Element el = (Element) nodeList.item(0);
		setSimType(el.getAttribute("id"));
		initiateParameterMap(nodeList);
	}
	
	
	/**
	 * Converts the nodeList of parameters into a HashMap of parameters to values, and a Hashmap of cell locations to cell type
	 * Also saves title and lattice type
	 * @param nodeList: of parameters and cells 
	 */
	private void initiateParameterMap(NodeList nodeList) {
		parameters = new HashMap<String,Double>();
		for (int i = 1; i<nodeList.getLength();i++) {
			Element element = (Element) nodeList.item(i);
			String attr = element.getNodeName();
			if (attr.equals("cells")) { 
				initiateCellMap(element);
				break;
			} 
			
			else if (attr.equals("title")) {
				myTitle = myDoc.getElementsByTagName(attr).item(0).getTextContent();
				continue;
			}
			
			else if (attr.equals("latticeType")) {
				myLatticeType = myDoc.getElementsByTagName(attr).item(0).getTextContent();
				continue;
			}
			Double value = Double.parseDouble(myDoc.getElementsByTagName(attr).item(0).getTextContent());
			parameters.put(attr, value);
		}
	}
	
	
	/**
	 * Inititate a Hashmap mapping cell locations to cell type
	 * @param el: element containing all the cells
	 */
	private void initiateCellMap(Element el) {
		cells = new HashMap<int[],String>();
		NodeList cellList = el.getElementsByTagName("cell");
		for (int idx = 0; idx<cellList.getLength();idx++) {
			String str = cellList.item(idx).getTextContent();
			String[] strArr = str.split(",");
			int[] coordinates = new int[2];
			coordinates[0] = Integer.parseInt(strArr[0]);
			coordinates[1] = Integer.parseInt(strArr[1]);
			cells.put(coordinates,strArr[2]);
		}
	}
	
	public void setSimType(String simType) {
		this.simType = simType;
	}

	public String getSimType() {
		return simType;
	}
	
	public String getTitle() {
		return myTitle;
	}
	
	public String getLatticeType() {
		return myLatticeType;
	}

	public Map<String, Double> getParameters() {
		return parameters;
	}
	public Map<int[],String> getCells(){
		return cells;
	}
}