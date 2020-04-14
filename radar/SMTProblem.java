package radar;
/**
 * 
 * Maintains a SMT constraint solving problem
 * 
 * 
 * */

import java.util.HashMap;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;

import input.Attribute;
import input.IntegerAttribute;

public class SMTProblem 
{
	public HashMap<String, String> cfg;
	public Context ctx;
	public Solver solver;
	public Model model;
	public int nConstraints;//set after resolved.
	public int nVars;//set after resolved;

	//associates location attributes with SMT problem variable declarations
	public HashMap<Attribute,IntExpr> problemVars;
	//associates SMT problem variable declarations with their satisfiable values
	public HashMap<IntExpr,Expr> modelValues;

	//--------------------------------------------------------------------
	 
	//--------------------------------------------------------------------
	public String toString()
	{
		return solver.toString();
	}
	//--------------------------------------------------------------------
	public void solve()
	{
		this.nConstraints=0;
		this.nVars=0;
		this.solver.check();
		this.model = solver.getModel();
		this.nConstraints = this.solver.getNumAssertions();

		for(Attribute attribute : problemVars.keySet())
		{
			IntegerAttribute ia = (IntegerAttribute) attribute;

			IntExpr declaration = problemVars.get(attribute);
			Expr value = model.eval(declaration, false);			
			modelValues.put(declaration,value);
			String str = value.getSExpr();
			ia.value = Integer.parseInt(str);
			this.nVars++;
		}
	}
	//--------------------------------------------------------------------
	public SMTProblem()
	{
		this.cfg = new HashMap<String, String>();
		this.cfg.put("model", "true");
		this.ctx =  new Context(cfg);
		this.solver = ctx.mkSimpleSolver();
		this.problemVars = new HashMap<Attribute,IntExpr>(); 
		this.modelValues = new HashMap<IntExpr,Expr>(); 
	}
}
