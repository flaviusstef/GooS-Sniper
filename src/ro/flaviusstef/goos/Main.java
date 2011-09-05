package ro.flaviusstef.goos;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class Main {

	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	private static final int ARG_ITEM_ID  = 3;
	
	private static final String AUCTION_RESOURCE = "Auction";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
	
	private static MainWindow ui;
	private static XMPPConnection connection;
	@SuppressWarnings("unused")
	private Chat notToBeGCd;
	
	public Main() throws Exception {
		startUserInterface();
	}
	
	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				ui = new MainWindow();
			}
		});
	}

	public static void main(String... args) throws Exception{
		Main main = new Main();
		
		connection = connectTo(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.joinAuction(connection, args[ARG_ITEM_ID]);
	}

	private void joinAuction(XMPPConnection connection, String itemId)
			throws XMPPException {
		disconnectWhenUICloses();
		final Chat chat = connection.getChatManager().
		                    createChat(auctionId(itemId, connection), null);
		
		this.notToBeGCd = chat;
		
		Auction auction = new XMPPAuction(chat);
		chat.addMessageListener(new AuctionMessageTranslator(connection.getUser(),
				new AuctionSniper(itemId, auction, new SniperStateDisplayer(ui))));
		auction.join();
	}

	private void disconnectWhenUICloses() {
		ui.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				connection.disconnect();
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
