package ro.flaviusstef.goos;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SnipersTableModel extends AbstractTableModel {
	private static final String[] STATUS_TEXT = {SniperStateDisplayer.STATUS_JOINING,
		                                         SniperStateDisplayer.STATUS_BIDDING,
		                                         SniperStateDisplayer.STATUS_WINNING};
	private SniperSnapshot snapshot = new SniperSnapshot("", 0, 0, SniperState.JOINING);
	private String state = SniperStateDisplayer.STATUS_JOINING;
	
	public enum Column {
		ITEM_IDENTIFIER,
		LAST_PRICE,
		LAST_BID,
		SNIPER_STATE;
		
		public static Column at(int offset) { return values()[offset]; }
	}

	public int getColumnCount() { return Column.values().length; }
	public int getRowCount() { return 1; }
	
	public Object getValueAt(int row, int column) { 
		switch (Column.at(column)) {
		case ITEM_IDENTIFIER:
			return snapshot.itemId;
		case LAST_PRICE:
			return snapshot.lastPrice;
		case LAST_BID:
			return snapshot.lastBid;
		case SNIPER_STATE:
			return state;
		default:
			throw new IllegalArgumentException("No column at " + column);
		}
	}
	
	public void sniperStatusChanged(SniperSnapshot newSnapshot) {
		this.snapshot = newSnapshot;
		this.state = STATUS_TEXT[newSnapshot.state.ordinal()];
		fireTableRowsUpdated(0, 0);
	}
	
	public void setStatusText(String status) {
		this.state = status;
		fireTableRowsUpdated(0, 0);
	}

}
