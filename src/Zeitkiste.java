import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;

public class Zeitkiste {

	public static Gpio gpio;
	public static Gui gui;
	public static File fileMan;
	public static File fileAuto;
	public static Display display;
	private static Database database;
	private static WebSocketServer webSocketServer;
	private static ConnectionHandler connHandler;
	private static Startliste startliste;
	private static Properties props;
	private static String standort;
	private static LogView logView;
	private static UrlPost urlPost;
	private static DecimalFormat df = new DecimalFormat("000");;
	private static int lauf;
	private static String dbip;
	private int aktuellerIndex = 0;
	private static int startnummer = 1;
	private long letzteAutoZeit;
	private static long letzteManZeit;
	private static boolean lsScharf = false;
	private static boolean manZeitGenommen = false;
	private static boolean autoZeitGenommen = false;
	private static String zeileEins = "-";
	private static String zeileZwei = "-";
	private static String zeileDrei = "-";
	private static String zeileVier = "-";

	public static void main(String[] args) throws IOException {
		logView = new LogView();
		props = new Properties();
		FileInputStream in = new FileInputStream("zeitkiste.ini");
		props.load(in);
		standort = props.getProperty("standort");
		lauf = Integer.parseInt(props.getProperty("lauf"));
		dbip = props.getProperty("dpip");
		gui = new Gui();
		startliste = new Startliste();
		database = new Database(dbip, standort, lauf);
		connHandler = new ConnectionHandler();
		webSocketServer = new WebSocketServer(connHandler, standort.equals("Start") ? 2001 : 2002);
		gpio = new Gpio();
		fileMan = new File(standort + "_" + lauf + "_man");
		fileAuto = new File(standort + "_" + lauf + "_auto");
		urlPost = new UrlPost(standort);
		display = new Display();
		zeileZwei = "Aktuelle Einstllngn:";
		zeileDrei = standort + ", " + lauf + ". Lauf";
		displayAktualisieren();
		ersteZeileAktualisieren("", "");
		logView.write("Zeitkiste " + getStandort() + ", " + getLauf() + ". Lauf ist einsatzbereit!");
	}

	public static void laufAendern(int pLauf) throws IOException {
		FileOutputStream out = new FileOutputStream("standort.ini");
		props.setProperty("lauf", Integer.toString(pLauf));
		props.store(out, null);
		out.close();
		fileMan.close();
		fileAuto.close();
		fileMan = new File(standort + "_" + lauf + "_man");
		fileAuto = new File(standort + "_" + lauf + "_auto");
	}

	public void setLsScharf() {
		if (autoZeitGenommen == false) {
			if (lsScharf == false && manZeitGenommen == false) {
				ersteZeileAktualisieren("       ", "*******");
				lsScharf = true;
			} else if (lsScharf == false && manZeitGenommen == true) {
				ersteZeileAktualisieren(disZeit(letzteManZeit), "*******");
				lsScharf = true;
			}
		}
	}

	public void lsAusgeloest() throws IOException {
		if (lsScharf == true) {
			letzteAutoZeit = System.currentTimeMillis();
			fileAuto.write(startnummer, letzteManZeit);
			lsScharf = false;
			connHandler.sendToAll(startnummer + "/" + Math.round(letzteAutoZeit / 10d));
			autoZeitGenommen = true;
			if (manZeitGenommen == true) {
				ersteZeileAktualisieren(disZeit(letzteManZeit), disZeit(letzteAutoZeit));
			} else if (manZeitGenommen == false) {
				ersteZeileAktualisieren("       ", disZeit(letzteAutoZeit));
			}
			logView.write("     Auto    " + df.format(startnummer) + "   " + letzteAutoZeit);
		} else {
			letzteAutoZeit = System.currentTimeMillis();
			logView.write("!!   Auto    " + df.format(startnummer) + "   " + letzteAutoZeit + "   unerwartet");
		}
	}

	public void manAusgeloest() throws IOException {
		if (manZeitGenommen == false) {
			letzteManZeit = System.currentTimeMillis();
			fileMan.write(startnummer, letzteManZeit);
			manZeitGenommen = true;
			if (autoZeitGenommen == true) {
				ersteZeileAktualisieren(disZeit(letzteManZeit), disZeit(letzteAutoZeit));
			} else if (autoZeitGenommen == false && lsScharf == true) {
				ersteZeileAktualisieren(disZeit(letzteManZeit), "*******");
			} else if (autoZeitGenommen == false && lsScharf == false) {
				ersteZeileAktualisieren(disZeit(letzteManZeit), "");
			}
			logView.write("     Man     " + df.format(startnummer) + "   " + letzteManZeit);
		} else {
			logView.write("!!   Man     " + df.format(startnummer) + "   " + System.currentTimeMillis() + "   unerwartet");
		}
	}

