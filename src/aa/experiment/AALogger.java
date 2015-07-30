package aa.experiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import aa.AAControl;

public class AALogger {
	
	AAControl control;
	String filePath;
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH'u'mm'm'ss's'";
	
	public AALogger(AAControl c) {
		control = c;
		filePath = "log/AALog - " + getTime() + ".csv";
	}
	
	public AALogger(AAControl c, String fileName, boolean appendTimestamp) {
		control = c;
		filePath = "log/" + fileName + (appendTimestamp ? " - " + getTime() : "") + ".csv";
	}
		
	public void write(String text) {
		try {	
			Writer output = new BufferedWriter(new FileWriter(new File(filePath), true));
	    	output.write(text);	    	
	    	output.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}				

	public static String getTime() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    return sdf.format(cal.getTime());	
	}
}


