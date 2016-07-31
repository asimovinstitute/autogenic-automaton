
package aa.experiment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import aa.AAControl;
import aa.AAParameters;
import aa.AAUtil;

// contains helper functions for running the experiments - not an integral part of the AA code 

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
	
	// startFixed: run the simulation with constant parameters for a number of time steps
	public void start() {
		
		// Preparation
		startTime = System.currentTimeMillis();					
		log = new AALogger(control, paramfile1 + (stable ? "" : ("-"+paramfile2)), true);

		// Calculate normalized information entropy over time, for all trials		
		int totalIterations = iterations1 + (stable ? 0 : iterations2);
		double[][] entropyG = new double[totalIterations][trials];
		double[][] entropyR = new double[totalIterations][trials];
		double[][] kld = new double[totalIterations][trials];
		System.out.print("\nTRIALS ");
		for(int t=0;t<trials;t++) {	
			File parameterFile = new File(paramfile1);			
			AAUtil.loadParameters(control, parameterFile, false);						
			control.resetSimulation();
						
			if(stable) {
				for(int it=0;it<totalIterations;it++) {						
					control.stepSimulation();
					entropyG[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);					
					entropyR[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);
					kld[it][t] = control.getModel().getGrid().getGridFunctions().getSymmetrizedKLDivergenceWithGammaSmoothing();
				}						
			}
			else { //change conditions
				for(int it=0;it<iterations1;it++) {						
					control.stepSimulation();
					entropyG[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);					
					entropyR[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);
					kld[it][t] = control.getModel().getGrid().getGridFunctions().getSymmetrizedKLDivergenceWithGammaSmoothing();
				}
				
				File parameterFile2 = new File(paramfile2);			
				AAParameters.PROBABILITIES = AAUtil.getProbabilities(parameterFile2);
				
				for(int it=iterations1;it<totalIterations;it++) {						
					control.stepSimulation();
					entropyG[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);					
					entropyR[it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);
					kld[it][t] = control.getModel().getGrid().getGridFunctions().getSymmetrizedKLDivergenceWithGammaSmoothing();
				}
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
		log.write("\n");
		
		// Calculate and log average entropy values over time for KLD
		double[] sumKld = new double[totalIterations];	
		log.write("Averaged Kullback-Leibler divergence of G particle locations and R reaction locations over "+totalIterations+" time steps: ,");
		for(int i=0;i<totalIterations;i++) {  
			for(int t=0;t<trials;t++) {
				sumKld[i] += kld[i][t];
			}
			log.write((sumKld[i] / (double)trials) +",");
		}			
		log.write("\n");
		
		// Calculate and log deviation over time for KLD
		log.write("Standard deviation of Kullback-Leibler divergence of G particle locations and R reaction locations over "+totalIterations+" time steps: ,");
		for(int i=0;i<totalIterations;i++) {  				
			log.write(calculateDev(kld[i])+",");
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




