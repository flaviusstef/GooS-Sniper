package ro.flaviusstef.goos;

public class AuctionSniper implements AuctionEventListener {

	private SniperListener sniperListener;
	private Auction auction;
	private boolean isWinning = false;
	private String itemId;

	public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
		this.itemId = itemId;
		this.auction = auction;
		this.sniperListener = sniperListener;
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
		switch (source) {
		case FromSniper:
			isWinning = true;
			sniperListener.sniperWinning();
			break;
		case FromOtherBidder:
			isWinning = false;
			auction.bid(bid);
			sniperListener.sniperBidding(new SniperState(itemId, price, bid));
			break;
			
		}
	}

}
