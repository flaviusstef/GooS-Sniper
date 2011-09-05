package ro.flaviusstef.goos;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SnipersTableModel extends AbstractTableModel {
	private SniperState state = new SniperState("", 0, 0);
	private String status = SniperStateDisplayer.STATUS_JOINING;
	
	public enum Column {
		ITEM_IDENTIFIER,
		LAST_PRICE,
		LAST_BID,
		SNIPER_STATUS;
		
		public static Column at(int offset) { return values()[offset]; }
	}

	public int getColumnCount() { return Column.values().length; }
	public int getRowCount() { return 1; }
	
	public Object getValueAt(int row, int column) { 
		switch (Column.at(column)) {
		case ITEM_IDENTIFIER:
			return state.itemId;
		case LAST_PRICE:
			return state.lastPrice;
		case LAST_BID:
			return state.lastBid;
		case SNIPER_STATUS:
			return status;
		default:
			throw new IllegalArgumentException("No column at " + column);
		}
	}
	
	public void sniperStatusChanged(SniperState state, String status) {
		this.state = state;
		this.status = status;
		fireTableRowsUpdated(0, 0);
	}
	public void setStatusText(String status) {
		this.status = status;
		fireTableRowsUpdated(0, 0);
	}

}
