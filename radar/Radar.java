package radar;

import input.RegionGraph;
import metropolis.RegionMap;
import metropolis.Requirements;
import tasks.Task;
import updates.Update;

/**
 * 
 * 
 * Radar Framework
 * 
 * TO USE Z3 add the following environment variable: DYLD_LIBRARY_PATH with value /Users/kjohnson/microsoft-z3/bin
 * 
 * */
public class Radar 
{
	public Planner planner;
	public Synthesiser synthesiser;
	public Solver solver;
	
	public RegionMap map;
	public RegionGraph graph;
	public Requirements requirements;
	//------------------------------------------------------------------------	
	public void compute(Task task)
	{
		this.planner.compute(task);
	}
	
	//------------------------------------------------------------------------
	public void update(Update u)
	{
		this.synthesiser.update(u);
	}
	//------------------------------------------------------------------------
	/**
	 * Apply Radar to the a graph and it's preparedness requirements.
	 * 
	 * */
	public Radar(RegionMap map,Requirements requirements)
	{		
		this.map = map;
		this.graph = map.graph();
		this.requirements = requirements;
		this.solver = new Solver(graph);
		this.planner = new Planner(this.solver);
		this.synthesiser = new Synthesiser(graph,requirements,solver);		
	}
	//------------------------------------------------------------------------
	public Radar(Synthesiser synth, Planner planner, Solver solver) 
	{
		this.synthesiser = synth;

		//grab the graph + requirements from the synth for convienence.
		this.graph = this.synthesiser.graph;
		this.requirements = this.synthesiser.requirements;

		this.planner = planner;
		this.solver = solver;
	}	
	//------------------------------------------------------------------------
	public void initialise() 
	{
		this.requirements.synth=this.synthesiser;
		this.requirements.regionGraph=this.synthesiser.graph;
		
		this.requirements.synthesise();
		this.solver.solve();
	}
	
}
