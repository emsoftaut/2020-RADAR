package metropolis;

import input.Location;
import input.RegionGraph;
import input.Tag;

public class Metropolis extends RegionMap
{
	public static Location a = new Location("a");
	public static Location b = new Location("b");
	public static Location c = new Location("c");
	public static Location d = new Location("d");
	public static Location e = new Location("e");
	public static Location f = new Location("f");
	public static Location g = new Location("g");;
//	;;;;\\\\;;;;;;;;;;;;;;;;;;;;;;;;;;=[-pl[0ompl0-yu,9k y,kn,,o b;lgjm,,]]
	//------------------------------------------------------------------------------------------
	@Override
	public RegionGraph graph()
	{
		
		//add tags to important landmarks in map
		e.tags.add(Tag.Hospital);
		d.tags.add(Tag.Hospital);
		
		a.tags.add(Tag.Supply);
		
		RegionGraph region = new RegionGraph();
		
		//add map locations
		region.addVertex(a);
		region.addVertex(b);
		region.addVertex(c);
		region.addVertex(d);
		region.addVertex(e);
		region.addVertex(f);
		region.addVertex(g);
		
		region.addBiEdge(a, b,2.0,"Nelson");		
		region.addBiEdge(a, c,5.0,"Hobson");		
		region.addBiEdge(a, d,9.0,"KRoad");		
		region.addBiEdge(b, c,1.0,"Cook");		
		region.addBiEdge(c, d,6.0,"Queen");		
		region.addBiEdge(d, g,36.0,"Northwestern");		
		region.addBiEdge(c, e,4.0,"Wellesley");
		
		region.masterGraph.getEdge(c, e).tags.add(Tag.Bridge);
		region.masterGraph.getEdge(e, c).tags.add(Tag.Bridge);
		
		region.addBiEdge(e,g,11.0,"Symonds");
		region.addBiEdge(e,f,8.0,"Mayoral");		
		region.addBiEdge(g,f,4.0,"Wakefield");
		
		
		return region;
	}
	
		
}
