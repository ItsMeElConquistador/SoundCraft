package elcon.mods.soundcraft.tileentities;

import net.minecraft.entity.player.EntityPlayerMP;
import elcon.mods.soundcraft.SoundCraft;
import elcon.mods.soundcraft.SoundCraftPacketHandler;
import elcon.mods.soundcraft.sounds.Sound;

public class TileEntitySpeaker extends TileEntitySoundAcceptor {

	@Override
	public boolean canAcceptSound(Sound sound) {
		return true;
	}

	@Override
	public void receiveSound(Sound sound) {
		if(SoundCraft.proxy.getMCServer().worldServerForDimension(worldObj.provider.dimensionId) != null) {
			for(Object o :SoundCraft.proxy.getMCServer().worldServerForDimension(worldObj.provider.dimensionId).playerEntities) {
				EntityPlayerMP player = null;
				if(o instanceof EntityPlayerMP) {
					player = (EntityPlayerMP) o;
					SoundCraftPacketHandler.sendSound(player, xCoord, yCoord, zCoord, sound.type, sound);
				}
			}	
		}
	}
}
