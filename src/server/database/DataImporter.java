package server.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * DataImporter that set up the Record.XML for parsing
 * 
 */

public class DataImporter {
	
	
	public static void main(String[] args) throws ParserConfigurationException  {
		
		String path=args[0];
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		File xmlFile = new File(path);
		File dest = new File("CurrentRecord");

		/*
		 * (**APACHE**)
		 */
		try
		{
			//	We make sure that the directory we are copying is not the the destination
			//	directory.  Otherwise, we delete the directories we are about to copy.
			if(!xmlFile.getParentFile().getCanonicalPath().equals(dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);
				
			//	Copy the directories (recursively) from our source to our destination.
			FileUtils.copyDirectory(xmlFile.getParentFile(), dest);
			
			//	Overwrite my existing *.sqlite database with an empty one.  Now, my
			//	database is completelty empty and ready to load with data.
			//FileUtils.copyFile(emptydb, currentdb);
		}
		catch (IOException e)
		{
			
		}
		/*
		 * (**APACHE**)
		 */
			
		try {
				File parsefile = new File(dest.getPath() + File.separator + xmlFile.getName());		
				Document doc= builder.parse(parsefile);
				doc.getDocumentElement().normalize();
				Element root=doc.getDocumentElement();
				IndexerData indexer =new IndexerData(root);
				 
				 Database.initialize();
				 Database.getDB().startTransaction();
				 Database.getDB().setIndex(indexer);
				 
				 indexer.createTable();
				 indexer.insertData();
				 
				 Database.getDB().endTransaction(true);
			} 
			catch (SAXException | IOException | DatabaseException e) {
				Database.getDB().endTransaction(false);
				e.printStackTrace();	
			}
						
	}//end of Main

	
	public static ArrayList<Element> getChildElements(Node node){
		
		 ArrayList<Element> result= new ArrayList<Element>();
		 
		 NodeList children=node.getChildNodes();
		 for(int i=0; i<children.getLength(); i++)
		 {
			Node child = children.item(i);
			if(child.getNodeType()==Node.ELEMENT_NODE)
				result.add((Element) child);
		 }
		 
		 return result;
	}//end of getChildElements()
	
	
	public static String getValue(Element element){
		
		String result="";	
		if(element.getFirstChild()!=null)
		{
		Node child=element.getFirstChild();
		result=child.getNodeValue();
		//System.out.println(child);
		return result;
		}
		else
			return null;
	}//end of getValue()
	
}//end of Class DataImporter
