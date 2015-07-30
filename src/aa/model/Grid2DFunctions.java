
package aa.model;

import java.util.ArrayList;

import aa.AAParameters;
import aa.AAUtil;
import aa.AAUtil.type;


public class Grid2DFunctions {
    
	private Grid2D grid;
	
	public Grid2DFunctions(Grid2D g) {
        grid = g;
    }    
	
	
	/**
	 *   0: gamma+
	 *   1: gamma-
	 *   2: kappa+
	 *   3: kappa-
	 *   4: phi+
	 *   5: phi-
	 */
	
	public static boolean checkRule1Prob() { //G+Gn->Gn+1		
		return (AAUtil.RANDOM.nextDouble() < AAParameters.PROBABILITIES[0]);		
	}
	
	public static boolean checkRule2Prob(int capsuleSize) { //Gn+1->Gn+G 		
		double prob = Math.pow(1.0 / (1.0 + Math.exp(AAParameters.PROBABILITIES[1])), capsuleSize);
		return AAUtil.RANDOM.nextDouble() < prob;		
	}
	
	public static boolean checkRule3Prob(int numberOfCatalysts) { //A+B-F>C 		
		double pow = 1.0/(Math.pow((double)numberOfCatalysts + 1.0, 2));
		double prob = Math.pow( 1.0 / (1.0 + Math.exp(AAParameters.PROBABILITIES[2])), pow);
		return AAUtil.RANDOM.nextDouble() < prob;				
	}
	
	public static boolean checkRule4Prob() { //C->A+B
		return (AAUtil.RANDOM.nextDouble() < AAParameters.PROBABILITIES[3]);
	}

	public static boolean checkRule5Prob(int numberOfCatalysts) { //D+E-C>F 		
		double pow = 1.0/(Math.pow((double)numberOfCatalysts + 1.0, 2));
		double prob = Math.pow( 1.0 / (1.0 + Math.exp(AAParameters.PROBABILITIES[4])), pow); 
		return AAUtil.RANDOM.nextDouble() < prob;			
	}
	
	public static boolean checkRule6Prob() { //F->D+E
		return (AAUtil.RANDOM.nextDouble() < AAParameters.PROBABILITIES[5]);
	}
	
	
    public AAUtil.type getType(int i) {
    	switch(i) {
    	case(0):{return type.A;}
    	case(1):{return type.B;}
    	case(2):{return type.C;}
    	case(3):{return type.D;}
    	case(4):{return type.E;}
    	case(5):{return type.F;}
    	case(6):{return type.G;}
    	default:{System.out.println("AAUtil error: unknown type");return type.A;}
    	}
    }    
    
    
    /***** ENTROPY FUNCTIONS *****/
        
    
    /* get data from current state of the system
    *  returns double array
    *  [0]: array of size [gridsize^2] containing a count of particles / reactions per tile
    *  [1]: total amount of particles / reactions (for efficiency)
    */
    public double[][] getDistribution(int type) {
    	
    	double[][] dist = new double[2][AAParameters.GRID_SIZE*AAParameters.GRID_SIZE];

    	int n;
    	if(type == 1) { // self-assembly
    		for(int i=0; i<AAParameters.GRID_SIZE; i++) {    		
        		for(int j=0; j<AAParameters.GRID_SIZE; j++) {
        			n = getBasicParticlesOfTypeAtXY(AAUtil.type.G, i, j);
        			dist[0][(i*AAParameters.GRID_SIZE+j)] = n;
        			dist[1][0] += n;  
        		}
    		}
    	}
    	else if(type == 2) { //reciprocal catalysis
        	int[][] reactionsArray = grid.getReactionsArray();  
    		for(int i=0; i<AAParameters.GRID_SIZE; i++) {    		
        		for(int j=0; j<AAParameters.GRID_SIZE; j++) {
    				n = reactionsArray[i][j];
        			dist[0][(i*AAParameters.GRID_SIZE+j)] = n;
        			dist[1][0] += n;  
        		}
    		}
    	}    	    	
		else {
			System.out.println("Error in getDistribution: unknown type");
			n = 0;
		}    	      	    	
    	return dist;
    }
    
    
    /* get data from current state of the system
     *  returns double array
     *  [0]: array of size [gridsize^2] containing a count of particles / reactions per tile
     *  [1]: total amount of particles / reactions (for efficiency)
     *  [2]: minimum non-zero value of particles / reactions per tile 
     */
     public double[][] getDistributionWithMinimum(int type) {
     	
     	double[][] dist = new double[3][AAParameters.GRID_SIZE*AAParameters.GRID_SIZE];

     	int n;
     	double min = 10000000;
     	if(type == 1) { // self-assembly
     		for(int i=0; i<AAParameters.GRID_SIZE; i++) {    		
         		for(int j=0; j<AAParameters.GRID_SIZE; j++) {
         			n = getBasicParticlesOfTypeAtXY(AAUtil.type.G, i, j);
         			dist[0][(i*AAParameters.GRID_SIZE+j)] = n;
         			dist[1][0] += n;
         			if(n > 0.0001 && n < min) {
         				min = n;         				
         			}         			
         		}
     		}
     		dist[2][0] = min;;
     	}
     	else if(type == 2) { //reciprocal catalysis
         	int[][] reactionsArray = grid.getReactionsArray();  
     		for(int i=0; i<AAParameters.GRID_SIZE; i++) {    		
         		for(int j=0; j<AAParameters.GRID_SIZE; j++) {
     				n = reactionsArray[i][j];
         			dist[0][(i*AAParameters.GRID_SIZE+j)] = n;
         			dist[1][0] += n;  
         			if(n > 0.0001 && n < min) {
         				min = n;         				
         			}
         		}
     		}
     		dist[2][0] = min;;
     	}    	    	
 		else {
 			System.out.println("Error in getDistribution: unknown type");
 			n = 0;
 		}    	      	    	
     	return dist;
     }
    
    
    
