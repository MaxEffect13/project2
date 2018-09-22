package com.revature.drivers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.revatre.xml.XMLParser;


public class XMLAttributeStats {
	
	private static final String FILE_LOCATION = 
			"heroes.xml";
	
	
	public static void main(String[] args) {
		XMLParser inputParse = null;
		
//		final String target = FileLoc.TARGET_MOVIE;
		final String baseTag = "element";
		
//		ClassLoader classLoader = .getClass().getClassLoader();
//		File file = new File(classLoader.getResource("file/test.xml").getFile());
		
		System.out.print("Parsing Input Files"); 
		try {
			//This is where input files are parsed. If you want to save time, 
			// comment out which parses you don't need for a given query. 
			inputParse = new XMLParser(new File(FILE_LOCATION));
			System.out.print("."); // For progress
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// If there is any sort of problem, print it and exit. 
			System.err.println(e.getMessage());
			System.exit(1);
		}
		
		System.out.println(" Done!");
		
		
		//Used to hold... Strings.
		String tagName = null;
		
		//Holds elements of the DOM.
		Element domChildElement = null;
		
		//Used to count tags
		HashMap<String, AtomicLong> tagCounter = new HashMap<>();
		
		//Used to keep track of order. (This was hacked together after the fact)
		LinkedList<String> orderOccurred = new LinkedList<>();
		
		//Get the elements of the film. 
		NodeList nodeList = inputParse.getDoc().getElementsByTagName(baseTag);
		NodeList childNodes;
		
		//Go through each element
		for (int i=0; i<nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Element.ELEMENT_NODE) {
				childNodes = ((Element)nodeList.item(i)).getChildNodes();
				
				//Go through each child of the current element
				for(int j=0; j<childNodes.getLength(); j++) {
					if (childNodes.item(j).getNodeType() == Element.ELEMENT_NODE) {
						domChildElement = (Element) childNodes.item(j);
						
						tagName = domChildElement.getTagName();
						
						//If we haven't encountered this tag, save a new 
						// reference to it, and count it. 
						if (!tagCounter.containsKey(tagName)) {
							orderOccurred.add(tagName);
							tagCounter.put(tagName, new AtomicLong(1));
						}
						//Else update the count of that element
						else {
							tagCounter.get(tagName).incrementAndGet();
						}
					} // end if //
				} // end for //
			} // end if //
		} // end for //
		
		System.out.println("Base Tag '" + baseTag + "': " + nodeList.getLength());
		
		//Go through each tag and print the number of occurrences and percentage
		for (String key : orderOccurred) {
			long occurrences = tagCounter.get(key).longValue();
			double percent = (double)occurrences / (double)nodeList.getLength();
			percent *= 100;
			//Print the statistics
			System.out.println(
					rightPad(key, ' ', 8) + ": " +
					rightPad("" + occurrences, ' ', 6) + ": " + 
					roundDouble(percent, 0.01) + "%"
				);
		}
		
	} // end of main

	
	private static String rightPad(String toPad, char padWith, int padTo) {
		StringBuilder builder = new StringBuilder(toPad);
		
		while (builder.length() < padTo) {
			builder.append(padWith);
		}
		
		return builder.toString();
	}
	
	//Hack together method of rounding to a decimal in a double. 
	//Takes to round as a 1 in the place to round to.
	private static double roundDouble(double toRound, double roundTo) {
		toRound /= roundTo;
		toRound = Math.round(toRound);
		toRound *= roundTo;
		return toRound;
	}
	
} // end of class AttributeStats








