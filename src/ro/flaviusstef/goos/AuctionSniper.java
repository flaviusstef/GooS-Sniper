package ro.flaviusstef.goos;

public class AuctionSniper implements AuctionEventListener {

	private SniperListener sniperListener;
	private Auction auction;
	private SniperSnapshot snapshot;

	public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
		this.snapshot = SniperSnapshot.joining(itemId);
	}
	
	public void auctionClosed() {
		snapshot = snapshot.closed();
		notifyChange();
	}

	public void currentPrice(int price, int increment, PriceSource source) {
		if (source == PriceSource.FromSniper) {
			snapshot = snapshot.winning(price);
		} else {
			int bid = price + increment;
			auction.bid(bid);
			snapshot = snapshot.bidding(price, bid);
		}
		notifyChange();
	}
	
	private void notifyChange() {
		sniperListener.sniperStateChanged(snapshot);
	}
}