    public double getInformationEntropy(int type) { //overloaded method
    	double[][] probDist = this.getDistribution(type);
    	return getInformationEntropy(probDist[0], (int)Math.round(probDist[1][0]));
    }
    
    //calculate information entropy
    public double getInformationEntropy(double[] p, int n) {
		
    	if(n<1) { //no data
			return 1.0;
		}
		else {						
			//create probability distribution by dividing by total
			double[] p2 = new double[p.length];
			for(int i=0; i<p2.length; i++) {
	    		p2[i] = p[i] / (double)n;
	    	}	
			
			//calculate information entropy 
			double entropy = 0.0;
    		for (int i=0; i < p2.length; i++) {    	 
    			if(p2[i] > 0) {
    				entropy += p2[i] * (Math.log(p2[i])/Math.log(2));      
    			}
    		}    		    		
    		return (-1.0 * entropy);  			
		}
	}
    
    public double getNormalizedInformationEntropy(int type) { //overloaded method
    	double[][] probDist = this.getDistribution(type);
    	return getNormalizedInformationEntropy(probDist[0], (int)Math.round(probDist[1][0]));
    }
    
    public double getNormalizedInformationEntropy(double[] p, int n) {
    	if(n<2) { 
			return 1.0;
		}
		else {				
			//n or p.length ?????????? --> denk het niet i.v.m. afwijkende definitie van p(x). (kans dat een deeltje ipv kans dat een tile).
			//antwoord: beiden blijken mogelijk! 
			
			/**** NORMALIZATIONTYPE IS DETERMINED HERE ****/
			
			return getInformationEntropy(p, n) / (Math.log(p.length)/Math.log(2));
			//return getInformationEntropy(p, n) / (Math.log(n)/Math.log(2));
		}
    }
    
    public double getNormalizedInformationEntropyMu() {
    	
    	double[][] ageData = grid.getGridFunctions().getAgeData();
    	double[] ageDist = new double[ageData.length];
    	
    	//NOTE: ONLY INTERESTED IN DISTRIBUTION OF NUMBER OF CAPSULES OVER TYPES
    	int sum = 0;
    	for(int i=0;i<ageDist.length;i++) {
    		ageDist[i] = ageData[i][1];
    		sum += ageDist[i];
    	}    	
    	
    	double ie;
    	//if(sum < 1) { //NOTE: CHANGED TO MAKE IT CONSISTENT WITH NORM.ENT FUNCTION
    	if(sum < 2) {
    		ie = 1;
    	}
    	else {
    		ie = getInformationEntropy(ageDist, (int)(Math.round(sum))) / (Math.log(ageDist.length)/Math.log(2));
    	}
		return ie;
    }
    

