package ro.flaviusstef.goos;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SnipersTableModel extends AbstractTableModel implements SniperListener {
	private static final String[] STATUS_TEXT = {"joining", "bidding", "winning", "lost", "won"};
	private SniperSnapshot snapshot = new SniperSnapshot("", 0, 0, SniperState.JOINING);
	
	public int getColumnCount() { return Column.values().length; }
	public int getRowCount() { return 1; }
	
	public Object getValueAt(int row, int column) { 
		return Column.at(column).valueIn(snapshot);
	}
	
	// TODO: state.ordinal() ?
	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}
	
	@Override
	public void sniperStateChanged(SniperSnapshot newSnapshot) {
		this.snapshot = newSnapshot;
		fireTableRowsUpdated(0, 0);
	}
}