package ro.flaviusstef.goos.test;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.flaviusstef.goos.AuctionSniper;
import ro.flaviusstef.goos.SniperListener;

@RunWith(JMock.class)
public class AuctionSniperTest {
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = 
		context.mock(SniperListener.class);
	private final AuctionSniper sniper = new AuctionSniper(sniperListener);
	
	@Test
	public void reportsWhenAuctionCloses() {
		context.checking(new Expectations() {{
			oneOf(sniperListener).sniperLost();
		}});
		
		sniper.auctionClosed();
	}
}
