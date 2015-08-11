
package aa.experiment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import aa.AAControl;
import aa.AAParameters;
import aa.AAUtil;

public class AAExperiment {
		
	AAControl control;
	String paramfile1;
	String paramfile2;
	int iterations1;
	int iterations2;
	int trials;		
	boolean stable;
	AALogger log; //ArrayList<AALogger> logs = new ArrayList<AALogger>();
	
	private static final String TIME_FORMAT = "mm'm'ss's'";
	private static long startTime;
		
	//Run a series of experiments, and write the results to a log file
	public AAExperiment(AAControl c, String p, int it, int tr) {
		control = c;				
		paramfile1 = p;
		iterations1 = it;
		trials = tr;
		stable = true;
	}		
	
	//Run a series of experiments with changing parameters, and write the results to a log file
	public AAExperiment(AAControl c, String p1, int it1, String p2, int it2, int tr) {
		control = c;				
		paramfile1 = p1;
		iterations1 = it1;
		paramfile2 = p2;
		iterations2 = it2;
		trials = tr;
		stable = false;
	}		

	public void start() {				

		if(stable) {
			startStable();  		
		}
		else { 
			startChange();  		
		}
	}	
	
	// startFixed: run the simulation with constant parameters for a number of time steps
	public void startStable() {
		
		// Preparation
		startTime = System.currentTimeMillis();					
		log = new AALogger(control, paramfile1, true);

		// Calculate normalized information entropy over time, for all trials
		double[][] entropyG = new double[iterations1][trials];
		double[][] entropyR = new double[iterations1][trials];
		System.out.print("\nTRIALS ");
		for(int t=0;t<trials;t++) {	
				File parameterFile = new File(paramfile1);			
				AAUtil.loadParameters(control, parameterFile, false);						
				control.resetSimulation();
									
				for(int it=0;it<iterations1;it++) {						
					control.stepSimulation();
					entropyG[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);					
					entropyR[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);
				}		
			System.out.print("|");				
		}
		System.out.println("\n");								
		
		// Calculate and log average entropy values over time for G
		double[] sumEntropyG = new double[iterations1];	
		log.write("Averaged normalized information entropy of G particle locations over "+iterations1+" time steps: ,");
		for(int i=0;i<iterations1;i++) {  
			for(int t=0;t<trials;t++) {
				sumEntropyG[i] += entropyG[i][t];
			}
			log.write((sumEntropyG[i] / (double)trials) +",");
		}			
		log.write("\n");
		
		// Calculate and log deviation over time for G
		log.write("Standard deviation of normalized information entropy of G particle locations over "+iterations1+" time steps: ,");
		for(int i=0;i<iterations1;i++) {  				
			log.write(calculateDev(entropyG[i])+",");
		}									
		log.write("\n");
		
		// Calculate and log average entropy values over time for R
		double[] sumEntropyR = new double[iterations1];	
		log.write("Averaged normalized information entropy of R reaction locations over "+iterations1+" time steps: ,");
		for(int i=0;i<iterations1;i++) {  
			for(int t=0;t<trials;t++) {
				sumEntropyR[i] += entropyR[i][t];
			}
			log.write((sumEntropyR[i] / (double)trials) +",");
		}			
		log.write("\n");
		
		// Calculate and log deviation over time for R
		log.write("Standard deviation of normalized information entropy of R reaction locations over "+iterations1+" time steps: ,");
		for(int i=0;i<iterations1;i++) {  				
			log.write(calculateDev(entropyR[i])+",");
		}									
		AAUtil.out("Done! ("+getTimeDifference()+")");								
	}
	
	// startAltered: run the simulation, change parameters after a number of time steps
	public void startChange() {
		
		// Preparation
		startTime = System.currentTimeMillis();					
		log = new AALogger(control, paramfile1+"-"+paramfile2, true);

		// Calculate normalized information entropy over time, for all trials
		int totalIterations = iterations1 + iterations2;
		double[][] entropyG = new double[totalIterations][trials];
		double[][] entropyR = new double[totalIterations][trials];
		System.out.print("\nTRIALS ");
		for(int t=0;t<trials;t++) {	
				File parameterFile = new File(paramfile1);			
				AAUtil.loadParameters(control, parameterFile, false);						
				control.resetSimulation();
									
				for(int it=0;it<iterations1;it++) {						
					control.stepSimulation();
					entropyG[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);					
					entropyR[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);
				}
				
				File parameterFile2 = new File(paramfile2);			
				AAParameters.PROBABILITIES = AAUtil.getProbabilities(parameterFile2);
				
				for(int it=iterations1;it<totalIterations;it++) {						
					control.stepSimulation();
					entropyG[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);					
					entropyR[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);
				}
				
			System.out.print("|");				
		}
		System.out.println("\n");								
		
		// Calculate and log average entropy values over time for G
		double[] sumEntropyG = new double[totalIterations];	
		log.write("Averaged normalized information entropy of G particle locations over "+totalIterations+" time steps: ,");
		for(int i=0;i<totalIterations;i++) {  
			for(int t=0;t<trials;t++) {
				sumEntropyG[i] += entropyG[i][t];
			}
			log.write((sumEntropyG[i] / (double)trials) +",");
		}			
		log.write("\n");
		
		// Calculate and log deviation over time for G
		log.write("Standard deviation of normalized information entropy of G particle locations over "+totalIterations+" time steps: ,");
		for(int i=0;i<totalIterations;i++) {  				
			log.write(calculateDev(entropyG[i])+",");
		}									
		log.write("\n");
		
		// Calculate and log average entropy values over time for R
		double[] sumEntropyR = new double[totalIterations];	
		log.write("Averaged normalized information entropy of R reaction locations over "+totalIterations+" time steps: ,");
		for(int i=0;i<totalIterations;i++) {  
			for(int t=0;t<trials;t++) {
				sumEntropyR[i] += entropyR[i][t];
			}
			log.write((sumEntropyR[i] / (double)trials) +",");
		}			
		log.write("\n");
		
		// Calculate and log deviation over time for R
		log.write("Standard deviation of normalized information entropy of R reaction locations over "+totalIterations+" time steps: ,");
		for(int i=0;i<totalIterations;i++) {  				
			log.write(calculateDev(entropyR[i])+",");
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




