package ro.flaviusstef.goos;

public class ApplicationRunner {

	protected static final String SNIPER_ID = "sniper";
	protected static final String SNIPER_PASSWORD = "sniper";
	protected static final String XMPP_HOSTNAME = "vaio";
	private static final String STATUS_JOINING = "joining";
	private static final String STATUS_LOST = "lost";
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
	
	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(STATUS_LOST);
	}
	
	public void stop() {
		if (driver != null)
			driver.dispose();
	}

}
