
package aa.model;

import aa.AAParameters;
import aa.AAUtil;

public class Capsule2D extends Particle2D {
  
	private Particle2DArrayList capsuleParticles;
	private Particle2DArrayList containedParticles;	
	
	//for selection experiments
	private int maxCapsuleSize;
	private int maxContained;
	private int creationTime;
		
	public Capsule2D() {
		capsuleParticles = new Particle2DArrayList();
		containedParticles = new Particle2DArrayList();		
    	this.setX(AAUtil.RANDOM.nextInt(AAParameters.GRID_SIZE));
    	this.setY(AAUtil.RANDOM.nextInt(AAParameters.GRID_SIZE));
    	
    	this.maxCapsuleSize = 2 + AAUtil.RANDOM.nextInt(AAParameters.MAX_CAPSULE_PARTICLES-1);
    	//this.maxContained = AAUtil.RANDOM.nextInt(AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT+1);
    	this.maxContained = 1 + AAUtil.RANDOM.nextInt(AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT);
    	this.creationTime = AAModel.getCurrentIteration();
	}			
	
	public Capsule2D(int newX, int newY) {    	
		this();		
		this.setX(newX);
		this.setY(newY);		
    }
	     
    public Capsule2D(int newX, int newY, Particle2DArrayList cap, Particle2DArrayList con) {
    	this(newX, newY);
    	this.setCapsuleParticles(cap);
    	this.setContainedParticles(con);    
    }
         
    public int getTotalBasicParticles() {    
    	int sum = 0;
    	for(Particle2D p : capsuleParticles) {
    		sum += p.getTotalBasicParticles();
    	}
    	for(Particle2D p : containedParticles) {
    		sum += p.getTotalBasicParticles();
    	}
    	return sum;    
    }
    
    public int getTotalBasicParticlesOfType(AAUtil.type t) {    
    	int sum = 0;
    	for(Particle2D p : capsuleParticles) {
    		sum += p.getTotalBasicParticlesOfType(t);
    	}
    	for(Particle2D p : containedParticles) {
    		sum += p.getTotalBasicParticlesOfType(t);
    	}
    	return sum;    	    	
    }
    
    public int getTotalAtomicParticlesOfType(AAUtil.type t) {
    	int sum = 0;
    	for(Particle2D p : capsuleParticles) {
    		sum += p.getTotalAtomicParticlesOfType(t);
    	}
    	for(Particle2D p : containedParticles) {
    		sum += p.getTotalAtomicParticlesOfType(t);
    	}
    	return sum;  
    }
    
    public int getTotalWeight() {
    	int sum = 0;
    	for(Particle2D p : capsuleParticles) {
    		sum += p.getTotalWeight();
    	}
    	for(Particle2D p : containedParticles) {
    		sum += p.getTotalWeight();
    	}
    	return sum;  
    }
    
    public int getTotalWeightOfType(AAUtil.type t) {
    	int sum = 0;
    	for(Particle2D p : capsuleParticles) {
    		sum += p.getTotalWeightOfType(t);
    	}
    	for(Particle2D p : containedParticles) {
    		sum += p.getTotalWeightOfType(t);
    	}
    	return sum;      	    	
    }       
    
    public int getContainedParticlesWeight() {
    	int sum = 0;
    	for(Particle2D p : containedParticles) {
    		sum += p.getTotalWeight();
    	}
    	return sum;
    }
    
    public Particle2DArrayList getCapsuleParticles() {
    	return capsuleParticles;
    }
    
    public void setCapsuleParticles(Particle2DArrayList c) {
    	capsuleParticles = c;
    }
    
    public Particle2DArrayList getContainedParticles() {
    	return containedParticles;
    }
    
    public void setContainedParticles(Particle2DArrayList c) {
    	containedParticles = c;
    } 
    
    @Override
    public void setX(int newX) {
    	super.setX(newX);
    	for(Particle2D p : capsuleParticles) {
    		p.setX(newX);
    	}
    	for(Particle2D p : containedParticles) {
    		p.setX(newX);
    	}
    }
    
    @Override
    public void setY(int newY) {
    	super.setY(newY);
    	for(Particle2D p : capsuleParticles) {
    		p.setY(newY);
    	}
    	for(Particle2D p : containedParticles) {
    		p.setY(newY);
    	}   	
    }
    
    public String particleToString() {
    	String s = "[cap: "+capsuleParticles.size()+" | con: ";    	
    	for(Particle2D p : containedParticles) {
    		s += (p instanceof BasicParticle2D ? ((BasicParticle2D)p).getType() + "-" : "ERROR: CAPSULE IN CAPSULE");
    	}
    	return s+"]";
    }
    
    public int getMaxContained() {
    	return maxContained;
    }
    
    public int getMaxCapsuleSize() {
    	return maxCapsuleSize;
    }
    
    public int getAge() {
    	return AAModel.getCurrentIteration() - creationTime;
    }
}
