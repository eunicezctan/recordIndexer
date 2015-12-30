package shared.model;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import server.database.DataImporter;

/**
 * ProjectModel class that set and store the data from the Project table in the Database
 * using the getter and setter. It parameters are model after the fields in the Project table.
 *
 */

public class ProjectModel {
	
	private int project_id;
	private String title;
	private int recordsPerImage; 
	private int firstYcoord;
	private int recordHeight;
	
	ArrayList<FieldModel> field = new ArrayList<FieldModel>();
	ArrayList<BatchModel> batch = new ArrayList<BatchModel>();
	
	public ProjectModel() {
	 
		setProject_id(-1);
		setTitle("New Title");
		setRecordsperimage(-1);
		setFirstycoord(-1);
		setRecordheight(-1);
    }	
	
public ProjectModel(int project_id2, String title2, int recordsPerimage2, int firstYcoord2, int recordHeight2) {
	    
		setProject_id(project_id2);
		setTitle(title2);
		setRecordsperimage(recordsPerimage2);
		setFirstycoord(firstYcoord2);
		setRecordheight(recordHeight2);
    }

	public ProjectModel(Element projectElement) {
	
		title = DataImporter.getValue((Element) projectElement.getElementsByTagName("title").item(0));
		recordsPerImage = Integer.parseInt(DataImporter.getValue((Element) projectElement.getElementsByTagName("recordsperimage").item(0)));
		firstYcoord = Integer.parseInt(DataImporter.getValue((Element) projectElement.getElementsByTagName("firstycoord").item(0)));
		recordHeight = Integer.parseInt(DataImporter.getValue((Element) projectElement.getElementsByTagName("recordheight").item(0)));

		//Element fieldsElement = (Element) projectElement.getElementsByTagName("fields").item(0);
		NodeList fieldElements = projectElement.getElementsByTagName("field");
		for(int i = 0; i < fieldElements.getLength(); i++) 
		{
		   field.add(new FieldModel((Element)fieldElements.item(i)));
		}
		
		Element BatchElement = (Element) projectElement.getElementsByTagName("images").item(0);
		NodeList BatchElements = BatchElement.getElementsByTagName("image");	
		for(int i = 0; i < BatchElements.getLength(); i++) 
		{
		   batch.add(new BatchModel((Element) BatchElements.item(i)));
		}	
				
	}
	
	public int getProject_id() {
		return project_id;
	}
		
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
		
	public String getTitle() {
		return title;
	}
		
	public void setTitle(String title) {
		this.title = title;
	}
		
	public int getRecordsperimage() {
		return recordsPerImage;
	}
		
	public void setRecordsperimage(int recordsperimage) {
		this.recordsPerImage = recordsperimage;
	}
		
	public int getFirstycoord() {
		return firstYcoord;
	}
		
	public void setFirstycoord(int firstycoord) {
		this.firstYcoord = firstycoord;
	}
		
	public int getRecordheight() {
		return recordHeight;
	}
		
	public void setRecordheight(int recordheight) {
		this.recordHeight = recordheight;
	}

	public ArrayList<FieldModel> getField() {
		return field;
	}

	public void setField(ArrayList<FieldModel> field) {
		this.field = field;
	}

	public ArrayList<BatchModel> getBatch() {
		return batch;
	}

	public void setBatch(ArrayList<BatchModel> batch) {
		this.batch = batch;
	}

	
}//end of class ProjectModel

