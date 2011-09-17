package ro.flaviusstef.goos;

public interface Auction {
	void bid(int price);
	void join();
	void addAuctionEventListener(AuctionEventListener listener);
}
