package metropolis;


import java.util.HashSet;
import org.jgrapht.graph.AsSubgraph;
import com.microsoft.z3.IntExpr;
import input.Location;
import input.Street;
import input.Tag;
import input.Variable;
import radar.Solver;
import updates.LocaleResource;
import radar.SMTProblem;

/**
 *  E and D are hospitals
 *  C and E are on the bridge
 *  A is a supply
 *  
 * 	R1 shipping location A has 100 units of medicine, sustenance and equipment
 *  R2 locations connected via the bridge have the same units of sustenance	(C & E)
 *  R3 hospitals receive at least 20 units of medicine, 15 of sustenance and 23 units of equipment (D,E)
    R4 location G receives between 7 and 10 units of medicine 
	R5 location F receives 5 more units of medicine & sustenence than location G	
	R6 locations within 8KM of A receives at least 40 units of sustenance (B,C) 
	R7 Only hospital or supply locations can receive equipment resources (B,C,F,G have 0 equipment)
 * 
 * */
public class MetropolisRequirements  extends Requirements
{	

	//----------------------------------------------------------------------------------------------------------------
	/**
	 * Updates requirement 1 for location a
	 * which weakens the original preparedness requirements.
	 * 
	 * */
	public void updateRequirement1(LocaleResource lr)
	{

		SMTProblem smta = new SMTProblem();


		//equipment = 100
		IntExpr aExpr_E = this.synth.declareSMTProblemVariable(Variable.E, Metropolis.a, smta);
		this.synth.defaultRequirements(smta,aExpr_E);
		this.synth.atLeast(smta,aExpr_E,50);		

		//sustanance = 100
		IntExpr aExpr_S = this.synth.declareSMTProblemVariable(Variable.S, Metropolis.a, smta);
		this.synth.defaultRequirements(smta,aExpr_S);
		this.synth.atLeast(smta,aExpr_S,50);


		//medicine = 100
		IntExpr aExpr_M = this.synth.declareSMTProblemVariable(Variable.M, Metropolis.a, smta);
		this.synth.defaultRequirements(smta,aExpr_M);
		this.synth.atLeast(smta,aExpr_M,50);

		lr.problem = smta;

	}
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * Suppose  B and C must have identical amounts of M resources 
	 * to support same capacity during a disaster scenario.
	 * We update the constraints accordingly.
	 * 
	 * Original constraints
	 * 
	 * [b] 
	 * (declare-fun bM () Int)
	 * (declare-fun bS () Int)
	 * (declare-fun bE () Int)
	 * (assert (>= bM 0))
	 * (assert (>= bS 0))
	 * (assert (>= bS 40))
	 * (assert (= bE 0))
	 * 
	 * [c, e] 
	 * (declare-fun cM () Int)
	 * (declare-fun cS () Int)
	 * (declare-fun eE () Int)
	 * (declare-fun eM () Int)
	 * (declare-fun eS () Int)
	 * (declare-fun cE () Int)
	 * (assert (>= cM 0))
	 * (assert (>= cS 0))
	 * (assert (>= eE 0))
	 * (assert (>= eM 0))
	 * (assert (= eS cS))
	 * (assert (not (<= eE 23)))
	 * (assert (not (<= cS 15)))
	 * (assert (not (<= eM 20)))
	 * (assert (>= cS 40))
	 * (assert (= cE 0))
	 * 
	 * 
	 * Updated constraints
	 * [b,c,e]
	 * (declare-fun bM () Int)
	 * (declare-fun bS () Int)
	 * (declare-fun bE () Int)
	 * (declare-fun cM () Int)
	 * (declare-fun cS () Int)
	 * (declare-fun eE () Int)
	 * (declare-fun eM () Int)
	 * (declare-fun eS () Int)
	 * (declare-fun cE () Int)
	 * 
	 * (assert (>= eE 0))
	 * (assert (>= eM 0))
	 * (assert (>= eS 0))
	 * (assert (>= bE 0))
	 * (assert (>= bM 0))
	 * (assert (>= bS 0))
	 * (assert (>= cE 0))
	 * (assert (>= cM 0))
	 * (assert (>= cS 0))
	 * (assert (not (<= eS 15)))
	 * (assert (not (<= eM 20)))
	 * (assert (not (<= eE 23)))
	 * (assert (>= bS 40))
	 * (assert (>= cS 40))
	 * (assert (= cS bS))
	 * 
	 * 
	 * */
	public LocaleResource updateRequirementsForBandC(Solver solver)
	{
		//locate the problems involving b and c
		LocaleResource lrb = solver.findLocaleResourceProblemFor(Metropolis.b);
		LocaleResource lrc = solver.findLocaleResourceProblemFor(Metropolis.c);

		//remove them from the list of graph constraints problems.

		//System.out.println(lrb.problem);
		//System.out.println(lrc.problem);
		solver.graphConstraints.remove(lrb);
		solver.graphConstraints.remove(lrc);


		//create a new subgraph merging subgraphs for b and c.
		HashSet<Location> locations = new HashSet<Location>();
		locations.addAll(lrb.sg.vertexSet());
		locations.addAll(lrc.sg.vertexSet());

		AsSubgraph<Location,Street> sg = new AsSubgraph<Location,Street>(this.regionGraph.masterGraph,locations);

		//now make a new smt problem
		SMTProblem smt = new SMTProblem();

		//declare the variables for the smt problem for [b,c,e]
		IntExpr eExpr_E = this.synth.declareSMTProblemVariable(Variable.E,Metropolis.e,smt);
		IntExpr eExpr_M = this.synth.declareSMTProblemVariable(Variable.M,Metropolis.e,smt);
		IntExpr eExpr_S = this.synth.declareSMTProblemVariable(Variable.S,Metropolis.e,smt);

		IntExpr bExpr_E = this.synth.declareSMTProblemVariable(Variable.E,Metropolis.b,smt);
		IntExpr bExpr_M = this.synth.declareSMTProblemVariable(Variable.M,Metropolis.b,smt);
		IntExpr bExpr_S = this.synth.declareSMTProblemVariable(Variable.S,Metropolis.b,smt);

		IntExpr cExpr_E = this.synth.declareSMTProblemVariable(Variable.E,Metropolis.c,smt);
		IntExpr cExpr_M = this.synth.declareSMTProblemVariable(Variable.M,Metropolis.c,smt);
		IntExpr cExpr_S = this.synth.declareSMTProblemVariable(Variable.S,Metropolis.c,smt);

		//default constraints.
		this.synth.defaultRequirements(smt, eExpr_E);
		this.synth.defaultRequirements(smt, eExpr_M);
		this.synth.defaultRequirements(smt, eExpr_S);

		this.synth.defaultRequirements(smt, bExpr_E);
		this.synth.defaultRequirements(smt, bExpr_M);
		this.synth.defaultRequirements(smt, bExpr_S);

		this.synth.defaultRequirements(smt, cExpr_E);
		this.synth.defaultRequirements(smt, cExpr_M);
		this.synth.defaultRequirements(smt, cExpr_S);


		//original constraints
		//maintain original constraints from R3
		this.synth.greaterThan(smt,eExpr_S,15);
		this.synth.greaterThan(smt,eExpr_M,20);
		this.synth.greaterThan(smt,eExpr_E,23);

		//maintain original requirements from R6
		this.synth.atLeast(smt,bExpr_S,40);
		this.synth.atLeast(smt,cExpr_S,40);


		//new constraints		
		//constraints for at least 50 units of sustenance for b and c				
		this.synth.atLeast(smt, cExpr_S,50);
		this.synth.atLeast(smt, bExpr_S,50);

		//new requirements to set equality for medicine in B and C
		this.synth.equals(smt,bExpr_M, cExpr_M);
		this.synth.atLeast(smt,bExpr_M, 10);
		this.synth.atLeast(smt,cExpr_M, 10);

		LocaleResource lrbc = new LocaleResource(sg,smt);
		solver.graphConstraints.add(lrbc);
		//System.out.println(smt);
		//System.out.println("update");
		return lrbc;
	}
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 *  R1 shipping location A has 100 units of medicine, sustenance and equipment 
	 *  
	 * */
	public void requirement1()
	{		
		//create a new subgraph
		HashSet<Location> aLocations = new HashSet<Location>();
		aLocations.add(Metropolis.a);
		AsSubgraph<Location,Street> sga = new AsSubgraph<Location,Street>(this.regionGraph.masterGraph,aLocations);


		//create a new smt problem for this subgraph as follows:		
		SMTProblem smta = new SMTProblem();

		//create a new local resouce problem and add it to the solver
		LocaleResource lra = new LocaleResource(sga,smta);
		this.synth.addLocaleProblem(lra);

		//equipment = 100
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
		this.synth.setValue(smta,aExpr_M,100);
	}
	//----------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * R2 locations connected via the bridge have the same units of sustenance	(C & E)
	 * 
	 * */
	public void requirement2()
	{
		//location E
		//create a new subgraph
		HashSet<Location> locations = new HashSet<Location>();
		locations.add(Metropolis.e);
		locations.add(Metropolis.c);
		AsSubgraph<Location,Street> sg = new AsSubgraph<Location,Street>(this.regionGraph.masterGraph,locations);

		//create a new smt problem for this subgraph as follows:		
		SMTProblem smt = new SMTProblem();

		//create a new local resouce problem and add it to the solver
		LocaleResource lr = new LocaleResource(sg,smt);
		this.synth.addLocaleProblem(lr);

		//declare variables for location c, with default requirements.
		IntExpr cExpr_S = this.synth.declareSMTProblemVariable(Variable.S, Metropolis.c, smt);
		this.synth.defaultRequirements(smt,cExpr_S);
		IntExpr cExpr_E = this.synth.declareSMTProblemVariable(Variable.E, Metropolis.c, smt);
		this.synth.defaultRequirements(smt,cExpr_E);		
		IntExpr cExpr_M = this.synth.declareSMTProblemVariable(Variable.M, Metropolis.c, smt);
		this.synth.defaultRequirements(smt,cExpr_M);


		//declare variables for location e, with default requirements.
		IntExpr eExpr_S = this.synth.declareSMTProblemVariable(Variable.S, Metropolis.e, smt);
		this.synth.defaultRequirements(smt,eExpr_S);
		IntExpr eExpr_E = this.synth.declareSMTProblemVariable(Variable.E, Metropolis.e, smt);
		this.synth.defaultRequirements(smt,eExpr_E);		
		IntExpr eExpr_M = this.synth.declareSMTProblemVariable(Variable.M, Metropolis.e, smt);
		this.synth.defaultRequirements(smt,eExpr_M);


		//constraint between c and e for same amount of sustenenace.				
		this.synth.equals(smt, cExpr_S,eExpr_S);

	}
	//----------------------------------------------------------------------------------------------------------------
	/** 
	 * R3 hospitals receive at least 20 units of medicine, 15 of sustenance and 23 units of equipment (D,E)
	 * 
	 * */	
	public void requirement3()
	{
		//Location D
		//create a new subgraph
		HashSet<Location> dLocations = new HashSet<Location>();
		dLocations.add(Metropolis.d);//hospital
		AsSubgraph<Location,Street> sgd = new AsSubgraph<Location,Street>(this.regionGraph.masterGraph,dLocations);

		//create a new smt problem for this subgraph as follows:		
		SMTProblem smtd = new SMTProblem();

		//create a new local resouce problem and add it to the solver
		LocaleResource lrd = new LocaleResource(sgd,smtd);
		this.synth.addLocaleProblem(lrd);

		//equipment = 23
		IntExpr dExpr_E = this.synth.declareSMTProblemVariable(Variable.E, Metropolis.d, smtd);
		this.synth.defaultRequirements(smtd,dExpr_E);
		this.synth.greaterThan(smtd,dExpr_E,23);

		//sustanance = 15
		IntExpr dExpr_S = this.synth.declareSMTProblemVariable(Variable.S, Metropolis.d, smtd);
		this.synth.defaultRequirements(smtd,dExpr_S);
		this.synth.greaterThan(smtd,dExpr_S,15);

		//medicine = 20
		IntExpr dExpr_M = this.synth.declareSMTProblemVariable(Variable.M, Metropolis.d, smtd);
		this.synth.defaultRequirements(smtd,dExpr_M);
		this.synth.greaterThan(smtd,dExpr_M,20);



		//location E		
		HashSet<Location> eLocations = new HashSet<Location>();
		eLocations.add(Metropolis.e);


		//find the resource problem that already exists for e.
		LocaleResource lre = this.synth.solver.findLocaleResourceProblemFor(Metropolis.e);
		//AsSubgraph<Location,Street> sge = lre.sg;
		SMTProblem smte = lre.problem;



		//equipment = 23
		IntExpr eExpr_E = this.synth.declareSMTProblemVariable(Variable.E, Metropolis.e, smte);
		this.synth.greaterThan(smte,eExpr_E,23);

		//sustanance = 15
		IntExpr eExpr_S = this.synth.declareSMTProblemVariable(Variable.S, Metropolis.e, smte);
		this.synth.greaterThan(smte,eExpr_S,15);

		//medicine = 20
		IntExpr eExpr_M = this.synth.declareSMTProblemVariable(Variable.M, Metropolis.e, smte);
		this.synth.greaterThan(smte,eExpr_M,20);



	}
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * R4 location G receives between 7 and  10 units of resources 
	 * */
	public void requirement4()
	{
		//create a new subgraph
		HashSet<Location> gLocations = new HashSet<Location>();
		gLocations.add(Metropolis.g);
		AsSubgraph<Location,Street> sgG = new AsSubgraph<Location,Street>(this.regionGraph.masterGraph,gLocations);

		//create a new smt problem for this subgraph as follows:		
		SMTProblem smtG = new SMTProblem();


		//create a new local resource problem and add it to the solver
		LocaleResource lrg = new LocaleResource(sgG,smtG);
		this.synth.addLocaleProblem(lrg);


		//medicine <= 10
		IntExpr gExpr_M = this.synth.declareSMTProblemVariable(Variable.M, Metropolis.g, smtG);
		this.synth.defaultRequirements(smtG,gExpr_M);
		this.synth.between(smtG,gExpr_M,7,10);

		//equipment
		IntExpr gExpr_E = this.synth.declareSMTProblemVariable(Variable.E, Metropolis.g, smtG);
		this.synth.defaultRequirements(smtG,gExpr_E);


		//sustenenace
		IntExpr gExpr_S = this.synth.declareSMTProblemVariable(Variable.S, Metropolis.g, smtG);
		this.synth.defaultRequirements(smtG,gExpr_S);

	}
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * R5 location F receives 5 more units of medicine & sustenence than location G
	 * */
	public void requirement5()
	{
		//g is already in the problems list... so retrieve that problem		
		LocaleResource lrg = this.synth.solver.findLocaleResourceProblemFor(Metropolis.g);

		//get the subgraph
		AsSubgraph<Location,Street> sgG = lrg.sg;

		//retrieve associated SMT problem for subgraph, since we will add to it		
		SMTProblem smtG = lrg.problem;


		//add f to the subgraph containing g.
		sgG.addVertex(Metropolis.f);

		//IntExpr gExpr_E = smtG.problemVars.get(Metropolis.g.E);
		IntExpr gExpr_S = smtG.problemVars.get(Metropolis.g.S);
		IntExpr gExpr_M = smtG.problemVars.get(Metropolis.g.M);

		//equipment
		IntExpr fExpr_E = this.synth.declareSMTProblemVariable(Variable.E, Metropolis.f, smtG);
		this.synth.defaultRequirements(smtG,fExpr_E);

		//sustanance
		IntExpr fExpr_S = this.synth.declareSMTProblemVariable(Variable.S, Metropolis.f, smtG);
		this.synth.defaultRequirements(smtG,fExpr_S);
		this.synth.moreThan(smtG,gExpr_S,fExpr_S, 5);

		//medicine
		IntExpr fExpr_M = this.synth.declareSMTProblemVariable(Variable.M, Metropolis.f, smtG);
		this.synth.defaultRequirements(smtG,fExpr_M);
		this.synth.moreThan(smtG,gExpr_M,fExpr_M, 5);
	}
	//----------------------------------------------------------------------------------------------------------------
	/**
	R6 locations within 8KM of A receives at least 40 units of sustenance (B,C)
	 */
	public void requirement6()
	{
		Location x = Metropolis.b;

		//create a new subgraph
		HashSet<Location> bLocations = new HashSet<Location>();
		bLocations.add(x);
		AsSubgraph<Location,Street> sgB = new AsSubgraph<Location,Street>(this.regionGraph.masterGraph,bLocations);

		//create a new smt problem for this subgraph as follows:		
		SMTProblem smtB = new SMTProblem();

		//create a new local resource problem and add it to the solver
		LocaleResource lrb = new LocaleResource(sgB,smtB);
		this.synth.addLocaleProblem(lrb);

		//medicine
		IntExpr bExpr_M = this.synth.declareSMTProblemVariable(Variable.M, x, smtB);
		this.synth.defaultRequirements(smtB,bExpr_M);

		//equipment
		IntExpr bExpr_E = this.synth.declareSMTProblemVariable(Variable.E, x, smtB);
		this.synth.defaultRequirements(smtB,bExpr_E);

		//sustenenace >= 40
		IntExpr bExpr_S = this.synth.declareSMTProblemVariable(Variable.S, x, smtB);
		this.synth.defaultRequirements(smtB,bExpr_S);
		this.synth.atLeast(smtB, bExpr_S, 40);


		//now location c
		LocaleResource lrc = this.synth.solver.findLocaleResourceProblemFor(Metropolis.c);
		IntExpr cExpr_S = lrc.problem.problemVars.get(Metropolis.c.S);
		//sustenance
		this.synth.atLeast(lrc.problem, cExpr_S, 40);
	}
	//----------------------------------------------------------------------------------------------------------------
	/**
	 * R7 Only hospitals+supplies can receive equipment resources (B,C,F,G have 0 equipment)  
	 * */
	public void requirement7()
	{
		for(LocaleResource lr : this.synth.solver.graphConstraints)
		{
			SMTProblem smt = lr.problem;
			for(Location l : lr.sg.vertexSet())
			{
				if(!(l.tags.contains(Tag.Hospital)||l.tags.contains(Tag.Supply)))
				{
					IntExpr equipmentExpression = smt.problemVars.get(l.E);
					this.synth.setValue(smt, equipmentExpression, 0);				
				}
			}
		}
	}
	//----------------------------------------------------------------------------------------------------------------
	public void synthesise()
	{
		this.requirement1();
		this.requirement2();
		this.requirement3();
		this.requirement4();
		this.requirement5();
		this.requirement6();
		this.requirement7();
	}
	//----------------------------------------------------------------------------------------------------------------
}
