
package aa.experiment;

import java.io.File;

import aa.AAControl;
import aa.AAParameters;
import aa.AAUtil;
import aa.AAUtil.type;

public class AAGAExperiment {
		
	AAControl control;
	AALogger log;
	
	//Run a series of experiments, and write the results to a log file
	public AAGAExperiment(AAControl c) {
		control = c;		
	}		

	public void start() {				
		this.runExperiment("GA.AG", 1000, 20, 1000, 3);		
	}	
	
	//type: 1 is crystallization (optimize H1), 2 is autocatalysis (H2), 3 is autogenesis (...) 
	private void runExperiment(String params, int generations, int popSize, int sampleLength, int type) {
				
		//initialize the fitness calculation
		FitnessCalc.setControl(control);
		FitnessCalc.setSampleLenght(sampleLength);
		FitnessCalc.setParameters(params);
		
		//initialize log
		log = new AALogger(control, params, true);
		
		optimizeFitness(generations, popSize, type);				
	}		
	
	private Individual optimizeFitness(int generations, int popSize, int type) {
				
		//Create an initial population
	    Population myPop = new Population(type, popSize, log);
	    
	    // Evolve our population 
	    int generationCount = 0;	        
	    Individual winner = new Individual(type);
	         	    
		for(;generationCount<generations;generationCount++) {
			
			/*log.write("Generation " + generationCount + " ");
			System.out.print("Generation " + generationCount + " ");
			for(int i=0;i<(5-(Integer.toString(generationCount).length()));i++) {
				System.out.print(" ");
				log.write(" ");
			}*/
			myPop.resetFitness();
			winner = myPop.getFittest(true);
			
			double[][] winnerParams = FitnessCalc.genotypeToPhenotype(3, winner);
			String s = AAUtil.arrayToString(winnerParams[0]) + ", " + winnerParams[1][0] + " " + winnerParams[1][1] + " FITNESS: " + winner.getFitness() + ",<=============== WINNER OF GENERATION "+generationCount;
    		System.out.println(s);
    		log.write(s + "\n");
    		
	        /*System.out.print(" ---> Fittest: " + winner.getFitness());
	        log.write(" ---> Fittest: " + winner.getFitness());
	        for(int i=0;i<(8-(Double.toString(winner.getFitness()).length()));i++) {
	        	log.write(" ");
				System.out.print(" ");
			}
	        log.write(winner + " -> " + winner.toProbabilitiesString() + " @" + AALogger.getTime() + "\n");
	        System.out.println(winner + " -> " + winner.toProbabilitiesString() + " @" + AALogger.getTime());*/
	        myPop = GeneticAlgorithm.evolvePopulation(type, myPop, log);			        	        	        
		}				
		return winner;
	}
}
