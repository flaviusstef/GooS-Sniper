package ro.flaviusstef.goos;

public class ApplicationRunner {

	public static final String SNIPER_ID = "sniper";
	protected static final String SNIPER_PASSWORD = "sniper";
	protected static final String XMPP_HOSTNAME = "localhost";
	private static final String STATUS_JOINING = "joining";
	private static final String STATUS_LOST = "lost";
	private static final String STATUS_BIDDING = "bidding";
	private static final String STATUS_WINNING = "winning";
	private static final String STATUS_WON = "won";
	public static final String SNIPER_XMPP_ID = "sniper@shary.dev.syneto.net/Auction";
	private AuctionSniperDriver driver;

	public void startBiddingIn(final FakeAuctionServer auction) {
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
	
	public void showsSniperHasWon() {
		driver.showsSniperStatus(STATUS_WON);
	}
	
	public void hasShownSniperIsBidding() {
		driver.showsSniperStatus(STATUS_BIDDING);
	}

	public void hasShownSniperIsWinning() {
		driver.showsSniperStatus(STATUS_WINNING);
	}

}
