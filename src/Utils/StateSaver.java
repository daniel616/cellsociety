package Utils;

import java.io.File;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author sarahzhou
 * Saves the current parameters of the Simulation into an XML file
 */
public class StateSaver {
	
	private Map<String,Double> myParameters;
	private Map<int[], String> myCells;
	private String mySimType;
	private String myFileName;
	
	private static final String directory = "SavedStates/";
	
	Document myDoc;
	
	
	/**
	 * Constructor that initializes fileName, cell Map, parameter Map, and calls the function that writes to XML file that user specifies
	 * @param fileName
	 * @param simType
	 * @param parameters
	 * @param cells
	 * @throws TransformerException
	 */
	public StateSaver(String fileName, String simType,Map<String,Double> parameters,Map<int[], String> cells) throws TransformerException {
		myFileName = fileName;
		myParameters = parameters;
		myCells = cells;
		mySimType = simType;
		writeToXML();
	}
	
	/**
	 * Writes XML file for the Simulation's state based on Maps of its parameters and cells, and Simulation type
	 * @throws TransformerException
	 */
	public void writeToXML() throws TransformerException {
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			myDoc = docBuilder.newDocument();
			Element simulationElement = myDoc.createElement("simulation");
			myDoc.appendChild(simulationElement);
			
			Attr attr = myDoc.createAttribute("id");
			attr.setValue(mySimType);
			simulationElement.setAttributeNode(attr);
		    
			writeParameters(simulationElement);
		    writeCells(simulationElement);
		 
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(myDoc);
			StreamResult result = new StreamResult(new File(directory+myFileName+".xml"));

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	} 
	
	/**
	 * Writes parameters to DOM element from parameters Hashmap
	 * @param parentEl: Element to write parameters to
	 */
	private void writeParameters(Element parentEl) {
		for (String parameter : myParameters.keySet()) {
			Element el = myDoc.createElement(parameter);
			el.appendChild(myDoc.createTextNode(myParameters.get(parameter).toString()));
			parentEl.appendChild(el);
		}
	}
	
	/**
	 * Writes cells and cell info to DOM element from cells Hashmap
	 * @param parentEl: Element to write cells to
	 */
	private void writeCells(Element parentEl) {
		Element cellEl = myDoc.createElement("cells");
		parentEl.appendChild(cellEl);
		for (int[] coordinates : myCells.keySet()) {
			Element cell = myDoc.createElement("cell");
			cell.appendChild(myDoc.createTextNode(myCells.get(coordinates)));
			cellEl.appendChild(cell);
		}
	}
	
	public void setParameters(Map<String,Double> parameters) {
		myParameters = parameters;
	}
	
	public void setCells(Map<int[], String> cells) {
		myCells = cells;
	}
	
	public void setSimType(String simType) {
		mySimType = simType;
	}
}
