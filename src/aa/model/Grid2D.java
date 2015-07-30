
package aa.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;

import aa.AAControl;
import aa.AAParameters;
import aa.AAUtil;
import aa.AAUtil.type;

public class Grid2D {
    
	private Particle2DArrayList particles; 
	
	//temp variables
	private Particle2DArrayList tempParticlesArray;
	private int[][] reactionsArray;
    
    private boolean useLogGs = false;
    private boolean logGs = false;
    private int addedGs = 0;
    //private int removedGs = 0;
    private int[] toAddGs = new int[0];
    //private int[] toRemoveGs = new int[0];
    //private int gBalance = 0;
    
    private boolean useMaxCapSize = false;
    private boolean useMaxContained = false;
	
	private AAModel model;  
	private static Grid2DFunctions functions;
          
    public Grid2D(AAModel m) {    	
        model = m;    	
        functions = new Grid2DFunctions(this);
        particles = new Particle2DArrayList();   
        reactionsArray = new int[AAParameters.GRID_SIZE][AAParameters.GRID_SIZE];
        initializeParticles(AAParameters.INIT_PARTICLES);                  
    }    
        
    private void initializeParticles(int[] np) {    	    	    	
        for(int i=0;i<np.length;i++) {
        	for(int j=0;j<np[i];j++) {
        		particles.add(new BasicParticle2D(functions.getType(i)));        		        		
        	}
        }                
    }
    
    public void next() {  
    	
    	resetReactions();
    	    	    	
    	computeNewPositions();
    	
    	resolveInteractions();
    	    	
    	resolveBreakup();
    	    	    	
    	if(model.getControl().viewerActive()) {
    		showCapsules();
    	}
    	
    	//printDebugOutput();    	
    	
    }
    
    private void resetReactions() {
    	reactionsArray = new int[AAParameters.GRID_SIZE][AAParameters.GRID_SIZE];
    	
    	if(logGs) {
    		addedGs = 0;
    		//removedGs = 0;
    	}
    }
    
    private void computeNewPositions() {    	
    	for(Particle2D p : particles) {
    		p.computeNewPosition();
    	}    	    	
    }
    
	private void resolveInteractions() {
		for(int i = 0; i < AAParameters.GRID_SIZE; i++) {
			for(int j = 0; j < AAParameters.GRID_SIZE; j++) {
				tempParticlesArray = particles.getAndRemoveParticlesAt(i, j);				 								
				particles.addAll(resolveInteractions(tempParticlesArray));
			}
    	}
		
		if(useLogGs) { //add G particles at random locations
			
			int numGs = toAddGs[AAModel.getCurrentIteration()];
			
			/*int tempGBalance = 0;
			for(int i=0;i<gBalance;i++) {
				if(numGs > 0) {
					numGs--;
				}
				else {
					tempGBalance++;
				}
			}
			gBalance = tempGBalance;	*/
			
			for(int i=0;i<numGs;i++) {
				particles.add(new BasicParticle2D(AAUtil.type.G));
			}
    	}    	
	}
	
	private void resolveBreakup() {
		for(int i = 0; i < AAParameters.GRID_SIZE; i++) {
			for(int j = 0; j < AAParameters.GRID_SIZE; j++) {
				tempParticlesArray = particles.getAndRemoveParticlesAt(i, j);							
				particles.addAll(resolveBreakup(tempParticlesArray));
			}
    	}
		/*if(useLogGs) { //remove random G particles (if available)
			int numGs = toRemoveGs[model.getCurrentIteration()];
			ArrayList<Integer> gs = new ArrayList<Integer>();
			for(int i=0;i<numGs;i++) {
				for(int j=0;j<particles.size();j++) { //construct list of Gs
					if(particles.get(j) instanceof BasicParticle2D && ((BasicParticle2D)particles.get(j)).isOfType(AAUtil.type.G)) {
						gs.add(j);
					}
				}
				if(gs.isEmpty()) {
					//System.out.println("At iteration "+model.getCurrentIteration()+" no G to be removed");
					gBalance++;
				} else { //pick and randomly remove
					particles.remove(gs.get(AAUtil.RANDOM.nextInt(gs.size()))); 
				}
			}
		}*/
	}	
	
