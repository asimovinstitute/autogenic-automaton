
package aa.experiment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import aa.AAControl;
import aa.AAParameters;
import aa.AAUtil;
import aa.AAUtil.type;
import aa.model.AAModel;
import aa.model.Capsule2D;
import aa.model.Grid2DFunctions;
import aa.model.Particle2D;

public class AAExp4 {
		
	AAControl control;
	AALogger log;
	AALogger logCSEL;  
	AALogger log1;
	AALogger log2;
	AALogger log3;
	AALogger log4;
	AALogger log5;
	AALogger log6;
	AALogger logG;	
	
	private static final String TIME_FORMAT = "mm'm'ss's'";
	private static long startTime;
		
	//Run a series of experiments, and write the results to a log file
	public AAExp4(AAControl c) {
		control = c;				
	}		

	public void start() {				

		int trials;
		
		//12s per trial  done
		trials = 100;	 //100	
		startSA3(5000,trials);  
			 
		//10m per trial (niet veel trials nodig)    400 datapoint 10 trials draaien
		//trials = 1;
		//startAS2(250,5000,trials);		 
		
		//30s per trial   done*/
		
		//startELIMSA(-2, -1, 1000, 5, trials, "ELIM.SA");
		/*startELIMSA(-2, -2, 1000, 5, trials, "ELIM.SA");   
		startELIMSA(-2, -3, 1000, 5, trials, "ELIM.SA");
		startELIMSA(-2, -4, 1000, 5, trials, "ELIM.SA");
		startELIMSA(-2, -5, 1000, 5, trials, "ELIM.SA");
		startELIMSA(-2, -6, 1000, 5, trials, "ELIM.SA");*/
		
		//trials = 1000;
		
		/*startELIMSA(-1, 0, 1000, 5, trials, "ELIM.SA");
		startELIMSA(-1, -1, 1000, 5, trials, "ELIM.SA");
		startELIMSA(-1, -2, 1000, 5, trials, "ELIM.SA");   
		startELIMSA(-1, -3, 1000, 5, trials, "ELIM.SA");
		startELIMSA(-1, -4, 1000, 5, trials, "ELIM.SA");
		
		startELIMSA(0, 0, 1000, 5, trials, "ELIM.SA");
		startELIMSA(0, -1, 1000, 5, trials, "ELIM.SA");
		startELIMSA(0, -2, 1000, 5, trials, "ELIM.SA");   
		startELIMSA(0, -3, 1000, 5, trials, "ELIM.SA");
		startELIMSA(0, -4, 1000, 5, trials, "ELIM.SA");*/
		
		//5m30s per 100 timesteps		
		//trials = 1;
		//startKLD(5000, trials);				
		
		//3 min per trial
		/*trials = 1000;
		startAG(0, 0, 1000, 5, trials, "AG", true, false);
		startAG(0, -1, 1000, 5, trials, "AG", true, false);
		startAG(0, -2, 1000, 5, trials, "AG", true, false);
		startAG(0, -3, 1000, 5, trials, "AG", true, false);
		startAG(0, -4, 1000, 5, trials, "AG", true, false);
		//startAG(0, -5, 1000, 5, trials, "AG", true, false);
		
		startAG(0, 0, 1000, 5, trials, "NAG", false, true);
		startAG(0, -1, 1000, 5, trials, "NAG", false, true);
		startAG(0, -2, 1000, 5, trials, "NAG", false, true);
		startAG(0, -3, 1000, 5, trials, "NAG", false, true);    
		startAG(0, -4, 1000, 5, trials, "NAG", false, true);*/
		//startAG(0, -5, 1000, 5, trials, "NAG", false, true);				
		
		//27s per trial --> 7.5h per 1000
		//trials = 1000;
		//startSEL(5000,trials,"SEL", true, false); //SEL
		//startSEL(5000,trials,"NSEL", false, true); //NSEL	
		
		//trials = 1000;
		//trials = 1000;
		//startSEL(5000,trials,"SEL0", true, false); //SEL
		//startSEL(5000,trials,"NSEL0", false, true); //NSEL	
		
		/*startSEL(5000,trials,"SEL-1", true, false); //SEL
		startSEL(5000,trials,"NSEL-1", false, true); //NSEL	*/
	}	
	
