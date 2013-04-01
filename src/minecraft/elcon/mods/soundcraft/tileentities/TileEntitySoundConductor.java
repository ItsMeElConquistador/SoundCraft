package elcon.mods.soundcraft.tileentities;

import elcon.mods.soundcraft.SoundCableType;
import elcon.mods.soundcraft.SoundCraftConfig;
import elcon.mods.soundcraft.sounds.Sound;

public abstract class TileEntitySoundConductor extends TileEntitySoundObject {
	
	public void conductSound(int side, Sound sound) {
		for(int i = 0; i < 6; i++) {
			if(i != side && neighbors[i] != null ) {
				if(neighbors[i] instanceof TileEntitySoundConductor) {
					boolean canConduct = true;
					if(neighbors[i] instanceof TileEntitySoundCable) {
						TileEntitySoundCable cable = ((TileEntitySoundCable) neighbors[i]);
						if(sound.blocksTraveled >= SoundCableType.soundCables[cable.worldObj.getBlockMetadata(cable.xCoord, cable.yCoord, cable.zCoord)].maxBlockDistance) {
							canConduct = false;
						}
						if(cable.isDetector) {
							System.out.println("detector");
							cable.emitRedstone = true;
							cable.emitTicks = 3000;
							worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, SoundCraftConfig.soundCableID);
						}
					}
					if(canConduct) {
						sound.blocksTraveled++;
						((TileEntitySoundConductor) neighbors[i]).conductSound(getOppositeSide(i), sound);
					}
				} else if(neighbors[i] instanceof TileEntitySoundAcceptor) {
					sound.blocksTraveled++;
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
