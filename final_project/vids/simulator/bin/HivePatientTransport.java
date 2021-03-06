import java.util.*;
import java.io.*;

public class HivePatientTransport extends PatientTransport
{

    ArrayList<String> payload = new ArrayList<String>();

    public void load(Object aPayload)
    {
	payload.add( (String)aPayload );
    }

    public String transport()
    {
	StringBuffer sb = new StringBuffer();
	sb.append('"');
	sb.append( "INSERT INTO TABLE VIDS.patient VALUES ");
	int i=0;
	while (i < payload.size() -1){
	    String data = payload.get(i);
	    sb.append( data );
	    sb.append(',');
	    i++;
	}
	String data = payload.get(i);
	sb.append( data );
	sb.append(';');
	sb.append('"');

	String insert = sb.toString();

	try
	    {
		ProcessBuilder builder = new ProcessBuilder("hive", "-e", insert);
		builder.redirectErrorStream(true);
		final Process process = builder.start();
		watch( process );
		payload = new ArrayList<String>();

	}
	catch( Exception e ){ e.printStackTrace() ;}
	System.out.println(insert);
	return insert;
    }

    private static void watch(final Process process) {
	new Thread() {
	    public void run() {
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = null; 
		try {
		    while ((line = input.readLine()) != null) {
			System.out.println(line);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}.start();
    }

}