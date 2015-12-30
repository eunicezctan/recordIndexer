package shared.communication;


/**
 * Communicator class that communicate with the client
 * Return a URL of the sample image for the specific project
 *
 */

public class GetSampleImageResult {

	private String imageURL;
	private int setIndex;
	
	public 	GetSampleImageResult(){
		this.setIndex = 0;	
	}
	
	public int getSetIndex() {
		return setIndex;
	}

	public void setSetIndex(int setIndex) {
		this.setIndex = setIndex;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	@Override
    public String toString() {
		
		if(setIndex==1)
			return imageURL+"\n";
		else 
			return "FAILED\n";
    }
	
	
	
	
}
