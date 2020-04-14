package scale;


import radar.Radar;

public class Experiments {


	/**
	 * nVertex    = 500,750,1000,1250,1500,1750,2000,2250,2500,2750,3000
	 * nPartition = 10,25,50,250,
	 * 
	 * */


	public static void main(String[] args) 
	{	

		int[] partSizes = new int[]{10,25,50,250};
		for(int nPart=0;nPart<partSizes.length;nPart++)
		{
			for(int nVertex = 500;nVertex<=2000;nVertex+=250)
			{
				ScaleRegion scaleRegion = new ScaleRegion(nVertex);
				ScaleRequirements scaleRequirements = new ScaleRequirements(partSizes[nPart]);
				Radar radar = new Radar(scaleRegion,scaleRequirements);				
				radar.initialise();
				long start = System.currentTimeMillis();				
				radar.solver.solve();
				long stop = System.currentTimeMillis();
				long dur = stop-start;
				System.out.println(dur+" ms");
			}
		}

	}

	public static void mainx(String[] args) 
	{	

		int[] partSizes = new int[]{10,25,50,250};
		for(int nVertex = 500;nVertex<=3000;nVertex+=250)
		{
			ScaleRegion scaleRegion = new ScaleRegion(nVertex);
			for(int nPart=0;nPart<partSizes.length;nPart++)
			{
				ScaleRequirements scaleRequirements = new ScaleRequirements(partSizes[nPart]);
				Radar radar = new Radar(scaleRegion,scaleRequirements);				
				radar.initialise();
				long start = System.currentTimeMillis();				
				radar.solver.solve();
				long stop = System.currentTimeMillis();
				long dur = stop-start;
				System.out.println(dur+" ms");
			}
		}

	}

}
