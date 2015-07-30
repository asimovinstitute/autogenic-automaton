package aa.experiment;

import aa.AAParameters;
import aa.AAUtil;

public class Population {
	
	Individual[] individuals;
	AALogger log;

	public Population(int type, int populationSize, AALogger l) {
        individuals = new Individual[populationSize];
        log = l;
        for (int i = 0; i < getSize(); i++) {                
            this.saveIndividual(i, new Individual(type));                
        }
    }

    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public Individual getFittest(boolean output) {
        Individual fittest = individuals[0];
        for (int i = 0; i < this.getSize(); i++) { 
        	double currentFitness = getIndividual(i).getFitness();
        	
        	
        	String s = AAUtil.arrayToString(AAParameters.PROBABILITIES) + ", " + AAParameters.MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION + " " + AAParameters.MAX_CAPSULE_PARTICLES + " FITNESS: " + currentFitness;
    		System.out.println(s);
    		log.write(s + "\n");
    		
        	
        	if (fittest.getFitness() < currentFitness) {
                fittest = getIndividual(i);
            }        	
        }
        return fittest;
    }

    public int getSize() {
        return individuals.length;
    }    
    
    public void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }
    
    public void resetFitness() {
    	for(Individual i : individuals) {
    		i.resetFitness();
    	}
    }
}
