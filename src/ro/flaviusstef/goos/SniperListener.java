package ro.flaviusstef.goos;

public interface SniperListener {
	public void sniperLost();
	public void sniperWon();
	public void sniperStateChanged(SniperSnapshot newSnapshot);
}
