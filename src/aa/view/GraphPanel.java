package aa.view;

import javax.swing.JPanel;

import aa.AAParameters;
import aa.AAUtil;

import java.awt.*;

public class GraphPanel extends JPanel {	    
			 
	private InformationPanel informationPanel;	
	private int currentScale;
	int maxUnits;
	int totalParticles;
		
    public GraphPanel(InformationPanel i) {
    	
    	informationPanel = i;
    	currentScale = 1;    	
    	maxUnits = maxValue(AAParameters.INIT_PARTICLES);
    	totalParticles = informationPanel.getView().getModel().getGrid().getGridFunctions().getTotalBasicParticles();
    	
    	this.setBackground(Color.WHITE);   
    	
    }
       
    public void reset() {   
    	maxUnits = maxValue(AAParameters.INIT_PARTICLES);
    	totalParticles = informationPanel.getView().getModel().getGrid().getGridFunctions().getTotalBasicParticles();
    	
    	this.repaint();
    }
    
    @Override
    public void paint(Graphics g) {
    	     	
    	
    	//DRAW AXIS
    	Graphics2D g2 = (Graphics2D) g;
    	    	
    	g2.setColor(Color.BLACK);    	    	
	    g2.setStroke(new BasicStroke(2));    	       
	    g2.drawLine(50, 25, 50, 225);
	    g2.drawLine(50, 225, 450, 225);
	    g2.drawLine(450, 25, 450, 225);
	    	    
	    //DRAW LEGEND	    
	    g2.drawString("Unit #", 6, 75);
	    g2.drawString("CapSize", 460, 75);
	    
	    g2.drawString(maxUnits+"", 20, 30);
	    g2.drawString(((int)Math.round(maxUnits/2))+"", 20, 130);
	    g2.drawString("0", 20, 225);
	    
	    g2.drawString("10", 460, 30);
	    g2.drawString("5", 460, 130);
	    g2.drawString("0", 460, 225);
	    	    
	    //g2.setColor(Color.LIGHT_GRAY);    
	    //g2.setStroke(new BasicStroke(1)); 
	    //g2.drawLine(52, 25, 448, 25);	    
	    //g2.drawLine(52, 125, 448, 125);	    
	    
	    //DRAW GRAPHS
	    
	    int[][] currentData;
	    if(currentScale == 1)
	    	currentData = informationPanel.getData100();
	    else if(currentScale == 2)
	    	currentData = informationPanel.getData1000();
	    else
	    	currentData = informationPanel.getData10000();
	    
	    for(int i=0;i<19;i++) {
	    	if(informationPanel.getChoicePanel().getChecked(i)) {
	    		int scale;	    		
	    		if(i==8 || i==10 || i==11 || i==12)
	    			scale = 1000;
	    		else if(i>=13 || i<=18) 
	    			scale = 10000;
	    		else
	    			scale = maxUnits;
	    		drawGraph(currentData[i], informationPanel.getCurrentIteration(), scale, g2, AAUtil.getColor(i));
	    	}		    			    	    
	    }
    }
    
    private void drawGraph(int[] data, int i, int max, Graphics2D g2, Color c) {
    	g2.setColor(c);
    	g2.setStroke(new BasicStroke(2));
    	
    	int[] points = scaleData(data, max);

    	if(currentScale == 1) {  
    		if(i > 1 && i<100) {    		
	    		for(int j=2; j<i ;j++) {   
	    			int x1 = 50 + (j-1)*4;
	    			int x2 = 50 + (j*4);
	    			int y1 = 224 - points[j-1];
	    			int y2 = 224 - points[j];
	    			if(!(y1 == 224 && y2 == 224))
	    				g2.drawLine(x1, y1, x2, y2);
	    		}
	    	}        	    	    	
	    	else if(i >= 100) {
	    		for(int j=1; j<99 ;j++) {     
	    			int x1 = 50 + (j*4);
	    			int x2 = 50 + ((j+1)*4);
	    			int y1 = 224 - points[((i+j)%100)];
	    			int y2 = 224 - points[((i+j+1)%100)];    			
	    			if(!(y1 == 224 && y2 == 224))
	    				g2.drawLine(x1, y1, x2, y2);
	    		}
	    	}
    	}
    	else if(currentScale == 2) {    		
    		if(i > 20 && i<1000) {    		
	    		for(int j=2; (j*10)<i ;j++) {
	    			int x1 = 50 + (j-1)*4;
	    			int x2 = 50 + (j*4);
	    			int y1 = 224 - points[j-2];
	    			int y2 = 224 - points[j-1];
	    			if(!(y1 == 224 && y2 == 224))
	    				g2.drawLine(x1, y1, x2, y2);
	    		}
	    	}        	    	    	
	    	else if(i >= 1000) {
	    		for(int j=1; j<98 ;j++) {   
	    			int x1 = 50 + (j*4);
	    			int x2 = 50 + ((j+1)*4);
	    			int y1 = 224 - points[((int)Math.floor((double)i/10.0)+j)%100];
	    			int y2 = 224 - points[((int)Math.floor((double)i/10.0)+j+1)%100];    			
	    			if(!(y1 == 224 && y2 == 224))
	    				g2.drawLine(x1, y1, x2, y2);
	    		}
	    	}
    	}
    	else {    		
    		if(i > 20 && i<10000) {    		
	    		for(int j=2; (j*100)<i ;j++) {
	    			int x1 = 50 + (j-1)*4;
	    			int x2 = 50 + (j*4);
	    			int y1 = 224 - points[j-2];
	    			int y2 = 224 - points[j-1];
	    			if(!(y1 == 224 && y2 == 224))
	    				g2.drawLine(x1, y1, x2, y2);
	    		}
	    	}        	    	    	
	    	else if(i >= 10000) {
	    		for(int j=1; j<98 ;j++) {   
	    			int x1 = 50 + (j*4);
	    			int x2 = 50 + ((j+1)*4);
	    			int y1 = 224 - points[((int)Math.floor((double)i/100.0)+j)%100];
	    			int y2 = 224 - points[((int)Math.floor((double)i/100.0)+j+1)%100];    			
	    			if(!(y1 == 224 && y2 == 224))
	    				g2.drawLine(x1, y1, x2, y2);
	    		}
	    	}
    	}
    }
    
    //more efficient: scale directly when adding to dataA
    public int[] scaleData(int[] data, int max) {
    	int[] newData = new int[data.length];
    	double factor = (double)200/(double)max;
    	for(int i=0; i<data.length; i++) {
    		newData[i] = (int)Math.round((double)data[i]*factor);
    	}
    	return newData;
    }
    
    private int maxValue(int[] array) {
    	int high = 0;
    	for(int i : array) {
    		if(i > high) {
    			high = i;
    		}
    	}
    	return high;
    }        
    
    public void setScale(int s) {
    	currentScale = s;
    }
}
