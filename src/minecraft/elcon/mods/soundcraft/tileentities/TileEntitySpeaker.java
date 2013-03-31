package elcon.mods.soundcraft.tileentities;

import elcon.mods.soundcraft.network.Sound;

public class TileEntitySpeaker extends TileEntitySoundAcceptor {

	@Override
	public boolean canAcceptSound(Sound sound) {
		return true;
	}

	@Override
	public void receiveSound(Sound sound) {
		System.out.println("received sound: " + sound.name);
		worldObj.playSound(xCoord, yCoord, zCoord, sound.name, 1.0F, 1.0F, false);
	}
}
