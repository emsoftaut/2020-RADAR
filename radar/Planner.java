package radar;

import tasks.Reachable;
import tasks.Deliver;
import tasks.Task;
import tasks.Route;

/**
 * 
 * Manages a library of graph algorithms.
 * 
 * Takes as input a task and applies an algorithm with the appropriate input
 *  
 * 
 * */
public class Planner
{	
	public Solver solver;
	//------------------------------------------------------------------------------------------------------
	public Planner(Solver solver)
	{
		this.solver = solver;
	}
	//------------------------------------------------------------------------------------------------------
	/**
	 * Takes as input a task and associates the task to a graph algorithm,
	 * with appropriate input and output
	 * 
	 * */
	public void compute(Task task)
	{
		if(task instanceof Reachable)
		{
			Reachable r = (Reachable) task;
			r.result = solver.graph.getShortestPath(r.source, r.target)==null?false:true;		
		}
		else
			if(task instanceof Route)
			{
				Route t = (Route) task;
				t.result = solver.graph.getShortestPath(t.source, t.target);
				System.out.println(t.result);
			}
			else
				if(task instanceof Deliver)
				{
					Deliver rd = (Deliver) task;				
					this.compute(rd.traverse);
					rd.result = this.solver.getAudit(rd);
				}

	}
}
