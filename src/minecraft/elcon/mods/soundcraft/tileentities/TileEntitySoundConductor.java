package elcon.mods.soundcraft.tileentities;

import elcon.mods.soundcraft.Sound;

public abstract class TileEntitySoundConductor extends TileEntitySoundObject {
	
	public TileEntitySoundObject[] neighbors = new TileEntitySoundObject[6];
	
	public void conductSound(int side, Sound s) {
		
	}
}
