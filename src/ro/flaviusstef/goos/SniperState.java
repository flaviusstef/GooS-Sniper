package ro.flaviusstef.goos;

import com.objogate.exception.Defect;

// TODO: learn about enums
public enum SniperState {
	JOINING {
		@Override public SniperState whenAuctionClosed() { return LOST;}
	},
	BIDDING {
		@Override public SniperState whenAuctionClosed() { return LOST;}
	},
	WINNING {
		@Override public SniperState whenAuctionClosed() { return WON;}
	},
	LOSING {
		@Override public SniperState whenAuctionClosed() { return LOST; }
	},
	LOST,
	WON;

	// TODO: what is Defect
	public SniperState whenAuctionClosed() {
		throw new Defect("Auction is already closed");	
	}
}
