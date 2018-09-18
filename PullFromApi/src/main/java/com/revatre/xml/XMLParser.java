package com.revatre.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class XMLParser {

	/**
	 * Parses XML documents. This is derived from the DocumentBuilderFactory. We
	 * only need one instance as it can parse multiple documents.
	 */
	private static DocumentBuilder dBuilder;
	
	/**
	 * The DOM that is derived from the input file. This can be traversed by the
	 * implementing subclass.
	 */
	private Document document;
	
	
	
	
	
	/**
	 * This is the constructor for XML Parsers. It takes a file and parses it. 
	 * Implementing classes should take the document and derive necessary data
	 * for themselves from this document. 
	 * @param inputFile
	 * @param mainElementsName
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public XMLParser(File inputFile) 
			throws ParserConfigurationException, SAXException, IOException {
		// If the document builder is not yet defined, define new one.
		if (dBuilder == null) {
			// Get a new document builder factory to make a new document builder
			DocumentBuilderFactory dbFactory = 
										DocumentBuilderFactory.newInstance();
			
			// Define a new document builder. Can throw
			// ParserConfigurationException, but only if java is screwed up or
			// someone messes with the factory instance.
			dBuilder = dbFactory.newDocumentBuilder();
		} // end if //

		// Parse the input file into the DOM document. 
		// Can throw parsing exceptions or IOexceptions. 
		document = dBuilder.parse(inputFile);
		
		//Normalize the document for consistent parsing. 
		document.normalize();
	} // end of constructor XMLParser()
	
	
	
	
	/** Returns the DOM in order to ask DOM questions.  */
	public Document getDoc() {
		return document;
	}
	
	
	
	
	
	
	
	/**
	 * Performs a recursive search of the parent element to see if a child
	 * with the specified name exists. 
	 * @param parent - The parent to check for the child. 
	 * @param childName - The tag name of the child to check for. 
	 * @return True if the child with the specified tag name exists, false 
	 * otherwise. 
	 */
	public static boolean hasChildElement(Element parent, String childName) {
		return getChildElement(parent, childName) != null;
	} // end of hasChildElement
	
	
	/**
	 * Performs a recursive search of the parent element and returns the first 
	 * instance of the child element requested, or null. 
	 * @param parent - The parent to check for the child. 
	 * @param childName - The tag name of the child to check for. 
	 * @return The child with the specified tag name or null if it doesn't 
	 * exist. 
	 */
	public static Element getChildElement(Element parent, String childName) {
		//The element to return once we find it. 
		Element child = null;
		
		//A temporary node used to traverse te children
		Node tempNode = null;
		//A temporary element used to traverse the children
		Element tempElement = null; 
		
		//Used to determine when the child element is found. 
		boolean childFound = false;
		
		//Get the children of the parent
		NodeList children = parent.getChildNodes();
		
		//For each child, check to see if the name matches. If so we are done. 
		// If a child has more children, recursively check them. 
		for (int i=0; i<children.getLength() && !childFound; i++) {
			tempNode = children.item(i);
			
			//If the node is an element, cast it and check it.
			if (tempNode != null && 
					tempNode.getNodeType() == Node.ELEMENT_NODE) {
				tempElement = (Element) tempNode;
				
				//If the element has the tag name, we have found the child. 
				if (tempElement.getTagName().equals(childName)) {
					childFound = true;
					child = tempElement;
				}
				//Else if the element has children, recursively search it.
				else if (tempElement.hasChildNodes()) {
					child = getChildElement(tempElement, childName);
					//If getChildElement returns not null, we have found it. 
					childFound = (child != null);
				}
				
			} // end if //
		} // end for //
		
		return child;
	} // end of getChildElement
	
	
	
	/**
	 * Returns the contents of the specified child element as text, or null
	 * if no such child element in the parent by the given name was found. 
	 * @param parent - The parent element to search through. 
	 * @param childName - The string name of the child element get the text from
	 * @return The text content of the child or null
	 */
	public static String getChildText(Element parent, String childName) {
		//Copied from MovieFileParse
		String returnVal = "";
		Element childElement = XMLParser.getChildElement(parent, childName);
		
		if (childElement != null) {
			returnVal = childElement.getTextContent();
		}
		
		return returnVal;
	} // end getChildText
	
	/**
	 * Takes a node list, and returns an array containing only the element tags
	 * within the list. 
	 * @param nodeList
	 * @return
	 */
	public static Element[] getElements(NodeList nodeList) {
		LinkedList<Element> list = new LinkedList<>();
		
		for (int i=0; i<nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Element.ELEMENT_NODE) {
				list.add((Element) nodeList.item(i));
			}
		}
		
		return list.toArray(new Element[0]);
	}
	
	
	
	
} // end of class XMLParser





