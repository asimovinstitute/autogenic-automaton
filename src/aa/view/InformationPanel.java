package aa.view;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import aa.model.Grid2D;
import aa.AAUtil;

import java.awt.*;
import java.util.ConcurrentModificationException;

public class InformationPanel extends JPanel {	    
	private static final long serialVersionUID = 884417055246796684L;

	private AAView view; 
             
	private GraphPanel graphPanel;	
	private ChoicePanel choicePanel;
	
	int[][] data100;
	int[][] data1000;
	int[][] data10000;
	
	private final int DATASIZE = 18;  
		 
    public InformationPanel(AAView v) {        
        view = v;
        
        graphPanel = new GraphPanel(this);
        graphPanel.setPreferredSize(new Dimension(520, 300));
        
        choicePanel = new ChoicePanel(this);
        choicePanel.setPreferredSize(new Dimension(520, 100));
        
        data100 = new int[DATASIZE][100];
        data1000 = new int[DATASIZE][100];
        data10000 = new int[DATASIZE][100];
        
        this.setBackground(Color.WHITE);    	
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder("Graph (ITERATION="+view.getModel().getCurrentIteration()+")"));
        this.add(graphPanel, BorderLayout.CENTER);
        this.add(choicePanel, BorderLayout.SOUTH);
               
        updateView();	
              
    }
    
    public void updateView() {
    	
    	try {
	    	this.setBorder(new TitledBorder("Graph (ITERATION="+view.getModel().getCurrentIteration()+")"));
	    	
	    	Grid2D g = view.getModel().getGrid();
	    	int iteration = view.getModel().getCurrentIteration();
	    	double[] entropies = g.getGridFunctions().getEntropyQuantifications();
	    	
	    	// DATA 100
	    	
	    	int i = iteration%100;   	    	
	    	data100[0][i] = g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.A);
	    	data100[1][i] = g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.B);
	    	data100[2][i] = g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.C);
	    	data100[3][i] = g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.D);
	    	data100[4][i] = g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.E);
	    	data100[5][i] = g.getGridFunctions().getTotalGsNotInCapsules();//g.getTotalBasicParticlesOfType(AAUtil.type.F);
	    	
	    	data100[6][i] = Math.round((float)g.getGridFunctions().getTotalEmptyCapsulesByContainedParticles()*10.0f);
	    	data100[7][i] = Math.round((float)g.getGridFunctions().getAverageEmptyCapsuleSizeByContainedParticles()*100.0f);
	    	data100[8][i] = Math.round((float)g.getGridFunctions().getTotalAutogensByContainedParticles()*10.0f);
	    	data100[9][i] = Math.round((float)g.getGridFunctions().getAverageAutogenSizeByContainedParticles()*100.0f);
	    	
	    	/*
	    	data100[10][i] = Math.round((float)entropies[0] * 1000.0f);
	    	data100[11][i] = Math.round((float)entropies[1] * 1000.0f);   
	    	data100[12][i] = Math.round((float)entropies[2] * 1000.0f);
	    	data100[13][i] = Math.round((float)entropies[3] * 1000.0f); 
	    	data100[14][i] = Math.round((float)entropies[4] * 1000.0f);
	    	data100[15][i] = Math.round((float)entropies[5] * 1000.0f);   
	    	data100[16][i] = Math.round((float)entropies[6] * 1000.0f);
	    	data100[17][i] = Math.round((float)entropies[7] * 1000.0f); */
	    		    	
	    	// DATA 1000
	    	
	    	int j = iteration%1000;
	    	if(j%10==0 && (j>0 || iteration>=1000)) {
	    		if(j==0) //ugly hacking, but it works
	    			j = 1000;
	    		int k = (j/10)-1;    
	    	    		
	    		for(int k2=0;k2<data1000.length;k2++) {
	    			data1000[k2][k] = getAverageData(data100[k2], (j-10));
	    		}    		
	    	}
	    	
	    	// DATA 10000
	    	
	    	int m = iteration%10000;
	    	if(m%100==0 && (m>0 || iteration>=10000)) {
	    		if(m==0)
	    			m = 10000;
	    		int n = (m/100)-1;
	    		
	    		for(int n2=0;n2<data10000.length;n2++) {
	    			data10000[n2][n] = getAverageData(data1000[n2], ((m/10)-10));
	    		}       		
	    	}    	
	    	graphPanel.repaint();
	    	graphPanel.revalidate();
    	}
    	catch(Exception ex) {
        	//do nothing, happens now and then at max speed
        }  
    }
    
    private int getAverageData(int[] data, int i) {
    	int sum = 0;
    	
    	for(int j=0; j<10; j++) {
    		sum += data[(i+j)%100];
    	}
    	return (int)Math.round((double)sum/10.0);    
    }
    
    public void reset() {
    	data100 = new int[DATASIZE][100];
    	data1000 = new int[DATASIZE][100];
    	data10000 = new int[DATASIZE][100];
    	choicePanel.changeScale();
    	graphPanel.reset();
    	this.updateView();    	
    }
    
    public int[][] getData100() {
    	return data100;
    }    

    public int[][] getData1000() {
    	return data1000;
    }    
    
    public int[][] getData10000() {
    	return data10000;
    }    

    public int getCurrentIteration() {
    	return view.getModel().getCurrentIteration();
    }
        
    public AAView getView() {
    	return view;
    }
    
    public ChoicePanel getChoicePanel() {
    	return choicePanel;
    }
    
    public GraphPanel getGraphPanel() {
    	return graphPanel;
    }
    
    public void printEntropies() {
    	double[] entropies = view.getModel().getGrid().getGridFunctions().getEntropyQuantifications();
    	System.out.println();
    	
    	//NOTE: type = 3: pdSADEF
    	
    	System.out.print("H1: "+entropies[0]+" ("+view.getModel().getGrid().getGridFunctions().getInformationEntropy(3)+")");
    	System.out.println("   H2: "+entropies[1]+" ("+view.getModel().getGrid().getGridFunctions().getInformationEntropy(2)+")");
    	System.out.print("MI: "+entropies[2]);
    	System.out.println("   JE: "+entropies[3]);
    	System.out.print("K1: "+entropies[4]);
    	System.out.println("   K2: "+entropies[5]);
    	System.out.print("C1: "+entropies[6]);
    	System.out.println("   C2: "+entropies[7]);
    	System.out.println("AVGCrossEntropy: "+view.getModel().getGrid().getGridFunctions().getSymmetrizedCrossEntropyWithEpsilon(0.0001));
    	System.out.print("#EC: "+view.getModel().getGrid().getGridFunctions().getTotalEmptyCapsulesByContainedParticles());
    	System.out.println("   AVGEC: "+view.getModel().getGrid().getGridFunctions().getAverageEmptyCapsuleSizeByContainedParticles());
    	System.out.print("#AG: "+view.getModel().getGrid().getGridFunctions().getTotalAutogensByContainedParticles());
    	System.out.println("   AVGAG: "+view.getModel().getGrid().getGridFunctions().getAverageAutogenSizeByContainedParticles());
    	
    	//System.out.println();
    	//AAUtil.printArray(view.getModel().getGrid().getGridFunctions().getDistribution(2)[0]);
    }
}
