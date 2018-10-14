import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PopUpFenster extends Zeitkiste{

	public PopUpFenster() {
		JFrame frame = new JFrame("StNr");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setBounds(100, 100, 250, 150);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		frame.add(panel);
		JLabel label = new JLabel("Startnummer hier eingeben:");
		panel.add(label);
		JTextField textField = new JTextField();
		textField.setBounds(5, 5, 15, 10);
		panel.add(textField);
		JButton button = new JButton("Bestätigen");
		button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (textField.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null,"Ihre Eingabe ist leer!","Eingabefehler",JOptionPane.WARNING_MESSAGE);
				}else {
					zuStnrSpringen(Integer.parseInt(textField.getText()));
					frame.dispose();
				}
			}
		});
		panel.add(button);
		//frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		
	}
	
	public boolean isText (String text) {
		  return text.matches("[a-zA-ZäÄöÖüÜß]");
		}
	
	public void zuStnrSpringen(int pStartnummer) {
		super.zuStnrSpringen(pStartnummer);
	}
}
