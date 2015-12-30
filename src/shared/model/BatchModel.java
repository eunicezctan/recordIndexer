package shared.model;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import server.database.DataImporter;

/**
 * BatchModel class that set and store the data from the Batch table in the Database
 * using the getter and setter. It parameters are model after the fields in the Batch table.
 *
 */

public class BatchModel {
	
	private int batch_id;
	private String file; 
	private int project_id;
	private int status;
	
	ArrayList<RecordModel> record = new ArrayList<RecordModel>();
	
	public BatchModel(){
		setBatch_id(-1);
		setFile("New URL");
		setProject_id(-1);
		setStatus(-1); 
	}
	
	public BatchModel(int batch_id, String file, int project_id, int status) {
		setBatch_id(batch_id);
		setFile(file);
		setProject_id(project_id);
		
	}

	public BatchModel(Element item) {
		
		file = DataImporter.getValue((Element) item.getElementsByTagName("file").item(0));
		
		if(item.getElementsByTagName("record").item(0) != null)
		{	
			setStatus(-1);
			NodeList RecordElements = item.getElementsByTagName("value");	
			for(int i = 0; i < RecordElements.getLength(); i++) 
			{
				record.add(new RecordModel((Element) RecordElements.item(i)));
			}	
		}
		else
			setStatus(1);
		
    }

	public int getBatch_id() {
		return batch_id;
	}
	
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	
	public String getFile() {
		return file;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	public int getProject_id() {
		return project_id;
	}
	
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ArrayList<RecordModel> getRecord() {
		return record;
	}

	public void setRecord(ArrayList<RecordModel> record) {
		this.record = record;
	}
	
	
	
}//end of Class BatchModel
