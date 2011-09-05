package ro.flaviusstef.goos.test;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.flaviusstef.goos.Auction;
import ro.flaviusstef.goos.AuctionEventListener.PriceSource;
import ro.flaviusstef.goos.AuctionSniper;
import ro.flaviusstef.goos.SniperListener;

@RunWith(JMock.class)
public class AuctionSniperTest {
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = 
		context.mock(SniperListener.class);
	private final States sniperState = context.states("sniper");
	private final Auction auction = context.mock(Auction.class);
	private final AuctionSniper sniper = new AuctionSniper(auction, sniperListener);
	
	@Test
	public void reportsWhenAuctionClosesImmediately() {
		context.checking(new Expectations() {{
			atLeast(1).of(sniperListener).sniperLost();
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
		
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}
	
	@Test
	public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
		context.checking(new Expectations() {{
			atLeast(1).of(sniperListener).sniperWinning();
		}});
		
		sniper.currentPrice(100, 10, PriceSource.FromSniper);
	}
	
	@Test
	public void reportsLostIfAuctionClosesWhenBidding() {
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperBidding();
			then(sniperState.is("bidding"));
			
			atLeast(1).of(sniperListener).sniperLost();
			when(sniperState.is("bidding"));
		}});
		
		sniper.currentPrice(100, 10, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}
	
	@Test
	public void reportsWonIfAuctionClosesWhenWinning() {
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperWinning();
			then(sniperState.is("winning"));
			
			atLeast(1).of(sniperListener).sniperWon();
			when(sniperState.is("winning"));
		}});
		
		sniper.currentPrice(100, 10, PriceSource.FromSniper);
		sniper.auctionClosed();
	}
}
