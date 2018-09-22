

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class File {

	FileWriter fw;
	BufferedWriter bw;
	
	public File(String pName) {
		try {
			fw = new FileWriter(pName + ".csv",true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);
	}
	public void write(int pStartnummer, long pZeit) throws IOException{
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
	    String uhrzeit = date.format(new Date());
	    System.out.println(uhrzeit);
		try {
			bw.write(uhrzeit + ";" + pStartnummer + ";" + pZeit);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}