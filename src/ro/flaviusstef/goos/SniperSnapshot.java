package ro.flaviusstef.goos;

public class SniperSnapshot {
	public final String itemId;
	public final int lastPrice;
	public final int lastBid;
	public SniperState state;
	
	public SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperState state){
		this.itemId = itemId;
		this.lastPrice = lastPrice;
		this.lastBid = lastBid;
		this.state = state;
	}
	
	public boolean equals(Object other) {
		if (other == null) return false;
		if (! (other instanceof SniperSnapshot)) return false;
		
		return itemId.equals(((SniperSnapshot) other).itemId) && 
		       lastPrice == ((SniperSnapshot) other).lastPrice &&
		       lastBid == ((SniperSnapshot) other).lastBid;
	}
	
	public String toString() {
		return String.format("Item: %s, last price: %d, last bid: %d", itemId, lastPrice, lastBid);
	}
	
	public SniperSnapshot bidding(int newLastPrice, int newLastBid) {
		return new SniperSnapshot(itemId, newLastPrice, newLastBid, SniperState.BIDDING);
	}
	
	public SniperSnapshot winning(int newLastPrice) {
		return new SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.WINNING);
	}
	
	public static SniperSnapshot joining(String itemId){
		return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
	}
	
	public SniperSnapshot closed() {
		return new SniperSnapshot(itemId, lastPrice, lastBid, state.whenAuctionClosed());
	}

	public boolean isForSameItemAs(SniperSnapshot other) {
		return itemId.equals(other.itemId);	
	}
}
