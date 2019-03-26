import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Backup {

	FileWriter fw;
	BufferedWriter bw;
	FileWriter fwl;
	BufferedWriter bwl;
	
	public Backup() {
		try {
			fw = new FileWriter("zeitkiste-backup.csv",true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);
		try {
			fwl = new FileWriter("zeitkiste-log.txt",true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bwl = new BufferedWriter(fwl);
	}
	
	public void save (int pStartnummer, long pAutomatik, long pManuell) {
		try {
			bw.write(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "," + pStartnummer + "," + pAutomatik + "," + pManuell);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void log (String pText) {
		try {
			bwl.write(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ": " + pText);
			bwl.newLine();
			bwl.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() throws IOException {
		fw.close();
		bw.close();
	}
}