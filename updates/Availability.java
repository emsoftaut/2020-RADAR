package updates;



public class Availability extends Update
{
	
	public String label;
	public boolean status;
	
	
	public Availability(String label,boolean status)
	{
		this.label=label;
		this.status=status;		
	}

}
