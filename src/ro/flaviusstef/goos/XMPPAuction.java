package ro.flaviusstef.goos;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuction implements Auction {
	private Chat chat;
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d";

	public XMPPAuction(Chat chat){
		this.chat = chat;
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
}