package ro.flaviusstef.goos;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuction implements Auction {
	private Chat chat;
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d";
	
	static final String AUCTION_RESOURCE = "Auction";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + XMPPAuction.AUCTION_RESOURCE;
	
	private final Announcer<AuctionEventListener> auctionEventListeners =
					Announcer.to(AuctionEventListener.class);

	public XMPPAuction(XMPPConnection connection, String itemId) {
		AuctionMessageTranslator translator = translatorFor(connection);
		chat = connection.getChatManager().createChat(auctionId(itemId, connection), translator);
		addAuctionEventListener(chatDisconnectorFor(translator));
	}
	
	public static String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
	}
	
	public void bid(int amount) {
		sendMessage(String.format(BID_COMMAND_FORMAT, amount));
	}
	
	public void join() {
		sendMessage(JOIN_COMMAND_FORMAT);
	}
	
	private void sendMessage(String message) {
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	
	public void addAuctionEventListener(AuctionEventListener listener) {
		auctionEventListeners.addListener(listener);
	}
	
	private AuctionMessageTranslator translatorFor(XMPPConnection connection) {
		// TODO: treaba asta cu announce() care returneaza un AuctionEventListener n-o prea pricep
		return new AuctionMessageTranslator(connection.getUser(), auctionEventListeners.announce());
	}
	
	private AuctionEventListener chatDisconnectorFor(final AuctionMessageTranslator translator) {
		return new AuctionEventListener() {

			@Override
			public void auctionClosed() { }

			@Override
			public void currentPrice(int price, int increment, PriceSource source) { }

			@Override
			public void auctionFailed() {
				chat.removeMessageListener(translator);
			}
		};
	}
}