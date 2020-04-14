package metropolis;

import input.RegionGraph;
import radar.Synthesiser;

abstract public class Requirements {
	
	public Synthesiser synth;
	public RegionGraph regionGraph;
	public Requirements()
	{
		this.synth = null;
		
	}
	
	public Requirements(Synthesiser synth)
	{
		
		this.synth=synth;
		this.regionGraph = this.synth.graph;
	}
	public abstract void synthesise();

}
