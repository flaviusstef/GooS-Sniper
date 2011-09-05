package ro.flaviusstef.goos;

import javax.swing.SwingUtilities;

public class SniperStateDisplayer implements SniperListener {
	private MainWindow ui;

	public static final String STATUS_WINNING = "winning";
	
	public static final String STATUS_BIDDING = "bidding";
	public static final String STATUS_JOINING = "joining";
	public static final String STATUS_LOST    = "lost";
	public static final String STATUS_WON    = "won";

	public SniperStateDisplayer(MainWindow ui) {
		this.ui = ui;
	}

	public void sniperLost()    { showStatus(STATUS_LOST); }
	public void sniperBidding() { showStatus(STATUS_BIDDING); }
	public void sniperWinning() { showStatus(STATUS_WINNING); }
	public void sniperWon() { showStatus(STATUS_WON); }
	
	private void showStatus(final String status) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ui.showStatus(status);
			}
		});
	}
}