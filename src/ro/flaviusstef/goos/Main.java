package ro.flaviusstef.goos;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class Main {

	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	
	private static final String AUCTION_RESOURCE = "Auction";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
	
	private final SnipersTableModel snipers = new SnipersTableModel();
	private static MainWindow ui;
	private ArrayList<Chat >notToBeGCd = new ArrayList<Chat>();
	
	public Main() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() { ui = new MainWindow(snipers); }
		});
	}

	public static void main(String... args) throws Exception{
		Main main = new Main();
		
		XMPPConnection connection = connectTo(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.disconnectWhenUICloses(connection);
		for (int i = 3; i<args.length; i++) {
			main.joinAuction(connection, args[i]);
		}
	}

	private void joinAuction(XMPPConnection connection, String itemId)
			throws Exception {
		safelyAddItemToModel(itemId);
		final Chat chat = connection.getChatManager().
		                    createChat(auctionId(itemId, connection), null);
		
		notToBeGCd.add(chat);
		
		Auction auction = new XMPPAuction(chat);
		chat.addMessageListener(new AuctionMessageTranslator(connection.getUser(),
				new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers))));
		snipers.sniperStateChanged(SniperSnapshot.joining(itemId));
		auction.join();
	}

	private void safelyAddItemToModel(final String itemId) throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				snipers.addSniper(SniperSnapshot.joining(itemId));
			}
		});
	}

	private void disconnectWhenUICloses(final Connection c) {
		ui.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				c.disconnect();
			}
		});
	}

	private static XMPPConnection 
	connectTo(String hostname, String username, String password) throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);
		
		return connection;
	}

	private static String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
	}
}
