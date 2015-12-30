package shared.model;

import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 * RecordModel class that set and store the data from the Record table in the Database
 * using the getter and setter. It parameters are model after the fields in the Record table.
 *
 */

public class RecordModel {
	private int record_id;
	private String value;
	private int record_no; 
	private int field_id;
	private int batch_id;
	

	ArrayList<RecordModel> record = new ArrayList<RecordModel>();
	
	public RecordModel() {
		value=null;
    }
	
public RecordModel(String value, int record_no, int field_id, int batch_id) {
		
		setValue(value);
		setRecord_no(record_no);
		setField_id(field_id);
		setBatch_id(batch_id);
    }
	
	public RecordModel(int record_id, String value, int record_no, int field_id, int batch_id) {
		
		setRecord_id(record_id);
		setValue(value);
		setRecord_no(record_no);
		setField_id(field_id);
		setBatch_id(batch_id);
    }

	public RecordModel(Element item) {
		
		value = item.getTextContent();
		//System.out.println(value);
    }

	
	public int getRecord_id() {
		return record_id;
	}

	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getRecord_no() {
		return record_no;
	}
	
	public void setRecord_no(int record_no) {
		this.record_no = record_no;
	}
	
	public int getField_id() {
		return field_id;
	}
	
	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	public int getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	
}
