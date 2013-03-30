package elcon.mods.soundcraft.network;

public interface ISoundConductor {

	public SoundNetworkGroup group = null;
	
	public boolean canConductSound(Sound sound);
}
