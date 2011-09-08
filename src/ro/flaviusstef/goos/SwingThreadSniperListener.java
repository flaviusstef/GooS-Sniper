package ro.flaviusstef.goos;

import javax.swing.SwingUtilities;

public class SwingThreadSniperListener implements SniperListener {
	private SnipersTableModel snipers;

	public SwingThreadSniperListener(SnipersTableModel snipers) {
		this.snipers = snipers;
	}

	@Override
	public void sniperStateChanged(final SniperSnapshot newSnapshot) {
		// TODO: what is this?
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				snipers.sniperStateChanged(newSnapshot);
			}
		});
	}
}