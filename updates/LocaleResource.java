package updates;

import org.jgrapht.graph.AsSubgraph;

import input.Location;
import input.Street;
import input.Tag;
import radar.SMTProblem;

public class LocaleResource extends Update
{
	
	public SMTProblem problem;
	public AsSubgraph<Location,Street> sg;
	
	
	
	public String toString()
	{
		return sg.vertexSet()+" "+problem.toString();
	}
	public boolean hasLocationWithTag(Tag tag)
	{
		
		for(Location l : this.sg.vertexSet())
		{
			if(l.tags.contains(tag))
			{
				return true;
			}

		}		
		return false;
	}
	
	public LocaleResource(AsSubgraph<Location,Street> sg,SMTProblem problem)
	{
		this.sg = sg;
		this.problem = problem;
		
	}

}
