package input;

public class IntegerAttribute extends Attribute{

	public int value;
	public String toString()
	{
		return this.value+"";
	}
	//------------------------------------------------------------------------
	public IntegerAttribute(Variable v,Location l,int value)
	{
		super(v,l);
		this.value = value;
	}
	//------------------------------------------------------------------------
	public IntegerAttribute(Variable v,Location l)
	{
		super(v,l);		
		this.value = 0;
	}
}
