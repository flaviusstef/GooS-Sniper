package ro.flaviusstef.goos;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import ro.flaviusstef.goos.ui.MainWindow;

public class Main {

	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	
	private static MainWindow ui;
	private final SniperPortfolio portfolio = new SniperPortfolio();
	
	public Main() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() { ui = new MainWindow(portfolio); }
		});
	}

	public static void main(String... args) throws Exception{
		Main main = new Main();
		
		XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connectTo(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.disconnectWhenUICloses(auctionHouse);
		main.addUserRequestListenerFor(auctionHouse);
	}

	private void addUserRequestListenerFor(final XMPPAuctionHouse auctionHouse){
		ui.addUserRequestListener(new SniperLauncher(auctionHouse, portfolio));
	}
	
	private void disconnectWhenUICloses(final XMPPAuctionHouse auctionHouse) {
		ui.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				auctionHouse.disconnect();
			}
		});
	}
}
