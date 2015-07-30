
package aa.model;

import aa.AAParameters;
import aa.AAUtil;

public class BasicParticle2D extends Particle2D {
  
	private AAUtil.type type;
	
	public BasicParticle2D(AAUtil.type t) {
		type = t;
    	this.setX(AAUtil.RANDOM.nextInt(AAParameters.GRID_SIZE));
    	this.setY(AAUtil.RANDOM.nextInt(AAParameters.GRID_SIZE));
	}
	
	public BasicParticle2D(AAUtil.type t, int newX, int newY) {    	
		this(t);
		this.setX(newX);
		this.setY(newY);
    }
    
	//Can be of all seven types
    public int getTotalBasicParticles() {
    	return 1;
    }
    
    //return 1 if type corresponds to one of six types
    public int getTotalBasicParticlesOfType(AAUtil.type t) {
    	//System.out.print((type));
    	return (type == t ? 1 : 0);
    }   
    
    //atomic particles: ABDE. C and F contain 2 atomic particles
    public int getTotalAtomicParticlesOfType(AAUtil.type t) {
    	if(type == AAUtil.type.C) {
    		return ((t == AAUtil.type.A || t == AAUtil.type.B) ? 1 : 0);
    	}
    	else if(type == AAUtil.type.F) {
    		return ((t == AAUtil.type.D || t == AAUtil.type.E) ? 1 : 0);
    	}
    	else { //Particle is atomic (i.e. A/B/D/E/G)
    		return (type == t ? 1 : 0);
    	}
    }
    
    //returns either 1 (ABDE) or 2 (CF)
    public int getTotalWeight() {
    	if(this.isOfType(AAUtil.type.C) || this.isOfType(AAUtil.type.F)) 
    		return 2;    
    	else
    		return 1;
    }
    
    //returns 1 or 2, if the type matches. Also checks within C and F particles for atomicparticles
    public int getTotalWeightOfType(AAUtil.type t) {
    	if(type == AAUtil.type.C) {
    		if(t == AAUtil.type.C) {
    			return 2;
    		}
    		else if(t == AAUtil.type.A || t == AAUtil.type.B) {
    			return 1;
    		}
    		else return 0;
    	}
    	else if(type == AAUtil.type.F) {
    		if(t == AAUtil.type.F) {
    			return 2;
    		}
    		else if(t == AAUtil.type.D || t == AAUtil.type.E) {
    			return 1;
    		}
    		else return 0;
    	}
    	else {
    		return (type == t ? 1 : 0);
    	}
    }    	
        
    public boolean isOfType(AAUtil.type t) {
    	return (t == type);
    }
    
    public AAUtil.type getType() {
    	return type;
    }
         
    public String particleToString() {
    	return type + "\n"; // + "(" + energy + ")\n";
    }
}
