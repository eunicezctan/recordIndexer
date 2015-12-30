package shared.model;

import org.w3c.dom.Element;

import server.database.DataImporter;

/**
 * FieldModel class that set and store the data from the Field table in the Database
 * using the getter and setter. It parameters are model after the fields in the Field table.
 *
 */

public class FieldModel {
	
	private int field_id;
	private String title;
	private int xcoord;
	private int width;
	private String helpHtml;
	private int column_no;
	private int project_id;
	private String knownData;
	
	public FieldModel() {
	    // TODO Auto-generated constructor stub
    }
	
	public FieldModel(int field_id, String title, int xcoord, int width,
            String helpHtml, int column_no ,int project_id, String knownData) {
		
		setField_id(field_id);
		setTitle(title);
		setXcoord(xcoord);
		setWidth(width);
		setHelpHtml(helpHtml);
		setColumn_no(column_no);
		setProject_id(project_id); 
		setKnownData(knownData);
	}

	
	public FieldModel(Element item) {
		
		title = DataImporter.getValue((Element) item.getElementsByTagName("title").item(0));
		xcoord = Integer.parseInt(DataImporter.getValue((Element) item.getElementsByTagName("xcoord").item(0)));
		width = Integer.parseInt(DataImporter.getValue((Element) item.getElementsByTagName("width").item(0)));
		helpHtml = DataImporter.getValue((Element) item.getElementsByTagName("helphtml").item(0));
	
		if(item.getElementsByTagName("knowndata").item(0) != null)
			knownData = DataImporter.getValue((Element) item.getElementsByTagName("knowndata").item(0));
			//System.out.println(knownData);
    }


	public int getField_id() {
		return field_id;
	}
	
	public void setField_id(int field_id) {
		this.field_id = field_id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getXcoord() {
		return xcoord;
	}
	
	public void setXcoord(int xcoord) {
		this.xcoord = xcoord;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	
	public int getProject_id() {
		return project_id;
	}
	
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public String getHelpHtml() {
		return helpHtml;
	}

	public void setHelpHtml(String helpHtml) {
		this.helpHtml = helpHtml;
	}

	public String getKnownData() {
		return knownData;
	}

	public void setKnownData(String knownData) {
		this.knownData = knownData;
	}


	public int getColumn_no() {
		return column_no;
	}


	public void setColumn_no(int column_no) {
		this.column_no = column_no;
	}

}
