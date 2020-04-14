package tasks;

import input.IntegerAttribute;

public class Audit extends Task{
	public IntegerAttribute E;
	public IntegerAttribute M;
	public IntegerAttribute S;
	
	public String toString()
	{
		return "E = "+this.E+" M = "+M+" S = "+S;
	}

}
