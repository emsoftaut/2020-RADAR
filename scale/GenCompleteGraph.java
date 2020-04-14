package scale;

import java.util.Iterator;
import java.util.function.Supplier;
import org.jgrapht.Graph;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.util.SupplierUtil;

import input.Location;
import input.Street;

public class GenCompleteGraph {
	
	
	
	
	public static DefaultDirectedWeightedGraph<Location, Street> generate(int size)
	{

		Supplier<Street> eSupplier = new Supplier<Street>()
		{
			private int id = 0;

			@Override
			public Street get()
			{
				Street s = new Street("v" + id++);
				
				return s;
			}
		};
		// Create the VertexFactory so the generator can create vertices
		Supplier<Location> vSupplier = new Supplier<Location>()
		{
			private int id = 0;

			@Override
			public Location get()
			{
				Location l = new Location("v" + id++);
				return l;
			}

		};

		// Create the graph object
		DefaultDirectedWeightedGraph<Location, Street> completeGraph =
				new DefaultDirectedWeightedGraph<Location,Street>(vSupplier,eSupplier);

		// Create the CompleteGraphGenerator object
		CompleteGraphGenerator<Location, Street> completeGenerator =
				new CompleteGraphGenerator<>(size);

		// Use the CompleteGraphGenerator object to make completeGraph a
		// complete graph with [size] number of vertices
		completeGenerator.generateGraph(completeGraph);

		return completeGraph;
	/*	// Print out the graph to be sure it's really complete
		Iterator<Location> iter = new DepthFirstIterator<>(completeGraph);
		while (iter.hasNext()) {
			Location vertex = iter.next();
			System.out.println(
					"Vertex " + vertex + " is connected to: "
							+ completeGraph.edgesOf(vertex).toString());
		}*/
	}
}


