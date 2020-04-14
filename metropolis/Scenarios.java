package metropolis;

import input.Location;
import radar.Radar;
import tasks.Reachable;
import tasks.Deliver;
import tasks.Route;
import updates.Availability;
import updates.LocaleResource;

public class Scenarios {	

	//------------------------------------------------------------------------
	public static void main(String[] args)
	{

		//obtain the input for RADAR
		RegionMap met = new Metropolis();
		MetropolisRequirements metRequirements = new MetropolisRequirements();
		Radar radar = new Radar(met,metRequirements);

		
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("Initialisation");
		//carry out the initialise step
		long start=System.currentTimeMillis();
		radar.initialise();
		long stop = System.currentTimeMillis();
		long dur = stop - start;
		System.out.println("Duration for initialation in MS: "+dur);

		//print out the current graph
		System.out.println("Metropolis graph:"+radar.graph);

		//print the initial assignment
		/*
		for(Location l : met.graph().masterGraph.vertexSet())
		{
			System.out.println(l);
			System.out.println("Sustenance: "+l.S);
			System.out.println("Medicine: "+l.M);
			System.out.println("Equipment: "+l.E);
		}
		 */
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("Reachability Task");
		//Give RADAR a Reachable task
		start=System.currentTimeMillis();
		Reachable reachFromAtoF = new Reachable(Metropolis.a,Metropolis.f);
		radar.compute(reachFromAtoF);
		stop = System.currentTimeMillis();
		dur = stop-start;
		System.out.println("Is there a path from location a to f: "+reachFromAtoF.result+" "+dur+" MS");
		 
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("Route Task");
		//Give RADAR a Route Task between a and f. 	
		start=System.currentTimeMillis();
		Route routeFromAtoF = new Route(Metropolis.a,Metropolis.f);
		//invoke the planner with the task
		radar.compute(routeFromAtoF);
		stop = System.currentTimeMillis();
		dur = stop-start;
		//task t contains the result: Result is the shortest path	[(a : b), (b : c), (c : e), (e : f)]
		System.out.println("Shortest path from location a to f: "+routeFromAtoF.result+" with weight: "+routeFromAtoF.result.getWeight()+" "+dur+" MS");

		
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("Deliver Task (a,f)");
		//Give RADAR a Deliver Task between a and f.
		//suppose we must deliver E=0,M=10,S=12 from location a to location f.
		System.out.println("Deliver E=0,M=10,S=12 resources from location a to location f");
		int E=0;
		int M=10;
		int S=12;
		Deliver rd = new Deliver(Metropolis.a,Metropolis.f,E,M,S);
		radar.compute(rd);
		System.out.println("Shortest path from location a to f: "+rd.traverse.result+" with weight: "+rd.traverse.result.getWeight());
		System.out.println("Audit result from location f is: "+rd.result);


		System.out.println("\n\n\n-----------------------------------------------------------------------------");
		System.out.println("Availability Update: Wellesley Bridge made inaccesible: edge c <-> e");
		//--------
		//Update Scenarios
		//now suppose that edge c<->e is unavailable, e.g. the bridge
		//We formulate this update by an Availability object.
		Availability a = new Availability("Wellesley",false);		
		start=System.currentTimeMillis();
		radar.update(a);//update the graph
		stop =System.currentTimeMillis();
		dur = stop-start;
		System.out.println("-----------------------------------------------------------------------------");
		
		
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("Deliver Task (a,f)");
		//Give RADAR a Route Task between a and f.
		radar.compute(routeFromAtoF);//compute the task again
		//the task result contains the shortest path: [(a : d)(true), (d : g)(true), (g : f)(true)]
		System.out.println("Shortest path from location a to f: "+routeFromAtoF.result+" with weight: "+routeFromAtoF.result.getWeight());

		
		System.out.println("-----------------------------------------------------------------------------");
		//weaken the constraints on the supply a
		LocaleResource a2 = radar.solver.findLocaleResourceProblemFor(Metropolis.a);
		metRequirements.updateRequirement1(a2);
		//System.out.println(a2); 
		radar.update(a2);


		System.out.println("-----------------------------------------------------------------------------");
		//update constraints for b and c
		System.out.println("update constraints for locations b and c (and e)");
		//returns a new constraint problem involving locations b,c,e
		LocaleResource ax = metRequirements.updateRequirementsForBandC(radar.solver);
		radar.update(ax);

		//print out the current graph
		System.out.println("Metropolis graph:"+radar.graph);

		
		System.out.println("-----------------------------------------------------------------------------");
		//now demonstrate that a to f is made inaccessible by removal of both paths.
		Availability a3 = new Availability("Northwestern",false);
		System.out.println("Northwestern Road made inaccesible");
		radar.update(a3);
		radar.planner.compute(reachFromAtoF);//compute the task
		System.out.println("Is there a path from location a to f: "+reachFromAtoF.result);
		System.out.println("-----------------------------------------------------------------------------");

	}

}
