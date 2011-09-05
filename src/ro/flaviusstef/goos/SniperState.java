package ro.flaviusstef.goos;

public class SniperState {
	public final String itemId;
	public final int lastPrice;
	public final int lastBid;
	
	public SniperState(String itemId, int lastPrice, int lastBid){
		this.itemId = itemId;
		this.lastPrice = lastPrice;
		this.lastBid = lastBid;
	}
	
	public boolean equals(Object other) {
		if (other == null) return false;
		if (! (other instanceof SniperState)) return false;
		
		return itemId.equals(((SniperState) other).itemId) && 
		       lastPrice == ((SniperState) other).lastPrice &&
		       lastBid == ((SniperState) other).lastBid;
	}
	
	public String toString() {
		return String.format("Item: %s, last price: %d, last bid: %d", itemId, lastPrice, lastBid);
	}

}
