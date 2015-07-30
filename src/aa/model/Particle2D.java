
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
    
    	
    	/*if(this.getDir() == AAUtil.dir2D.N) {
			int y = this.getY() + 1;    			
			if(y >= AAParameters.GRID_SIZE) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.SW);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.S);
				else this.setDir(AAUtil.dir2D.SE);	    					    			
			}	    			
			else {
				this.setY(y);
			}
		}
		else if(this.getDir() == AAUtil.dir2D.NE) {
			int y = this.getY() + 1;  
			int x = this.getX() + 1;  
			if(y >= AAParameters.GRID_SIZE && x >= AAParameters.GRID_SIZE) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.W);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.SW);
				else this.setDir(AAUtil.dir2D.S);	    					    			
			}	    			
			else if(y >= AAParameters.GRID_SIZE) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.SW);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.S);
				else this.setDir(AAUtil.dir2D.SE);	
			}
			else if(x >= AAParameters.GRID_SIZE) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.NW);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.W);
				else this.setDir(AAUtil.dir2D.SW);	
			}
			else {
				this.setX(x);
				this.setY(y);
			}
		}
		else if(this.getDir() == AAUtil.dir2D.E) {
			int x = this.getX() + 1;    			
			if(x >= AAParameters.GRID_SIZE) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.NW);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.W);
				else this.setDir(AAUtil.dir2D.SW);	    					    			
			}	    			
			else {
				this.setX(x);
			}
		}
		else if(this.getDir() == AAUtil.dir2D.SE) {
			int y = this.getY() - 1;  
			int x = this.getX() + 1;  
			if(y < 0 && x >= AAParameters.GRID_SIZE) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.W);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.NW);
				else this.setDir(AAUtil.dir2D.N);	    					    			
			}	    			
			else if(y < 0) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.NW);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.N);
				else this.setDir(AAUtil.dir2D.NE);	
			}
			else if(x >= AAParameters.GRID_SIZE) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.SW);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.W);
				else this.setDir(AAUtil.dir2D.NW);	
			}
			else {
				this.setX(x);
				this.setY(y);
			}
		}
		else if(this.getDir() == AAUtil.dir2D.S) {
			int y = this.getY() - 1;    			
			if(y < 0) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.NW);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.N);
				else this.setDir(AAUtil.dir2D.NE);	    					    			
			}	    			
			else {
				this.setY(y);
			}
		}
		else if(this.getDir() == AAUtil.dir2D.SW) {
			int y = this.getY() - 1;  
			int x = this.getX() - 1;  
			if(y < 0 && x < 0) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.N);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.NE);
				else this.setDir(AAUtil.dir2D.E);	    					    			
			}	    			
			else if(y < 0) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.NW);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.N);
				else this.setDir(AAUtil.dir2D.NE);	
			}
			else if(x < 0) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.SE);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.E);
				else this.setDir(AAUtil.dir2D.NE);	
			}
			else {
				this.setX(x);
				this.setY(y);
			}
		}
		else if(this.getDir() == AAUtil.dir2D.W) {
			int x = this.getX() - 1;    			
			if(x < 0) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.NE);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.E);
				else this.setDir(AAUtil.dir2D.SE);	    					    			
			}	    			
			else {
				this.setX(x);
			}
		}
		else if(this.getDir() == AAUtil.dir2D.NW) {
			int y = this.getY() + 1;  
			int x = this.getX() - 1;  
			if(y >= AAParameters.GRID_SIZE && x < 0) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.E);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.SE);
				else this.setDir(AAUtil.dir2D.S);	    					    			
			}	    			
			else if(y >= AAParameters.GRID_SIZE) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.SW);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.S);
				else this.setDir(AAUtil.dir2D.SE);	
			}
			else if(x < 0) {
				int r = AAUtil.RANDOM.nextInt(3);
				if(r==0) this.setDir(AAUtil.dir2D.NE);	    				
				else if(r==1) this.setDir(AAUtil.dir2D.E);
				else this.setDir(AAUtil.dir2D.SE);	
			}
			else {
				this.setX(x);
				this.setY(y);
			}    			    		    		
		}    	
    }*/
    
    
    public int getX() {
    	return x;
    }
       
    public int getY() {
    	return y;
    }
       
    /*public AAUtil.dir2D getDir() {
    	return dir2D;
    }*/
    
    /*public AAUtil.dir2D getDirection(int d) {    	
    	switch(d) {
	    	case(0):{return AAUtil.dir2D.N;}
	    	case(1):{return AAUtil.dir2D.NE;}
	    	case(2):{return AAUtil.dir2D.E;}
	    	case(3):{return AAUtil.dir2D.SE;}
	    	case(4):{return AAUtil.dir2D.S;}
	    	case(5):{return AAUtil.dir2D.SW;}
	    	case(6):{return AAUtil.dir2D.W;}
	    	case(7):{return AAUtil.dir2D.NW;}
	    	default:{return AAUtil.dir2D.N;}
    	}
    }*/
    
    public void setX(int newX) {
    	x = newX;     	
    }
    
    public void setY(int newY) {
    	y = newY;     	
    }                
    
    /*public void setDir(AAUtil.dir2D d) {
    	dir2D = d;
    } */          
}
