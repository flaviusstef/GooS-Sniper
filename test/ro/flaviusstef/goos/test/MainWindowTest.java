package ro.flaviusstef.goos.test;

import org.junit.Test;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

import ro.flaviusstef.goos.Item;
import ro.flaviusstef.goos.SniperPortfolio;
import ro.flaviusstef.goos.UserRequestListener;
import ro.flaviusstef.goos.ui.MainWindow;
import static org.hamcrest.CoreMatchers.*;


public class MainWindowTest {
	private final SniperPortfolio portfolio = new SniperPortfolio();
	private final MainWindow mainWindow = new MainWindow(portfolio);
	private final AuctionSniperDriver driver = new AuctionSniperDriver(100);
	
	@Test
	public void makesUserRequestWhenJoinButtonClicked() {
		// TODO: no, asta ce Dumnezo mai ii?
		final ValueMatcherProbe<Item> itemProbe = 
			new ValueMatcherProbe<Item>(equalTo(new Item("un item-id", 789)), "item request");
		
		mainWindow.addUserRequestListener(new UserRequestListener() {
			public void joinAuction(Item item) {
				itemProbe.setReceivedValue(item);
			}
		});
		
		driver.startBiddingFor("un item-id", 789);
		driver.check(itemProbe);
	}
	
}
