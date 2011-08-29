package ro.flaviusstef.goos;

import javax.swing.SwingUtilities;

public class SniperStateDisplayer implements SniperListener {
	private MainWindow ui;
	
	public static final String STATUS_BIDDING = "bidding";
	public static final String STATUS_JOINING = "joining";
	public static final String STATUS_LOST    = "lost";

	public SniperStateDisplayer(MainWindow ui) {
		this.ui = ui;
	}

	@Override
	public void sniperLost() {
		showStatus(SniperStateDisplayer.STATUS_LOST);
	}

	@Override
	public void sniperBidding() {
		showStatus(SniperStateDisplayer.STATUS_BIDDING);
	}

	@Override
	public void sniperWinning() {
		showStatus(MainWindow.STATUS_WINNING);
	}
	
	private void showStatus(final String status) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ui.showStatus(status);
			}
		});
	}
}