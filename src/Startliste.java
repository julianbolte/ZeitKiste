import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Startliste extends Zeitkiste{
	
	public Startliste() {
		super.neueStartliste(getStnrListe());
	}
	
	public ArrayList<Integer> getStnrListe(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.178.230/zeitmessung?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "zeitkiste", "");
			String query = "SELECT * FROM startliste ORDER BY rennen_id ASC, startplatz DESC";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	        	list.add(Integer.parseInt(rs.getString("stnr")));
	        }
	        stmt.close();
	        System.out.println("Startliste erfolgreich geladen");
	        return list;
		} catch (SQLException e) {
			System.out.println("Fehler beim Verbindungsaufbau der Datenbank " + e.getMessage());
		}
		return getStnrListeBeiFehler();
	}
	
	public ArrayList<Integer> getStnrListeBeiFehler(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			FileReader fr = new FileReader("startliste.txt");
			BufferedReader br = new BufferedReader(fr);
	    
			String line = br.readLine();
		    for (String l: line.split(",")){
		    	list.add(Integer.parseInt(l));
		    }
				br.close();
				fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

}