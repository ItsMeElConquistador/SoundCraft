package elcon.mods.soundcraft.tileentities;

import elcon.mods.soundcraft.Sound;

public class TileEntitySpeaker extends TileEntitySoundAcceptor {

	@Override
	public boolean canAcceptSound(Sound sound) {
		return true;
	}

	@Override
	public void receiveSound(Sound sound) {
		System.out.println("received sound: " + sound.name);
		if(sound.name.equalsIgnoreCase("stop")) {
			
		} else {
			worldObj.playSound(xCoord, yCoord, zCoord, sound.name, 1.0F, 1.0F, false);
		}
	}
}
