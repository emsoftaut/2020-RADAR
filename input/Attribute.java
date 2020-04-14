package input;

abstract public class Attribute 
{
	public String label;
	
	
	
	private String generateLabel(Variable v,Location l)
	{
		return l.label+v.toString();
	}
	
	public Attribute(Variable v,Location l)
	{
		this.label = generateLabel(v,l);
	}

}
