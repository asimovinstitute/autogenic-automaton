package aa.experiment;

import aa.AAUtil;

public class Individual {
	
	int type; 	
    private byte[] genes;
    // Cache fitness
    private double fitness = -1;

    public Individual(int t) {
    	
    	type = t;    	
    	if(type == 2) genes = new byte[20]; //AC    
    	if(type == 3) genes = new byte[36]; //AG    
    	else {AAUtil.out("Error in Individual: unknown type"); genes = new byte[0];}
    			
		for (int i = 0; i < size(); i++) {
            byte gene = (byte) Math.round(Math.random());
            genes[i] = gene;
        }
    }    
    
    public byte getGene(int index) {
        return genes[index];
    }
    
    public byte[] getGenes(int startIndex, int endIndex) {
    	int length = (endIndex-startIndex)+1;
    	byte[] byteArray = new byte[length];    	
    	for(int i=0;i<length;i++) {
    		byteArray[i] = genes[startIndex+i];
    	}
    	return byteArray;
    }

    public void setGene(int index, byte value) {
        genes[index] = value;
        fitness = -1;
    }

    public int size() {
        return genes.length;
    }

    public double getFitness() {
    	if (fitness < 0) {
            fitness = FitnessCalc.getFitness(type, this);
        }
        return fitness;
    }
    
    public void resetFitness() {
    	fitness = -1;
    }
    
    public String toProbabilitiesString() {
    	double[] ds = FitnessCalc.genotypeToPhenotype(type, this)[0];
    	return AAUtil.arrayToString(ds);    	
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i);
        }
        return geneString;
    }
}
