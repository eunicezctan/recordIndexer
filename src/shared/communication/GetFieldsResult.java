package shared.communication;

import java.util.List;

import shared.model.FieldModel;


/**
 * Communicator class that communicate with the client
 * Return project id, field id and field title
 *
 */

public class GetFieldsResult {
	

	private List<FieldModel> field;
	private int setIndex;
	
	public GetFieldsResult(){
		this.setIndex = 0;
	}
	
	public int getSetIndex() {
		return setIndex;
	}
	public void setSetIndex(int setIndex) {
		this.setIndex = setIndex;
	}
	public List<FieldModel> getField() {
		return field;
	}
	public void setField(List<FieldModel> field) {
		this.field = field;
	}
	
	
	@Override
    public String toString() {
		
		StringBuilder getString= new StringBuilder ();
		
		if(setIndex==1)
		{
			for(FieldModel getField:field)
			{
				getString.append(getField.getProject_id()+"\n");
				getString.append(getField.getField_id()+"\n");
				getString.append(getField.getTitle()+"\n");
			}
			return getString.toString();	
		}
		else
			return "FAILED\n";
    }	

}
