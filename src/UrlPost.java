import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UrlPost {
	
	private URL url;
	private URLConnection con;
	private PrintStream ps;

	public UrlPost(String pStandort) {
		try {
		url = new URL("http://ergebniszwei/zeitkiste" + pStandort + ".php");
		con = url.openConnection();
	    con.setDoOutput(true);
		ps = new PrintStream(con.getOutputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(int pStartnummer, long pZeit) {
		ps.print("stnr=" + pStartnummer);
	    ps.print("&time=" + pZeit);
	}
	
	public void close() {
		ps.close();
	}

}
