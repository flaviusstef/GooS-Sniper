package ro.flaviusstef.goos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.*;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	public static final String SNIPER_STATUS_NAME = "sniper status";
	public static final String MAIN_WINDOW_NAME = "Auction Sniper";
	private static final String SNIPERS_TABLE_NAME = "snipers table";
	private final SnipersTableModel snipers = new SnipersTableModel();
	
	public MainWindow() {
		super("Auction Sniper");
		setName(MAIN_WINDOW_NAME);
		fillContentPane(makeSnipersTable());
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void fillContentPane(JTable snipersTable) {
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
	}

	private JTable makeSnipersTable() { 
		final JTable snipersTable = new JTable(snipers);
		snipersTable.setName(SNIPERS_TABLE_NAME);
		return snipersTable;
	}

	public void showStatus(String status) {
		snipers.setStatusText(status);
	}

	public void sniperStatusChanged(SniperState state, String status) {
		snipers.sniperStatusChanged(state, status);
	}
}
