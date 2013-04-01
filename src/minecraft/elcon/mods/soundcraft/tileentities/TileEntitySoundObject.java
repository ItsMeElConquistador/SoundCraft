package elcon.mods.soundcraft.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySoundObject extends TileEntity {

	public TileEntitySoundObject[] neighbors = new TileEntitySoundObject[6];
	
	public int group;
	public int node;
	
	public boolean checkedNeighbors = false;
	
	@Override
	public void updateEntity() {
		if(!checkedNeighbors) {
			
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		group = nbt.getInteger("SoundGroupID");
		node = nbt.getInteger("SoundNodeID");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("SoundGroupID", group);
		nbt.setInteger("SoundNodeID", node);
	}
}
