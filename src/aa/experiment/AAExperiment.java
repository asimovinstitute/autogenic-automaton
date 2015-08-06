
package aa.experiment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import aa.AAControl;
import aa.AAParameters;
import aa.AAUtil;

public class AAExperiment {
		
	AAControl control;
	String type;
	String paramfile;
	int trials;
	int iterations;
	ArrayList<AALogger> logs = new ArrayList<AALogger>();	
	
	private static final String TIME_FORMAT = "mm'm'ss's'";
	private static long startTime;
		
	//Run a series of experiments, and write the results to a log file
	public AAExperiment(AAControl c, String t, String p, int tr, int it) {
		control = c;				
		type = t;
		paramfile = p;
		trials = tr;
		iterations = it;
	}		

	public void start() {				

		if(type.equals("SA")) {
			startSA();  		
		}
		else {
			System.out.println("Experiment type unknown: "+type);
		}			 		
	}	
	
	// Self-assembly
	public void startSA() {
		
		// Preparation
		startTime = System.currentTimeMillis();					
		logs.add(new AALogger(control, "SA", true));

		// Calculate normalized information entropy over time, for all trials
		double[][] entropy = new double[iterations][trials];
		System.out.print("\nTRIALS ");
		for(int t=0;t<trials;t++) {	
				File parameterFile = new File(paramfile);			
				AAUtil.loadParameters(control, parameterFile, false);						
				control.resetSimulation();
									
				for(int it=0;it<iterations;it++) {						
					control.stepSimulation();
					entropy[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);
				}		
			System.out.print("|");				
		}
		System.out.println("\n");								
		
		// Calculate and log average entropy values over time 
		double[] sumEntropy = new double[iterations];	
		((AALogger)logs.get(0)).write("Averaged normalized information entropy over "+iterations+" time steps: ,");
		for(int i=0;i<iterations;i++) {  
			for(int t=0;t<trials;t++) {
				sumEntropy[i] += entropy[i][t];
			}
			((AALogger)logs.get(0)).write((sumEntropy[i] / (double)trials) +",");
		}			
		((AALogger)logs.get(0)).write("\n");

		// Calculate and log deviation over time
		((AALogger)logs.get(0)).write("Standard deviation of normalized information entropy over "+iterations+" time steps: ,");
		for(int i=0;i<iterations;i++) {  				
			((AALogger)logs.get(0)).write(calculateDev(entropy[i])+",");
		}									
		AAUtil.out("Done! ("+getTimeDifference()+")");				
	}
	
	
	//Calculates the standard deviation of an array of doubles
	public double calculateDev(double[] d) {

		double size = (double)d.length;
		
		//first, calculate mean
		double sum = 0;
		for(int k = 0;k<d.length;k++) {
			sum += d[k];
		}
		double mean = sum/size;
						
		double stdev = 0;
		for(int k = 0;k<d.length;k++) {
		    stdev += Math.pow((d[k] - mean), 2) / size;										   
		}								
		return Math.sqrt(stdev);
	}
	
			
	public static String getTimeDifference() {
	    SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
	    return sdf.format(System.currentTimeMillis() - startTime);	
	}
}