    public double[][] getAgeData() {    	
    	double[][] data = new double[AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT][2];    	
    	for(int i=0;i<grid.getParticles().size();i++) {
    		if(grid.getParticles().get(i) instanceof Capsule2D) {
    			data[((Capsule2D)grid.getParticles().get(i)).getMaxContained()-1][0] += ((Capsule2D)grid.getParticles().get(i)).getAge();
    			data[((Capsule2D)grid.getParticles().get(i)).getMaxContained()-1][1]++;
    		}
    	}      	
    	return data;
    }
    
    public ArrayList<ArrayList<Integer>> getAgeData2() {        	
    	ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
    	for(int i=0;i<AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT;i++) {
    		data.add(new ArrayList<Integer>());
    	}
    	for(Particle2D p : grid.getParticles()) {
    		if(p instanceof Capsule2D) {
    			int index = ((Capsule2D) p).getMaxContained()-1;
    			data.get(index).add(((Capsule2D) p).getAge());
    		}    		
    	}      	
    	return data;    	
    }
    
    public ArrayList<ArrayList<Integer>> getSizeData() {        	
    	ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
    	for(int i=0;i<AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT;i++) {
    		data.add(new ArrayList<Integer>());
    	}
    	for(Particle2D p : grid.getParticles()) {
    		if(p instanceof Capsule2D) {
    			int index = ((Capsule2D) p).getMaxContained()-1;
    			data.get(index).add(((Capsule2D) p).getCapsuleParticles().size());
    		}    		
    	}      	
    	return data;    	
    }
    
    //Special function for improved efficiency of CE calculations
    public double[] getCEData(double epsilon) {
    	double[] ced = new double[6];
    	
    	double[][] d1 = getDistribution(1);
		double[][] d2 = getDistribution(2);
			
		double[] ce = new double[2];
		ce[0] = getCrossEntropyWithEpsilon(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]), epsilon);
		ce[1] = getCrossEntropyWithEpsilon(d2[0], (int)Math.round(d2[1][0]), d1[0], (int)Math.round(d1[1][0]), epsilon);		
		ced[0] =  (ce[0] + ce[1]);
		
		ced[1] = getNormalizedInformationEntropy(d1[0], (int)Math.round(d1[1][0]));
		ced[2] = getNormalizedInformationEntropy(d2[0], (int)Math.round(d2[1][0]));
		
		double[] kld = new double[2];
		kld[0] = getKLDivergenceWithEpsilon(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]), epsilon);
		kld[1] = getKLDivergenceWithEpsilon(d2[0], (int)Math.round(d2[1][0]), d1[0], (int)Math.round(d1[1][0]), epsilon);
		ced[3] =  (kld[0] + kld[1]);
		
		ced[4] = getInformationEntropy(d1[0], (int)Math.round(d1[1][0]));
		ced[5] = getInformationEntropy(d2[0], (int)Math.round(d2[1][0]));
				
		return ced;
    }
    
    
    /**** CROSS ENTROPY METHODS ****/
      