	public void pressedUp() {
		aktuellerIndex++;
		if (aktuellerIndex > startliste.getStnrListe().size() - 1) {
			aktuellerIndex = 0;
		}
		startnummer = startliste.getStnrListe().get(aktuellerIndex);
		lsScharf = false;
		if (manZeitGenommen == true || autoZeitGenommen == true) {
			displayAktualisieren();
		} else {
			ersteZeileAktualisieren("", "");
		}
		if (autoZeitGenommen == false && letzteManZeit != 0) {
			urlPost.send(startnummer,letzteManZeit);
		}
		manZeitGenommen = false;
		autoZeitGenommen = false;
		letzteManZeit = 0;
		letzteAutoZeit = 0;
	}

	public void pressedDown() {
		aktuellerIndex--;
		if (aktuellerIndex < 0) {
			aktuellerIndex = startliste.getStnrListe().size() - 1;
		}
		startnummer = startliste.getStnrListe().get(aktuellerIndex);
		lsScharf = false;
		if (manZeitGenommen == true || autoZeitGenommen == true) {
			displayAktualisieren();
		} else {
			ersteZeileAktualisieren("", "");
		}
		if (autoZeitGenommen == false && letzteManZeit != 0) {
			urlPost.send(startnummer,letzteManZeit);
		}
		manZeitGenommen = false;
		autoZeitGenommen = false;
		letzteManZeit = 0;
		letzteAutoZeit = 0;

	}

	public void zuStnrSpringen(int pStartnummer) {
		if (startliste.getStnrListe().contains(pStartnummer)) {
			aktuellerIndex = startliste.getStnrListe().indexOf(pStartnummer);
			startnummer = pStartnummer;
			if (manZeitGenommen == true || autoZeitGenommen == true) {
				displayAktualisieren();
			} else {
				ersteZeileAktualisieren("", "");
			}
			manZeitGenommen = false;
			autoZeitGenommen = false;
			letzteManZeit = 0;
			letzteAutoZeit = 0;
		} else {
			JOptionPane.showMessageDialog(null, "Startnumer in Startliste nicht gefunden", "Fehler", JOptionPane.WARNING_MESSAGE);
		}
	}

	public static void ersteZeileAktualisieren(String pDisLeft, String pDisRight) {
		zeileEins = df.format(startnummer) + ": " + pDisLeft + " " + pDisRight;
		gui.virtErsteZeileAktualisieren(zeileEins);
		display.phyErsteZeileAktualisieren(zeileEins);
	}

	public static void displayAktualisieren() {
		zeileVier = zeileDrei;
		zeileDrei = zeileZwei;
		zeileZwei = zeileEins;
		ersteZeileAktualisieren("       ", "        ");
		gui.virtDisplayAktualisieren(zeileEins, zeileZwei, zeileDrei, zeileVier);
		display.phyDisplayAktualisieren(zeileEins, zeileZwei, zeileDrei, zeileVier);
	}

	public String disZeit(long pZeit) {
		if (pZeit != 0) {
			String pString = Long.toString(pZeit);
			pString = pString.substring(pString.length() - 7, pString.length());
			return pString;
		} else {
			return "       ";
		}
	}

	public static String getStandort() {
		return standort;
	}

	public static int getLauf() {
		return lauf;
	}

	public void setStandort(String pStandort) throws IOException {
		standort = pStandort;
		FileOutputStream out = new FileOutputStream("zeitkiste.ini");
		props.setProperty("standort", pStandort);
		props.store(out, null);
	}

	public void setLauf(int pLauf) throws IOException {
		lauf = pLauf;
		FileOutputStream out = new FileOutputStream("zeitkiste.ini");
		props.setProperty("lauf", Integer.toString(pLauf));
		props.store(out, null);
	}

	
	public static String aktuelleZeit() {
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
	    return date.format(new Date());
	}
	
	public void writeLog(String pLog) {
		logView.write(pLog);
	}
	
	public void close() throws SQLException, IOException {
		logView.close();
		urlPost.close();
		try {
		database.close();
		webSocketServer.close();
		connHandler.closeConnections();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

}
