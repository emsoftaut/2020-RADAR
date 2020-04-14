package input;

import java.util.ArrayList;

public class Location {

	//attributes
	public String label;
	public boolean available;

	public IntegerAttribute E;//equipment resources	
	public IntegerAttribute M;//medical supply resources
	public IntegerAttribute S;//sustenance resources
		

	public ArrayList<Tag> tags;
	//------------------------------------------------------
	public String toString()
	{
		//String attrs = "E="+this.E.value+",M="+this.M.value+",S="+this.S.value;
		//return this.label+"("+attrs+")";
		return this.label;
	}
	//------------------------------------------------------
	public Location(String label,int e,int m,int s)
	{
		this(label);
		this.E = new IntegerAttribute(Variable.E,this,e);
		this.M = new IntegerAttribute(Variable.M,this,m);
		this.S = new IntegerAttribute(Variable.S,this,s);	
	}
	//------------------------------------------------------
	public Location(String label)
	{
		this.label = label;
		this.tags = new ArrayList<Tag>();
		this.available=true;
		this.E = new IntegerAttribute(Variable.E,this,0);
		this.M = new IntegerAttribute(Variable.M,this,0);
		this.S = new IntegerAttribute(Variable.S,this,0);
	}
	//------------------------------------------------------
}
