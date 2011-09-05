package ro.flaviusstef.goos.test;

import org.junit.After;
import org.junit.Test;

import ro.flaviusstef.goos.ApplicationRunner;


public class AuctionSniperEndToEndTest {

	private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private final ApplicationRunner application = new ApplicationRunner();
	
	@Test
	public void sniperJoinsAuctionButLoses() throws Exception {
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLost();
	}
	
	@Test
	public void sniperMakesAHigherBidButLoses() throws Exception {
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding();
		
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLost();
	}
	
	@Test
	public void sniperBidsHigherAndWins() throws Exception {
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(1000, 98);
		
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
		application.hasShownSniperIsWinning(1098);
		
		auction.announceClosed();
		application.showsSniperHasWon(1098);
	}

	@After
	public void stopAuction() {
		auction.stop();
	}
	
	@After public void stopApplication() {
		application.stop();
	}
}
