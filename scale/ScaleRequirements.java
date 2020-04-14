package scale;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import org.jgrapht.graph.AsSubgraph;

import com.microsoft.z3.IntExpr;

import input.Location;
import input.RegionGraph;
import input.Street;
import input.Variable;
import metropolis.Metropolis;
import metropolis.Requirements;
import radar.SMTProblem;
import updates.LocaleResource;

public class ScaleRequirements extends Requirements
{

	public int partSize;
	private ArrayList<ArrayList<Location>> partition;


	public ScaleRequirements(int partSize)
	{
		super();
		this.partSize = partSize;
		this.partition = new ArrayList<ArrayList<Location>>();

	}

	//-------------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * */
	public void requirement2Equality(ArrayList<Location> part)
	{	
		//create a new subgraph
		HashSet<Location> locations = new HashSet<Location>();
		locations.addAll(part);		
		AsSubgraph<Location,Street> sg = new AsSubgraph<Location,Street>(this.regionGraph.masterGraph,locations);

		//create a new smt problem for this subgraph as follows:		
		SMTProblem smt = new SMTProblem();

		//create a new local resouce problem and add it to the solver
		LocaleResource lr = new LocaleResource(sg,smt);
		this.synth.addLocaleProblem(lr);

		ArrayList<IntExpr> varsE = new ArrayList<IntExpr>();
		ArrayList<IntExpr> varsS = new ArrayList<IntExpr>();
		ArrayList<IntExpr> varsM = new ArrayList<IntExpr>();
		for(Location a : locations)
		{
			//declare the var
			IntExpr aExpr_E = this.synth.declareSMTProblemVariable(Variable.E, a, smt);			
			IntExpr aExpr_S = this.synth.declareSMTProblemVariable(Variable.S, a, smt);
			IntExpr aExpr_M = this.synth.declareSMTProblemVariable(Variable.M, a, smt);

			//add it to lists of vars
			varsE.add(aExpr_E);
			varsS.add(aExpr_S);
			varsM.add(aExpr_M);

			//default constraints on vars
			this.synth.defaultRequirements(smt,aExpr_E);
			this.synth.defaultRequirements(smt,aExpr_S);
			this.synth.defaultRequirements(smt,aExpr_M);	
		}
		
		equality(smt,varsE);
		
		
		//System.out.println(smt);
		
		equality(smt,varsS);
		equality(smt,varsM);
	}
	//-------------------------------------------------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------------------------------------------------
	private void equality(SMTProblem smt,ArrayList<IntExpr> vars)
	{
		Iterator<IntExpr> iter = vars.iterator();
		IntExpr a = iter.next();
		//System.out.println(vars);
		while(iter.hasNext())
		{
			IntExpr b = iter.hasNext()? iter.next():null;
			if(b!= null)
			{
				this.synth.equals(smt,a,b);
		//		System.out.println(a+" = "+b);
			}
			a = b;
		}		
	}
	//-------------------------------------------------------------------------------------------------------------------
	public void requirements(ArrayList<Location> part)
	{
		//create a new subgraph
		HashSet<Location> aLocations = new HashSet<Location>();
		aLocations.addAll(part);

		//aLocations.add(Metropolis.a);
		AsSubgraph<Location,Street> sga = new AsSubgraph<Location,Street>(this.regionGraph.masterGraph,aLocations);


		//create a new smt problem for this subgraph as follows:		
		SMTProblem smta = new SMTProblem();

		//create a new local resouce problem and add it to the solver
		LocaleResource lra = new LocaleResource(sga,smta);
		this.synth.addLocaleProblem(lra);


		//z1 = z2 = z3 = z4
		//for each pair of variables we suppose there is a relationship.
		//z1 > z2, z2 = z3, z3 < z4
		ArrayList<IntExpr> varsE = new ArrayList<IntExpr>();
		ArrayList<IntExpr> varsS = new ArrayList<IntExpr>();
		ArrayList<IntExpr> varsM = new ArrayList<IntExpr>();
		for(Location a : aLocations)
		{
			//declare the var
			IntExpr aExpr_E = this.synth.declareSMTProblemVariable(Variable.E, a, smta);			
			IntExpr aExpr_S = this.synth.declareSMTProblemVariable(Variable.S, a, smta);
			IntExpr aExpr_M = this.synth.declareSMTProblemVariable(Variable.M, a, smta);

			//add it to lists of vars
			varsE.add(aExpr_E);
			varsS.add(aExpr_S);
			varsM.add(aExpr_M);

			//default constraints on vars
			this.synth.defaultRequirements(smta,aExpr_E);
			this.synth.defaultRequirements(smta,aExpr_S);
			this.synth.defaultRequirements(smta,aExpr_M);		
		}



		/*//equipment = 100
		IntExpr aExpr_E = this.synth.declareSMTProblemVariable(Variable.E, Metropolis.a, smta);
		this.synth.defaultRequirements(smta,aExpr_E);
		this.synth.setValue(smta,aExpr_E,100);		

		//sustanance = 100
		IntExpr aExpr_S = this.synth.declareSMTProblemVariable(Variable.S, Metropolis.a, smta);
		this.synth.defaultRequirements(smta,aExpr_S);
		this.synth.setValue(smta,aExpr_S,100);


		//medicine = 100
		IntExpr aExpr_M = this.synth.declareSMTProblemVariable(Variable.M, Metropolis.a, smta);
		this.synth.defaultRequirements(smta,aExpr_M);
		this.synth.setValue(smta,aExpr_M,100);*/

	}

	@Override
	public void synthesise() 
	{		
		RegionGraph g = this.synth.solver.graph;		
		int vertexCount = 0;
		ArrayList<Location> part = new ArrayList<Location>();
		partition.add(part);

		for(Location l : g.masterGraph.vertexSet())
		{			
			if(vertexCount >= partSize)
			{
				part = new ArrayList<Location>();
				partition.add(part);
				vertexCount=0;
			}			
			vertexCount++;
			part.add(l);	
		}
		System.out.println("Total number of vertices: "+g.masterGraph.vertexSet().size());
		System.out.println("Total number of partitions: "+partition.size()+" with "+partition.get(0).size()+" vertices each");
		for(ArrayList<Location> p : partition)
		{
			requirement2Equality(p);
		}
	}

}
