
package aa;

import aa.experiment.AAExperiment;
import aa.model.AAModel;
import aa.view.AAView;

public class AAControl {
    
	/*
	 * - experimenten uit paper herhalen in headless mode 
	 *  (liefst via jar proberen) —> juiste settings achterhalen 
	 *  en output controleren. Nadenken over handige I/O (csv, 
	 *  verschillend per experiment type?)
	 * - experiment type in args verwerken
	 * - SEL negeren
	 * - uiteindelijk .jar file opleveren
	 */
	
    //initialization parameters
	private boolean pauseModelThread;
	private boolean viewerActive = true;
    
    private AAModel model; 
    private AAView view;    
    
	// NOTE deprecated, as running as a jar file would be better to explicitly state setting directory
    public static String DEFAULT_SETTINGS_FILEPATH = "./settings";
	
	public static void main(String[] args) {

		System.out.println("Launching Autogenic Automaton");
        AAControl control = new AAControl();	        
        System.out.println("Finished launching");
        System.out.println("-----------------------------");
                
        if(args.length == 0) {
        	System.out.println("Running GUI");
        	control.run();        	
        }
        else if (args.length == 5 && args[0].equals("-exp")) {
        	/*
        	 * necessary parameters: 
        	 * 		-exp (run an experiment, rather than the GUI
        	 * 		(TODO: is the type parameter really necessary? Would be great if not) 
        	 * 		type (what type of experiment to run (e.g. SA, AC, AS, SEL): see paper for details)
        	 *  	parameter file 
        	 *  	trials (how many times should the experiment be run? (e.g. for averaging over multiple runs))
        	 *  	iterations (for how many time steps should one experiment run?)
        	 * e.g.“java AA.jar -exp SA SA.csv 10 1000”
        	 */
        	
        	System.out.println("Running experiment "+args[1]+ 
        			" with parameters set according to "+args[2]+
        			" for "+args[3]+" trials, consisting of "+args[4]+" time steps each.");        
        	control.experiment(args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));	
        } else if (args.length == 1 && args[0].equals("-test")) {
        	System.out.println("Running tests");
        	control.test(false);
        }
        else {
        	System.out.println("Please enter a different set of arguments. Are you missing some (e.g. parameterfile)?");
        }        
	}  
	 	 
    public void experiment(String type, String paramfile, int trials, int iterations) {    	
    	model = new AAModel(this);  
    	//no visible viewer in headless mode - perhaps add later 
    	viewerActive = false;
    	if(viewerActive) {
    		initializeViewer();
    		//model.setPriority(Thread.MIN_PRIORITY);
    	}
    	AAExperiment exp = new AAExperiment(this, type, paramfile, trials, iterations);    	
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
