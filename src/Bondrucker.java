import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Bondrucker {
	
	private static String standort;
	private static int lauf;
	
	public Bondrucker(String pStandort, int pLauf) {
		standort = pStandort;
		lauf = pLauf;
		try {
		    URL url = new URL("http://192.168.178.230/wdm-zeitmessung/bondrucker.php");
		    URLConnection con = url.openConnection();
		    con.setDoOutput(true);
		    PrintStream ps = new PrintStream(con.getOutputStream());
		    ps.print("login=" + standort);
		    ps.print("&login2=" + lauf);
		    con.getInputStream();
		    ps.close();
		    } catch (MalformedURLException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}
	
	public void drucken(int pStnr, long pMan, long pAuto) {
		try {
		    URL url = new URL("http://192.168.178.230/wdm-zeitmessung/wdm-zeitkiste.php");
		    URLConnection con = url.openConnection();
		    con.setDoOutput(true);
		    PrintStream ps = new PrintStream(con.getOutputStream());
		    ps.print("standort=" + standort);
		    ps.print("&stnr=" + pStnr);
		    ps.print("&man=" + pMan);
		    ps.print("&auto=" + pAuto);
		    con.getInputStream();
		    ps.close();
		    } catch (MalformedURLException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}
}
