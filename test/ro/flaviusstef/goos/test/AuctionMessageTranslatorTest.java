package ro.flaviusstef.goos.test;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.flaviusstef.goos.ApplicationRunner;
import ro.flaviusstef.goos.AuctionEventListener;
import ro.flaviusstef.goos.AuctionEventListener.PriceSource;
import ro.flaviusstef.goos.AuctionMessageTranslator;

@RunWith(JMock.class)
public class AuctionMessageTranslatorTest {
	private final Mockery context = new Mockery();
	private final AuctionEventListener listener = context.mock(AuctionEventListener.class);
	public static final Chat UNUSED_CHAT = null;
	private final AuctionMessageTranslator translator = 
		new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID, listener);

	@Test
	public void notifiesAuctionClosedWhenCloseMessageReceived() {
		context.checking(new Expectations() {{
			oneOf(listener).auctionClosed();
		}});
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event:CLOSE;");
		
		translator.processMessage(UNUSED_CHAT , message);
	}
	
	@Test
	public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
		context.checking(new Expectations(){{
			exactly(1).of(listener).currentPrice(192, 7, PriceSource.FromSniper);
		}});

		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: " + ApplicationRunner.SNIPER_ID + ";");
		
		translator.processMessage(UNUSED_CHAT , message);
	}
	
	@Test
	public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
		context.checking(new Expectations(){{
			exactly(1).of(listener).currentPrice(192, 7, PriceSource.FromOtherBidder);
		}});

		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: bubu");
		
		translator.processMessage(UNUSED_CHAT , message);
	}
}
