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

	public PopUpFenster(String pZweck) {
		JFrame frame = new JFrame("Eingabefenster Zeitkiste");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setBounds(100, 100, 250, 150);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		frame.add(panel);
		JLabel label = new JLabel("StNr / Nachricht hier eingeben:");
		panel.add(label);
		JTextField textField = new JTextField();
		panel.add(textField);
		JButton button = new JButton("Bestätigen");
		button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (textField.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null,"Ihre Eingabe ist leer!","Eingabefehler",JOptionPane.WARNING_MESSAGE);
				}else if (textField.getText().length()>20) {
					JOptionPane.showMessageDialog(null,"Ihre Eingabe ist zu lang!","Eingabefehler",JOptionPane.WARNING_MESSAGE);
				} else {
					if (pZweck == "ZUSTNRSPRINGEN") {
						zuStnrSpringen(Integer.parseInt(textField.getText()));
					} else if (pZweck == "WARNUNGAUSGEBEN") {
						warnungAusgeben(textField.getText());
					}
				frame.dispose();
				}
			}
		});
		panel.add(button);
		frame.setResizable(false);
		frame.setVisible(true);
		
	}
	
	public void zuStnrSpringen(int pStartnummer) {
		super.zuStnrSpringen(pStartnummer);
	}
	public void warnungAusgeben(String pWarnung) {
		super.warnungAusgeben(pWarnung);
	}
}
