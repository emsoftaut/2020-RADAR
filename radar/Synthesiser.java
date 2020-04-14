package radar;

import java.util.ArrayList;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
//import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;

import input.Location;
import input.RegionGraph;
import input.Street;
import input.Variable;
import metropolis.Requirements;
import updates.Availability;
import updates.LocaleResource;
import updates.Update;


/**
 * 
 * Takes as input  knowledge and synthesises an adaptation to the graph
 * contains methods that translate knowledge into graph transformations (adaptations). 
 * 
 * */

public class Synthesiser 
{
	public RegionGraph graph;
	public Requirements requirements;
	public Solver solver;
	//------------------------------------------------------------------------
	public Synthesiser(RegionGraph graph,Requirements requirements,Solver solver)
	{
		this.graph = graph;
		this.solver = solver;
		this.requirements = requirements;		
	}
	//------------------------------------------------------------------------
	public void update(Update u)
	{
		//change in resource constraints
		if(u instanceof LocaleResource)
		{
			LocaleResource lr = (LocaleResource) u;
			this.solver.solve(lr.problem);						
		}
		if(u instanceof Availability)
		{
			Availability a = (Availability) u;
			Location l = this.graph.getLocation(a.label);
			if(l != null)
			{
				l.available = a.status;	
			}
			else
			{
				ArrayList<Street> streets = this.graph.getMatchingStreets(a.label);
				for(Street s : streets)
				{
					s.available=a.status;
				}

			}

		}
	}

	//------------------------------------------------------------------------
	public void addLocaleProblem(LocaleResource l)
	{
		this.solver.graphConstraints.add(l);//mj0inu7,8
	}
	//----------------------------------------------------------------------------------------------------------------	

	//----------------------------------------------------------------------------------------------------------------
	public void defaultRequirements(SMTProblem smt,IntExpr var)
	{
		IntExpr val = smt.ctx.mkInt(0+"");
		BoolExpr assertEq = smt.ctx.mkGe(var,val);
		smt.solver.add(assertEq);
	}
	//----------------------------------------------------------------------------------------------------------------
	public void atMost(SMTProblem smt,IntExpr var,int value)
	{
		IntExpr val = smt.ctx.mkInt(value+"");
		BoolExpr assertEq = smt.ctx.mkLe(var,val);
		smt.solver.add(assertEq);
	}
	//----------------------------------------------------------------------------------------------------------------
	public void atLeast(SMTProblem smt,IntExpr var,int s)
	{
		IntExpr sVal = smt.ctx.mkInt(s+"");
		smt.solver.add(smt.ctx.mkGe(var,sVal));
	}
	//----------------------------------------------------------------------------------------------------------------
	public void between(SMTProblem smt,IntExpr var,int s,int e)
	{
		IntExpr sVal = smt.ctx.mkInt(s+"");
		IntExpr eVal = smt.ctx.mkInt(e+"");

		smt.solver.add(smt.ctx.mkGe(var,sVal));
		smt.solver.add(smt.ctx.mkLe(var,eVal));

	}
	//----------------------------------------------------------------------------------------------------------------
	public void equals(SMTProblem smt,IntExpr g,IntExpr f)
	{
		BoolExpr assertEq = smt.ctx.mkEq(f,g);
		smt.solver.add(assertEq);
	}
	//----------------------------------------------------------------------------------------------------------------
	//e.g. f = g+value 
	public void moreThan(SMTProblem smt,IntExpr g,IntExpr f,int value)
	{		
		IntExpr val = smt.ctx.mkInt(value);		
		ArithExpr addition = smt.ctx.mkAdd(g,val);		
		BoolExpr assertEq = smt.ctx.mkEq(f,addition);
		smt.solver.add(assertEq);
		//	System.out.println(assertEq);
	}
	//----------------------------------------------------------------------------------------------------------------
	public void greaterThan(SMTProblem smt,IntExpr var,int value)
	{
		IntExpr val = smt.ctx.mkInt(value+"");
		BoolExpr assertEq = smt.ctx.mkGt(var,val);
		smt.solver.add(assertEq);
	}
	//----------------------------------------------------------------------------------------------------------------
	public void setValue(SMTProblem smt,IntExpr var,int value)
	{					
		IntExpr val = smt.ctx.mkInt(value+"");
		BoolExpr assertEq = smt.ctx.mkEq(var,val);
		smt.solver.add(assertEq);
	}
	//----------------------------------------------------------------------------------------------------------------
	public IntExpr declareSMTProblemVariable(Variable v,Location l,SMTProblem smt)
	{		

		if(v == Variable.E)
		{
			String key = l.E.label;

			IntExpr x =  smt.ctx.mkIntConst(key);
			smt.problemVars.put(l.E,x);
			return x;
		}
		if(v == Variable.M)
		{
			String key = l.M.label;
			IntExpr x =  smt.ctx.mkIntConst(key);			
			smt.problemVars.put(l.M, x);
			return x;
		}
		if(v == Variable.S)
		{
			String key = l.S.label;
			IntExpr x =  smt.ctx.mkIntConst(key);			
			smt.problemVars.put(l.S, x);
			return x;
		}

		return null;
	}
	//----------------------------------------------------------------------------------------------------------------	
}
