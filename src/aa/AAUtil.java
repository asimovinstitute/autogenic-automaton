
package aa;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

import aa.model.Particle2D;
import aa.model.Particle2DArrayList;

// some methods used throughout the code

public final class AAUtil {
	
	public static enum type{A, B, C, D, E, F, G};
	
	/*****   RANDOM  *****/	
	
	public static enum NumberType{FRACTION, INTEGER, ONE_TO_LARGE, SMALL_TO_LARGE, SMALL_TO_ONE};
	
	public final static Random RANDOM = new Random();
	
	public static int LARGE = 1000000;
		
	public static double getRandomNumber(NumberType r) {		
		switch(r) {
		case FRACTION       : { return RANDOM.nextDouble(); }
		case INTEGER		: { return RANDOM.nextInt(); }
		case ONE_TO_LARGE   : { return RANDOM.nextInt(LARGE)+1; }
		case SMALL_TO_LARGE : { return getRandomNumber(RANDOM.nextBoolean() ? NumberType.ONE_TO_LARGE : NumberType.SMALL_TO_ONE); } 
		case SMALL_TO_ONE   : { return 1.0/getRandomNumber(NumberType.ONE_TO_LARGE); } 		
		default: {out("Error in AAUtil: no such random number type"); return 0.0;}
		}	
	}
	
	public static double getNumber(NumberType r, byte[] bytes) {
		switch(r) {
		case FRACTION       : { 			
			double teller = AAUtil.getNumber(NumberType.INTEGER, bytes);
			double noemer = Math.pow(2, bytes.length)-1;
			return teller/noemer;				
		}
		case INTEGER        : {
			double sum = 0;
			switch(bytes.length) {
			case 0 : { out("Error in AAUtil: empty byte array"); return 0.0;}
			case 1 : { sum = bytes[0]; break; }
			case 2 : { sum = bytes[0] + bytes[1]*2; break; }
			case 3 : { sum = bytes[0] + bytes[1]*2 + bytes[2]*4; break; }
			case 4 : { sum = bytes[0] + bytes[1]*2 + bytes[2]*4 + bytes[3]*8; break; }
			case 5 : { sum = bytes[0] + bytes[1]*2 + bytes[2]*4 + bytes[3]*8 + bytes[4]*16; break; }
			case 6 : { sum = bytes[0] + bytes[1]*2 + bytes[2]*4 + bytes[3]*8 + bytes[4]*16 + bytes[5]*32; break; }
			default : {out("Error in AAUtil: byte array too long"); return 0.0;}
			}		
			return sum;	
		}
		case ONE_TO_LARGE   : {
			int factor = 0;
			switch(bytes.length) {
			case 0 : { out("Error in AAUtil: empty byte array"); return 0.0;}
			case 1 : { factor = bytes[0]; break; }
			case 2 : { factor = bytes[0] + bytes[1]*2; break; }
			case 3 : { factor = bytes[0] + bytes[1]*2 + bytes[2]*4; break; }
			case 4 : { factor = bytes[0] + bytes[1]*2 + bytes[2]*4 + bytes[3]*8; break; }
			case 5 : { factor = bytes[0] + bytes[1]*2 + bytes[2]*4 + bytes[3]*8 + bytes[4]*16; break; }
			case 6 : { factor = bytes[0] + bytes[1]*2 + bytes[2]*4 + bytes[3]*8 + bytes[4]*16 + bytes[5]*32; break; }
			default : {out("Error in AAUtil: byte array too long"); return 0.0;}
			}		
			return Math.pow(10, factor)/10.0;
		}
		case SMALL_TO_LARGE : {
			byte[] bs = new byte[bytes.length-1];
			for(int i=0; i<bs.length; i++) {
				bs[i] = bytes[i+1];
			}				
			return getNumber(bytes[0] == 1 ? NumberType.ONE_TO_LARGE : NumberType.SMALL_TO_ONE, bs); 
		} 
		case SMALL_TO_ONE   : { return 1.0/getNumber(NumberType.ONE_TO_LARGE, bytes); } 		
		default: {out("Error in AAUtil: no such random number type"); return 0.0;}
		}		
	}
	
		
	
	/***** LOAD/SAVE *****/
	
