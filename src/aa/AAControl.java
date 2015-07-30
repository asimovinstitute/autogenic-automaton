
package aa;

import aa.experiment.AAExp4;
import aa.model.AAModel;
import aa.model.Grid2DFunctions;
import aa.view.AAView;

public class AAControl {
    
    //initialization parameters
	private boolean pauseModelThread;
	private boolean viewerActive = true;
    
    private AAModel model;
    private AAView view;    
    
	public static String DEFAULT_SETTINGS_FILEPATH = "./settings";
	
	public static void main(String[] args) {

		System.out.println("Launching Autogenic Automaton");
        AAControl control = new AAControl();	        
        System.out.println("Finished launching");
        System.out.println("-----------------------------");
        
        //test
        
        //control.test(false);
        control.run();
        //control.experiment(false);        
	}  
	 	 
    public void experiment(boolean viewer) {    	
    	model = new AAModel(this);  
    	viewerActive = viewer;
    	if(viewer) {
    		initializeViewer();
    		//model.setPriority(Thread.MIN_PRIORITY);
    	}
    	AAExp4 exp = new AAExp4(this);
    	model.start();    	
    	exp.start(); 
    }
             
	private void test(boolean viewer) {    	  
    	System.out.println("test");
    	model = new AAModel(this);    	    	
    	model.start();    	
    	model.getGrid().getGridFunctions().getNormalizedInformationEntropyMu();    	
    }
		    
    public void run()
    {        
    	model = new AAModel(this);
        initializeViewer();   
        model.setPriority(Thread.MIN_PRIORITY);
    	model.start();   
    }
    
    public AAControl()
    {       
    	pauseModelThread = true;
    }
    
    private void initializeViewer() {
        viewerActive = true;
    	view = new AAView(model, this);      
		model.setView(view);      
		System.out.println("Starting Viewer");
		view.initialize();
    }
    
    public void runSimulation(int steps) {
    	
    	try{
	    	while(model.getCurrentIteration() < steps) {
	    		stepSimulation();
	    	}   
    	}
    	catch(Exception ex) {
    		System.out.println("Threading exception");
    	}
    }

    public void runSimulation()
    {        
        synchronized (model) {
            pauseModelThread = false;
            model.notify();
        }
    }        

    public void stopSimulation() {
       synchronized (model) {
            pauseModelThread = true;
        }
    }
    
    public void stepSimulation() {
    	model.step();
    }
  
    public void resetSimulation() {    	
        model.reset();
        if(viewerActive) {
        	view.reset(); //from parameters to view        	
        }
    }       
          
    public AAModel getModel() {
        return model;
    }      
    
    public AAView getView() {
    	if(viewerActive) return view;
    	else {System.out.println("Error in AAControl: no viewer exists"); return null;}
    }
    
    public boolean viewerActive() {
    	return viewerActive;
    }
   
    public boolean getPauseModelThread() {
    	return pauseModelThread;
    }
}
