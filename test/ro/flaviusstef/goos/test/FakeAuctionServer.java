package ro.flaviusstef.goos.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import ro.flaviusstef.goos.XMPPAuction;

import static org.junit.Assert.*;

public class FakeAuctionServer {
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_RESOURCE = "Auction";
	public static final String XMPP_HOSTNAME = "localhost";
	private static final String AUCTION_PASSWORD = "auction";
	
	private final String itemId;
	private final XMPPConnection connection;
	private Chat currentChat;
	
	private final SingleMessageListener messageListener = new SingleMessageListener();
	
	public static class SingleMessageListener implements MessageListener {

		private ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

		public void processMessage(Chat chat, Message message) {
			messages.add(message);
		}
		
		public void receivesAMessage(Matcher<? super String> messageMatcher) throws InterruptedException {
			final Message message = messages.poll(3, TimeUnit.SECONDS);
			assertThat("Message", message, is(notNullValue()));
			assertThat(message.getBody(), messageMatcher);
		}

	}
	
	public FakeAuctionServer(String itemId){
		this.itemId = itemId;
		this.connection = new XMPPConnection(XMPP_HOSTNAME);
	}
	
	public String getItemId() {
		return itemId;
	}
	
	public void startSellingItem() throws XMPPException {
		connection.connect();
		connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(
			new ChatManagerListener() {
				public void chatCreated(Chat chat, boolean createdLocally) {
					currentChat = chat;
					chat.addMessageListener(messageListener);
				}
			}
		);
	}
	
	public void announceClosed() throws XMPPException {
		currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE");
	}
	
	public void stop() {
		connection.disconnect();
	}
	
	public void sendInvalidMessageContaining(String message) throws XMPPException {
		currentChat.sendMessage(message);
	}
	
	public void reportPrice(int price, int minimumIncrement, String currentWinner) throws XMPPException {
		currentChat.sendMessage(
		  String.format(
		    "SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;", 
		    price, minimumIncrement, currentWinner));
	}
	
	public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
		receivesAMessageMatching(sniperId, equalTo(XMPPAuction.JOIN_COMMAND_FORMAT));
	}

	public void hasReceivedBid(int bid, String sniperId) throws InterruptedException {
		receivesAMessageMatching(sniperId, equalTo(
		  String.format(XMPPAuction.BID_COMMAND_FORMAT, bid)));
	}
	
	private void receivesAMessageMatching(String sniperId, Matcher<? super String> matcher) 
	  throws InterruptedException
	{
		messageListener.receivesAMessage(matcher);
		assertThat(currentChat.getParticipant(), equalTo(sniperId));
	}


}
