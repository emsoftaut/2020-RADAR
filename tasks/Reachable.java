package tasks;


import input.Location;


public class Reachable extends Task{
	public Location source;
	public Location target;
	public Boolean result;
	
	public Reachable(Location source,Location target)
	{
		this.source = source;
		this.target = target;
		this.result = null;
	}
	

}
