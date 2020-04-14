package radar;

import java.util.ArrayList;
import input.Location;
import input.RegionGraph;
import tasks.Audit;
import tasks.Deliver;
import updates.LocaleResource;

/**
 * 
 * 
 * 
 * 
 * 
 * 
 * */
public class Solver 
{
	public ArrayList<LocaleResource> graphConstraints;	
	public RegionGraph graph;
	
	
	//----------------------------------------------------------------------------------------
	public Audit getAudit(Deliver rd)
	{	
		Audit a = new Audit();
		
		rd.traverse.source.E.value -= rd.E;
		rd.traverse.source.M.value -= rd.M;
		rd.traverse.source.S.value -= rd.S;
		
		rd.traverse.target.E.value += rd.E;
		rd.traverse.target.M.value += rd.M;
		rd.traverse.target.S.value += rd.S;
		
		a.E = rd.traverse.target.E;
		a.M = rd.traverse.target.M;
		a.S = rd.traverse.target.S;
		
		return a;
	}
	//----------------------------------------------------------------------------------------
	public Solver(RegionGraph graph)
	{
		this.graph = graph;
		//this.alloc = new HashMap<AsSubgraph<Location,Street>,SMTProblem>();
		this.graphConstraints = new ArrayList<LocaleResource>();
	}
	//----------------------------------------------------------------------------------------
	public void solve(SMTProblem prob)
	{
		prob.solve();
	}
	//----------------------------------------------------------------------------------------
	public void solve()
	{
		for(LocaleResource lr : this.graphConstraints)
		{			
			SMTProblem prob =lr.problem;						
			//System.out.println(lr.problem);
			this.solve(prob);					
		}		
	}
	//----------------------------------------------------------------------------------------
	public SMTProblem findSMTProblemFor(Location f)
	{
		for(LocaleResource lr : this.graphConstraints)
		{

			if(lr.sg.containsVertex(f))
			{
				return lr.problem;
			}
		}
		return null;
	}
	//----------------------------------------------------------------------------------------
	public LocaleResource findLocaleResourceProblemFor(Location g) 
	{
		for(LocaleResource lr : this.graphConstraints)
		{

			if(lr.sg.containsVertex(g))
			{
				return lr;
			}
		}
		return null;
	}

	
}