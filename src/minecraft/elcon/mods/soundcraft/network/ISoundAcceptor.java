package elcon.mods.soundcraft.network;

public abstract interface ISoundAcceptor {

	public SoundNetworkGroup group = null;
	
	public abstract boolean canAcceptSound(Sound sound);
	
	public abstract void receiveSound(Sound sound);
}
