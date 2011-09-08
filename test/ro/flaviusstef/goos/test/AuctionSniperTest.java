package ro.flaviusstef.goos.test;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.*;
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
import ro.flaviusstef.goos.SniperSnapshot;
import ro.flaviusstef.goos.SniperState;
import static ro.flaviusstef.goos.SniperState.*;

@RunWith(JMock.class)
public class AuctionSniperTest {
	private static final String ITEM_ID = "item id";
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = 
		context.mock(SniperListener.class);
	private final States sniperState = context.states("sniper");
	private final Auction auction = context.mock(Auction.class);
	private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);
	
	@Test
	public void reportsWhenAuctionClosesImmediately() {
		context.checking(new Expectations() {{
			atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(LOST)));
		}});
		
		sniper.auctionClosed();
	}
	
	@Test
	public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
		final int price = 100;
		final int increment = 10;
		final int bid = price+increment;

		context.checking(new Expectations() {{
			one(auction).bid(bid);
			atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
		}});
		
		sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}
	
	@Test
	public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
		context.checking(new Expectations() {{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(BIDDING)));
			then(sniperState.is("bidding"));
			
			atLeast(1).of(sniperListener).sniperStateChanged(
					new SniperSnapshot(ITEM_ID, 20, 20, WINNING));
			when(sniperState.is("bidding"));
		}});
		
		sniper.currentPrice(10, 10, PriceSource.FromOtherBidder);
		sniper.currentPrice(20, 10, PriceSource.FromSniper);
	}
	
	@Test
	public void reportsLostIfAuctionClosesWhenBidding() {
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(BIDDING)));
			then(sniperState.is("bidding"));
			
			atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatHas(LOST)));
			when(sniperState.is("bidding"));
		}});
		
		sniper.currentPrice(100, 10, PriceSource.FromOtherBidder);
		sniper.auctionClosed();
	}
	
	@Test
	public void reportsWonIfAuctionClosesWhenWinning() {
		context.checking(new Expectations(){{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(WINNING)));
			then(sniperState.is("winning"));
			
			atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatHas(WON)));
			when(sniperState.is("winning"));
		}});
		
		sniper.currentPrice(100, 10, PriceSource.FromSniper);
		sniper.auctionClosed();
	}

	// TODO: read about generics
	// TODO: read about hamcrest matchers
	private Matcher<SniperSnapshot> aSniperThatIs(final SniperState state) {
		return new FeatureMatcher<SniperSnapshot, SniperState>(equalTo(state), "sniper that is", "was") {
			protected SniperState featureValueOf(SniperSnapshot actual) {
				return actual.state;
			}
		};
	}
	
	// syntactic sugar
	private Matcher<SniperSnapshot> aSniperThatHas(final SniperState state) {
		return aSniperThatIs(state);
	}
}