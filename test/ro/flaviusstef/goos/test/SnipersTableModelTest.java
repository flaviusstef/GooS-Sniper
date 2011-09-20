package ro.flaviusstef.goos.test;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;

import com.objogate.exception.Defect;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import ro.flaviusstef.goos.AuctionSniper;
import ro.flaviusstef.goos.Item;
import ro.flaviusstef.goos.SniperSnapshot;
import ro.flaviusstef.goos.SniperState;
import ro.flaviusstef.goos.ui.Column;
import ro.flaviusstef.goos.ui.SnipersTableModel;

@RunWith(JMock.class)
public class SnipersTableModelTest {

	private final Mockery context = new Mockery();
	private TableModelListener listener = context.mock(TableModelListener.class);
	private final SnipersTableModel model = new SnipersTableModel();
	private AuctionSniper sniper = new AuctionSniper(new Item("item id",234), null);
	
	@Before
	public void attachModelListener() {
		model.addTableModelListener(listener);
	}
	
	@Test
	public void hasEnoughColumns() {
		assertThat(model.getColumnCount(), equalTo(Column.values().length));
	}
	
	@Test
	public void holdsSnipersInAdditionOrder() {
		context.checking(new Expectations(){{
			ignoring(listener);
		}});
		AuctionSniper another_sniper = new AuctionSniper(new Item("item 2", 345), null);
		
		model.sniperAdded(sniper);
		model.sniperAdded(another_sniper);
		
		assertEquals("item id", cellValue(0, Column.ITEM_IDENTIFIER));
		assertEquals("item 2", cellValue(1, Column.ITEM_IDENTIFIER));
	}
	
	@Test
	public void updatesCorrectRowForSniper() {
		context.checking(new Expectations(){{
			ignoring(listener);
		}});
		
		SniperSnapshot s11 = sniper.getSnapshot().bidding(100, 150);
		AuctionSniper another_sniper = new AuctionSniper(new Item("another one", 345), null);
		
		model.sniperAdded(sniper); model.sniperAdded(another_sniper);
		
		assertEquals(SnipersTableModel.textFor(SniperState.JOINING), cellValue(0, Column.SNIPER_STATE));
		model.sniperStateChanged(s11);
		assertEquals(SnipersTableModel.textFor(SniperState.BIDDING), cellValue(0, Column.SNIPER_STATE));
	}
	
	@Test(expected = Defect.class)
	public void throwsDefectIfNoExistingSniperForAnUpdate() {
		
		context.checking(new Expectations(){{
			ignoring(listener);
		}});
		
		SniperSnapshot another_sniper = SniperSnapshot.joining("another");
		
		model.sniperAdded(sniper); 
		
		model.sniperStateChanged(another_sniper);
	}
	
	@Test
	public void setsSniperValuesInColumns() {
		context.checking(new Expectations(){{
			allowing(listener).tableChanged(with(anyInsertionEvent()));
			one(listener).tableChanged(with(aChangeInRow(0)));
		}});
		
		model.sniperAdded(sniper);
		model.sniperStateChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING));
		
		assertColumnEquals(Column.ITEM_IDENTIFIER, "item id");
		assertColumnEquals(Column.LAST_PRICE, 555);
		assertColumnEquals(Column.LAST_BID, 666);
		assertColumnEquals(Column.SNIPER_STATE, SnipersTableModel.textFor(SniperState.BIDDING));
	}


	private void assertColumnEquals(Column column, Object expected) {
		final int rowIndex = 0;
		final int columnIndex = column.ordinal();
		assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
	}
	
	@Test
	public void setsUpColumnHeadings() {
		for (Column column : Column.values()) {
			assertEquals(column.name, model.getColumnName(column.ordinal()));
		}
	}

	@Test
	public void notifiesListenersWhenAddingASniper() {
		AuctionSniper joining = new AuctionSniper(new Item("item",234), null);
		context.checking(new Expectations() {{
			one(listener).tableChanged(with(anInsertionAtRow(0)));
		}});
		
		assertEquals(0, model.getRowCount());
		
		model.sniperAdded(joining);
		
		assertEquals(1, model.getRowCount());
		assertRowMatchesSniper(0, joining);
		
	}
	
	private void assertRowMatchesSniper(int row, AuctionSniper sniper) {
		SniperSnapshot snapshot = sniper.getSnapshot();
		assertEquals(snapshot.itemId, cellValue(row, Column.ITEM_IDENTIFIER));
		assertEquals(snapshot.lastPrice, cellValue(row, Column.LAST_PRICE));
		assertEquals(snapshot.lastBid, cellValue(row, Column.LAST_BID));
		assertEquals(SnipersTableModel.textFor(snapshot.state), cellValue(row, Column.SNIPER_STATE));
	}

	private Object cellValue(int row, Column column) {
		return model.getValueAt(row, column.ordinal());
	}

	// TODO: what is this?
	protected Matcher<TableModelEvent> aChangeInRow(int row) {
		return samePropertyValuesAs(new TableModelEvent(model, row));
	}
	
	protected Matcher<TableModelEvent> anInsertionAtRow(int row) {
		return samePropertyValuesAs(new TableModelEvent(model, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}
	
	protected Matcher<TableModelEvent> anyInsertionEvent() {
		return hasProperty("type", equalTo(TableModelEvent.INSERT));
	}
}
