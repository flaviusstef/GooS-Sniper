package ro.flaviusstef.goos.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import ro.flaviusstef.goos.AuctionSniper;
import ro.flaviusstef.goos.PortfolioListener;
import ro.flaviusstef.goos.SniperListener;
import ro.flaviusstef.goos.SniperSnapshot;
import ro.flaviusstef.goos.SniperState;

import com.objogate.exception.Defect;

@SuppressWarnings("serial")
public class SnipersTableModel extends AbstractTableModel implements SniperListener, PortfolioListener {
	private static final String[] STATUS_TEXT = {"joining", "bidding", "winning", "losing", "lost", "won"};
	private ArrayList<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();
	private ArrayList<AuctionSniper>notToBeGCd = new ArrayList<AuctionSniper>();
	
	public int getColumnCount() { return Column.values().length; }
	public int getRowCount() { return snapshots.size(); }
	
	public Object getValueAt(int row, int column) { 
		return Column.at(column).valueIn(snapshots.get(row));
	}
	
	@Override
	public String getColumnName(int column) {
		return Column.at(column).name;
	}
	
	// TODO: state.ordinal() ?
	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}
	
	@Override
	public void sniperStateChanged(SniperSnapshot newSnapshot) {
		int row = rowMatching(newSnapshot);
		snapshots.set(row, newSnapshot);
		fireTableRowsUpdated(row, row);
	}
	
	private int rowMatching(SniperSnapshot snapshot) {
		for (int i=0; i<snapshots.size(); i++) {
			if (snapshot.isForSameItemAs(snapshots.get(i)))
				return i;
		}
		throw new Defect("Cannot find match for" + snapshot);
	}
	
	private void addSniperSnapshot(SniperSnapshot sniperSnapshot) {
		snapshots.add(sniperSnapshot);
		int row = snapshots.size() - 1;
		fireTableRowsInserted(row, row);
	}
	
	@Override
	public void sniperAdded(AuctionSniper sniper) {
		notToBeGCd.add(sniper);
		addSniperSnapshot(sniper.getSnapshot());
		sniper.addSniperListener(new SwingThreadSniperListener(this));
	}
}