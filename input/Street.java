package input;

import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Street extends DefaultWeightedEdge
{
	public String label;
	public boolean available;
	public ArrayList<Tag> tags;
	
	public String toString()
	{
		return super.toString();//+"("+this.available+")";
	}
	public Street()
	{
		super();
		this.label = "";
		this.tags = new ArrayList<Tag>(); 
		this.available=true;
	}
	
	public Street(String label)
	{
		super();
		this.label = label;
		this.tags = new ArrayList<Tag>(); 
		this.available=true;	
	}

}
