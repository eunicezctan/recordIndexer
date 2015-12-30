package shared.communication;

/**
* Communicator class that communicate with the client
* Return boolean for result of the operation
*
*/

public class SubmitBatchResult {

	private int setIndex;
	private int totalIndex;
	
	public int getSetIndex() {
		return setIndex;
	}

	public void setSetIndex(int setIndex) {
		this.setIndex = setIndex;
	}

	public SubmitBatchResult(){
		this.setIndex = 0;
	}

	public int getTotalIndex() {
		return totalIndex;
	}

	public void setTotalIndex(int totalIndex) {
		this.totalIndex = totalIndex;
	}

	@Override
    public String toString() {
		
		if(setIndex==1)
			return 	"TRUE\n";
		
		else
			return "FAILED\n";

  }
	
}
