package elcon.mods.soundcraft.tileentities;

import elcon.mods.soundcraft.Sound;

public abstract class TileEntitySoundConductor extends TileEntitySoundObject {
	
	public TileEntitySoundConductor[] neighbors = new TileEntitySoundConductor[6];
	
	public void conductSound(int side, Sound s) {
		for(int i = 0; i < 6; i++) {
			if(i != side && neighbors[i] != null ) {
				neighbors[i].conductSound(getOppositeSide(i), s);
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
