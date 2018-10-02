import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class LogView {
	
	private static JFrame frame;
	private static JPanel panel;
	private static JLabel label;
	private static JTextArea textArea;
	private static JScrollPane scroll;
	
	public LogView() {
		frame = new JFrame("Überwachungsfenster Zeitkiste");
		frame.setBounds(100, 100, 950, 400);
		frame.getContentPane().setBackground(new java.awt.Color(89, 98, 117));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		panel = new JPanel();
		panel.setBackground(new java.awt.Color(89, 98, 117));
		label = new JLabel("Letzter aktiver Zugriff:");
		label.setFont(new java.awt.Font("Tahoma", Font.BOLD, 20));
		panel.add(label);
		textArea = new JTextArea(16, 60);
		textArea.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 15));
		textArea.setBorder(new LineBorder(new Color(89,98,117),2));
		textArea.setText(now() + "   ### Logfenster gestartet");
		textArea.setEditable(false);
		scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scroll);
		frame.add(panel);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public String now() {
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
	    return date.format(new Date());
	}
	
	public void write(String pLog) {
		frame.setTitle(now() + " Aktivitätsanzeige Zeitkiste");
		label.setText("Letzter aktiver Zugriff: " + now());
		textArea.setText(now() + "   " + pLog + "\n" + textArea.getText());
	}
	
	public void close() {
		frame.dispose();
	}
	
}