package shared.communication;

import java.util.List;

import shared.model.SearchModel;

/**
 * Communicator class that communicate with the client
 * Return a list of BatchModel and RecordModel for successful search
 *
 */

public class GetSearchResult {
	
	private List<SearchModel> search;
	private String hostURL;
	private int setIndex;
	
	public GetSearchResult(){
		this.setIndex = 0;
	}
	
	
	public List<SearchModel> getSearch() {
		return search;
	}


	public void setSearch(List<SearchModel> search) {
		this.search = search;
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
	
	
	@Override
    public String toString() {
		
		StringBuilder getString= new StringBuilder ();
		
		if(setIndex==1)
		{
			for(SearchModel getSearch:search)
			{
				getString.append(getSearch.getBatch_id()+"\n");
				getString.append(hostURL + getSearch.getFile()+"\n");			
				getString.append(getSearch.getRecord_no()+"\n");
				getString.append(getSearch.getField_id() +"\n");	
			}
				
			return getString.toString();	
		}
		else
			return "FAILED\n";
    }

}
