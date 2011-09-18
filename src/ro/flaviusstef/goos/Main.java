package ro.flaviusstef.goos;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

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
	
	public Main() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() { ui = new MainWindow(snipers); }
		});
	}

	public static void main(String... args) throws Exception{
		Main main = new Main();
		
		XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connectTo(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.disconnectWhenUICloses(auctionHouse);
		main.addUserRequestListenerFor(auctionHouse);
	}

	private void addUserRequestListenerFor(final XMPPAuctionHouse house){
		ui.addUserRequestListener(new UserRequestListener() {
			@Override
			public void joinAuction(String itemId) {
				snipers.addSniper(SniperSnapshot.joining(itemId));
				Auction auction = house.auctionFor(itemId);
				notToBeGCd.add(auction);
				auction.addAuctionEventListener(
				    new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers)));
				auction.join();
			}
		});
	}
	
	private void disconnectWhenUICloses(final XMPPAuctionHouse connection) {
		ui.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				connection.disconnect();
			}
		});
	}
}
