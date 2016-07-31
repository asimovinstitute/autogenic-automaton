
package aa.model;

import aa.AAParameters;
import aa.AAUtil;

public abstract class Particle2D {
	
	private int x;
    private int y;
    
    //* totalWeight: ABDEG = 1, CF = 2, alles optellen ook in capsule
    //* totalBasicParticles: ABCDEFG = 1, optellen in capsule
    //* totalAtomicParticlesOfType(ABDE): ABDEG = 1, CF(if contained AB/DE) = 1 
    
    public abstract int getTotalBasicParticles();
    
    public abstract int getTotalBasicParticlesOfType(AAUtil.type t);
            
    public abstract int getTotalAtomicParticlesOfType(AAUtil.type t);
    
    public abstract int getTotalWeight();
    
    public abstract int getTotalWeightOfType(AAUtil.type t);        
   
    public abstract String particleToString();
    
    
    public int computeNewPosition() {
    	if(AAUtil.RANDOM.nextDouble() < AAParameters.PARTICLE_SPEED) {
    		this.move();
    	}
    	return 0;
    	/*if(AAUtil.RANDOM.nextDouble() < AAParameters.PARTICLE_SPEED) {
    		int friction = AAParameters.PARTICLE_FRICTION * this.getTotalWeight();
    		if(this.getEnergy() >= friction) {
    			this.subtractEnergy(friction);
        		this.move();
        		return friction;
    		}
    		else {
    			return 0;
    		}
    	}
    	else {
    		return 0;
    	}*/
    }    
    
    public void move() {    	
    	if(AAUtil.RANDOM.nextBoolean()) x = getNewX();
    	else y = getNewY();
    }
    
    private int getNewX() {
    	int newX = x + (AAUtil.RANDOM.nextBoolean() ? 1 : -1);
    	if(newX >= 0 && newX < AAParameters.GRID_SIZE) return newX;
    	else return getNewX();    		
    }
    
    private int getNewY() {
    	int newY = y + (AAUtil.RANDOM.nextBoolean() ? 1 : -1);
    	if(newY >= 0 && newY < AAParameters.GRID_SIZE) return newY;
    	else return getNewY();    		
    }
        
    public int getX() {
    	return x;
    }
       
    public int getY() {
    	return y;
    }
    
    public void setX(int newX) {
    	x = newX;     	
    }
    
    public void setY(int newY) {
    	y = newY;     	
    }                
              
}
