import java.sql.*;
import java.util.Stack;

public class Database {
	
	private static Connection conn;
	private static Statement stmt;
	private static int suc;
	private static Stack<Bib> fehlerDaten = new Stack<>();
	private static String standort;
	private static int lauf;

		
	public Database(String pStandort, int pLauf) {
		standort = pStandort;
		lauf = pLauf;
	}
	
	public static void update(int pStnr, long pMan, long pAuto) throws SQLException {
			try {
				conn = DriverManager.getConnection("jdbc:mysql://192.168.178.230/zeitmessung?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "zeitkiste", "");
				stmt = conn.createStatement();
				if (standort.equals("Start")) {
					if (lauf == 1) {
						suc = stmt.executeUpdate(
	"INSERT INTO `zeiten` (`stnr`, `lauf1_start_auto`, `lauf1_start_man`) VALUES ('"+pStnr+"', '"+pAuto+"', '"+pMan+"') "
			+ "ON DUPLICATE KEY UPDATE lauf1_start_auto='"+pAuto+"',lauf1_start_man='"+pAuto+"'");
					} else if (lauf == 2) {
						suc = stmt.executeUpdate(
	"INSERT INTO `zeiten` (`stnr`, `lauf2_start_auto`, `lauf2_start_man`) VALUES ('"+pStnr+"', '"+pAuto+"', '"+pMan+"') "
			+ "ON DUPLICATE KEY UPDATE lauf2_start_auto='"+pAuto+"',lauf2_start_man='"+pAuto+"'");
					}
				} else if (standort.equals("Ziel")) {
					if (lauf == 1) {
						suc = stmt.executeUpdate(
	"INSERT INTO `zeiten` (`stnr`, `lauf1_ziel_auto`, `lauf1_ziel_man`) VALUES ('"+pStnr+"', '"+pAuto+"', '"+pMan+"') "
			+ "ON DUPLICATE KEY UPDATE lauf1_ziel_auto='"+pAuto+"',lauf1_ziel_man='"+pAuto+"'");				
					} else if (lauf == 2) {
						suc = stmt.executeUpdate(
	"INSERT INTO `zeiten` (`stnr`, `lauf2_ziel_auto`, `lauf2_ziel_man`) VALUES ('"+pStnr+"', '"+pAuto+"', '"+pMan+"') "
			+ "ON DUPLICATE KEY UPDATE lauf2_ziel_auto='"+pAuto+"',lauf2_ziel_man='"+pAuto+"'");
					}
				} else {
					System.out.println("Standort konnte der Datenbank nicht zugeordnet werden");
				}
				conn.close();
			} catch (SQLException e){
				e.printStackTrace();
				suc = 0;
				System.out.println(e.getMessage());
			}
		if (suc > 0) {
			System.out.println("Übertragung erfolgreich: Startnummer " + pStnr);
			if (!fehlerDaten.isEmpty() && fehlerDaten.peek().getStnr() == pStnr) {
				fehlerDaten.pop();
				System.out.println("Updateversuch erfolgreich");
			}
			datenbankAktualisieren();
		} else {
			datenbankFehler(pStnr, pMan, pAuto);
			System.out.println("Übertragung gescheitert");
		}
	}
	
	private static void datenbankAktualisieren() throws SQLException {
		if (!fehlerDaten.isEmpty()) {
			update(fehlerDaten.peek().getStnr(), fehlerDaten.peek().getAuto(), fehlerDaten.peek().getMan());
			System.out.println("Updateversuch gestartet");
		}
	}

	private static void datenbankFehler(int pStnr, long pMan, long pAuto) {
		fehlerDaten.push(new Bib(pStnr, pMan, pAuto));
	}

}
