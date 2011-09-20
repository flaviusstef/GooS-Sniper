package ro.flaviusstef.goos.test;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.flaviusstef.goos.Auction;
import ro.flaviusstef.goos.AuctionHouse;
import ro.flaviusstef.goos.AuctionSniper;
import ro.flaviusstef.goos.Item;
import ro.flaviusstef.goos.SniperCollector;
import ro.flaviusstef.goos.SniperLauncher;

@RunWith(JMock.class)
public class SniperLauncherTest {
	private final Mockery context = new JUnit4Mockery();
	private final States auctionState = context.states("auction state").startsAs("not joined");
	private final Auction auction = context.mock(Auction.class);
	private final AuctionHouse auctionHouse = context.mock(AuctionHouse.class);
	private SniperCollector sniperCollector = context.mock(SniperCollector.class);
	private final SniperLauncher launcher = new SniperLauncher(auctionHouse, sniperCollector );
	
	@Test
	public void addsNewSniperToCollectorAndThenJoinsAuction() {
		final Item item = new Item("item 123", 345);
		context.checking(new Expectations() {{
			allowing(auctionHouse).auctionFor(item); will(returnValue(auction));
			
			oneOf(auction).addAuctionEventListener(with(sniperForItem(item)));
			when(auctionState.is("not joined"));
			oneOf(sniperCollector).addSniper(with(sniperForItem(item)));
			when(auctionState.is("not joined"));
			
			one(auction).join(); then(auctionState.is("joined"));
		}});

		launcher.joinAuction(item);
	}
	
	// TODO: cum e cu meciarii astia?
	private Matcher<AuctionSniper> sniperForItem(Item item) {
		return new FeatureMatcher<AuctionSniper, String>(equalTo(item.identifier), "sniper with item id", "item") {
			@Override
			protected String featureValueOf(AuctionSniper actual) {
				return actual.getSnapshot().itemId;
			}
		};
	}
}
