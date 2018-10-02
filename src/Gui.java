import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Gui extends Zeitkiste implements MouseListener{
	
	private JFrame frame;
	private JLabel lblZeileEins;
	private JLabel lblZeileZwei;
	private JLabel lblZeileDrei;
	private JLabel lblZeileVier;
	@SuppressWarnings("unused") //Java ist dumm
	private PopUpFenster popup;
	private JTextArea textArea;
	private JScrollPane scroll;

	
	public Gui(){
		frame = new JFrame("Zeitkiste " + super.getStandort() + ", " + super.getLauf() + ". Lauf ");
		frame.getContentPane().setBackground(new java.awt.Color(89, 98, 117));
		try {
			frame.setTitle(frame.getTitle() + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setBounds(100, 100, 543, 190);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JPanel panel = new JPanel();
		panel.setBackground(new java.awt.Color(89, 98, 117));
		panel.setBounds(20, 0, 475, 122);
		panel.setLayout(null);
		JPanel disPanel = new JPanel();
		disPanel.setBackground(new java.awt.Color(196, 229, 56));
		disPanel.setBounds(110, 26, 250, 96);
		disPanel.setBorder(new LineBorder(new Color(0,0,0),2));
		disPanel.setLayout(new BoxLayout(disPanel, BoxLayout.Y_AXIS));

		JButton btnUp = new JButton("\u2191");
		btnUp.setBackground(Color.RED);
		btnUp.setBorder(new LineBorder(new Color(0,0,0),2));
		btnUp.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnUp.setBounds(10, 5, 50, 50);
		btnUp.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				up();
			}
		});
		panel.add(btnUp);
		JButton btnDown = new JButton("\u2193");
		btnDown.setBackground(Color.RED);
		btnDown.setBorder(new LineBorder(new Color(0,0,0),2));
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
		btnAuto.setBorder(new LineBorder(new Color(0,0,0),2));
		btnAuto.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnAuto.setBounds(415, 5, 50, 50);
		btnAuto.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				auto();
			}
		});
		panel.add(btnAuto);
		JButton btnMan = new JButton("M");
		btnMan.setBackground(Color.GREEN);
		btnMan.setBorder(new LineBorder(new Color(0,0,0),2));
		btnMan.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnMan.setBounds(415, 72, 50, 50);
		btnMan.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				try {
					man();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel.add(btnMan);
		lblZeileEins = new JLabel("Zeitkiste ver_0.1");
		lblZeileEins.setFont(new Font("DialogInput", Font.BOLD, 16));
		lblZeileEins.setHorizontalAlignment(SwingConstants.CENTER);
		disPanel.add(lblZeileEins);
		lblZeileZwei = new JLabel("Unkorrekte Angaben");
		lblZeileZwei.setFont(new Font("DialogInput", Font.BOLD, 16));
		lblZeileZwei.setHorizontalAlignment(SwingConstants.CENTER);
		disPanel.add(lblZeileZwei);
		lblZeileDrei = new JLabel("bitte sofort melden");
		lblZeileDrei.setFont(new Font("DialogInput", Font.BOLD, 16));
		lblZeileDrei.setHorizontalAlignment(SwingConstants.CENTER);
		disPanel.add(lblZeileDrei);
		lblZeileVier = new JLabel("Modus: " + super.getStandort() + ", " + super.getLauf() + ".Lauf");
		lblZeileVier.setFont(new Font("DialogInput", Font.BOLD, 16));
		lblZeileVier.setHorizontalAlignment(SwingConstants.CENTER);
		disPanel.add(lblZeileVier);
		JLabel virtLED = new JLabel("*");
		virtLED.setForeground(Color.GREEN);
		virtLED.setFont(new Font("DialogInput", Font.BOLD, 24));
		virtLED.setBounds(110, 6, 22, 27);
		panel.add(virtLED);
		JMenuBar menuBar= new JMenuBar();
		menuBar.setBackground(new java.awt.Color(89,98,117));
		menuBar.setBorder(new LineBorder(new java.awt.Color(89,98,117),2));
		frame.setJMenuBar(menuBar);
		JMenu funktionen = new JMenu("Funktionen");
		funktionen.setForeground(java.awt.Color.WHITE);
		menuBar.add(funktionen);
		JMenuItem stnrWechsel = new JMenuItem("Startnummer springen");
		stnrWechsel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				popup = new PopUpFenster("ZUSTNRSPRINGEN");
			}
		});
		funktionen.add(stnrWechsel);
		JMenuItem warnungAusgeben = new JMenuItem("Warnung ausgeben");
		warnungAusgeben.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				popup = new PopUpFenster("WARNUNGAUSGEBEN");
			}
		});
		funktionen.add(warnungAusgeben);
		JMenu liveUhr = new JMenu("LIVE Uhr");
		liveUhr.setForeground(java.awt.Color.WHITE);
		menuBar.add(liveUhr);
		JMenuItem liveAktivieren = new JMenuItem("Aktivieren / Deaktivieren");
		liveAktivieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Funktion noch nicht implememntiert");
			}
		});
		liveUhr.add(liveAktivieren);
		JMenuItem startenStoppen = new JMenuItem("Start- / Stopimpuls geben");
		startenStoppen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Funktion noch nicht implememntiert");
			}
		});
		liveUhr.add(startenStoppen);
		JMenu einstellungen = new JMenu("Einstellungen");
		einstellungen.setForeground(java.awt.Color.WHITE);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(einstellungen);
		JMenuItem startliste = new JMenuItem("Neue Startliste laden");
		startliste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("startliste neu");
			}
		});
		einstellungen.add(startliste);
		JMenuItem standort = new JMenuItem("Standort ändern");
		standort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					eA("standort");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		einstellungen.add(standort);
		JMenuItem lauf = new JMenuItem("Lauf ändern");
		lauf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					eA("lauf");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		einstellungen.add(lauf);
		panel.add(disPanel);
		frame.add(panel);
		frame.setVisible(true);
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
	public void man() throws IOException {
		super.manAusgeloest();
	}
	public void virtErsteZeileAktualisieren(String pZeileEins) {
		lblZeileEins.setText(pZeileEins);
	}
	public void virtDisplayAktualisieren(String pZeileEins, String pZeileZwei, String pZeileDrei, String pZeileVier) {
		lblZeileEins.setText(pZeileEins);
		lblZeileZwei.setText(pZeileZwei);
		lblZeileDrei.setText(pZeileDrei);
		lblZeileVier.setText(pZeileVier);
	}
	public void eA(String pAenderung) throws IOException {
		System.out.println(super.getStandort());
		System.out.println(super.getLauf());
		if (pAenderung == "standort") {
			if (super.getStandort() == "Start") {
				super.setStandort("Ziel");
			} else {
				super.setStandort("Start");
			}
			frame.setTitle("Zeitkiste " + super.getStandort() + ", " + super.getLauf() + ". Lauf");
		} else if (pAenderung == "lauf") {
			if (super.getLauf() == 1) {
				super.setLauf(2);
			} else {
				super.setLauf(1);
			}
			frame.setTitle("Zeitkiste " + super.getStandort() + ", " + super.getLauf() + ". Lauf");
		} else {
			System.out.println("Error Ocurred: Unbekannte Änderung erwünscht!"); //Fehlermeldung ausgeben
		}
		System.out.println("Einstellungen wurden geändert: " + super.getStandort() + ", " + super.getLauf() + ". Lauf");
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}