package ro.flaviusstef.goos;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import ro.flaviusstef.goos.ui.MainWindow;
import ro.flaviusstef.goos.ui.SnipersTableModel;
import ro.flaviusstef.goos.ui.SwingThreadSniperListener;

public class Main {

	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	
	
	private final SnipersTableModel snipers = new SnipersTableModel();
	private static MainWindow ui;
	private ArrayList<Auction>notToBeGCd = new ArrayList<Auction>();
	static final String AUCTION_RESOURCE = "Auction";
	
	public Main() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() { ui = new MainWindow(snipers); }
		});
	}

	public static void main(String... args) throws Exception{
		Main main = new Main();
		
		XMPPConnection connection = connectTo(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.disconnectWhenUICloses(connection);
		main.addUserRequestListenerFor(connection);
	}

	private void addUserRequestListenerFor(final XMPPConnection connection){
		ui.addUserRequestListener(new UserRequestListener() {
			@Override
			public void joinAuction(String itemId) {
				snipers.addSniper(SniperSnapshot.joining(itemId));
				Auction auction = new XMPPAuction(connection, itemId);
				notToBeGCd.add(auction);
				auction.addAuctionEventListener(
				    new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers)));
				auction.join();
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
}
