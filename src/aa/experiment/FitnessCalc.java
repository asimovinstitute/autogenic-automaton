package aa.experiment;

import java.io.File;

import aa.AAControl;
import aa.AAParameters;
import aa.AAUtil;
import aa.AAUtil.NumberType;

public class FitnessCalc {

	private static AAControl control;
	private static int sampleLength;
	private static String paramFileName;
	
	public static double getFitness(int type, Individual individual) {

		File parameterFile = new File(AAControl.DEFAULT_SETTINGS_FILEPATH + "/" + paramFileName +".csv");		
		AAUtil.loadParameters(control, parameterFile, false);
		
		double[][] phenotype = genotypeToPhenotype(type, individual);
		AAParameters.PROBABILITIES = phenotype[0];
		AAParameters.MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION = (int)Math.round(phenotype[1][0]);
		AAParameters.MAX_CAPSULE_PARTICLES = (int)Math.round(phenotype[1][1]);				
		control.resetSimulation();		
		
		
		/*control.getView().loadParameters();  //set the parameters to the text fields
		control.getModel().reset(); //set the parameters to the model
		control.getView().reset(); //reset the other viewer panels, that are based on the model (e.g. activationPanel)		
					
		double maxFitness = 0;
		double minFitness = Double.MAX_VALUE;*/
		
		double currentFitness = 0;
		
		//for a number of trials (e.g. 10): to add later						
		//run the simulation for a certain number of steps		
		for(int i=0;i<sampleLength;i++) {
			control.stepSimulation();
			if(type==2) {
				currentFitness += control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(2); //AC
			}
			if(type==3) {
				currentFitness += control.getModel().getGrid().getGridFunctions().getMutualInformation(); //MI
			}
		}
		
		//System.out.println(" --- TOTALP= "+control.getModel().getGrid().getGridFunctions().getTotalWeightOfParticles());
		
		double fitness = (type == 2 ? 1.0 - (currentFitness/(double)sampleLength) : currentFitness/(double)sampleLength);
		
		return fitness;
		
		
			//fitness as the shannon entropy (KL divergence between current and uniform distribution)
			//uniform distibution has 0 shannon entropy, so the higher the value, the more ordering
			
			//as a measure for self-organization: get the highest deviation from disorder, i.e. the maxFitness over the time interval
			
			/* NOTE: Shannon entropy needs to be minimized. Check if this works. 
			currentFitness = 1.0 - control.getModel().getGrid().getGridFunctions().getNormalizedInformationEntropy(type);			 
			if(currentFitness > maxFitness) maxFitness = currentFitness;
			if(currentFitness < minFitness) minFitness = currentFitness;										
		}		
		return (int) Math.round(maxFitness-minFitness);		*/
	}
	
