import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Gui extends Zeitkiste implements MouseListener{
	
	private JLabel lblZeileEins;
	private JLabel lblZeileZwei;
	private JLabel lblZeileDrei;
	private JLabel lblZeileVier;
	
	public Gui(){
		JFrame frame = new JFrame("Zeitkiste GUI");
		frame.setBounds(100, 100, 543, 187);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JPanel panel = new JPanel();
		panel.setBounds(20, 0, 475, 122);
		panel.setLayout(null);
		JPanel disPanel = new JPanel();
		disPanel.setBounds(110, 26, 250, 96);
		disPanel.setBorder(new LineBorder(new Color(0,0,0),2));
		disPanel.setLayout(new BoxLayout(disPanel, BoxLayout.Y_AXIS));
		
		JButton btnUp = new JButton("\u2191");
		btnUp.setBackground(Color.RED);
		btnUp.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnUp.setBounds(10, 0, 50, 50);
		btnUp.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				up();
			}
		});
		panel.add(btnUp);
		JButton btnDown = new JButton("\u2193");
		btnDown.setBackground(Color.RED);
		btnDown.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDown.setBounds(10, 72, 50, 50);
		btnDown.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				down();
			}
		});
		panel.add(btnDown);
		JButton btnAuto = new JButton("A");
		btnAuto.setBackground(Color.RED);
		btnAuto.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnAuto.setBounds(415, 0, 50, 50);
		btnAuto.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				auto();
			}
		});
		panel.add(btnAuto);
		JButton btnMan = new JButton("M");
		btnMan.setBackground(Color.GREEN);
		btnMan.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnMan.setBounds(415, 72, 50, 50);
		btnMan.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				man();
			}
		});
		panel.add(btnMan);
		lblZeileEins = new JLabel("Zeile Eins");
		lblZeileEins.setFont(new Font("Monospaced", Font.BOLD, 16));
		lblZeileEins.setHorizontalAlignment(SwingConstants.CENTER);
		disPanel.add(lblZeileEins);
		lblZeileZwei = new JLabel("Zeile Zwei");
		lblZeileZwei.setFont(new Font("Monospaced", Font.BOLD, 16));
		lblZeileZwei.setHorizontalAlignment(SwingConstants.CENTER);
		disPanel.add(lblZeileZwei);
		lblZeileDrei = new JLabel("Zeile Drei");
		lblZeileDrei.setFont(new Font("Monospaced", Font.BOLD, 16));
		lblZeileDrei.setHorizontalAlignment(SwingConstants.CENTER);
		disPanel.add(lblZeileDrei);
		lblZeileVier = new JLabel("Zeile Vier");
		lblZeileVier.setFont(new Font("Monospaced", Font.BOLD, 16));
		lblZeileVier.setHorizontalAlignment(SwingConstants.CENTER);
		disPanel.add(lblZeileVier);
		JLabel virtLED = new JLabel("*");
		virtLED.setForeground(Color.GREEN);
		virtLED.setFont(new Font("Arial Black", Font.BOLD, 24));
		virtLED.setBounds(110, 6, 22, 27);
		panel.add(virtLED);
		
		
		
		
		
		
		panel.add(disPanel);
		frame.add(panel);
		frame.setVisible(true);
		System.out.println("GUI erstellt!");
		
	}
	public void up() {
		super.pressedUp();
	}
	public void down() {
		super.pressedDown();
	}
	public void auto() {
		super.setLsScharf();
	}
	public void man() {
		super.manAusgeloest();
	}
	public void virtDisplayAktualisieren(String pZeileEins, String pZeileZwei, String pZeileDrei, String pZeileVier) {
		lblZeileEins.setText(pZeileEins);
		lblZeileZwei.setText(pZeileZwei);
		lblZeileDrei.setText(pZeileDrei);
		lblZeileVier.setText(pZeileVier);
	}
}