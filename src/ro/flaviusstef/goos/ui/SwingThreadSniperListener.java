package ro.flaviusstef.goos.ui;

import javax.swing.SwingUtilities;

import ro.flaviusstef.goos.SniperListener;
import ro.flaviusstef.goos.SniperSnapshot;

public class SwingThreadSniperListener implements SniperListener {
	private SnipersTableModel snipers;

	public SwingThreadSniperListener(SnipersTableModel snipers) {
		this.snipers = snipers;
	}

	@Override
	public void sniperStateChanged(final SniperSnapshot newSnapshot) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				snipers.sniperStateChanged(newSnapshot);
			}
		});
	}
}