	public void startSEL(int iterations, int trials, String filename, boolean logGs, boolean useLoggedGs) {
		startTime = System.currentTimeMillis();
		
		AALogger log1 = new AALogger(control, filename, true);
		
		double[] muEntropy = new double[iterations];
		logCSEL = new AALogger(control, "C.SEL", true);
		
		double[] addG = new double[iterations];
		//double[] removeG = new double[iterations];
		if(logGs) {
			logG = new AALogger(control, "G.SEL", false);	
		}
		
		File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/"+filename+".csv");			
		AAUtil.loadParameters(control, parameterFile, false);
	
		//double[] averageAge = new double[AAParameters.MAX_CAPSULE_PARTICLES-1];
		//double[] averageAge = new double[AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT];				
		
		for(int i=0;i<trials;i++) {	
			
			control.resetSimulation();
			
			control.getModel().getGrid().setLogGs(logGs);
			control.getModel().getGrid().setUseLogGs(useLoggedGs);
			
			control.getModel().getGrid().setUseMaxCapSize(false);
			control.getModel().getGrid().setUseMaxContained(true);			
			
			if(useLoggedGs) {
				control.getModel().getGrid().loadGs("log/G.SEL.csv", iterations);
			}
			//double[] capsulesOverTime = new double[AAParameters.MAX_CAPSULE_PARTICLES-1];
			//double[] capsulesOverTime = new double[AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT];
			//double[] tempAverageAge = new double[AAParameters.MAX_CAPSULE_PARTICLES-1];
			//double[] tempAverageAge = new double[AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT];
			
			//int[] totalLifeSpan = new int[AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT];
			//int[] totalCrystals = new int[AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT];
			int[] totalCrystalSize = new int[AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT];
			int[] totalCrystals = new int[AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT];
									
			for(int time=0;time<iterations;time++) {
				control.stepSimulation();
				
				muEntropy[time] += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropyMu();
								
				if(logGs) {
					addG[time] += control.getModel().getGrid().getAddedGs();
					//removeG[time] += control.getModel().getGrid().getRemovedGs();
				}
				
				//ArrayList<ArrayList<Integer>> data = control.getModel().getGrid().getGridFunctions().getAgeData2();
				ArrayList<ArrayList<Integer>> data = control.getModel().getGrid().getGridFunctions().getSizeData();
				for(int j=0;j<data.size();j++) {
					ArrayList<Integer> sizes = data.get(j);
					for(int k=0;k<sizes.size();k++) {
						totalCrystalSize[j] += sizes.get(k);
						totalCrystals[j]++;
						//logs[j].write(ages.get(k)+",");
					}
				}
				
				//double[][] ageData = control.getModel().getGrid().getGridFunctions().getAgeData();				
				/*for(int j=0;j<tempAverageAge.length;j++) {
					if(ageData[j][1] > 0) {
						tempAverageAge[j] += (ageData[j][0] / ageData[j][1]);
						capsulesOverTime[j] += ageData[j][1];
					}
					//System.out.print(averageAge[j]+" ");
				}*/				
				//System.out.print("\n");
			}		
			
			for(int j=0;j<totalCrystalSize.length;j++) {
				log1.write(((double)totalCrystalSize[j]/(double)totalCrystals[j])+",");								
			}
			log1.write("\n");
			
						
			/*for(int k=0;k<averageAge.length;k++) {		
				if(capsulesOverTime[k] > 0) {
					averageAge[k] += tempAverageAge[k] / capsulesOverTime[k]; //average over all capsules present
				}
			}	*/
			//for(int k=0;k<averageAge.length;k++) {
			//	System.out.print(averageAge[k]+", ");
			//}
			//System.out.print("\n");
			if(i%10==0){
				System.out.print(" ");
			}
			if(i%100==0){
				System.out.print("\n");
			}
			System.out.print("|");
		}
		
		//average logGs and muEntropy over trials and write to log				
		for(int i=0;i<iterations;i++) {			
		
			logCSEL.write((muEntropy[i] / (double)trials)+",");
			
			if(logGs) {			
				int addedGs = (int)Math.round(addG[i] / (double)trials);
				//int removedGs = (int)Math.round(addG[i] / (double)trials);
				logG.write(addedGs+"\n");
				//logG.write(addedGs+","+removedGs+"\n");
			}
		}
		
		//finally, average over trials
		//for(int k=0;k<averageAge.length;k++) {
		//	log.write((averageAge[k] / (double)trials) +",");
		//	System.out.print((averageAge[k] / (double)trials) +",");						
		//}				
		System.out.print("\n");
		AAUtil.out("Done! ("+getTimeDifference()+")");				
	}
	

