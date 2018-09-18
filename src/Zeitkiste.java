public class Zeitkiste {
	
	static Gpio gpio;
	public static Gui gui;
	private int startnummer = 1;
	private int aktuellerIndex;
	private long letzteAutoZeit;
	private long letzteManZeit;
	private long[] manZeiten;
	private long[] autoZeiten;
	private boolean lsScharf;
	private String zeileEins;
	private String zeileZwei;
	private String zeileDrei;
	private String zeileVier;
	
	public static void main(String[] args) {
		gui = new Gui();
		System.out.println("Zeitkiste durchlaufen!");
	}
	public void setLsScharf() {
		lsScharf = true;
		System.out.println("LS Scharf");
	}
	
	public void lsAusgeloest() {
		letzteAutoZeit = System.currentTimeMillis(); //Zeit sichern
		//Sicherungsdatei
		//Datenbank
		//Live-Timing
		lsScharf = false;
		this.displayAktualisieren();
		System.out.print("Lichtschranke wurde ausgelöst: " + System.currentTimeMillis());
	}
	
	public void manAusgeloest() {
		letzteManZeit = System.currentTimeMillis(); //Zeit sichern
		//Sicherungsdatei
		//Datenbank
		this.displayAktualisieren();
		System.out.println("Manuelle LS wurde ausgelöst: " + System.currentTimeMillis());
	}
	
	public void pressedUp() {
		startnummer++;
		this.displayAktualisieren();
	}
	
	public void pressedDown() {
		if (startnummer > 1) {
		startnummer--;
		this.displayAktualisieren();
		}
	}
	
	public void displayAktualisieren() {
		zeileEins = startnummer + "  " + this.disZeit(System.currentTimeMillis()) + " " + this.disZeit(System.currentTimeMillis());
		zeileZwei = startnummer-1 + "  " + this.disZeit(System.currentTimeMillis()) + " " + this.disZeit(System.currentTimeMillis());
		zeileDrei = startnummer-2 + "  " + this.disZeit(System.currentTimeMillis()) + " " + this.disZeit(System.currentTimeMillis());
		zeileVier = startnummer-3 + "  " + this.disZeit(System.currentTimeMillis()) + " " + this.disZeit(System.currentTimeMillis());
		gui.virtDisplayAktualisieren(zeileEins, zeileZwei, zeileDrei, zeileVier);
	}
	
	public String disZeit(long pZeit) {
		if (pZeit != 0) { //Kuerzt die Zeit runter
			String pString = Long.toString(pZeit);
			pString = pString.substring(pString.length()-7, pString.length());
			return pString;
		} else {
			return "       ";
		}
	}
	

}
