package ro.flaviusstef.goos;

import java.util.List;
import java.util.ArrayList;

public class SniperPortfolio implements SniperCollector {
	private List<PortfolioListener> listeners = new ArrayList<PortfolioListener>();
	
	public SniperPortfolio() {
	}
	
	public SniperPortfolio(PortfolioListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void addSniper(AuctionSniper sniper) {
		for (PortfolioListener listener : listeners) {
			listener.sniperAdded(sniper);
		}
	}

	public void addPortfolioListener(PortfolioListener listener) {
		this.listeners.add(listener);
	}
}
