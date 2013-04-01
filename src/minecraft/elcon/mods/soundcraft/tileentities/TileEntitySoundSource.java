package elcon.mods.soundcraft.tileentities;

import elcon.mods.soundcraft.sounds.Sound;

public abstract class TileEntitySoundSource extends TileEntitySoundObject {

	public void sendSound(Sound sound) {
		for(int i = 0; i < 6; i++) {
			if(neighbors[i] != null ) {
				if(neighbors[i] instanceof TileEntitySoundConductor) {
					((TileEntitySoundConductor) neighbors[i]).conductSound(getOppositeSide(i), sound);
				} else if(neighbors[i] instanceof TileEntitySoundAcceptor) {
					((TileEntitySoundAcceptor) neighbors[i]).receiveSound(sound);
				}
			}
		}
	}
	
	public int getOppositeSide(int i) {
		switch(i) {
			case 0: return 1;
			case 1: return 0;
			case 2: return 3;
			case 3: return 2;
			case 4: return 5;
			case 5: return 4;
		}
		return 0;
	}
}
