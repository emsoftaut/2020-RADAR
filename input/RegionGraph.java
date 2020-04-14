package input;


import java.util.ArrayList;
import java.util.HashSet;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class RegionGraph {

	//the given input graph, whose attributes are updated as new knowledge is supplied
	public DefaultDirectedWeightedGraph<Location,Street> masterGraph;
	
	//the current subgraph is the graph we perform all algorithms on
	//it is generated before an algorithm is performed and set to null afterwards
	private AsSubgraph<Location,Street> currentSubgraph;
	
	//----------------------------------------------------------------------------------------
	public GraphPath<Location,Street> getShortestPath(Location source, Location target)
	{	
		this.currentSubgraph = this.generateCurrentSubgraph();
		BellmanFordShortestPath<Location,Street> bfsp =  new BellmanFordShortestPath<Location,Street>(this.currentSubgraph);
		GraphPath<Location,Street> path = bfsp.getPath(source, target);
		this.currentSubgraph=null;
		return path;
	}
	//----------------------------------------------------------------------------------------
	public ArrayList<GraphPath<Location,Street>> getAllDirectedPaths(Location source, Location target)
	{
		this.currentSubgraph = this.generateCurrentSubgraph();
		AllDirectedPaths<Location,Street> ds = new AllDirectedPaths<Location,Street>(this.currentSubgraph);
		ArrayList<GraphPath<Location,Street>> paths = (ArrayList<GraphPath<Location,Street>>) ds.getAllPaths(source, target, true, null); 
		this.currentSubgraph=null;
		
		return paths; 
	}
	//----------------------------------------------------------------------------------------
	private AsSubgraph<Location,Street> generateCurrentSubgraph()
	{

		//create node subset based on availability
		HashSet<Location> locations = new HashSet<Location>();
		for(Location l : this.masterGraph.vertexSet())
		{
			if(l.available)
			{
				locations.add(l);
			}
		}

		//create street subset based on availability
		HashSet<Street> streets = new HashSet<Street>();
		for(Street s : this.masterGraph.edgeSet())
		{
			if(s.available)
			{
				streets.add(s);
			}
		}
		AsSubgraph<Location,Street> ag = new AsSubgraph<Location,Street>(this.masterGraph,locations,streets);
		return ag;
	}
	//------------------------------------------------------------------------------------------
	public ArrayList<Street> getMatchingStreets(String label) 
	{
		ArrayList<Street> streets = new ArrayList<Street>();
		for(Street s : this.masterGraph.edgeSet())
		{
			if(s.label.equalsIgnoreCase(label))
			{		
				streets.add(s);
			}
		}
		return streets;
	}
	//------------------------------------------------------------------------------------------
	public Location getLocation(String label)
	{
		for(Location l : this.masterGraph.vertexSet())
		{
			if(l.label.equalsIgnoreCase(label))
			{
				return l;
			}
		}
		return null;
	}
	//------------------------------------------------------------------------------------------
	public String toString()
	{
		return this.masterGraph.toString();
	}
	//------------------------------------------------------------------------------------------
	public RegionGraph()
	{
		this.masterGraph = new DefaultDirectedWeightedGraph<Location,Street>(Street.class);
	}
	//------------------------------------------------------------------------------------------
	public void addVertex(Location x)
	{
		this.masterGraph.addVertex(x);
	}
	//------------------------------------------------------------------------------------------
	public void addBiEdge(Location x,Location y,double weight,String label)
	{	
		Street sxy = this.masterGraph.addEdge(x,y);
		sxy.label = label;
		Street syx = this.masterGraph.addEdge(y,x);
		syx.label = label;

		this.masterGraph.setEdgeWeight(sxy, weight);
		this.masterGraph.setEdgeWeight(syx, weight);

	}

}