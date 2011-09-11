package ro.flaviusstef.goos;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.*;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class AuctionSniperDriver extends JFrameDriver {
	
	@SuppressWarnings("unchecked")
	public AuctionSniperDriver (int timeoutMillis) {
		super(new GesturePerformer(),
		      JFrameDriver.topLevelFrame(
		        named(MainWindow.MAIN_WINDOW_NAME), 
		        showingOnScreen()),
		      new AWTEventQueueProber(timeoutMillis, 100));
	}
	
	@SuppressWarnings("unchecked")
	public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String status) {
		JTableDriver table = new JTableDriver(this);
		table.hasRow(matching(withLabelText(itemId), withLabelText(String.valueOf(lastPrice)),
		withLabelText(String.valueOf(lastBid)), withLabelText(status)));
	}

	@SuppressWarnings("unchecked")
	public void hasColumnTitles() {
		JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
		headers.hasHeaders(matching(withLabelText("Item"), withLabelText("Last Price"), 
				           withLabelText("Last Bid"), withLabelText("State")));
	}

	public void startBiddingFor(String itemId) {
		JTextFieldDriver field = itemIdField();
		field.selectAll();
		itemIdField().deleteSelectedText();
		itemIdField().typeText(itemId);
		bidButton().click();
	}
	
	@SuppressWarnings("unchecked")
	private JButtonDriver bidButton() {
		return new JButtonDriver(this, JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
	}

	// TODO: ce-i asta cu unchecked?
	@SuppressWarnings("unchecked")
	private JTextFieldDriver itemIdField() {
		JTextFieldDriver newItemId = 
			new JTextFieldDriver(this, JTextField.class, named(MainWindow.NEW_ITEM_ID_NAME));
		newItemId.focusWithMouse();
		return newItemId;
	}

}
