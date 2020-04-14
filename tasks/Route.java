package tasks;



import org.jgrapht.GraphPath;

import input.Location;
import input.Street;

public class Route extends Task
{
	public Location source;
	public Location target;
	public GraphPath<Location,Street> result;
	
	public Route(Location source,Location target)
	{
		this.source = source;
		this.target = target;
		this.result = null;
	}

}
