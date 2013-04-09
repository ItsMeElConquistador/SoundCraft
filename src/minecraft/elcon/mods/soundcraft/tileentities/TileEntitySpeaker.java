package elcon.mods.soundcraft.tileentities;

import net.minecraft.entity.player.EntityPlayerMP;
import elcon.mods.soundcraft.SoundCraft;
import elcon.mods.soundcraft.SoundCraftPacketHandler;
import elcon.mods.soundcraft.sounds.Sound;

public class TileEntitySpeaker extends TileEntitySoundAcceptor {

	//public final float baseVolume = 0.25F;
	
	//public int neighborSpeakers = 0;
	//public float volume = 0.25F;
	//public boolean updateNeighbors = true;
	//public boolean playSound = true;
	
	/*@Override
	public void onUpdateNeighbors() {
		if(updateNeighbors) {
			System.out.println("checkd neighbors");
			neighborSpeakers = 0;
			for(int i = 0; i < 6; i++) {
				if(neighbors[i] != null) {
					if(neighbors[i] instanceof TileEntitySpeaker) {
						neighborSpeakers++;
						System.out.println("found neighbor " + i);
						((TileEntitySpeaker) neighbors[i]).updateNeighbors = false;
						((TileEntitySpeaker) neighbors[i]).playSound = false;
					}
				}
			}
			volume = neighborSpeakers * baseVolume;
		}
	}*/
	
	@Override
	public boolean canAcceptSound(Sound sound) {
		return true;
	}

	@Override
	public void receiveSound(Sound sound) {
		//System.out.println("can play sound: " + playSound + " - should update: " + updateNeighbors);
		//if(playSound) {
			if(SoundCraft.proxy.getMCServer().worldServerForDimension(worldObj.provider.dimensionId) != null) {
				for(Object o :SoundCraft.proxy.getMCServer().worldServerForDimension(worldObj.provider.dimensionId).playerEntities) {
					EntityPlayerMP player = null;
					if(o instanceof EntityPlayerMP) {
						player = (EntityPlayerMP) o;
						//System.out.println(xCoord + "," + yCoord + "," + zCoord + " - speakers: " + neighborSpeakers + " - volume: " + volume);
						//sound.volume = volume;
						SoundCraftPacketHandler.sendSound(player, xCoord, yCoord, zCoord, sound.type, sound);
					}
				}	
			}
		//}
	}
}
