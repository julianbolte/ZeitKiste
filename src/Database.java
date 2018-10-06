import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Database extends Zeitkiste{

	private static Connection conn;
	private static Statement stmt;
	@SuppressWarnings("unused")
	private static int suc;
	private static long[] lostInetMan = new long[500];
	private static long[] lostInetAuto = new long[500];
	
	public Database(String pIP) {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + pIP + "/zeitmessung?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			stmt = conn.createStatement();
		} catch (SQLException e) {
			super.writeLog("Fehler beim Verbindungsaufbau der Datenbank " + e.getMessage());
		}
	}
	
	public static void updateDatabase(String pStandort, int pLauf, int pStnr, long pMan, long pAuto) {
		try {
			if (pStandort == "Start") {
				if (pLauf == 1) {
					suc = stmt.executeUpdate(
"INSERT INTO `zeiten` (`stnr`, `lauf1_start_auto`, `lauf1_start_man`) VALUES ('"+pStnr+"', '"+pAuto+"', '"+pMan+"') "
		+ "ON DUPLICATE KEY UPDATE lauf1_start_auto='"+pAuto+"',lauf1_start_man='"+pAuto+"'");
				} else if (pLauf == 2) {
					suc = stmt.executeUpdate(
"INSERT INTO `zeiten` (`stnr`, `lauf2_start_auto`, `lauf2_start_man`) VALUES ('"+pStnr+"', '"+pAuto+"', '"+pMan+"') "
		+ "ON DUPLICATE KEY UPDATE lauf2_start_auto='"+pAuto+"',lauf2_start_man='"+pAuto+"'");
				}
			} else if (pStandort == "Ziel") {
				if (pLauf == 1) {
					suc = stmt.executeUpdate(
"INSERT INTO `zeiten` (`stnr`, `lauf1_ziel_auto`, `lauf1_ziel_man`) VALUES ('"+pStnr+"', '"+pAuto+"', '"+pMan+"') "
		+ "ON DUPLICATE KEY UPDATE lauf1_ziel_auto='"+pAuto+"',lauf1_ziel_man='"+pAuto+"'");				
				} else if (pLauf == 2) {
					suc = stmt.executeUpdate(
"INSERT INTO `zeiten` (`stnr`, `lauf2_ziel_auto`, `lauf2_ziel_man`) VALUES ('"+pStnr+"', '"+pAuto+"', '"+pMan+"') "
		+ "ON DUPLICATE KEY UPDATE lauf2_ziel_auto='"+pAuto+"',lauf2_ziel_man='"+pAuto+"'");
				}
			} else {
				System.out.println("Standort konnte der Datenbank nicht zugeordnet werden");
			}
		} catch (SQLException e){
			lostInetMan[pStnr] = pMan;
			lostInetAuto[pStnr] = pAuto;
			System.out.println(aktuelleUhrzeit() + " DB-F : " + pStnr + " " + lostInetMan[pStnr] + " " + lostInetMan[pStnr]);
			e.printStackTrace();
		}
	}
	
	public void close() throws SQLException {
		try {
		conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String aktuelleUhrzeit() {
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
	    String uhrzeit = date.format(new Date());
	    return uhrzeit;
	}
}
