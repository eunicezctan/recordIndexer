package shared.communication;

import java.util.ArrayList;
import java.util.List;

import shared.model.BatchModel;
import shared.model.FieldModel;
import shared.model.ProjectModel;

/**
 * Communicator class that communicate with the client
 * Return a batch and a list of fields associate with the project id
 *
 */

public class DownloadBatchResult {

	private ArrayList<Integer> getXcoord =new ArrayList<Integer>();
	private ArrayList<Integer> getWidth = new ArrayList<Integer>();
	private ArrayList<String> getCname = new ArrayList<String>();
	private ArrayList<String> getChelp = new ArrayList<String>();
	private ArrayList<String> getKnown = new ArrayList<String>();
	private BatchModel batch;
	private List<FieldModel> field;
	private ProjectModel project;
	private String hostURL;
	private String ImageUrl;	
	private int batch_id;
	private int project_id;
	private int recordsPerImage; 
	private int firstYcoord;
	private int recordHeight;
	private int fieldSize;
	private int setIndex;
	
	
	public ArrayList<String> getGetChelp() {
		return getChelp;
	}
	public void setGetChelp(ArrayList<String> getChelp) {
		this.getChelp = getChelp;
	}
	public ArrayList<String> getGetCname() {
		return getCname;
	}
	public void setGetCname(ArrayList<String> getCname) {
		this.getCname = getCname;
	}
	public String getHostURL() {
		return hostURL;
	}
	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}
	
	public int getSetIndex() {
		return setIndex;
	}
	
	public void setSetIndex(int setIndex) {
		this.setIndex = setIndex;
	}
	
	public DownloadBatchResult(){
		this.setIndex = 0;
	}
	
	public BatchModel getBatch() {
		return batch;
	}
	
	public void setBatch(BatchModel batch) {
		this.batch = batch;
	}
	
	public List<FieldModel> getField() {
		return field;
	}
	
	public void setField(List<FieldModel> field) {
		this.field = field;
	}
	
	public ProjectModel getProject() {
		return project;
	}
	
	public void setProject(ProjectModel project) {
		this.project = project;
	}
	
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public int getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public int getRecordsPerImage() {
		return recordsPerImage;
	}
	public void setRecordsPerImage(int recordsPerImage) {
		this.recordsPerImage = recordsPerImage;
	}
	public int getFirstYcoord() {
		return firstYcoord;
	}
	public void setFirstYcoord(int firstYcoord) {
		this.firstYcoord = firstYcoord;
	}
	public int getRecordHeight() {
		return recordHeight;
	}
	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}
	public int getFieldSize() {
		return fieldSize;
	}
	public void setFieldSize(int fieldSize) {
		this.fieldSize = fieldSize;
	}		
	
	
	public void setParam() {
		batch_id = batch.getBatch_id();
		project_id = project.getProject_id();
		ImageUrl = hostURL + batch.getFile(); 
		firstYcoord = project.getFirstycoord();
		recordHeight = project.getRecordheight();
		recordsPerImage = project.getRecordsperimage();
		fieldSize = field.size();
		
		for(FieldModel getField:field)
		{
			getCname.add(getField.getTitle());
			getXcoord.add(getField.getXcoord());
			getWidth.add(getField.getWidth());
			getChelp.add(hostURL + getField.getHelpHtml());
			
			if(getField.getKnownData() != null)
				getKnown.add(hostURL + getField.getKnownData());
			else 
				getKnown.add(null);
		}

	}
	
	public ArrayList<Integer> getGetXcoord() {
		
		return getXcoord;
	}
	public void setGetXcoord(ArrayList<Integer> getXcoord) {
		this.getXcoord = getXcoord;
	}
	public ArrayList<Integer> getGetWidth() {
		return getWidth;
	}
	public void setGetWidth(ArrayList<Integer> getWidth) {
		this.getWidth = getWidth;
	}

	
	public ArrayList<String> getGetKnown() {
		return getKnown;
	}
	public void setGetKnown(ArrayList<String> getKnown) {
		this.getKnown = getKnown;
	}
	@Override
    public String toString() {
	  
		StringBuilder getString= new StringBuilder ();
		
		if(setIndex==1)
		{
			getString.append(batch_id+"\n");
			getString.append(project_id+"\n");
			getString.append(ImageUrl+"\n");
			getString.append(firstYcoord+"\n");
			getString.append(recordHeight+"\n");
			getString.append(recordsPerImage+"\n");
			getString.append(fieldSize+"\n");
			
			for(FieldModel getField:field)
			{
				getString.append(getField.getField_id()+"\n");
				getString.append(getField.getColumn_no()+"\n");
				getString.append(getField.getTitle()+"\n");
				getString.append(hostURL + getField.getHelpHtml()+"\n");
				getString.append(getField.getXcoord()+"\n");
				getString.append(getField.getWidth()+"\n");
				
				if(getField.getKnownData() != null)
					getString.append(hostURL + getField.getKnownData()+"\n");
			}
			
			return getString.toString();	
		}
		else
			return "FAILED\n";
	}

}
