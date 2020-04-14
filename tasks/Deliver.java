package tasks;

import input.Location;


public class Deliver extends Task{

	public Route traverse;
	public int E;//equipment
	public int M;//medicine
	public int S;//sustenance.
	public Audit result;
	
	//----------------------------------------------------------------------
	public Deliver(Location source,Location target,int E,int M,int S)
	{
		this.traverse = new Route(source,target);		
		
		this.E=E;
		this.M=M;
		this.S=S;
		
		this.result = null;
	}
	//----------------------------------------------------------------------
}
