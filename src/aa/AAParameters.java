
package aa;

import java.io.BufferedReader;

// contains a set of hard-coded, default parameter values for running experiments 
// whenever a parameter-.csv file is specified, the values below are overridden 

public class AAParameters {

    public static int[] INIT_PARTICLES = new int[]{1000,1000,0,1000,1000,0,0};
    public static int GRID_SIZE = 10;
    public static double PARTICLE_SPEED = 0.1;
    public static int SIM_SPEED = 4;    
    
    public static double[] PROBABILITIES = new double[]{0, 0, 0, 0, 0, 0}; 
    public static int MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION = 4;
    public static int MAX_CAPSULE_PARTICLES = 1000000;
    public static int MAX_CONTAINED_PARTICLES_WEIGHT = 1000000;    
    public static boolean ENCAPSULATION = false;    
    public static boolean GREMOVAL = false;
   
    public static String parametersToString() { //save
    	
    	String s = "";
    	
    	s += "Initial particles,";
    	for(int i=0;i<INIT_PARTICLES.length;i++){
    		s += INIT_PARTICLES[i] + ",";	
    	}
    	s += "\n";
    	s += "Grid size," + GRID_SIZE + "\n";
    	s += "Particle speed," + PARTICLE_SPEED + "\n"; 
      	s += "Simulation speed," + SIM_SPEED + "\n";

    	s += "Probabilities,";
    	for(int i=0;i<PROBABILITIES.length;i++){
    		s += PROBABILITIES[i] + ",";	
    	}
    	s += "\n";
    	s += "Minimum capsule particles for encapsulation," + MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION + "\n";
    	s += "Maximum capsule particles," + MAX_CAPSULE_PARTICLES + "\n";
    	s += "Maximum contained particles weight," + MAX_CONTAINED_PARTICLES_WEIGHT + "\n";
    	s += "Encapsulation," + ENCAPSULATION + "\n";
    	s += "G removal," + GREMOVAL + "\n";
    	s += "\n\n";
    	
    	return s;    	
    }   
    
    
    
    public static void stringToParameters(BufferedReader input) { //load
    	
    	try {
	    	String s; 
	    	while((s = input.readLine()) != null) { 
	    		
	    		String[] params = s.split(",");
	    		
	    		//no switch statements on strings in this java version
	    		if(params[0].equals("Initial particles")) {
	    			for(int i=0;i<INIT_PARTICLES.length;i++) {
	    				INIT_PARTICLES[i] = Integer.parseInt(params[i+1]);	    	    		 
	    			}
	    		}
	    		else if(params[0].equals("Grid size")) {
	    			GRID_SIZE = Integer.parseInt(params[1]);
	    		}	    		
	    		else if(params[0].equals("Particle speed")) {
	    			PARTICLE_SPEED = Double.parseDouble(params[1]);
	    		}
	    		else if(params[0].equals("Simulation speed")) {
	    			SIM_SPEED = Integer.parseInt(params[1]);
	    		}
	    		else if(params[0].equals("Probabilities")) {
	    			for(int i=0;i<PROBABILITIES.length;i++) {
	    				PROBABILITIES[i] = Double.parseDouble(params[i+1]);	    	    		 
	    			}
	    		}
	    		else if(params[0].equals("Minimum capsule particles for encapsulation")) {
	    			MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION = Integer.parseInt(params[1]);
	    		}
	    		else if(params[0].equals("Maximum capsule particles")) {
	    			MAX_CAPSULE_PARTICLES = Integer.parseInt(params[1]);
	    		}
	    		else if(params[0].equals("Maximum contained particles weight")) {
	    			MAX_CONTAINED_PARTICLES_WEIGHT = Integer.parseInt(params[1]);
	    		}	    		
	    		else if(params[0].equals("Encapsulation")) {
	    			ENCAPSULATION = Boolean.parseBoolean(params[1]);
	    		}	
	    		else if(params[0].equals("G removal")) {
	    			GREMOVAL = Boolean.parseBoolean(params[1]);
	    		}	
	    	}
    	}
    	catch(Exception e){
    		System.out.println(e.getMessage());    		
    	}    	
    }	
    

    public static double[] getProbabilities(BufferedReader input) {
    	double[] prob = new double[PROBABILITIES.length];
    	try {
	    	String s; 
	    	while((s = input.readLine()) != null) { 
	    		
	    		String[] params = s.split(",");

	    		if(params[0].equals("Probabilities")) {
	    			for(int i=0;i<prob.length;i++) {
	    				prob[i] = Double.parseDouble(params[i+1]);	    	    		 
	    			}
	    		}	    		
	    	}
    	}
    	catch(Exception e){
    		System.out.println(e.getMessage());    		
    	}   
    	return prob;
    }
}