	public static double[][] genotypeToPhenotype(int type, Individual individual) {
		
		double[][] phenotype = new double[2][AAParameters.PROBABILITIES.length];
				
		if(type == 2) { //AC
		
			phenotype[0][0] = AAUtil.getNumber(NumberType.INTEGER, individual.getGenes(0, 4));// Bc+[0-32]
			phenotype[0][1] = 0;
			phenotype[0][2] = 0.1 + AAUtil.getNumber(NumberType.FRACTION, individual.getGenes(5, 9));// Bc-[0-1]
			phenotype[0][3] = AAUtil.getNumber(NumberType.INTEGER, individual.getGenes(10, 14));// Bf+[0-32]
			phenotype[0][4] = 0;
			phenotype[0][5] = 0.1 + AAUtil.getNumber(NumberType.FRACTION, individual.getGenes(15, 19));// Bf-[0-1]
						
			phenotype[0][6] = 1; //with SA this same (formerly: no SA
			phenotype[0][7] = 0;	
			phenotype[0][8] = -2;
			phenotype[0][9] = 0;
			
			
		} if(type == 3) { //AG
			
			phenotype[0][0] = AAUtil.getNumber(NumberType.INTEGER, individual.getGenes(0, 4));// Bc+[0-32]
			phenotype[0][1] = 0;
			phenotype[0][2] = 0.1 + AAUtil.getNumber(NumberType.FRACTION, individual.getGenes(5, 9));// Bc-[0.1-1]
			phenotype[0][3] = AAUtil.getNumber(NumberType.INTEGER, individual.getGenes(10, 14));// Bf+[0-32]
			phenotype[0][4] = 0;
			phenotype[0][5] = 0.1 + AAUtil.getNumber(NumberType.FRACTION, individual.getGenes(15, 19));// Bf-[0.1-1]
						
			phenotype[0][6] = 0.1 + AAUtil.getNumber(NumberType.FRACTION, individual.getGenes(20, 24));// A+[0.1-1]
			phenotype[0][7] = 0;	
			phenotype[0][8] = (individual.getGene(25) == 0 ? 1 : -1) * AAUtil.getNumber(NumberType.INTEGER, individual.getGenes(26, 29)); //A-[-8-8]
			phenotype[0][9] = 0;
			
			phenotype[1][0] = 2 + AAUtil.getNumber(NumberType.INTEGER, individual.getGenes(30, 32)); //minCap[2-9]
			phenotype[1][1] = 2 + AAUtil.getNumber(NumberType.INTEGER, individual.getGenes(33, 35)); //maxCap[2-9]
			
		}
		else {
			AAUtil.out("Error in FitnessCalc, unknown type "+type);
		}
		
		return phenotype;
			
		
		
		/*if(type == 1 || type == 3) { //self-assembly: genelenght of 22 bits total
			
			//5: Sigmoid
			probs[6] = AAUtil.getNumber(NumberType.INTEGER, individual.getGenes(0, 2));  
			int sign5 = (individual.getGene(3) == 1 ? 1 : -1);
			probs[7] = sign5 * AAUtil.getNumber(NumberType.SMALL_TO_LARGE, individual.getGenes(4, 7)) * AAUtil.getNumber(NumberType.FRACTION, individual.getGenes(8, 13));
			
			//6: Sigmoid
			probs[8] = AAUtil.getNumber(NumberType.INTEGER, individual.getGenes(14, 16));  
			int sign6 = (individual.getGene(17) == 1 ? 1 : -1);
			probs[9] = sign6 * AAUtil.getNumber(NumberType.SMALL_TO_LARGE, individual.getGenes(18, 21)) * AAUtil.getNumber(NumberType.FRACTION, individual.getGenes(22, 27));
						
			return probs;					
		}
		if(type == 2 || type == 3) { //autocatalysis: genelength 44 bits
			
			//6 parameters
			//3rd & 6st parameters: 0-1 4 bits 
			//1st, 2nd, 4th & 5th par: -10000 - 10000 8 bits: 4 for value, 4 for order, 1 for sign
						*/
			/*double numerator = getNumerator16(individual.getGene(0), individual.getGene(1), individual.getGene(2), individual.getGene(3));
			double denominator = getDenominatorSmallToLarge(individual.getGene(4), individual.getGene(5), individual.getGene(6), individual.getGene(7));
			double sign = (individual.getGene(8) == 0 ? 1 : -1);
			probs[0] = (sign * numerator) / denominator; 
			
			numerator = getNumerator16(individual.getGene(9), individual.getGene(10), individual.getGene(11), individual.getGene(12));
			denominator = getDenominatorSmallToLarge(individual.getGene(13), individual.getGene(14), individual.getGene(15), individual.getGene(16));
			sign = (individual.getGene(17) == 0 ? 1 : -1);
			probs[1] = (sign * numerator) / denominator; 
			
			probs[2] = getNumerator16(individual.getGene(18), individual.getGene(19), individual.getGene(20), individual.getGene(21));
									
			numerator = getNumerator16(individual.getGene(22), individual.getGene(23), individual.getGene(24), individual.getGene(25));
			denominator = getDenominatorSmallToLarge(individual.getGene(26), individual.getGene(27), individual.getGene(28), individual.getGene(29));
			sign = (individual.getGene(30) == 0 ? 1 : -1);
			probs[3] = (sign * numerator) / denominator; 
			
			numerator = getNumerator16(individual.getGene(31), individual.getGene(32), individual.getGene(33), individual.getGene(34));
			denominator = getDenominatorSmallToLarge(individual.getGene(35), individual.getGene(36), individual.getGene(37), individual.getGene(38));
			sign = (individual.getGene(39) == 0 ? 1 : -1);
			probs[4] = (sign * numerator) / denominator; 
			
			probs[5] = getNumerator16(individual.getGene(40), individual.getGene(41), individual.getGene(42), individual.getGene(43));
									
			return probs;							
		}	*/
	}
	
	public static void setControl(AAControl c) {
		control = c;
	}
	
	public static void setSampleLenght(int s) {
		sampleLength = s;
	}
	
	public static void setParameters(String params) {
		paramFileName = params; 			
	}
}
