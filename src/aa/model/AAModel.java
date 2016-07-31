
package aa.model;

import java.util.ArrayList;

import aa.AAControl;
import aa.AAParameters;
import aa.view.AAView;

public class AAModel extends Thread {
			
    private AAControl control;
    private AAView view;            
    private Grid2D g;    
    
    //Parameters               
    private static int iteration;        
    
    public AAModel(AAControl c) {        
        control = c;
        iteration = 0;        
        g = new Grid2D(this);           
    }

    public void reset() {    	
    	iteration = 0;    	
    	g = new Grid2D(this);    	    	    	
    }        

    public void run() {
        while (true) {
            //First, check if the simulation has been paused
            synchronized (this) {
                while (control.getPauseModelThread()) {
                    try {
                        wait();
                    }
                    catch (Exception e) {
                        System.out.println("Thread Exception: "+e.getMessage());
                    }
                }
            }

            this.step();
            
            try {
                sleep(getSleepTime(AAParameters.SIM_SPEED));
            }
            catch(Exception e) {
            
            }
        }
    }
    
    public void step() {      	    	
       	this.update();
    	if(control.viewerActive()) {
    		view.update();
    	}
    }
    
    private int getSleepTime(int speed) {
    	switch(speed) {
    	case 1: return 1000;
    	case 2: return 250;
    	case 3: return 50;
    	case 4: return 0;
    	default: {System.out.println("AModel.getSleepTime: Unknown Speed"); return 1000;}
    	}
    }

    public void update() {            		
    	g.next();    	    	  
    	iteration++;  
    }
   
    public Grid2D getGrid() {
        return g;
    }
    
    public void setView(AAView v) {
        view = v;
    }
    
    public ArrayList<Particle2D> getParticles() {
        return g.getParticles();
    }  
    
    public AAControl getControl() {
    	return control;
    }
      
    public AAView getView() {
    	return view;
    }
    
    public static int getCurrentIteration() {
    	return iteration;
    } 
}
