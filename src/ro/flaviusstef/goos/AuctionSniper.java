package ro.flaviusstef.goos;

public class AuctionSniper implements AuctionEventListener {

	private SniperListener sniperListener;
	private Auction auction;
	private boolean isWinning = false;
	private SniperSnapshot snapshot;

	public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
		this.snapshot = SniperSnapshot.joining(itemId);
	}
	
	public void auctionClosed() {
		if (isWinning)
			sniperListener.sniperWon();
		else
			sniperListener.sniperLost();
	}

	public void currentPrice(int price, int increment, PriceSource source) {
		isWinning = (source == PriceSource.FromSniper);
		int bid = price + increment;
		if (isWinning) {
			snapshot = snapshot.winning(price);
		} else {
			auction.bid(bid);
			snapshot = snapshot.bidding(price, bid);
		}
		sniperListener.sniperStateChanged(snapshot);
	}

}
