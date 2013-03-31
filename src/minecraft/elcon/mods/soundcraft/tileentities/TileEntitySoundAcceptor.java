package elcon.mods.soundcraft.tileentities;

import elcon.mods.soundcraft.network.Sound;

public abstract class TileEntitySoundAcceptor extends TileEntitySoundObject {

	public abstract boolean canAcceptSound(Sound sound);
	
	public abstract void receiveSound(Sound sound);
}