package ro.flaviusstef.goos.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import ro.flaviusstef.goos.Announcer;
import ro.flaviusstef.goos.Item;
import ro.flaviusstef.goos.SniperPortfolio;
import ro.flaviusstef.goos.UserRequestListener;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	public static final String SNIPER_STATUS_NAME = "sniper status";
	public static final String MAIN_WINDOW_NAME = "Auction Sniper";
	private static final String SNIPERS_TABLE_NAME = "snipers table";
	public static final String APPLICATION_TITLE = MAIN_WINDOW_NAME;
	public static final String NEW_ITEM_ID_NAME = "new item id";
	public static final String JOIN_BUTTON_NAME = "join button";
	public static final String NEW_ITEM_STOP_PRICE_NAME = "stop price";
	
	private final Announcer<UserRequestListener> userRequests = 
		Announcer.to(UserRequestListener.class);
	private SniperPortfolio portfolio;
	private JTextField itemIdField;
	private JFormattedTextField stopPriceField;
	
	public MainWindow(SniperPortfolio portfolio) {
		super(APPLICATION_TITLE);
		this.portfolio = portfolio;
		setName(MAIN_WINDOW_NAME);
		fillContentPane(makeSnipersTable(), makeControls());
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private JPanel makeControls() {
		JPanel controls = new JPanel(new FlowLayout());
		itemIdField = new JTextField();
		itemIdField.setColumns(20);
		itemIdField.setName(NEW_ITEM_ID_NAME);
		stopPriceField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		stopPriceField.setColumns(7);
		stopPriceField.setName(NEW_ITEM_STOP_PRICE_NAME);
		controls.add(itemIdField);
		controls.add(stopPriceField);
		
		JButton joinAuctionButton = new JButton("Join Auction");
		joinAuctionButton.setName(JOIN_BUTTON_NAME);
		joinAuctionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Item item = new Item(itemId(), stopPrice());
				userRequests.announce().joinAuction(item);
			}

		});
		controls.add(joinAuctionButton);
		
		return controls;
	}

	private int stopPrice() {
		return ((Number)stopPriceField.getValue()).intValue();
	}
	
	private String itemId() {
		return itemIdField.getText();
	}
	
	private void fillContentPane(JTable snipersTable, JPanel controls) {
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		contentPane.add(controls, BorderLayout.NORTH);
		contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
	}

	private JTable makeSnipersTable() { 
		SnipersTableModel model = new SnipersTableModel();
		portfolio.addPortfolioListener(model);
		final JTable snipersTable = new JTable(model);
		snipersTable.setName(SNIPERS_TABLE_NAME);
		return snipersTable;
	}

	public void addUserRequestListener(UserRequestListener userRequestListener) {
		userRequests.addListener(userRequestListener);
	}
}