package scale;


import input.RegionGraph;
import metropolis.RegionMap;

public class ScaleRegion extends RegionMap
{


	public int size=100;
	
	public ScaleRegion(int size)
	{
		
		super();
		this.size = size;
		this.graph();
	}
	
	@Override
	public RegionGraph graph() 
	{
		RegionGraph region = new RegionGraph();		
		region.masterGraph = GenCompleteGraph.generate(size);	
		return region;
	}
	
	
	

}