	private void showCapsules() {
		
		//count all capsules, organized by size
		for(Particle2D p : particles) {					
			if(p instanceof Capsule2D) {
				if(!((Capsule2D)p).getContainedParticles().isEmpty()) { //add to autogen minimap
					model.getView().getSmallActivationsPanel(3).updateActivations(p.getX(), p.getY(), 10); //add to autogen minimap									
				}
				else {
					model.getView().getSmallActivationsPanel(2).updateActivations(p.getX(), p.getY(), 10); //add to capsule minimap
				}				
			}			
		}		
	}
	
	
	/***********************************    SUBMETHODS    ***********************************/
	
	
	private Particle2DArrayList resolveInteractions(Particle2DArrayList possibleReactants) {
		
		if(possibleReactants.isEmpty()) {
			return possibleReactants;
		}
		else {
			Particle2D p = possibleReactants.get(0);
			possibleReactants.remove(p);	
			Particle2DArrayList newParticles = new Particle2DArrayList();
			boolean hasReacted = false;
						
			for(int i=0; i < possibleReactants.size(); i++) {
							
				if(hasReacted) {
					break;
				}
				else {	
					
					Particle2D p2 = possibleReactants.get(i); 			
					
					if(p instanceof BasicParticle2D && p2 instanceof BasicParticle2D) {						
						// A + B =(F)> C (R3)   
						if(((((BasicParticle2D) p).isOfType(AAUtil.type.A) && ((BasicParticle2D) p2).isOfType(AAUtil.type.B)) || (((BasicParticle2D) p).isOfType(AAUtil.type.B) && ((BasicParticle2D) p2).isOfType(AAUtil.type.A)))) { 																						
							int catalystsF = getTotalCatalysts((BasicParticle2D)p);
							if(functions.checkRule3Prob(catalystsF)) {  								
								possibleReactants.remove(p2);
								newParticles.add(new BasicParticle2D(AAUtil.type.C, p.getX(), p.getY())); 
								if(catalystsF > 0 && model.getControl().viewerActive()) { 
									model.getView().getSmallActivationsPanel(1).updateActivations(p.getX(), p.getY(), 25); //add to catalysis minimap if catalysis took place 
								}
								reactionsArray[p.getX()][p.getY()]++;									
								hasReacted = true;
							}																							
						}							
						// D + E =(C)> F + G (R5)   
						else if(((((BasicParticle2D) p).isOfType(AAUtil.type.D) && ((BasicParticle2D) p2).isOfType(AAUtil.type.E)) || (((BasicParticle2D) p).isOfType(AAUtil.type.E) && ((BasicParticle2D) p2).isOfType(AAUtil.type.D)))) { 							
							int catalystsC = getTotalCatalysts((BasicParticle2D)p);
							if(functions.checkRule5Prob(catalystsC)) { 									
								possibleReactants.remove(p2);
								
								newParticles.add(new BasicParticle2D(AAUtil.type.F, p.getX(), p.getY())); 
								newParticles.add(new BasicParticle2D(AAUtil.type.G, p.getX(), p.getY()));								
								
								if(catalystsC > 0 && model.getControl().viewerActive()) { 
									model.getView().getSmallActivationsPanel(1).updateActivations(p.getX(), p.getY(), 25); //add to catalysis minimap if catalysis took place 
								}																	
								reactionsArray[p.getX()][p.getY()]++;									
								if(logGs) {								
									addedGs++;
								}
								hasReacted = true;
							}																	
						}		
						// G + G => GG (R1)
						else if(((BasicParticle2D) p).isOfType(AAUtil.type.G) && ((BasicParticle2D) p2).isOfType(AAUtil.type.G) && functions.checkRule1Prob()) { 																								
							possibleReactants.remove(p2);								
							Particle2DArrayList cap = new Particle2DArrayList();
							cap.add((BasicParticle2D)p);
							cap.add((BasicParticle2D)p2);									
							Capsule2D c = new Capsule2D(p.getX(), p.getY());
							c.setCapsuleParticles(cap);
							
							//Encapsulation
							if(AAParameters.ENCAPSULATION && AAParameters.MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION <= 2) {
								int toAddParticles = c.getMaxContained() - c.getContainedParticles().size();
								if(!useMaxContained) { //if not used, just add all
									c.getContainedParticles().addAll(possibleReactants.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.C, p2.getX(), p2.getY()));
									c.getContainedParticles().addAll(possibleReactants.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.F, p2.getX(), p2.getY()));
									c.getContainedParticles().addAll(newParticles.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.C, p2.getX(), p2.getY()));		
									c.getContainedParticles().addAll(newParticles.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.F, p2.getX(), p2.getY()));											
								}
								else if(toAddParticles > 0) { //else, only add up to max
									for(int tap=0;tap<toAddParticles;tap++) { //add particles
										int totalP = possibleReactants.getTotalCatalysts(c.getX(), c.getY());
										int totalN = newParticles.getTotalCatalysts(c.getX(), c.getY());	
										if(totalP + totalN > 0) { //only if there are catalysts to be added
											//choose weighted random
											if(AAUtil.RANDOM.nextDouble() < ((double)totalP / (double)(totalP+totalN))) {
												c.getContainedParticles().add(possibleReactants.getAndRemoveRandomCatalyst(c.getX(), c.getY(), totalP));
											}										
											else {
												c.getContainedParticles().add(newParticles.getAndRemoveRandomCatalyst(c.getX(), c.getY(), totalN));
											}
										}
									}
								}								
							}										
							newParticles.add(c);								
							hasReacted = true;								
						}
					}												
					else if(p instanceof BasicParticle2D && p2 instanceof Capsule2D) {
						//G + Gn => Gn+1 (R1)
					
						if(((BasicParticle2D) p).isOfType(AAUtil.type.G) && functions.checkRule1Prob() && ((Capsule2D) p2).getCapsuleParticles().size() < AAParameters.MAX_CAPSULE_PARTICLES) { 																																
							
							if(!useMaxCapSize || ((Capsule2D) p2).getCapsuleParticles().size() < ((Capsule2D) p2).getMaxCapsuleSize()) {							
								((Capsule2D) p2).getCapsuleParticles().add(p);								
								hasReacted = true;	
															
								//Encapsulation
								if(AAParameters.ENCAPSULATION && AAParameters.MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION <= 2) {
									int toAddParticles = ((Capsule2D)p2).getMaxContained() - ((Capsule2D)p2).getContainedParticles().size();
									if(!useMaxContained) { //if not used, just add all
										((Capsule2D)p2).getContainedParticles().addAll(possibleReactants.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.C, p2.getX(), p2.getY()));
										((Capsule2D)p2).getContainedParticles().addAll(possibleReactants.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.F, p2.getX(), p2.getY()));
										((Capsule2D)p2).getContainedParticles().addAll(newParticles.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.C, p2.getX(), p2.getY()));		
										((Capsule2D)p2).getContainedParticles().addAll(newParticles.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.F, p2.getX(), p2.getY()));											
									}
									else if(toAddParticles > 0) { //else, only add up to max
										for(int tap=0;tap<toAddParticles;tap++) { //add particles
											int totalP = possibleReactants.getTotalCatalysts(p2.getX(), p2.getY());
											int totalN = newParticles.getTotalCatalysts(p2.getX(), p2.getY());	
											if(totalP + totalN > 0) { //only if there are catalysts to be added
												//choose weighted random
												if(AAUtil.RANDOM.nextDouble() < ((double)totalP / (double)(totalP+totalN))) {
													((Capsule2D)p2).getContainedParticles().add(possibleReactants.getAndRemoveRandomCatalyst(p2.getX(), p2.getY(), totalP));
												}										
												else {
													((Capsule2D)p2).getContainedParticles().add(newParticles.getAndRemoveRandomCatalyst(p2.getX(), p2.getY(), totalN));
												}
											}
										}
									}								
								}										
							}
						}		
					}					
					else if(p instanceof Capsule2D && p2 instanceof BasicParticle2D) {						
						
						//Gn + G => Gn+1 (R1) 
						if(((BasicParticle2D) p2).isOfType(AAUtil.type.G) && functions.checkRule1Prob() && ((Capsule2D) p).getCapsuleParticles().size() < AAParameters.MAX_CAPSULE_PARTICLES) {																
							
							if(!useMaxCapSize || ((Capsule2D) p).getCapsuleParticles().size() < ((Capsule2D) p).getMaxCapsuleSize()) {
								((Capsule2D) p).getCapsuleParticles().add(p2);
								possibleReactants.remove(p2);
								newParticles.add(p);								
								hasReacted = true;
								
								if(AAParameters.ENCAPSULATION && AAParameters.MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION <= 2) {
									int toAddParticles = ((Capsule2D)p).getMaxContained() - ((Capsule2D)p).getContainedParticles().size();
									if(!useMaxContained) { //if not used, just add all
										((Capsule2D)p).getContainedParticles().addAll(possibleReactants.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.C, p.getX(), p.getY()));
										((Capsule2D)p).getContainedParticles().addAll(possibleReactants.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.F, p.getX(), p.getY()));
										((Capsule2D)p).getContainedParticles().addAll(newParticles.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.C, p.getX(), p.getY()));		
										((Capsule2D)p).getContainedParticles().addAll(newParticles.getAndRemoveBasicParticlesOfTypeAt(AAUtil.type.F, p.getX(), p.getY()));											
									}
									else if(toAddParticles > 0) { //else, only add up to max
										for(int tap=0;tap<toAddParticles;tap++) { //add particles
											int totalP = possibleReactants.getTotalCatalysts(p.getX(), p.getY());
											int totalN = newParticles.getTotalCatalysts(p.getX(), p.getY());	
											if(totalP + totalN > 0) { //only if there are catalysts to be added
												//choose weighted random
												if(AAUtil.RANDOM.nextDouble() < ((double)totalP / (double)(totalP+totalN))) {
													((Capsule2D)p).getContainedParticles().add(possibleReactants.getAndRemoveRandomCatalyst(p.getX(), p.getY(), totalP));
												}										
												else {
													((Capsule2D)p).getContainedParticles().add(newParticles.getAndRemoveRandomCatalyst(p.getX(), p.getY(), totalN));
												}
											}
										}
									}								
								}																										
							}
						}												
					}
					else if(p instanceof Capsule2D && p2 instanceof Capsule2D) {
						//do nothing
					}											
				}				
			}	
			if(!hasReacted) {
					newParticles.add(p);
			}	
						
			return AAUtil.addParticleArrayLists(newParticles, resolveInteractions(possibleReactants));
		}
	}	
			
	
	private int getTotalCatalysts(BasicParticle2D p) {
		int sum = 0;
		if(p.isOfType(AAUtil.type.A) || p.isOfType(AAUtil.type.B)) {
			for(Particle2D possibleCatalyst: tempParticlesArray) {
				if(possibleCatalyst instanceof BasicParticle2D && ((BasicParticle2D) possibleCatalyst).isOfType(AAUtil.type.F)) {
					sum++;
				}
			}
		}
		else if(p.isOfType(AAUtil.type.D) || p.isOfType(AAUtil.type.E)) {
			for(Particle2D possibleCatalyst: tempParticlesArray) {
				if(possibleCatalyst instanceof BasicParticle2D && ((BasicParticle2D) possibleCatalyst).isOfType(AAUtil.type.C)) {
					sum++;
				}
			}
		}		
		return sum;
	}
	
	
	private Particle2DArrayList resolveBreakup(Particle2DArrayList p) {
		if(p.isEmpty()) {
			return p;
		}
		else {
			Particle2D particle = p.get(0);	
			p.remove(particle);				
			Particle2DArrayList newParticles = new Particle2DArrayList();
			newParticles.addAll(breakup(particle));			
			
			return AAUtil.addParticleArrayLists(newParticles, resolveBreakup(p));						
		}		
	}			
	
	private Particle2DArrayList breakup(Particle2D p) {
				
		Particle2DArrayList newParticles = new Particle2DArrayList();
		
		if(p instanceof BasicParticle2D) {
			if(((BasicParticle2D) p).isOfType(AAUtil.type.C) && functions.checkRule4Prob()) { //C -> A + B (R4)
				newParticles.add(new BasicParticle2D(AAUtil.type.A, p.getX(), p.getY())); 
				newParticles.add(new BasicParticle2D(AAUtil.type.B, p.getX(), p.getY())); 												
			}
			else if(((BasicParticle2D) p).isOfType(AAUtil.type.F) && functions.checkRule6Prob()) { //F -> D + E (R6)
				newParticles.add(new BasicParticle2D(AAUtil.type.D, p.getX(), p.getY())); 			
				newParticles.add(new BasicParticle2D(AAUtil.type.E, p.getX(), p.getY())); 											
			}
			
			/**G breakup**/
			
			else if(AAParameters.GREMOVAL && ((BasicParticle2D) p).isOfType(AAUtil.type.G) && functions.checkRule2Prob(1)) { //G removal
								
				//do nothing, effectively removing G particle
				//System.out.println("G removed");
				/*if(logGs) {
					removedGs++;
				}*/
			}
			else {
				newParticles.add(p);				
			}			
		}		
		else if(p instanceof Capsule2D) {							
			//(R6) Capsule breakup
						
			if(functions.checkRule2Prob(((Capsule2D) p).getCapsuleParticles().size())) {					
				
				Particle2D p2 = ((Capsule2D) p).getCapsuleParticles().remove(0);				
				
				/*** CHANGED 10/10/2013 (and changed back) ***/
				newParticles.add(p2); //add G to grid
							
				
				/*********ALWAYS RELEASE C PARTICLES UPON BREAKUP OF CAPSULE ***********/				
				newParticles.addAll(((Capsule2D) p).getContainedParticles());				
				((Capsule2D) p).setContainedParticles(new Particle2DArrayList());
				/********************/
							
				if(((Capsule2D) p).getCapsuleParticles().size() >= AAParameters.MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION) {	//enough particles remain
					newParticles.add(p);
				}				
				else {
					
					/***** BEFORE: ONLY RELEASE WHEN N < MIN
					/******newParticles.addAll(((Capsule2D) p).getContainedParticles()); 	//if not enough capsule particles for encapsulation, release all contained particles
					((Capsule2D) p).setContainedParticles(new Particle2DArrayList());********/
					
					if(((Capsule2D) p).getCapsuleParticles().size() >= 2) { //but also smaller than MIN_CAP: still a capsule, but no encapsulation
						System.out.println("With MIN_CAPS set to 2, this should never occur");
						newParticles.add(p);	
					}
					else if (((Capsule2D) p).getCapsuleParticles().size() == 1){ //size < 2, change Capsule to BasicParticle
						newParticles.add(new BasicParticle2D(AAUtil.type.G, p2.getX(), p2.getY())); //add G		
					}
					else { //i.e. size = 0
						//release all contained particles
						System.out.println("Error in Grid2D: incorrect number of particles in capsule: "+((Capsule2D) p).getCapsuleParticles().size()+", "+((Capsule2D) p).getContainedParticles().size()+", "+((Capsule2D) p).getMaxContained());
						newParticles.addAll(((Capsule2D) p).getContainedParticles());
					}	
				}				
			}
			else {
				newParticles.add(p);
			}
		}
		return newParticles;
	}	
	
	
    public Particle2DArrayList getParticles() {
    	return particles;
    }        
    
    public int[][] getReactionsArray() {
    	return reactionsArray;
    }
    
    public AAModel getModel() {
    	return model;
    }  
    
    public Grid2DFunctions getGridFunctions() {
    	return functions;
    }
    
    private void printDebugOutput() {    	
    	AAUtil.out(//" KLD1= " + this.getKLDivergence(1) 
    			", IE1= "+functions.getNormalizedInformationEntropy(1) 
    			//+ " KLD2= " + this.getKLDivergence(2) 
    			+ " IE2= " + functions.getNormalizedInformationEntropy(2)
    			+ " JointEntropy= " + functions.getJointEntropy()   
    			+ " MutualInformation= "+ functions.getMutualInformation());
    }
    
    public void setUseLogGs(boolean b) {
    	useLogGs = b;
    }
    
    public void setLogGs(boolean b) {
    	logGs = b;
    }
    
    public void setUseMaxContained(boolean b) {
    	useMaxContained = b;
    }
        
    public void setUseMaxCapSize(boolean b) {
    	useMaxCapSize = b;
    }
    
    public int getAddedGs() {
    	return addedGs;
    }
    
   /* public int getRemovedGs() {
    	return removedGs;
    }*/    
    

	public void loadGs(String filePath, int numberOfTimesteps) {
		
		toAddGs = new int[numberOfTimesteps];  
    	//toRemoveGs = new int[numberOfTimesteps];
    	    
		try {								
			BufferedReader input = new BufferedReader(new FileReader(filePath));
			String s; 
			int count = 0;
	    	while((s = input.readLine()) != null) { 	    		
	    		//String[] params = s.split(",");
	    		//toAddGs[count] = Integer.parseInt(params[0]);
	    		toAddGs[count] = Integer.parseInt(s);
	    		//toRemoveGs[count] = Integer.parseInt(params[1]);
	    		count++;
	    		//System.out.println(count);
	    	}
			input.close(); 				
		} 			
		catch(Exception e) {
			System.out.println("Exception in loadGs: "+e.getMessage());
		}							
	}
}
