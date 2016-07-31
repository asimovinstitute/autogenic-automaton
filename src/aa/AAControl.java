
package aa;

import aa.experiment.AAExperiment;
import aa.model.AAModel;
import aa.view.AAView;

public class AAControl {
    
	
    // initialization of parameters
	private boolean pauseModelThread;
	private boolean viewerActive = true;
    
    private AAModel model; 
    private AAView view;    
    
	// running as a jar file would be better to explicitly state setting directory
    public static String DEFAULT_SETTINGS_FILEPATH = "./settings";
	
	public static void main(String[] args) {
	
        AAControl control = new AAControl();	        
                        
        if(args.length == 0) {
        	
        	System.out.println("\nRunning GUI");
        	control.run();
        	
        } 
        else if (args.length == 4 && args[0].equals("-exp")) {        	        	
        	
        	System.out.println("\nRunning experiment with parameters set according to "+args[1]+
        			" for "+args[2]+
        			" iterations, over "+args[3]+" trials.");
        	control.experiment(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        	System.exit(0);
        	
        } 
        else if (args.length == 6 && args[0].equals("-exp")) {        	
        	
        	System.out.println("\nRunning experiment with parameters set according to "+args[1]+
        			" for the first "+args[2]+
        			" iterations, then using "+args[3]+
        			" for the remaining "+args[4]+" iterations, over "+args[5]+" trials.");
        	control.experiment(args[1], Integer.parseInt(args[2]), args[3], Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        	System.exit(0);
        	
        } 
        else if (args.length == 1 && args[0].equals("-test")) {
        	System.out.println("\nRunning test(s)");
        	control.test(false);
        	System.exit(0);
        }
        else if (args.length == 1 && (args[0].equals("-help") || args[0].equals("-h"))) {
        	System.out.println("\nthe Autogenic Automaton");
        	System.out.println("created by Stefan Leijnen (stefan@leijnen.com), 2011-2016");
        	System.out.println("*********************************************************");
        	System.out.println("AA.jar can be used with the following parameters:");
        	System.out.println("  to run as a stand-alone GUI: \"java AA.jar\"");
        	System.out.println("  to run experiment with stable conditions: \"java AA.jar -exp [parameterfile] [iterations] [trials]\" (e.g. \"java AA.jar -exp SA.csv 1000 10\")");
        	System.out.println("  to run experiment with changing conditions: \"java AA.jar -exp [parameterfile1] [iterations1] [parameterfile2] [iterations2] [trials]\" (e.g. \"java AA.jar -exp SA1.csv 100 SA2.csv 900 10\")");
        	System.out.println("  to run debug test(s): \"java AA.jar -test\"");
        	System.out.println("  to show help: \"java AA.jar -help\"\n");
        	System.exit(0);
        }
        else {
        	System.out.println("\nArguments not recognized; are you perhaps missing a parameter(file)?");
        	System.exit(0);
        }        
	}  
	 	 
    public void experiment(String paramfile, int iterations, int trials) {    	
    	model = new AAModel(this);  
    	//no visible viewer in headless mode 
    	viewerActive = false;
    	if(viewerActive) {
    		initializeViewer();
    		//model.setPriority(Thread.MIN_PRIORITY);
    	}
    	AAExperiment exp = new AAExperiment(this, paramfile, iterations, trials);    	
    	model.start();    	
    	exp.start(); 
    }
    
    public void experiment(String paramfile1, int iterations1, String paramfile2, int iterations2, int trials) {    	
    	model = new AAModel(this);  
    	//no visible viewer in headless mode 
    	viewerActive = false;
    	if(viewerActive) {
    		initializeViewer();
    		//model.setPriority(Thread.MIN_PRIORITY);
    	}
    	AAExperiment exp = new AAExperiment(this, paramfile1, iterations1, paramfile2, iterations2, trials);    	
    	model.start();    	
    	exp.start(); 
    }
             
	private void test(boolean viewer) {    	  
    	System.out.println("test");
    	//model = new AAModel(this);    	    	
    	//model.start();    	
    	//model.getGrid().getGridFunctions().getNormalizedInformationEntropyMu();    	
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
