package ro.flaviusstef.goos;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.objogate.exception.Defect;

@SuppressWarnings("serial")
public class SnipersTableModel extends AbstractTableModel implements SniperListener {
	private static final String[] STATUS_TEXT = {"joining", "bidding", "winning", "lost", "won"};
	private ArrayList<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();
	
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
		fireTableRowsUpdated(0, 0);
	}
	
	private int rowMatching(SniperSnapshot snapshot) {
		for (int i=0; i<snapshots.size(); i++) {
			if (snapshot.isForSameItemAs(snapshots.get(i)))
				return i;
		}
		throw new Defect("Cannot find match for" + snapshot);
	}
	
	public void addSniper(SniperSnapshot sniper) {
		snapshots.add(sniper);
		fireTableRowsInserted(0, 0);
	}
}