package ro.flaviusstef.goos;

import ro.flaviusstef.goos.test.FakeAuctionServer;

public class ApplicationRunner {

	public static final String SNIPER_ID = "sniper";
	protected static final String SNIPER_PASSWORD = "sniper";
	protected static final String XMPP_HOSTNAME = "localhost";
	protected static final String STATUS_JOINING = "joining";
	private static final String STATUS_LOST = "lost";
	private static final String STATUS_BIDDING = "bidding";
	private static final String STATUS_WINNING = "winning";
	private static final String STATUS_WON = "won";
	public static final String SNIPER_XMPP_ID = "sniper@vaio/Auction";
	private AuctionSniperDriver driver;
	private String itemId;

	public void startBiddingIn(final FakeAuctionServer auction) {
		itemId = auction.getItemId();
		Thread thread = new Thread("Test Application") {
			@Override
			public void run() {
				try {
					Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		driver = new AuctionSniperDriver(1000);
		driver.showsSniperStatus(STATUS_JOINING);
	}
		
	public void stop() {
		if (driver != null)
			driver.dispose();
	}

	public void showsSniperHasLost() {
		driver.showsSniperStatus(STATUS_LOST);
	}
	
	public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid, STATUS_BIDDING);
	}

	public void hasShownSniperIsWinning(int winningBid) {
		driver.showsSniperStatus(itemId, winningBid, winningBid, STATUS_WINNING);
	}

	public void showsSniperHasWon(int lastPrice) {
		driver.showsSniperStatus(itemId, lastPrice, lastPrice, STATUS_WON);
	}

}
