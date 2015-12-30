package shared.model;

/**
 * SearchModel class that set and store the data from the Search function in Record and Batch table in the Database
 * using the getter and setter. It parameters are model after the fields in the Record and Batch table.
 *
 */

public class SearchModel{
	
	private int batch_id;
	private String file;
	private int record_no; 
	private int field_id;
	
	public SearchModel(int batch_id, String file, int record_no, int field_id) {
		
		setBatch_id(batch_id);
		setFile(file);
		setRecord_no(record_no);
		setField_id(field_id);	
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
	

}
