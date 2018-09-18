import java.io.BufferedWriter;
import java.io.FileWriter;

public class file {
	FileWriter fw;
    BufferedWriter bw;
    
    public file() {
    	fw = new FileWriter("ausgabe.txt");
        bw = new BufferedWriter(fw);

        bw.write("test test test");
        bw.newLine();
        bw.write("tset tset tset");

        bw.close();
    }
}
