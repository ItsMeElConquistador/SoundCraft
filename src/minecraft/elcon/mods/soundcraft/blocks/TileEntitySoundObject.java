package elcon.mods.soundcraft.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import elcon.mods.soundcraft.network.SoundNetwork;
import elcon.mods.soundcraft.network.SoundNetworkGroup;

public class TileEntitySoundObject extends TileEntity {

	public int group;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		group = nbt.getInteger("SoundGroupID");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("SoundGroupID", group);
	}
}
