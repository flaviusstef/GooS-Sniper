package ro.flaviusstef.goos.test;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.flaviusstef.goos.Auction;
import ro.flaviusstef.goos.AuctionSniper;
import ro.flaviusstef.goos.SniperListener;

@RunWith(JMock.class)
public class AuctionSniperTest {
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = 
		context.mock(SniperListener.class);
	private final Auction auction = context.mock(Auction.class);
	private final AuctionSniper sniper = new AuctionSniper(auction, sniperListener);
	
	@Test
	public void reportsWhenAuctionCloses() {
		context.checking(new Expectations() {{
			oneOf(sniperListener).sniperLost();
		}});
		
		sniper.auctionClosed();
	}
	
	@Test
	public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
		final int price = 100;
		final int increment = 10;

		context.checking(new Expectations() {{
			one(auction).bid(price+increment);
			atLeast(1).of(sniperListener).sniperBidding();
		}});
		
		sniper.currentPrice(price, increment);
	}
}
