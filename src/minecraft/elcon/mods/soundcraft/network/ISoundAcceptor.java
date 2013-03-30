package elcon.mods.soundcraft.network;

public interface ISoundAcceptor {
	
	public boolean canAcceptSound(Sound sound);
	
	public void receiveSound(Sound sound);
}
