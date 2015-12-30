package shared.communication;

import java.util.List;

import shared.model.ProjectModel;

/**
 * Communicator class that communicate with the client
 * Return a list of ProjectModel from Project table
 *
 */

public class GetProjectsResult {

	private List<ProjectModel> project;
	private int setIndex;
	
	public GetProjectsResult(){
		this.setIndex = 0;
	}

	public int getSetIndex() {
		return setIndex;
	}

	public void setSetIndex(int setIndex) {
		this.setIndex = setIndex;
	}

	public List<ProjectModel> getProject() {
		return project;
	}

	public void setProject(List<ProjectModel> project) {
		this.project = project;
	}

	@Override
    public String toString() {
		
		StringBuilder getString= new StringBuilder ();
		
		if(setIndex==1)
		{
			for(ProjectModel getProject:project)
			{
				getString.append(getProject.getProject_id()+"\n");
				getString.append(getProject.getTitle()+"\n");		
			}
			return getString.toString();	
		}
		else
			return "FAILED\n";
    }
	

}
