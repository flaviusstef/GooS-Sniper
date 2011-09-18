package ro.flaviusstef.goos;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuctionHouse implements AuctionHouse {
	private XMPPConnection connection;

	private XMPPAuctionHouse(String hostname, String username, String password) throws XMPPException {
		connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, XMPPAuction.AUCTION_RESOURCE);
	}
	
	public static XMPPAuctionHouse connectTo(String hostname, String username, String password) throws XMPPException {
		return new XMPPAuctionHouse(hostname, username, password);
	}

	@Override
	public Auction auctionFor(String itemId) {
		return new XMPPAuction(connection, itemId);
	}
	
	public void disconnect() {
		connection.disconnect();
	}

}
