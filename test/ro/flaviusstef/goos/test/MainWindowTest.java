package ro.flaviusstef.goos.test;

import org.junit.Test;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

import ro.flaviusstef.goos.AuctionSniperDriver;
import ro.flaviusstef.goos.MainWindow;
import ro.flaviusstef.goos.SnipersTableModel;
import ro.flaviusstef.goos.UserRequestListener;
import static org.hamcrest.CoreMatchers.*;


public class MainWindowTest {
	private final SnipersTableModel tableModel = new SnipersTableModel();
	private final MainWindow mainWindow = new MainWindow(tableModel);
	private final AuctionSniperDriver driver = new AuctionSniperDriver(100);
	
	@Test
	public void makesUserRequestWhenJoinButtonClicked() {
		final ValueMatcherProbe<String> buttonProbe = 
			new ValueMatcherProbe<String>(equalTo("un item-id"), "join request");
		
		mainWindow.addUserRequestListener(new UserRequestListener() {
			public void joinAuction(String itemId) {
				buttonProbe.setReceivedValue(itemId);
			}
		});
		
		driver.startBiddingFor("un item-id");
		driver.check(buttonProbe);
	}
	
}
