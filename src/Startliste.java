import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Startliste {
	
	public ArrayList<Integer> getStnrListe(){
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