package ro.flaviusstef.goos;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import ro.flaviusstef.goos.AuctionEventListener.PriceSource;

public class AuctionMessageTranslator implements MessageListener {

	private AuctionEventListener listener;
	private String sniperId;

	public AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
		this.listener = listener;
		this.sniperId = sniperId;
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		try {
			translate(message.getBody());
		} catch (Exception e) {
			listener.auctionFailed();
		}
	}

	private void translate(String messageBody) throws MissingValueException {
		AuctionEvent event = AuctionEvent.from(messageBody);
		
		String eventType = event.type();
		if ("CLOSE".equals(eventType)) {
			listener.auctionClosed();
		} else if ("PRICE".equals(eventType)) {
			listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
		}
	}

	@SuppressWarnings("serial")
	private static class MissingValueException extends Exception {
	}
	
	private static class AuctionEvent {
		private final Map<String, String> fields = new HashMap<String, String>();
		public String type() throws MissingValueException { return get("Event"); }
		public int currentPrice() throws NumberFormatException, MissingValueException { return getInt("CurrentPrice"); }
		public int increment() throws NumberFormatException, MissingValueException { return getInt("Increment"); }
		private String bidder() throws MissingValueException { return get("Bidder"); }
		public PriceSource isFrom(String sniperId) throws MissingValueException {
			return (bidder().equals(sniperId)) ? PriceSource.FromSniper : PriceSource.FromOtherBidder;
		}
		
		private int getInt(String fieldName) throws NumberFormatException, MissingValueException {
			return Integer.parseInt(get(fieldName));
		}
		
		private String get(String fieldName) throws MissingValueException {
			String value = fields.get(fieldName);
			if (null == value) {
				throw new MissingValueException();
			}
			return value;
		}
		
		private void addField(String field) {
			String[] pair = field.split(":");
			fields.put(pair[0].trim(), pair[1].trim());
		}
		
		static AuctionEvent from(String messageBody) {
			AuctionEvent event = new AuctionEvent();
			for (String field : fieldsIn(messageBody)) {
				event.addField(field);
			}
			
			return event;
		}
		private static String[] fieldsIn(String messageBody) {
			return messageBody.split(";");
		}
	}
}
