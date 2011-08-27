package ro.flaviusstef.goos;

public interface AuctionEventListener {

	public void auctionClosed();
	public void currentPrice(int price, int increment);
}
