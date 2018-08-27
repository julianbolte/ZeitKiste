import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TimeCatcherFrame {

	public JFrame frame;
	private int aktuelleStnr;
	private int aktuellerIndex;
	private Zeit letzteZeitA;
	private Zeit letzteZeitM;
	private JLabel lblZ1; //Label für die erste Displayzeile
	private JLabel lblZ2;  //Label für die zweite Displayzeile
	private JLabel lblZ3; //Label für die dritte Displayzeile
	private JLabel lblZ4; //Label für die vierte Displayzeile
	private JLabel virtLED;
	private boolean aAusgeloest; //Auto-Zeit gesetzt?
	private boolean mAusgeloest; //Manu-Zeit gesetzt?
	private boolean lichtschrankeScharf; //Lichtschranke scharf?
	private Stnr stnr;
	private ServerCOM server;
	private String name; //Start oder Ziel
	private GPIO_COM gpio;
	private TcDisplay display;
	private Zeit zeit;
	private SprintUhrClient sprintUhrClient;
	private boolean sprint;
	/**
	 * Create the application.
	 */
	public TimeCatcherFrame() {
		initialize();
	}

	/*Funktionalitäten für den DOWN-Knopf.*/
	public void down(){
		letzteZeitSpeichern();
		aktuellerIndex--;
		if (aktuellerIndex<0){
			aktuellerIndex=stnr.getStnrListe().size()-1;
		}
		aktuelleStnr = stnr.getStnrListe().get(aktuellerIndex);
		mAusgeloest = false;
		aAusgeloest = false;
		letzteZeitA = null;
		letzteZeitM = null;
		lichtschrankeScharf = false;
		aktualisiereLabel1();
	}
	/*Funktionalitäten für den UP-Knopf.*/
	public void up(){
		letzteZeitSpeichern();
		aktuellerIndex++;
		if (aktuellerIndex>stnr.getStnrListe().size()-1){
			aktuellerIndex=0;
		}
		aktuelleStnr = stnr.getStnrListe().get(aktuellerIndex);
		mAusgeloest = false;
		aAusgeloest = false;
		letzteZeitA = null;
		letzteZeitM = null;
		lichtschrankeScharf = false;
		aktualisiereLabel1();
		
	}
	/*Funktionalitäten für den AOK-Knopf*/
	public void aok(){
		if (!aAusgeloest){
			aAusgeloest = true;
			lichtschrankeScharf = true;
			if (letzteZeitM != null){
				lblZ1.setText(aktuelleStnr + ": ******* " + letzteZeitM.getDisplayZeit());
				display.ersteZeileSchreiben(aktuelleStnr + ": ******* " + letzteZeitM.getDisplayZeit());
			}
			else{
				lblZ1.setText(aktuelleStnr + ": ******* " + "        ");
				display.ersteZeileSchreiben(aktuelleStnr + ": ******* " + "        ");
			}
			System.out.println("Lichtschranke scharf für Startnummer " + aktuelleStnr);
		}
		
	}
	/*Funktionalitäten für den M-Knopf*/
	public void m(){
		if (!mAusgeloest){
			zeit = new Zeit();
			zeit = zeit.getSysTime();
			letzteZeitM = zeit;
			if (letzteZeitA != null){
				lblZ1.setText(aktuelleStnr + ": " + letzteZeitA.getDisplayZeit() + " " + letzteZeitM.getDisplayZeit());
				display.ersteZeileSchreiben(aktuelleStnr + ": " + letzteZeitA.getDisplayZeit() + " " + letzteZeitM.getDisplayZeit());
			}
			else{
				if (lichtschrankeScharf){
					lblZ1.setText(aktuelleStnr + ": " + "*******" + " " + letzteZeitM.getDisplayZeit());
					display.ersteZeileSchreiben(aktuelleStnr + ": " + "*******" + " " + letzteZeitM.getDisplayZeit());
				}
				else{
					lblZ1.setText(aktuelleStnr + ": " + "       " + " " + letzteZeitM.getDisplayZeit());
					display.ersteZeileSchreiben(aktuelleStnr + ": " + "       " + " " + letzteZeitM.getDisplayZeit());
				}
			}
			mAusgeloest = true;
			System.out.println("Manuelle Zeit am " + name + " aufgenommen für Startnummer " + aktuelleStnr + ": " + letzteZeitM.toString());
		}
	}
	/*Funktionalitäten wenn die Lichtschranke ausgelöst wird*/
	public void lichtschranke(){
		if (lichtschrankeScharf){
			zeit = new Zeit();
			zeit = zeit.getSysTime();
			if (sprint){
				Thread t1 = new Thread(new Runnable(){
					@Override
					public void run(){
						if (name.equalsIgnoreCase("Start"))
							sprintUhrClient.start(zeit.getMillis());
						else
							sprintUhrClient.stop(zeit.getMillis());
					}
				});
				t1.start();
			}
			letzteZeitA = zeit;
			if (letzteZeitM != null){
				lblZ1.setText(aktuelleStnr + ": " + letzteZeitA.getDisplayZeit() + " " + letzteZeitM.getDisplayZeit());
				display.ersteZeileSchreiben(aktuelleStnr + ": " + letzteZeitA.getDisplayZeit() + " " + letzteZeitM.getDisplayZeit());
			}
			else{
				lblZ1.setText(aktuelleStnr + ": " + letzteZeitA.getDisplayZeit() + "        ");
				display.ersteZeileSchreiben(aktuelleStnr + ": " + letzteZeitA.getDisplayZeit() + "        ");
			}
			lichtschrankeScharf = false;
			System.out.println("Lichtschranke ausgelöst am " + name + " für Startnummer " + aktuelleStnr + ": " + letzteZeitA.toString());
		}
	}
	
	public void aktualisiereLabel1 (){
		lblZ1.setText(aktuelleStnr + ": ");
		display.ersteZeileSchreiben(aktuelleStnr + ": ");
	}
	
	public void letzteZeitSpeichern(){
		FileWriter fw;
		BufferedWriter bw;
		try {
			fw = new FileWriter(name.toLowerCase() + ".csv",true);
			bw = new BufferedWriter(fw);

			if (letzteZeitA!=null|letzteZeitM!=null){
				if (letzteZeitA==null)
					letzteZeitA = new Zeit();
				if (letzteZeitM==null)
					letzteZeitM = new Zeit();
				
				bw.write(aktuelleStnr + ";" + letzteZeitA.getMillis() + ";" + letzteZeitM.getMillis());
				display.neueZeileInStack(aktuelleStnr + ": " + letzteZeitA.getDisplayZeit() + " " + letzteZeitM.getDisplayZeit());
				lblZ4.setText(lblZ3.getText());
				lblZ3.setText(lblZ2.getText());
				lblZ2.setText(aktuelleStnr + ": " + letzteZeitA.getDisplayZeit() + " " + letzteZeitM.getDisplayZeit());
				bw.newLine();
			}
			bw.flush();
			bw.close();
			server.uploadZeiten(name.toLowerCase() + ".csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
	}
	
	public void nameInIniSchreiben(){
		FileWriter fw;
		BufferedWriter bw;
		try {
			fw = new FileWriter("timecatcher.ini",false);
			bw = new BufferedWriter(fw);
			bw.write(name);
			bw.flush();
			bw.close();
			fw = new FileWriter("sprint.ini",false);
			bw = new BufferedWriter(fw);
			if (sprint)
				bw.write("sprint=1");
			else
				bw.write("sprint=0");
			bw.flush();
			bw.close();
		} catch (IOException es) {
			// TODO Auto-generated catch block
			es.printStackTrace();
		  }
		try {
			frame.setTitle("TimeCatcher: " + name + " " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stnrEinfuegen(int stnr){
		letzteZeitSpeichern();
		
		aktuelleStnr = stnr;
		mAusgeloest = false;
		aAusgeloest = false;
		letzteZeitA = null;
		letzteZeitM = null;
		aktualisiereLabel1();
		lblZ2.setText("");
		lblZ3.setText("");
	}
	
	public void zuStnrSpringen(int stnr){
		if (this.stnr.getStnrListe().contains(stnr)){
			letzteZeitSpeichern();
			aktuellerIndex = this.stnr.getStnrListe().indexOf(stnr);
			aktuelleStnr = stnr;
			mAusgeloest = false;
			aAusgeloest = false;
			letzteZeitA = null;
			letzteZeitM = null;
			aktualisiereLabel1();
			lblZ2.setText("");
			lblZ3.setText("");
		}
		else
			JOptionPane.showMessageDialog(null,"Startnumer in Startliste nicht gefunden","Fehler",JOptionPane.WARNING_MESSAGE);
	}
	
	public void setVirtLED(boolean anAus){
		if (anAus)
			virtLED.setForeground(Color.GREEN);
		else
			virtLED.setForeground(Color.BLACK);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		server = new ServerCOM();
		gpio = new GPIO_COM(this);
		server.downloadStartliste();
		display = new TcDisplay();
		stnr = new Stnr();
		aktuellerIndex = 0;
		aktuelleStnr = stnr.getStnrListe().get(aktuellerIndex);
		letzteZeitM = null;
		letzteZeitA = null;
		display.ersteZeileSchreiben(aktuelleStnr + ": ");
		
		if (sprint){
			Thread t1 = new Thread(new Runnable(){
				@Override
				public void run(){
					if (name.equalsIgnoreCase("Start"))
						sprintUhrClient = new SprintUhrClient("192.168.178.234",8082);
					else
						sprintUhrClient = new SprintUhrClient("192.168.178.234",9082);
				}
			});
			t1.start();
		}
		
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				if (sprint)
					sprintUhrClient.beenden();
			}
		});
		
		//timecatcher.ini auslesen und name setzen
		FileReader fr;
		BufferedReader br;
		try {
			fr = new FileReader("timecatcher.ini");
			br = new BufferedReader(fr);
			name = br.readLine();
		} catch (FileNotFoundException e) {
			name = "Ziel";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			fr = new FileReader("sprint.ini");
			br = new BufferedReader(fr);
			if (br.readLine()=="sprint=1")
				sprint = true;
			else
				sprint = false;
		} catch (FileNotFoundException e) {
			sprint = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		nameInIniSchreiben();
		
		frame.setBounds(100, 100, 543, 187);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(20, 0, 475, 122);
		frame.getContentPane().add(panel_1);
		
		JButton btnUp = new JButton("\u2191");
		btnUp.setBackground(Color.RED);
		btnUp.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnUp.setBounds(10, 0, 50, 50);
		btnUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				up();
			}
		});
		panel_1.setLayout(null);
		panel_1.add(btnUp);
		
		JButton btnDown = new JButton("\u2193");
		btnDown.setBackground(Color.RED);
		btnDown.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnDown.setBounds(10, 72, 50, 50);
		btnDown.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				down();
			}
		});
		panel_1.add(btnDown);
		
		JButton btnAok = new JButton("A");
		btnAok.setBackground(Color.GREEN);
		btnAok.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnAok.setBounds(415, 72, 50, 50);
		panel_1.add(btnAok);
		
		JButton btnM = new JButton("M");
		btnM.setBackground(Color.RED);
		btnM.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnM.setBounds(415, 0, 50, 50);
		panel_1.add(btnM);
		
		JPanel panel = new JPanel();
		panel.setBounds(110, 26, 250, 96);
		panel_1.add(panel);
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		lblZ1 = new JLabel(aktuelleStnr + ": ");
		lblZ1.setFont(new Font("Monospaced", Font.BOLD, 16));
		lblZ1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblZ1);
		
		lblZ2 = new JLabel();
		lblZ2.setFont(new Font("Monospaced", Font.PLAIN, 11));
		lblZ2.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblZ2);
		
		lblZ3 = new JLabel();
		lblZ3.setHorizontalAlignment(SwingConstants.CENTER);
		lblZ3.setFont(new Font("Monospaced", Font.PLAIN, 11));
		panel.add(lblZ3);
		
		lblZ4 = new JLabel();
		lblZ4.setHorizontalAlignment(SwingConstants.CENTER);
		lblZ4.setFont(new Font("Monospaced", Font.PLAIN, 11));
		panel.add(lblZ4);
		
		virtLED = new JLabel("*");
		virtLED.setForeground(Color.GREEN);
		virtLED.setFont(new Font("Arial Black", Font.BOLD, 24));
		virtLED.setBounds(110, 6, 22, 27);
		panel_1.add(virtLED);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnEinstellungen = new JMenu("Einstellungen");
		menuBar.add(mnEinstellungen);
		JMenuItem mntmAlsStartzielFestlegen = new JMenuItem();
		if (name.equalsIgnoreCase("Ziel")){
			mntmAlsStartzielFestlegen.setText("Als Start festlegen");
		}
		else{
			mntmAlsStartzielFestlegen.setText("Als Ziel festlegen");
		}
		
		//display.ersteZeileSchreiben(aktuelleStnr + "");
		
		mntmAlsStartzielFestlegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (name.equalsIgnoreCase("Ziel")){
					name = "Start";
					mntmAlsStartzielFestlegen.setText("Als Ziel festlegen");
				}
				else{
					name = "Ziel";
					mntmAlsStartzielFestlegen.setText("Als Start festlegen");
				}
				nameInIniSchreiben();
			}
		});
		
		JMenuItem mntmSprintAnAus = new JMenuItem();
		if (sprint){
			mntmSprintAnAus.setText("Sprint Aus");
		}
		else{
			mntmSprintAnAus.setText("Sprint An");
		}
		
		mntmSprintAnAus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (sprint){
					sprint = false;
					mntmSprintAnAus.setText("Sprint An");
					sprintUhrClient.beenden();
				}
				else{
					sprint = true;
					mntmSprintAnAus.setText("Sprint Aus");
					Thread t1 = new Thread(new Runnable(){
						@Override
						public void run(){
							if (name.equalsIgnoreCase("Start"))
								sprintUhrClient = new SprintUhrClient("192.168.178.234",8082);
							else
								sprintUhrClient = new SprintUhrClient("192.168.178.234",9082);
						}
					});
					t1.start();
				}
				nameInIniSchreiben();
			}
		});
		
		mnEinstellungen.add(mntmSprintAnAus);
		
		mnEinstellungen.add(mntmAlsStartzielFestlegen);
		
		JMenuItem mntmZielzeitenLschen = new JMenuItem("Zeiten l\u00F6schen");
		mntmZielzeitenLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File f = new File(name.toLowerCase() + ".csv");
				f.delete();
			}
		});
		
		mnEinstellungen.add(mntmZielzeitenLschen);
		
		JMenuItem mntmStartlisteLschen = new JMenuItem("Startliste neu laden");
		mntmStartlisteLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server.downloadStartliste();
			}
		});

		mnEinstellungen.add(mntmStartlisteLschen);
		
		JMenuItem mntmSpringeZuStartnummer = new JMenuItem("Springe zu Startnummer");
		mntmSpringeZuStartnummer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StnrEingabePopUp popUp = new StnrEingabePopUp(TimeCatcherFrame.this,StnrEingabePopUp.SPRINGEN);
			}
		});
		mnEinstellungen.add(mntmSpringeZuStartnummer);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Startnummer einf\u00FCgen");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StnrEingabePopUp popUp = new StnrEingabePopUp(TimeCatcherFrame.this,StnrEingabePopUp.EINFUEGEN);
			}
		});
		mnEinstellungen.add(mntmNewMenuItem);
		
		btnM.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				m();
			}
		});
		btnAok.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				aok();
			}
		});
	}
}