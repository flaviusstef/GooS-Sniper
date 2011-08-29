package ro.flaviusstef.goos;

import java.awt.Color;

import javax.swing.*;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	public static final String SNIPER_STATUS_NAME = "sniper status";
	public static final String MAIN_WINDOW_NAME = "Auction Sniper";
	public static final String STATUS_WINNING = "won";
	private final JLabel sniperStatus = createLabel(SniperStateDisplayer.STATUS_JOINING);
	
	public MainWindow() {
		super("Auction Sniper");
		setName(MAIN_WINDOW_NAME);
		add(sniperStatus);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private JLabel createLabel(String initialText) {
		JLabel result = new JLabel(initialText);
		result.setName(SNIPER_STATUS_NAME);
		result.setBorder(new LineBorder(Color.BLACK));
		return result;
	}

	public void showStatus(String status) {
		sniperStatus.setText(status);
	}
}