/*
	public double[] getCrossEntropy() {//overloaded
		double[][] d1 = getDistribution(1);
		double[][] d2 = getDistribution(2);
		double[] ce = new double[2];
		ce[0] = getCrossEntropy(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]));
		ce[1] = getCrossEntropy(d2[0], (int)Math.round(d2[1][0]), d1[0], (int)Math.round(d1[1][0]));
		return ce;
	}
		
	public double getCrossEntropy(double[] p1, int n1, double[] p2, int n2) {
		
		/** <<<<----- TO FIX (epsilon) **
		
		if(n1<2 || n2<2) { //not enough data 
			return 1.0;
		}
		else {
		
			double[] px = new double[p1.length];
			double[] py = new double[p2.length];
			
			//create probability distribution by dividing by total
			for(int i=0; i<p1.length; i++) {
	    		px[i] = p1[i] / (double)n1;
	    	}	
			for(int i=0; i<p2.length; i++) {
	    		py[i] = p2[i] / (double)n2;
	    	}	
			
			double crossEntropy = 0.0;
			for (int i=0; i < px.length; i++) {
				if(px[i] > 0 && py[i] > 0) { 
					crossEntropy += px[i] * (Math.log(py[i])/Math.log(2));				
				}	
			}
			return -1.0*crossEntropy;
		}
	}
	
	public double getSymmetrizedCrossEntropy() {
		double[] ce = getCrossEntropy();
		return (ce[0] + ce[1])/2.0;
	}*/
	
	public double getSymmetrizedCrossEntropyWithEpsilon(double epsilon) {
		double[][] d1 = getDistribution(1);
		double[][] d2 = getDistribution(2);
			
		double[] ce = new double[2];
		ce[0] = getCrossEntropyWithEpsilon(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]), epsilon);
		ce[1] = getCrossEntropyWithEpsilon(d2[0], (int)Math.round(d2[1][0]), d1[0], (int)Math.round(d1[1][0]), epsilon);
		return (ce[0] + ce[1]);
	}
	
	public double getCrossEntropyWithEpsilon(double[] p1, int n1, double[] p2, int n2, double epsilon) {
			
		double[] px = new double[p1.length];
		double[] py = new double[p2.length];
		

		for(int i=0; i<px.length; i++) {
    		px[i] = (p1[i] + epsilon) / ((double)n1 + ((double)px.length * epsilon));
    	}		
		for(int i=0; i<py.length; i++) {
	    	py[i] = (p2[i] + epsilon) / ((double)n2 + ((double)py.length * epsilon));
	    }	
		
		double ce = 0.0;
		for (int i=0; i < px.length; i++) {
			ce += px[i] * (Math.log(py[i])/Math.log(2));			
		}
		return -1.0*ce; 		
	}
	
	
	/*public double getNormalizedSymmetrizedCrossEntropy() {					
		double[][] d1 = getDistribution(1);
		double[][] d2 = getDistribution(2);
			
		double[] ce = new double[2];
		ce[0] = getCrossEntropy(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]));
		ce[1] = getCrossEntropy(d2[0], (int)Math.round(d2[1][0]), d1[0], (int)Math.round(d1[1][0]));
			
		/**** NORMALIZATIONTYPE IS DETERMINED HERE ****

		return (ce[0] + ce[1])/ (2.0*(Math.log(d1[0].length)/Math.log(2))); //delen door aantal tiles
		//return (ce[0] + ce[1])/ (2.0*(Math.log((d1[1][0] + d2[1][0])*0.5)/Math.log(2)));  //delen door gemiddeld aantal voorkomens (?)		
	}*/
	
	
	
    /**** KL METHODS ****/

	/*public double getNormalizedSymmetrizedKLDivergence() {					
		double[][] d1 = getDistribution(1);
		double[][] d2 = getDistribution(2);
			
		double[] kld = new double[2];
		kld[0] = getKLDivergence(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]));
		kld[1] = getKLDivergence(d2[0], (int)Math.round(d2[1][0]), d1[0], (int)Math.round(d1[1][0]));
			
		return (kld[0] + kld[1])/ (2.0*(Math.log(d1[0].length)/Math.log(2))); //delen door aantal tiles
	}*/
	
	
	/*public double getSymmetrizedKLDivergence() {					
		double[][] d1 = getDistribution(1);
		double[][] d2 = getDistribution(2);
			
		double[] kld = new double[2];
		kld[0] = getKLDivergence(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]));
		kld[1] = getKLDivergence(d2[0], (int)Math.round(d2[1][0]), d1[0], (int)Math.round(d1[1][0]));
			
		return (kld[0] + kld[1]) / 2.0;
	}*/
	
	
	/*
	 * http://staff.science.uva.nl/~tsagias/?p=185
	 * (only available in google cache)
	 */
	public double getSymmetrizedKLDivergenceWithGammaSmoothing() {
		double[][] d1 = getDistributionWithMinimum(1);
		double[][] d2 = getDistributionWithMinimum(2);
		
		double sumD1 = (int)Math.round(d1[1][0]); //(should be all integers)
		double sumD2 = (int)Math.round(d2[1][0]);

		//if either list is empty, return high number
		if(sumD1 < 0.0001 || sumD2 < 0.0001) {
			//return Math.pow(10, 20);
			return -1.0; //negative value indicates that measure is invalid due to no G or R
			//return 15;
		}
		
		double minD1 = (int)Math.round(d1[2][0]); //(should be all integers)
		double minD2 = (int)Math.round(d2[2][0]); //(should be all integers)		
		
		double[] dist1 = d1[0];
		double[] dist2 = d2[0];

 		//convert to two non-zero index lists (=tokenize): tuples of (tileNumber, number of G/R) 
		//currently not done
		
		//diff = calculate number of items in G list that are not in R (and vice versa?) (only look at keys)
		double difference = getDifferenceLength(dist1, dist2);
		
		//calculate epsilon: 		
		double epsilon = Math.min(minD1/sumD1, minD2/sumD2) * 0.001;
		
		//calculate gamma:
		double gamma = 1.0 - (difference * epsilon);
		
		//check if probabilities sum to 1 (just for checks)
		//sum g(i)/sumG for all i in G 
		//same for R
		//TODO (perhaps just print)
		
		double div = 0.0;
		
		for(int i=0;i<dist1.length;i++) { //for all tiles
			if(dist1[i] > 0) { //only for actual values
				double pts = dist1[i] / sumD1;
				double ptt = epsilon;
				
				if(dist2[i] > 0) {
					ptt = gamma * (dist2[i] / sumD2);
				}
				div += (pts - ptt) * (Math.log(pts/ptt)/Math.log(2));
			}
		}		
		
		
		if(sumD1 < 1 || sumD2 < 1) System.out.println(sumD1 +" "+ sumD2);
		
		return div;		
	}
	
	//returns number of items in dist1 that are not also in dist2
	//where item is defined as entry with a value above zero
	private double getDifferenceLength(double[] dist1, double[] dist2) {
		double diff = 0.0;
		for(int i=0;i<dist1.length;i++) {
			if(dist1[i] > 0.0001 && dist2[i] < 0.0001) {
				diff++;
			}
		}
		return diff;
	}
	
	
	
	
	
	
	
	public double getSymmetrizedKLDivergenceWithEpsilon(double epsilon) {					
		double[][] d1 = getDistribution(1);
		double[][] d2 = getDistribution(2);
			
		double[] kld = new double[2];
		kld[0] = getKLDivergenceWithEpsilon(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]), epsilon);
		kld[1] = getKLDivergenceWithEpsilon(d2[0], (int)Math.round(d2[1][0]), d1[0], (int)Math.round(d1[1][0]), epsilon);
			
		return (kld[0] + kld[1]);
	}
	
	

	public double getKLDivergenceWithEpsilon(double[] p1, int n1, double[] p2, int n2, double epsilon) {
			
		double[] px = new double[p1.length];
		double[] py = new double[p2.length];
		

		for(int i=0; i<px.length; i++) {
    		px[i] = (p1[i] + epsilon) / ((double)n1 + ((double)px.length * epsilon));
    	}		
		for(int i=0; i<py.length; i++) {
	    	py[i] = (p2[i] + epsilon) / ((double)n2 + ((double)py.length * epsilon));
	    }	
		
		double klDiv = 0.0;
		for (int i=0; i < px.length; i++) {
			klDiv += px[i] * (Math.log(px[i] / py[i])/Math.log(2));			
		}
		return klDiv; 		
	}
	

	/*
	public double getKLDivergence(double[] p1, int n1, double[] p2, int n2) {
			
		if(n1<2 || n2<2) { //not enough data 
			return 1.0;
		}
		else {
		
			double[] px = new double[p1.length];
			double[] py = new double[p2.length];
			
			//create probability distribution by dividing by total
			for(int i=0; i<px.length; i++) {
	    		px[i] = p1[i] / (double)n1;
	    	}	
			for(int i=0; i<py.length; i++) {
	    		py[i] = p2[i] / (double)n2;
	    	}			
			
			double klDiv = 0.0;
			for (int i=0; i < px.length; i++) {
				if(px[i] > 0 && py[i] > 0) {   
					klDiv += px[i] * (Math.log(px[i] / py[i])/Math.log(2)); // X/Y or Y/X ??? 				
				}	
			}
			return klDiv; 
		}
	}*/
	
	/*
	public double[] getKLDivergence() {//overloaded
		double[][] d1 = getDistribution(1);
		double[][] d2 = getDistribution(2);
		double[] kld = new double[2];
		kld[0] = getKLDivergence(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]));
		kld[1] = getKLDivergence(d2[0], (int)Math.round(d2[1][0]), d1[0], (int)Math.round(d1[1][0]));
		return kld;
	}*/
	
	
	
	
	
	
	
	
	
	
	/*public double getAverageCrossEntropy() {//overloaded
		double[][] d1 = getDistribution(1);
		double[][] d2 = getDistribution(2);
		return getAverageCrossEntropy(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]));		
	}*/
	
	/*public double getAverageCrossEntropy(double[] p1, int n1, double[] p2, int n2) {				
		return (0.5*getCrossEntropy(p1,n1,p2,n2))+(0.5*getCrossEntropy(p2,n2,p1,n1));
	}*/
	
	/*public double getAverageNormalizedCrossEntropy() {//overloaded
		double[][] d1 = getDistribution(1);
		double[][] d2 = getDistribution(2);
		return getAverageNormalizedCrossEntropy(d1[0], (int)Math.round(d1[1][0]), d2[0], (int)Math.round(d2[1][0]));		
	}
	
	public double getAverageNormalizedCrossEntropy(double[] p1, int n1, double[] p2, int n2) {		
		double factor = 1.0/(2*(Math.log(AAParameters.GRID_SIZE*AAParameters.GRID_SIZE)/Math.log(2)));
		return (factor*getCrossEntropy(p1,n1,p2,n2))+(factor*getCrossEntropy(p2,n2,p1,n1));
	}*/
	

    
	
    
    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
    /**** UNUSED METHODS ****/
    
    public double getMutualInformation(double[] p1, int n1, double[] p2, int n2) {
    	
    	//try 1: just add distributions and calculate new totals
    	double[] pxy = new double[p1.length];
    	double[] px = new double[p1.length];
    	double[] py = new double[p1.length];
    	
    	int total = n1 + n2;
    	for(int i=0;i<pxy.length;i++) { //p(x,y)
    		pxy[i] = (p1[i] + p2[i]) / (double)total;
    	}    	
    	for(int i=0;i<px.length;i++) { //(p(x)
    		px[i] = p1[i] / (double)n1;
    	}
    	for(int i=0;i<py.length;i++) { //(p(y)
    		py[i] = p2[i] / (double)n2;
    	}
    	
    	//I(X;Y) = - sum_x sum_y p(x,y) log (p(x,y) / p(x)p(y))
    	double mutualInformation = 0;
    	for(int i=0;i<pxy.length;i++) {    		
			if(pxy[i] > 0 && px[i] > 0 && py[i] > 0) {  
				mutualInformation += pxy[i] * ( (Math.log(pxy[i] / (px[i]*py[i])))/Math.log(2) );    			
			}    		
    	}    	
    	return mutualInformation;    	    		
    }
    
    
	public double getMutualInformation() {
		double[][] probDist1 = this.getDistribution(1);
		double[][] probDist2 = this.getDistribution(2);
		return getMutualInformation(probDist1[0], (int)Math.round(probDist1[1][0]), probDist2[0], (int)Math.round(probDist2[1][0]));
	}
	
	public double getJointEntropy() { 
		return (getInformationEntropy(1) + getInformationEntropy(2)) - getMutualInformation();
	}
	
	
	/*
    public int getSumCompoundParticlesAtXY(int x, int y) {
    	int total = 0;    	
    	for(Particle2D p : grid.getParticles()) {
    		if(p instanceof BasicParticle2D && p.getX() == x && p.getY() == y && (((BasicParticle2D)p).isOfType(AAUtil.type.C) || ((BasicParticle2D)p).isOfType(AAUtil.type.F))) {
    			total++;
    		}    		
    	}    
    	return total;
    }*/
    
    /*public int getTotalCompoundParticles() {
    	int total = 0;    	
    	for(Particle2D p : grid.getParticles()) {    		
    		if(p instanceof BasicParticle2D && (((BasicParticle2D)p).isOfType(AAUtil.type.C) || ((BasicParticle2D)p).isOfType(AAUtil.type.F))) {
    			total++;
    		}			
    	}    		    	    	
    	return total;
    }*/
    
    public int getBasicParticlesOfTypeAtXY(AAUtil.type t, int x, int y) {
    	int total = 0;    	
    	for(Particle2D p : grid.getParticles()) {
    		if(p.getX() == x && p.getY() == y) {
    			total += p.getTotalBasicParticlesOfType(t);
    		}    		
    	}    	
    	return total;
    }
    
    public int getTotalBasicParticles() {
    	int sum = 0;
    	for(Particle2D p : grid.getParticles()) {
    		sum += p.getTotalBasicParticles();   		
    	}
    	return sum;    	
    }  
     
    
    //modified version for speeding up the calculation
    /*public int getSumBasicParticlesAtXY(int x, int y) {
    	int total = 0;    	
    	
    	System.out.println("SBPatXY: when used, note why");
    	
    	for(Particle2D p : grid.getParticles()) {
    		if(p.getX() == x && p.getY() == y) {
    			if(p instanceof Capsule2D) {
        			total += ((Capsule2D)p).getTotalBasicParticlesOfType(AAUtil.type.A);
        			total += ((Capsule2D)p).getTotalBasicParticlesOfType(AAUtil.type.B);
        			total += ((Capsule2D)p).getTotalBasicParticlesOfType(AAUtil.type.D);
        			total += ((Capsule2D)p).getTotalBasicParticlesOfType(AAUtil.type.E);
        		}
    			else {
    				AAUtil.type t = ((BasicParticle2D)p).getType();
    				if(t==AAUtil.type.A || t==AAUtil.type.B || t==AAUtil.type.D || t==AAUtil.type.E) {    				
    					total++;
    				}  
    			}
    		}    		
    	}    	
    	return total;   	
    	
    }*/
    
    /*
    public int getSumTotalBasicParticles() {   	
    	int total = 0;  
    	
    	System.out.println("STBPwhen used, note why");
    	
    	for(Particle2D p : grid.getParticles()) {    		
			if(p instanceof Capsule2D) {
    			total += ((Capsule2D)p).getTotalBasicParticlesOfType(AAUtil.type.A);
    			total += ((Capsule2D)p).getTotalBasicParticlesOfType(AAUtil.type.B);
    			total += ((Capsule2D)p).getTotalBasicParticlesOfType(AAUtil.type.D);
    			total += ((Capsule2D)p).getTotalBasicParticlesOfType(AAUtil.type.E);
    		}
			else {
				AAUtil.type t = ((BasicParticle2D)p).getType();
				if(t==AAUtil.type.A || t==AAUtil.type.B || t==AAUtil.type.D || t==AAUtil.type.E) {    				
					total++;
				}  
			}
    	}    		    	    	
    	return total;   	    	
    }*/
       
   
    public int getWeightOfParticlesOfTypeAtXY(AAUtil.type t, int x, int y) {
    	int total = 0;    	
    	for(Particle2D p : grid.getParticles()) {
    		if(p.getX() == x && p.getY() == y) {
    			total += p.getTotalWeightOfType(t);
    		}
    	}    	
    	return total;
    }    
    
    public int getWeightOfParticlesAtXY(int x, int y) {
    	int total = 0;    	
    	for(Particle2D p : grid.getParticles()) {
    		if(p.getX() == x && p.getY() == y) {
    			total += p.getTotalWeight();
    		}
    	}    	
    	return total;
    }
    
    public Particle2DArrayList getBasicParticlesOfType(AAUtil.type t) {
    	Particle2DArrayList newParticles = new Particle2DArrayList();
    	for(Particle2D p : grid.getParticles()) {
    		if(p instanceof BasicParticle2D && ((BasicParticle2D) p).isOfType(t)) {
    			newParticles.add(p);
    		}
    	}
    	return newParticles;    	
    } 
    
  
    //Gives total number of particles of types A/B/C/D/E/F/G
    public int getTotalBasicParticlesOfType(AAUtil.type t) {
    	int sum = 0;
    	for(Particle2D p : grid.getParticles()) {
    		sum += p.getTotalBasicParticlesOfType(t);
    	}
    	return sum;
    }
    
    public int getTotalWeightOfParticles() {
    	int sum = 0;
    	for(Particle2D p : grid.getParticles()) {
    		sum += p.getTotalWeight();    		
    	}
    	return sum;    	
    }  

    /*
    //Gives total number of particles of types A/B/D/E, where C and F count as composed of their subunits
    public int getTotalAtomicParticlesOfType(AAUtil.type t) {
    	int sum = 0;
    	for(Particle2D p : grid.getParticles()) {
    		sum += p.getTotalAtomicParticlesOfType(t);    		
    	}
    	return sum;
    }*/    
    
    //return the number of F particles that are not contained (or part of) a capsule 
    public int getTotalGsNotInCapsules() {
    	int number = 0;
    	for(Particle2D p : grid.getParticles()) {
    		if(p instanceof BasicParticle2D && ((BasicParticle2D) p).isOfType(AAUtil.type.G)) {
    			number++;    			
    		}
    	}    	
    	return number;
	}
        
    public int getTotalAutogensByContainedParticles() {
    	int sum = 0;
    	for(Particle2D p : grid.getParticles()) {
    		if(p instanceof Capsule2D && !((Capsule2D) p).getContainedParticles().isEmpty()) {
    			sum++;
    		}
    	}
    	return sum;
    }
    
    public double getAverageAutogenSizeByContainedParticles() {
    	int size = 0;
    	int number = 0;
    	for(Particle2D p : grid.getParticles()) {
    		if(p instanceof Capsule2D && !((Capsule2D)p).getContainedParticles().isEmpty()) {
    			number++;
    			size += ((Capsule2D) p).getCapsuleParticles().size();
    		}
    	}    	
    	return (number>0?((double)size/(double)number):0.0);    	
    }
      
    public int getTotalEmptyCapsulesByContainedParticles() {
    	int sum = 0;
    	for(Particle2D p : grid.getParticles()) {
    		if(p instanceof Capsule2D && ((Capsule2D)p).getContainedParticles().isEmpty()) {
    			sum++;
    		}
    	}
    	return sum;
    }
    
    public double getAverageEmptyCapsuleSizeByContainedParticles() {
    	int size = 0;
    	int number = 0;
    	for(Particle2D p : grid.getParticles()) {
    		if(p instanceof Capsule2D && ((Capsule2D)p).getContainedParticles().isEmpty()) {
    			number++;
    			size += ((Capsule2D) p).getCapsuleParticles().size();
    		}
    	}    	
    	return (number>0?((double)size/(double)number):0.0);    	
    }
    
  
    
    public double[] getEntropyQuantifications(){
    	
    	double[] ed = new double[8];
    	double[][] pdSA = getDistribution(1); //Self-assembly particles
    	double[][] pdAC = getDistribution(2); //Autocatalysis reactions
    		    		    		
    	//Normalized Information Entropy for Self-assembly H(X) 
    	ed[0] = getNormalizedInformationEntropy(pdSA[0], (int)Math.round(pdSA[1][0]));

    	//Normalized Information Entropy for Autocatalysis H(Y) 
    	ed[1] = getNormalizedInformationEntropy(pdAC[0], (int)Math.round(pdAC[1][0]));

    	//Mutual Information I(X;Y)
    	ed[2] = getMutualInformation(pdSA[0], (int)Math.round(pdSA[1][0]), pdAC[0], (int)Math.round(pdAC[1][0]));
    	
    	//Joint Entropy H(X,Y) NOTE: Not Normalized to fit with MI
    	ed[3] = (getInformationEntropy(pdSA[0], (int)Math.round(pdSA[1][0])) + getInformationEntropy(pdAC[0], (int)Math.round(pdAC[1][0]))) - ed[2]; //echt heel lui
    	
    	//KLDIV X || Y
    	ed[4] = getKLDivergenceWithEpsilon(pdSA[0], (int)Math.round(pdSA[1][0]), pdAC[0], (int)Math.round(pdAC[1][0]), 0.0001);
    	
    	//KLDIV Y || X
    	ed[5] = getKLDivergenceWithEpsilon(pdAC[0], (int)Math.round(pdAC[1][0]), pdSA[0], (int)Math.round(pdSA[1][0]), 0.0001);
    	
    	//Crossentropy H(X,Y)
    	ed[6] = getCrossEntropyWithEpsilon(pdSA[0], (int)Math.round(pdSA[1][0]), pdAC[0], (int)Math.round(pdAC[1][0]), 0.0001);
    	
    	//Crossentropy H(Y,X)
    	ed[7] = getCrossEntropyWithEpsilon(pdAC[0], (int)Math.round(pdAC[1][0]), pdSA[0], (int)Math.round(pdSA[1][0]), 0.0001);
    	
    	return ed;
    }
    
    
        /*
    public double[] getParticleData() {
    	double[] data = new double[5];   
    	
    	for(Particle2D p : grid.getParticles()) {
    		if(p instanceof Capsule2D) {    			
    			if(((Capsule2D) p).isAutogen()) {
    				data[0]++; //#autogens
    				data[1] += ((Capsule2D)p).getCapsuleParticles().size();
    				data[2] += ((Capsule2D)p).getContainedParticles().size();
    			}
    			else { //non-autogen, never contained particles
    				data[3]++; //#empty caps
    				data[4] += ((Capsule2D)p).getCapsuleParticles().size(); //totals capsule
    			}    				
    		}    		
    	}    	
    	return data;    	
    }*/
}
