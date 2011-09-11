package ro.flaviusstef.goos.test;

import static ro.flaviusstef.goos.SnipersTableModel.textFor;
import ro.flaviusstef.goos.AuctionSniperDriver;
import ro.flaviusstef.goos.Main;
import ro.flaviusstef.goos.MainWindow;
import ro.flaviusstef.goos.SniperState;

public class ApplicationRunner {

	public static final String SNIPER_ID = "sniper";
	protected static final String SNIPER_PASSWORD = "sniper";
	protected static final String XMPP_HOSTNAME = "localhost";
	protected static final String STATUS_JOINING = "joining";
	private static final String STATUS_LOST = "lost";
	private static final String STATUS_WINNING = "winning";
	private static final String STATUS_WON = "won";
	public static final String SNIPER_XMPP_ID = "sniper@vaio/Auction";
	private AuctionSniperDriver driver;

	public void startBiddingIn(final FakeAuctionServer... auctions) {
		startSniper(auctions);
		for (FakeAuctionServer auction : auctions) {
			final String itemId = auction.getItemId();
			driver.startBiddingFor(itemId);
			driver.showsSniperStatus(itemId, 0, 0, textFor(SniperState.JOINING));
		}
	}
	
	private void startSniper(final FakeAuctionServer... auctions) {
		Thread thread = new Thread("Test Application") {
			@Override
			public void run() {
				try {
					Main.main(arguments(auctions));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		driver = new AuctionSniperDriver(1000);
		driver.hasTitle(MainWindow.APPLICATION_TITLE);
		driver.hasColumnTitles();
	}
	protected static String[] arguments(FakeAuctionServer... auctions) {
		String[] arguments = new String[auctions.length + 3];
		arguments[0] = XMPP_HOSTNAME;
		arguments[1] = SNIPER_ID;
		arguments[2] = SNIPER_PASSWORD;
		for (int i=0; i<auctions.length; i++) {
			arguments[i+3] = auctions[i].getItemId();
		}
		
		return arguments;
	}
		
	public void stop() {
		if (driver != null)
			driver.dispose();
	}

	public void showsSniperHasLost(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, STATUS_LOST);
	}
	
	public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.BIDDING));
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
		driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, STATUS_WINNING);
	}

	public void showsSniperHasWon(FakeAuctionServer auction, int lastPrice) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, STATUS_WON);
	}

}