	public void startAG(int startalpha, int endalpha, int iterations, int timefactor, int trials, String filename, boolean logGs, boolean useLoggedGs) {
		startTime = System.currentTimeMillis();
		log = new AALogger(control, filename+"."+startalpha+"."+endalpha, false);	
		
		double[] entropy = new double[iterations*timefactor];
		double[] addG = new double[iterations*timefactor];
		//double[] removeG = new double[iterations*timefactor];
		if(logGs) {
			logG = new AALogger(control, "G.AG."+startalpha+"."+endalpha, false);			
		}
		
		for(int i=0;i<trials;i++) {								
			File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/"+filename+".csv");			
			AAUtil.loadParameters(control, parameterFile, false);		
			AAParameters.PROBABILITIES[0] = 1;
			AAParameters.PROBABILITIES[1] = startalpha;  			
			
			//AAParameters.PROBABILITIES[2] = 6; //rho+					
			//AAParameters.PROBABILITIES[3] = 0.5;//0.5; //immediate breakup
			//AAParameters.PROBABILITIES[4] = 6; //rho+
			//AAParameters.PROBABILITIES[5] = 0.5;//0.5; //immediate breakup			
			
			control.resetSimulation();
			
			control.getModel().getGrid().setLogGs(logGs);
			control.getModel().getGrid().setUseLogGs(useLoggedGs);
			
			if(useLoggedGs) {
				control.getModel().getGrid().loadGs("log/G.AG."+startalpha+"."+endalpha+".csv", (iterations*timefactor));
			}
			
			int time=0;
			for(;time<iterations;time++) {
				control.stepSimulation();
				entropy[time] += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);
				if(logGs) {
					addG[time] += control.getModel().getGrid().getAddedGs();
					//removeG[time] += control.getModel().getGrid().getRemovedGs();
				}
			}	
			AAParameters.PROBABILITIES[1] = endalpha; //gamma-  
			for(;time<iterations*timefactor;time++) {
				control.stepSimulation();
				entropy[time] += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);
				if(logGs) {
					addG[time] += control.getModel().getGrid().getAddedGs();
					//removeG[time] += control.getModel().getGrid().getRemovedGs();
				}
			}
			System.out.print(" | " + control.getModel().getGrid().getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.G));		
		}
		//average out values and log				
		for(int k=0;k<iterations*timefactor;k++) {
			double avg = entropy[k] / (double)trials;			
			log.write(avg+",");
			if(logGs) {
				int addedGs = (int)Math.round(addG[k] / (double)trials);
				//int removedGs = (int)Math.round(addG[k] / (double)trials);
				//logG.write(addedGs+","+removedGs+"\n");
				logG.write(addedGs+"\n");
			}
		}		
		AAUtil.out("Done! ("+getTimeDifference()+")");				
	}
	
	
	public void startKLD(int iterations, int trials) {//, double epsilon) {				
		
		startTime = System.currentTimeMillis();					
		log1 = new AALogger(control, "KLD", true);		
		//log2 = new AALogger(control, "KLDG", true);			
		//log3 = new AALogger(control, "KLDR", true);
		
		
		int datapoints = 21;
		//double[][][] entropyG = new double[trials][datapoints][datapoints];
		//double[][][] entropyR = new double[trials][datapoints][datapoints];				
		double[][] kldValues = new double[iterations][datapoints*datapoints];
		
		for(int t=0;t<trials;t++) {						
			for(int i=0;i<datapoints;i++) {
				for(int j=0; j<datapoints; j++) { 				
					File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/KLD.csv");			
					AAUtil.loadParameters(control, parameterFile,false);
					AAParameters.PROBABILITIES[0] = 1.0; //gamma+
					AAParameters.PROBABILITIES[1] = -10+i; //gamma-
					AAParameters.PROBABILITIES[2] = -10+j; //rho+					
					AAParameters.PROBABILITIES[3] = 0.5; //immediate breakup
					AAParameters.PROBABILITIES[4] = -10+j; //rho+
					AAParameters.PROBABILITIES[5] = 0.5; //immediate breakup
					control.resetSimulation();
					
					for(int it=0;it<iterations;it++) {
						control.stepSimulation();
						//entropy[t][i][j] += control.getModel().getGrid().getGridFunctions().getNormalizedSymmetrizedKLDivergence();
						//entropy[t][i][j] += control.getModel().getGrid().getGridFunctions().getSymmetrizedKLDivergenceWithEpsilon(epsilon);
						
						kldValues[it][i*datapoints+j] = control.getModel().getGrid().getGridFunctions().getSymmetrizedKLDivergenceWithGammaSmoothing();												
						//entropyG[t][i][j] += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);
						//entropyR[t][i][j] += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);						
					}					
					System.out.print("|");
					log1.write("\n");
					//System.out.println("g- "+ (-10+(i*2)) + " r+ " + (-10+(j*2)) + " G " +control.getModel().getGrid().getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.G));					
				}
				System.out.println();
			}
		}		
		
		for(int i=0;i<kldValues.length;i++) {
			//double sum = 0;
			//double total = 0;
			for(int j=0;j<kldValues[0].length;j++) {
				log1.write(kldValues[i][j] + ",");
			}
			log1.write("\n");
				
			/*
				if(kldValues[j][i] > 0) {				//if(kldValues[j][i] >= 0) {    //     <========= HERE >0?
					sum += kldValues[j][i];				// note in article: we only take positive KL values into account
					total++;
				}
			}	
			if(total > 0) {
				log1.write(sum/total+",");
			}
			else {
				//System.out.println("not enough data");
				log1.write(-1+",");
			}*/
		}
		
		/*
		
		for(int datap=0;datap<kldValues[0].length;datap++) {
			for(int it=0;it<kldValues.length;it++) {
				log1.write(kldValues[it][datap]+",");
			}			
			log1.write("\n");
		}
		
		
		/*double[][] entropy2 = new double[datapoints][datapoints];
		double[][] entropy2G = new double[datapoints][datapoints];
		double[][] entropy2R = new double[datapoints][datapoints];
		
		for(int t=0;t<trials;t++) {
			for(int i=0;i<datapoints;i++) {
				for(int j=0; j<datapoints; j++) { 		
					
					if(numberOfMeasurements[i][j] > 0) { //i.e. valid result
						entropy2[i][j] += entropy[t][i][j];
					}					
					
					entropy2G[i][j] += entropyG[t][i][j];	
					entropy2R[i][j] += entropyR[t][i][j];	
				}				
			}
		}
		
		for(int i=0;i<datapoints;i++) {
			for(int j=0; j<datapoints; j++) { 		
				
				//potentieel: -1 vervangen met max divergence 
				//log1.write(entropy2[i][j] / ((double)trials*(double)iterations)+",");
				if(numberOfMeasurements[i][j] > 0) {
					log1.write(entropy2[i][j] / numberOfMeasurements[i][j]+",");
				}
				else {
					System.out.println("Error in AAExp4: no valide measurements found in KLD");
					log1.write("-1,");
				}
				
				log2.write(entropy2G[i][j] / ((double)trials*(double)iterations)+",");
				log3.write(entropy2R[i][j] / ((double)trials*(double)iterations)+",");
			}
			//log1.write("\n");	
			log2.write("\n");	
			log3.write("\n");	
		}*/
		
		
		AAUtil.out("Done! ("+getTimeDifference()+")");		
	}
		
	public void startCE(int iterations, int trials, double epsilon) {
		startTime = System.currentTimeMillis();					
		log1 = new AALogger(control, "CE", true);	
		log2 = new AALogger(control, "CEG", true);	
		log3 = new AALogger(control, "CER", true);
		log4 = new AALogger(control, "KLD", true);
		log5 = new AALogger(control, "IE1", true);
		log6 = new AALogger(control, "IE2", true);
		
		int datapoints = 21;
		
		double[][][] entropy = new double[trials][datapoints][datapoints];
		double[][][] entropyG = new double[trials][datapoints][datapoints];
		double[][][] entropyR = new double[trials][datapoints][datapoints];
		double[][][] entropyKLD = new double[trials][datapoints][datapoints];
		double[][][] entropyIE1 = new double[trials][datapoints][datapoints];
		double[][][] entropyIE2 = new double[trials][datapoints][datapoints];
		
		for(int t=0;t<trials;t++) {						
			for(int i=0;i<datapoints;i++) {
				for(int j=0; j<datapoints; j++) { 				
					File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/CE.csv");			
					AAUtil.loadParameters(control, parameterFile,false);
					AAParameters.PROBABILITIES[0] = 1.0; //gamma+
					AAParameters.PROBABILITIES[1] = -10+i;//-10+(i*2);// //gamma-
					AAParameters.PROBABILITIES[2] = -10+j;//-10+(j*2);// //rho+					
					AAParameters.PROBABILITIES[3] = 0.5; 
					AAParameters.PROBABILITIES[4] = -10+j;//-10+(j*2); //rho+
					AAParameters.PROBABILITIES[5] = 0.5; 
					control.resetSimulation();
					
					for(int it=0;it<iterations;it++) {
						control.stepSimulation();
						
						double[] cedata = control.getModel().getGrid().getGridFunctions().getCEData(epsilon);
						
						entropy[t][i][j] += cedata[0]; //control.getModel().getGrid().getGridFunctions().getSymmetrizedCrossEntropyWithEpsilon(epsilon);
						entropyG[t][i][j] += cedata[1]; //control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);
						entropyR[t][i][j] += cedata[2]; //control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);
						entropyKLD[t][i][j] += cedata[3];
						entropyIE1[t][i][j] += cedata[4];
						entropyIE2[t][i][j] += cedata[5];
					}												
					//System.out.println("g- "+ (-10+(i*2)) + " r+ " + (-10+(j*2)) + " G " +control.getModel().getGrid().getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.G));					
				}
				System.out.print("|");
			}
		}		
		
		double[][] entropy2 = new double[datapoints][datapoints];
		double[][] entropy2G = new double[datapoints][datapoints];
		double[][] entropy2R = new double[datapoints][datapoints];
		double[][] entropy2KLD = new double[datapoints][datapoints];
		double[][] entropy2IE1 = new double[datapoints][datapoints];
		double[][] entropy2IE2 = new double[datapoints][datapoints];
		
		for(int t=0;t<trials;t++) {
			for(int i=0;i<datapoints;i++) {
				for(int j=0; j<datapoints; j++) { 		
					entropy2[i][j] += entropy[t][i][j];	
					entropy2G[i][j] += entropyG[t][i][j];	
					entropy2R[i][j] += entropyR[t][i][j];
					entropy2KLD[i][j] += entropyKLD[t][i][j];
					entropy2IE1[i][j] += entropyIE1[t][i][j];
					entropy2IE2[i][j] += entropyIE2[t][i][j];
				}				
			}
		}
		
		for(int i=0;i<datapoints;i++) {
			for(int j=0; j<datapoints; j++) { 		
				log1.write(entropy2[i][j] / ((double)trials*(double)iterations)+",");
				log2.write(entropy2G[i][j] / ((double)trials*(double)iterations)+",");
				log3.write(entropy2R[i][j] / ((double)trials*(double)iterations)+",");
				log4.write(entropy2KLD[i][j] / ((double)trials*(double)iterations)+",");
				log5.write(entropy2IE1[i][j] / ((double)trials*(double)iterations)+",");
				log6.write(entropy2IE2[i][j] / ((double)trials*(double)iterations)+",");
			}
			log1.write("\n");	
			log2.write("\n");	
			log3.write("\n");	
			log4.write("\n");
			log5.write("\n");	
			log6.write("\n");	
		}
		AAUtil.out("Done! ("+getTimeDifference()+")");		
	}
	
	public void startELIMSA(int startalpha, int endalpha, int iterations, int timefactor, int trials, String filename) {
		startTime = System.currentTimeMillis();
		log = new AALogger(control, filename+"."+startalpha+"."+endalpha, false);	
		
		double[] entropy = new double[iterations*timefactor];		
		
		for(int i=0;i<trials;i++) {								
			File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/"+filename+".csv");			
			AAUtil.loadParameters(control, parameterFile, false);		
			AAParameters.PROBABILITIES[0] = 1;
			AAParameters.PROBABILITIES[1] = startalpha;  
			AAParameters.PROBABILITIES[2] = 0; //rho+					
			AAParameters.PROBABILITIES[3] = 0;//0.5; //immediate breakup
			AAParameters.PROBABILITIES[4] = 0; //rho+
			AAParameters.PROBABILITIES[5] = 0;//0.5; //immediate breakup
			control.resetSimulation();
			
			int time=0;
			for(;time<iterations;time++) {
				control.stepSimulation();
				entropy[time] += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);				
			}	
			AAParameters.PROBABILITIES[1] = endalpha; //gamma-  
			for(;time<iterations*timefactor;time++) {
				control.stepSimulation();
				entropy[time] += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);				
			}
			System.out.print("|");		
		}
		//average out values and log				
		for(int k=0;k<iterations*timefactor;k++) {
			double avg = entropy[k] / (double)trials;			
			log.write(avg+",");			
		}		
		AAUtil.out("Done! ("+getTimeDifference()+")");				
	}
	
	
	public void startAS2(int particles, int iterations, int trials) {
		startTime = System.currentTimeMillis();					
		log = new AALogger(control, "AS2."+particles, true);
		log2 = new AALogger(control, "AS2SIGMA."+particles, true);

		int datapoints = 21;
		double[][][] entropy = new double[datapoints][datapoints][trials];
		for(int t=0;t<trials;t++) {	
			System.out.print("\nTRIAL "+(t+1)+" \n");
			for(int i=0;i<datapoints;i++) { 
				for(int j=0;j<datapoints;j++) { 
					File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/AS.csv");			
					AAUtil.loadParameters(control, parameterFile, false);						
					AAParameters.INIT_PARTICLES = new int[]{particles,particles,0,particles,particles,0,0};
					/**
					 *   0: gamma+
					 *   1: gamma-
					 *   2: chi+
					 *   3: chi-
					 *   4: phi+
					 *   5: phi-
					 */				
					AAParameters.PROBABILITIES[0] = 0.0; //no self-assembly
					AAParameters.PROBABILITIES[1] = -10.0; //immediately remove G particles to reduce computation time
					AAParameters.PROBABILITIES[2] = -10+i;//-10+(i*2); //chi+					
					AAParameters.PROBABILITIES[3] = 0.05*j;//0.1*j; //1.0; //immediate breakup
					AAParameters.PROBABILITIES[4] = -10+i;//-10+(i*2); //phi+
					AAParameters.PROBABILITIES[5] = 0.05*j;//0.1*j; //immediate breakup
					control.resetSimulation();
										
					for(int it=0;it<iterations;it++) {						
						control.stepSimulation();
						entropy[i][j][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);
					}		
					System.out.print("|");
				}
				System.out.println();
			}			
		}
		
		

		//average out values and log		
		for(int i=0;i<datapoints;i++) { 
			for(int j=0;j<datapoints;j++) {  				
				log2.write(calculateDev(entropy[i][j])+",");
			}
			log2.write("\n");
		}
		
		double[][] entropy2 = new double[datapoints][datapoints];
		for(int i=0;i<datapoints;i++) { 
			for(int j=0;j<datapoints;j++) {  
				for(int t=0;t<trials;t++) {
					entropy2[i][j] += entropy[i][j][t];
				}
			}
		}	
		
		for(int i=0;i<datapoints;i++) { 
			for(int j=0;j<datapoints;j++) {  
				log.write((entropy2[i][j] / ((double)trials * (double)iterations)) +",");
				//System.out.print((entropy2[i][j] / (double)trials) +",");
			}
			log.write("\n");
			//System.out.println();
		}						
		AAUtil.out("Done! ("+getTimeDifference()+")");					
	}
	
	public void startAS(int particles, int iterations, int trials) {
		startTime = System.currentTimeMillis();					
		log = new AALogger(control, "AS."+particles, true);

		int datapoints = 21;
		double[][] entropy = new double[datapoints][datapoints];
		for(int t=0;t<trials;t++) {	
			System.out.print("\nTRIAL "+(t+1)+" \n");
			for(int i=0;i<datapoints;i++) { 
				for(int j=0;j<datapoints;j++) { 
					File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/AS.csv");			
					AAUtil.loadParameters(control, parameterFile, false);						
					AAParameters.INIT_PARTICLES = new int[]{particles,particles,0,particles,particles,0,0};
					/**
					 *   0: gamma+
					 *   1: gamma-
					 *   2: chi+
					 *   3: chi-
					 *   4: phi+
					 *   5: phi-
					 */				
					AAParameters.PROBABILITIES[0] = 0.0; //no self-assembly
					AAParameters.PROBABILITIES[1] = -10.0; //immediately remove G particles to reduce computation time
					AAParameters.PROBABILITIES[2] = -10+i;//-10+(i*2); //chi+					
					AAParameters.PROBABILITIES[3] = 0.05*j;//0.1*j; //1.0; //immediate breakup
					AAParameters.PROBABILITIES[4] = -10+i;//-10+(i*2); //phi+
					AAParameters.PROBABILITIES[5] = 0.05*j;//0.1*j; //immediate breakup
					control.resetSimulation();
										
					for(int it=0;it<iterations;it++) {						
						control.stepSimulation();
						entropy[i][j] += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);
					}		
					System.out.print("|");
				}
				System.out.println();
			}			
		}
		//average out values and log			
		for(int i=0;i<datapoints;i++) { 
			for(int j=0;j<datapoints;j++) {  
				log.write((entropy[i][j] / ((double)trials * (double)iterations)) +",");
				System.out.print((entropy[i][j] / ((double)trials * (double)iterations)) +",");
			}
			log.write("\n");
			System.out.println();
		}						
		AAUtil.out("Done! ("+getTimeDifference()+")");				
	}
	
	//same as SA2, but now with significance measurement
	public void startSA3(int iterations, int trials) {
		startTime = System.currentTimeMillis();					
		log = new AALogger(control, "SA3", true);
		log2 = new AALogger(control, "SA3SIGMA", true);

		int datapoints = 6;
		double[][][] entropy = new double[datapoints][iterations][trials];
		for(int t=0;t<trials;t++) {	
			System.out.print("\nTRIAL "+t+" ");
			for(int i=0;i<datapoints;i++) { //i = g-					
				File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/SA3.csv");			
				AAUtil.loadParameters(control, parameterFile, false);						
				//AAParameters.INIT_PARTICLES = new int[]{0,0,0,0,0,0,particles};
				AAParameters.PROBABILITIES[1] = i-5; //gamma-
				control.resetSimulation();
									
				for(int it=0;it<iterations;it++) {						
					control.stepSimulation();
					entropy[i][it][t] = control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);
				}		
				System.out.print("|");
			}			
		}
		
		
		
		//average out values and log		
		for(int i=0;i<datapoints;i++) { 
			for(int j=0;j<iterations;j++) {  				
				log2.write(calculateDev(entropy[i][j])+",");
			}
			log2.write("\n");
		}
		
		double[][] entropy2 = new double[datapoints][iterations];
		for(int i=0;i<datapoints;i++) { 
			for(int j=0;j<iterations;j++) {  
				for(int t=0;t<trials;t++) {
					entropy2[i][j] += entropy[i][j][t];
				}
			}
		}	
		
		for(int i=0;i<datapoints;i++) { 
			for(int j=0;j<iterations;j++) {  
				log.write((entropy2[i][j] / (double)trials) +",");
				//System.out.print((entropy2[i][j] / (double)trials) +",");
			}
			log.write("\n");
			//System.out.println();
		}						
		AAUtil.out("Done! ("+getTimeDifference()+")");				
	}
		
	
	public void startSA2(int iterations, int trials) {
		startTime = System.currentTimeMillis();					
		log = new AALogger(control, "SA2", true);

		int datapoints = 6;
		double[][] entropy = new double[datapoints][iterations];
		for(int t=0;t<trials;t++) {	
			System.out.print("\nTRIAL "+t+" ");
			for(int i=0;i<datapoints;i++) { //i = g-					
				File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/SA2.csv");			
				AAUtil.loadParameters(control, parameterFile, false);						
				//AAParameters.INIT_PARTICLES = new int[]{0,0,0,0,0,0,particles};
				AAParameters.PROBABILITIES[1] = i-5; //gamma-
				control.resetSimulation();
									
				for(int it=0;it<iterations;it++) {						
					control.stepSimulation();
					entropy[i][it] += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);
				}		
				System.out.print("|");
			}			
		}
		//average out values and log			
		for(int i=0;i<datapoints;i++) { 
			for(int j=0;j<iterations;j++) {  
				log.write((entropy[i][j] / (double)trials) +",");
				System.out.print((entropy[i][j] / (double)trials) +",");
			}
			log.write("\n");
			System.out.println();
		}						
		AAUtil.out("Done! ("+getTimeDifference()+")");				
	}
	
	public void startAC(int particles, int iterations, int trials) {
		startTime = System.currentTimeMillis();					
		log = new AALogger(control, "AC."+particles, true);

		double[][] entropy = new double[20][20];
		for(int t=0;t<trials;t++) {								
			for(int i=0;i<20;i++) { //i = kappa+-
				for(int j=0;j<20;j++) { //j = phi+
					
					File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/AC.csv");			
					AAUtil.loadParameters(control, parameterFile, false);						
					AAParameters.INIT_PARTICLES = new int[]{particles,particles,0,particles,particles,0,0};	
					
					/**
					 *   0: gamma+
					 *   1: gamma-
					 *   2: kappa+
					 *   3: kappa-
					 *   4: phi+
					 *   5: phi-
					 */
					
					AAParameters.PROBABILITIES[0] = 0.0; //no self-assembly
					AAParameters.PROBABILITIES[1] = 1.0; //remove G particles to reduce computation time
					AAParameters.PROBABILITIES[2] = -20+(i*2); //kappa+					
					AAParameters.PROBABILITIES[3] = 1.0; //immediate breakup
					AAParameters.PROBABILITIES[4] = -20+(j*2); //phi+
					AAParameters.PROBABILITIES[5] = 1.0; //immediate breakup
					control.resetSimulation();					
					
					double d = 0;
					for(int it=0;it<iterations;it++) {
						control.stepSimulation();
						d += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2);
					}		
					entropy[i][j] += (d/(double)iterations);
					System.out.print("|");
				}						
			}	
			System.out.print("\n");
		}
		//average out values and log			
		for(int i=0;i<20;i++) { 
			for(int j=0;j<20;j++) {  
				log.write((entropy[i][j] / (double)trials) +",");
			}
			log.write("\n");
		}						
		AAUtil.out("Done! ("+getTimeDifference()+")");	
	}
	

	public void startSA(int particles, int iterations, int trials) {
		startTime = System.currentTimeMillis();					
		log = new AALogger(control, "SA."+particles, true);

		double[][] entropy = new double[5][21];
		for(int t=0;t<trials;t++) {								
			for(int i=0;i<5;i++) { //i = a+
				for(int j=0;j<21;j++) { //j = a- 
					
					File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/SA.csv");			
					AAUtil.loadParameters(control, parameterFile, false);						
					AAParameters.INIT_PARTICLES = new int[]{0,0,0,0,0,0,particles};
					AAParameters.PROBABILITIES[0] = i*0.25;
					AAParameters.PROBABILITIES[1] = -10 + j;			
					control.resetSimulation();
										
					double d = 0;
					for(int it=0;it<iterations;it++) {						
						control.stepSimulation();
						d += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(1);
					}		
					entropy[i][j] += (d/(double)iterations);
					System.out.print("|");
				}
				System.out.print("\n");
			}	
			System.out.println("TRIAL "+t);
		}
		//average out values and log			
		for(int i=0;i<5;i++) { //i = a+
			for(int j=0;j<21;j++) { //j = a- 
				log.write((entropy[i][j] / (double)trials) +",");
				System.out.print((entropy[i][j] / (double)trials) +",");
			}
			log.write("\n");
			System.out.println();
		}						
		AAUtil.out("Done! ("+getTimeDifference()+")");				
	}
	
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
