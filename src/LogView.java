import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class LogView {
	
	private static JFrame frame;
	private static JPanel panel;
	private static JLabel label;
	private static JTextArea textArea;
	private static JScrollPane scroll;
	
	public LogView() {
		frame = new JFrame("Überwachungsfenster Zeitkiste");
		frame.setBounds(100, 100, 643, 290);
		frame.getContentPane().setBackground(new java.awt.Color(89, 98, 117));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		panel = new JPanel();
		panel.setBackground(new java.awt.Color(89, 98, 117));
		label = new JLabel("Letzter aktiver Zugriff:");
		label.setFont(new java.awt.Font("Helvetica", Font.BOLD, 22));
		label.setForeground(java.awt.Color.WHITE);
		panel.add(label);
		textArea = new JTextArea(10, 45);
		textArea.setFont(new java.awt.Font("Helvetica", Font.PLAIN, 15));
		textArea.setBackground(new Color(89,98,117));
		textArea.setForeground(java.awt.Color.WHITE);
		textArea.setText(aktuelleUhrzeit() + " Logfenster gestartet");
		textArea.setEditable(false);
		scroll = new JScrollPane(textArea);
		scroll.setBorder(null);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scroll);
		frame.add(panel);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public void close() throws IOException {
		FileWriter fw = new FileWriter("logfile.txt",true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.newLine();
		bw.write("=========== " + aktuellesDatum() + "===========");
		bw.newLine();
		bw.write(textArea.getText());
		bw.flush();
		bw.close();
		frame.dispose();
	}

	public void write(String pLog) {
		frame.setTitle(aktuelleUhrzeit() + " Log Zeitkiste");
		label.setText("Letzter aktiver Zugriff: " + aktuelleUhrzeit());
		textArea.setText(aktuelleUhrzeit() + "   " + pLog + "\n" + textArea.getText());
	}
	
	public static String aktuelleUhrzeit() {
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
	    return date.format(new Date());
	}
	
	public static String aktuellesDatum() {
		SimpleDateFormat date = new SimpleDateFormat("d.M.y");
	    return date.format(new Date());
	}
}