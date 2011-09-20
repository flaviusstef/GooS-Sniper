package ro.flaviusstef.goos;

import java.util.ArrayList;
import java.util.List;

public class AuctionSniper implements AuctionEventListener {

	private Auction auction;
	private SniperSnapshot snapshot;
	private List<SniperListener> sniperListeners = new ArrayList<SniperListener>();
	private Item item;

	public AuctionSniper(Item item, Auction auction) {
		this.auction = auction;
		this.item = item;
		this.snapshot = SniperSnapshot.joining(item.identifier);
	}

	public void addSniperListener(SniperListener sniperListener) {
		sniperListeners.add(sniperListener);
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
			if (item.allowsBid(bid)) {
				auction.bid(bid);
				snapshot = snapshot.bidding(price, bid);
			} else {
				snapshot = snapshot.losing(price);
			}
		}
		notifyChange();
	}
	
	public SniperSnapshot getSnapshot() {
		return snapshot;
	}
	
	private void notifyChange() {
		for (SniperListener listener : sniperListeners) {
			listener.sniperStateChanged(snapshot);
		}
	}

	@Override
	public void auctionFailed() {
	}
}