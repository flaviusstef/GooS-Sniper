package ro.flaviusstef.goos.test.integration;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.flaviusstef.goos.Auction;
import ro.flaviusstef.goos.AuctionEventListener;
import ro.flaviusstef.goos.XMPPAuctionHouse;
import ro.flaviusstef.goos.test.ApplicationRunner;
import ro.flaviusstef.goos.test.FakeAuctionServer;


public class XMPPAuctionHouseTest {

	private FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
	private XMPPAuctionHouse auctionHouse; 
	
	@Before
	public void before() throws XMPPException {
		auctionHouse = XMPPAuctionHouse.connectTo("localhost", "sniper", "sniper");
		auctionServer.startSellingItem();
	}
	
	@After
	public void after() {
		auctionHouse.disconnect();
	}
	
	@Test
	public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
		// TODO: What is this?
		CountDownLatch auctionWasClosed = new CountDownLatch(1);
		
		Auction auction = auctionHouse.auctionFor(auctionServer.getItemId());
		auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
		
		auction.join();
		auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auctionServer.announceClosed();
		
		assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS));
	}

	private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
		return new AuctionEventListener() {
			@Override
			public void currentPrice(int price, int increment, PriceSource source) {
				// not implemented
			}
			
			@Override
			public void auctionClosed() {
				auctionWasClosed.countDown();
			}
		};
	}
	
}