	public static void saveParameters(AAControl control, File fileToSave) {
		
		try {	
			Writer output = new BufferedWriter(new FileWriter(fileToSave));																		
	    	output.write(AAParameters.parametersToString());
	    	output.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public static void loadParameters(AAControl control, File fileToLoad, boolean reset) {
		
		try {								
			BufferedReader input = new BufferedReader(new FileReader(fileToLoad));
			AAParameters.stringToParameters(input);						
			input.close(); 				
		} 			
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		if(reset) {
			control.resetSimulation();
		}		
	}
	
	public static double[] getProbabilities(File fileToLoad) {		
		double[] prob = new double[AAParameters.PROBABILITIES.length];
		try {								
			BufferedReader input = new BufferedReader(new FileReader(fileToLoad));
			prob = AAParameters.getProbabilities(input);
			input.close(); 				
		} 			
		catch(Exception e) {
			System.out.println(e.getMessage());
		}	
		return prob;
	}
	
	
	
	
	/***** ARRAYLISTS *****/
	
	
	public static ArrayList<Particle2D> addArrayLists(ArrayList<Particle2D> p1, ArrayList<Particle2D> p2) {
		p1.addAll(p2);
		return p1;		
	}
	
	public static Particle2DArrayList addParticleArrayLists(Particle2DArrayList p1, Particle2DArrayList p2) {
		p1.addAll(p2);
		return p1;		
	}
	
	
    /***** MATH *****/
       
    public static int square(int n) {
    	return (int)Math.round(Math.pow((double)n, 2.0));
    }
    
    
    /***** PRINTING *****/      
    
    public static void printArray(int[] is) {
    	System.out.print(arrayToString(is));
    }
    
    public static void printArray(double[] ds) {
    	System.out.print(arrayToString(ds));
    }
    
    public static void out(String s) {
    	System.out.println(s);
    }
    
    public static String arrayToString(double[] ds) {
    	String s = "";//"[";
    	for(double d : ds) {
    		s += d + ",";
    	}
    	return s;// + "]";    	
    }
    
    public static String arrayToString(int[] is) {
    	String s = "";//"[";
    	for(int i : is) {
    		s += i + ",";
    	}
    	return s;// + "]";    	
    }    
    
    /***** METRICS *****/
    

    public static int getRecurrentAddition(int number) {
    	if(number<2) {
    		return 0;
    	}
    	else {
    		return (int)Math.round((Math.pow((number-1),2) + (number-1))/2);
    	}
    }
      
    public static int getManhattanDistance(Particle2D p1, Particle2D p2) {
    	return (Math.abs(p1.getX()-p2.getX()) + Math.abs(p1.getY()-p2.getY()));
    }
    
    
    /***** ARRAY FUNCTIONS *****/
    
    public static int sumArray(int[] a) {
    	int sum = 0;
    	for(int i : a) {
    		sum += i;
    	}
    	return sum;
    }
    
    public static int avgArray(int[] a) {
    	return (int)Math.round((double)sumArray(a) / (double)a.length);    	
    }   
    
    
    /***** ARRAY FUNCTIONS *****/

    public static Color getColor(int t) {    	    	    
    	if(t == 0) return Color.GREEN;
    	else if(t == 1) return Color.MAGENTA;
    	else if(t == 2) return Color.CYAN;
    	else if(t == 3) return Color.BLUE;
    	else if(t == 4) return Color.YELLOW;
    	else if(t == 5) return Color.RED;    
    	else if(t == 6) return Color.ORANGE;  
    	else if(t == 7) return Color.PINK;  
    	else if(t == 8) return Color.LIGHT_GRAY; 
    	else if(t == 9) return Color.BLACK; 
    	else if(t == 10) return Color.DARK_GRAY; 
    	else if(t == 11) return Color.GRAY; 
    	else if(t == 12) return Color.GREEN;  //out of colors, so reuse others
    	else if(t == 13) return Color.MAGENTA; 
    	else if(t == 14) return Color.CYAN; 
    	else if(t == 15) return Color.BLUE;
    	else if(t == 16) return Color.YELLOW;
    	else if(t == 17) return Color.RED;
    	else if(t == 18) return Color.ORANGE;
    	else {System.out.println("Unknown type: "+t); return Color.WHITE;}
    }
    
    public static Color getBWColor(int n) {
    	int color = 255-Math.min(n*50, 255);
    	return new Color(color,color,color);
    }
}
