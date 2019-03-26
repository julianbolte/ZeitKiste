import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;

public class Zeitkiste {

	public static Gpio gpio;
	public static Gui gui;
	public static Display display;
	public static Database database;
	public static Backup backup;
	private static WebSocketServer webSocketServer;
	private static ConnectionHandler connHandler;
	private static ArrayList<Integer> startliste;
	private static Properties props;
	private static String standort;
	private static Bondrucker bondrucker;
	private static DecimalFormat df = new DecimalFormat("000");;
	private static int lauf;
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
		props = new Properties();
		FileInputStream in = new FileInputStream("zeitkiste.ini");
		props.load(in);
		standort = props.getProperty("standort");
		lauf = Integer.parseInt(props.getProperty("lauf"));
		backup = new Backup();
		new Startliste();
		connHandler = new ConnectionHandler();
		webSocketServer = new WebSocketServer(connHandler, standort.equals("Start") ? 2001 : 2002);
		gpio = new Gpio();
		display = new Display();
		zeileZwei = "Aktuelle Einstllngn:";
		zeileDrei = standort + ", " + lauf + ". Lauf";
		bondrucker = new Bondrucker(standort, lauf);
		database = new Database(standort, lauf);
		gui = new Gui();
		ersteZeileAktualisieren("START", "BEREIT");
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

	public void lsAusgeloest() throws IOException, SQLException {
		if (lsScharf == true) {
			letzteAutoZeit = System.currentTimeMillis();
			backup.log("Erwartete Automatische Zeit: " + startnummer + ", " + letzteAutoZeit);
			lsScharf = false;
			connHandler.sendToAll(startnummer + "/" + Math.round(letzteAutoZeit / 10d));
			autoZeitGenommen = true;
			if (manZeitGenommen == true) {
				ersteZeileAktualisieren(disZeit(letzteManZeit), disZeit(letzteAutoZeit));
				pressedUp();
			} else if (manZeitGenommen == false) {
				ersteZeileAktualisieren("       ", disZeit(letzteAutoZeit));
			}
		} else {
			letzteAutoZeit = System.currentTimeMillis();
			backup.log("Unerwartete Automatische Zeit: " + startnummer + ", " + letzteAutoZeit);
		}
	}

	public void manAusgeloest() throws IOException, SQLException {
		if (manZeitGenommen == false) {
			letzteManZeit = System.currentTimeMillis();
			backup.log("Erwartete Manuelle Zeit: " + startnummer + ", " + letzteManZeit);
			manZeitGenommen = true;
			if (autoZeitGenommen == true) {
				ersteZeileAktualisieren(disZeit(letzteManZeit), disZeit(letzteAutoZeit));
				pressedUp();
			} else if (autoZeitGenommen == false && lsScharf == true) {
				ersteZeileAktualisieren(disZeit(letzteManZeit), "*******");
			} else if (autoZeitGenommen == false && lsScharf == false) {
				ersteZeileAktualisieren(disZeit(letzteManZeit), "");
			}
		} else {
			backup.log("Zweite Manuelle Zeit: " + startnummer + ", " + System.currentTimeMillis());
		}
	}

	public void pressedUp() throws SQLException {
		if (zeileZwei == "Aktuelle Einstllngn:") {
			zeileEins = "X";
			zeileZwei = "X";
			zeileDrei = "X";
			zeileVier = "X";
			displayAktualisieren();
			aktuellerIndex = -1;
		}
		if (autoZeitGenommen == true || manZeitGenommen == true) {
			backup.save(startnummer, letzteAutoZeit, letzteManZeit);
			bondrucker.drucken(startnummer, letzteManZeit, letzteAutoZeit);
			Database.update(startnummer, letzteManZeit, letzteAutoZeit);
		}
		aktuellerIndex++;
		if (aktuellerIndex > startliste.size() - 1) {
			aktuellerIndex = 0;
		}
		startnummer = startliste.get(aktuellerIndex);
		lsScharf = false;
		if (manZeitGenommen == true || autoZeitGenommen == true) {
			displayAktualisieren();
		} else {
			ersteZeileAktualisieren("", "");
		}		
		manZeitGenommen = false;
		autoZeitGenommen = false;
		letzteManZeit = 0;
		letzteAutoZeit = 0;
	}

	public void pressedDown() throws SQLException {
		if (autoZeitGenommen == true || manZeitGenommen == true) {
			backup.save(startnummer, letzteAutoZeit, letzteManZeit);
			bondrucker.drucken(startnummer, letzteManZeit, letzteAutoZeit);
			Database.update(startnummer, letzteManZeit, letzteAutoZeit);
		}
		aktuellerIndex--;
		if (aktuellerIndex < 0) {
			aktuellerIndex = startliste.size() - 1;
		}
		startnummer = startliste.get(aktuellerIndex);
		lsScharf = false;
		if (manZeitGenommen == true || autoZeitGenommen == true) {
			displayAktualisieren();
		} else {
			ersteZeileAktualisieren("", "");
		}
		manZeitGenommen = false;
		autoZeitGenommen = false;
		letzteManZeit = 0;
		letzteAutoZeit = 0;

	}

	public void zuStnrSpringen(int pStartnummer) {
		if (startliste.contains(pStartnummer)) {
			aktuellerIndex = startliste.indexOf(pStartnummer);
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
	
	public static String aktuelleZeit() {
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
	    return date.format(new Date());
	}
	
	public void neueStartliste(ArrayList<Integer> pStartliste) {
		startliste = pStartliste;
		aktuellerIndex = 0;
		startnummer = startliste.get(aktuellerIndex);
		manZeitGenommen = false;
		autoZeitGenommen = false;
		letzteManZeit = 0;
		letzteAutoZeit = 0;
	}
	
	public void neueStartlisteLaden() {
		new Startliste();
	}
	
	public void close() throws SQLException, IOException {
		try {
			webSocketServer.close();
			connHandler.closeConnections();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

}
