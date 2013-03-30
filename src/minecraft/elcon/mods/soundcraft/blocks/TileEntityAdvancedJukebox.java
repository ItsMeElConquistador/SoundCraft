package elcon.mods.soundcraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import elcon.mods.soundcraft.network.ISoundSource;

public class TileEntityAdvancedJukebox extends TileEntitySoundObject implements ISoundSource {

	public ItemStack record;

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);

		if(par1NBTTagCompound.hasKey("RecordItem")) {
			setRecord(ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("RecordItem")));
		} else if(par1NBTTagCompound.getInteger("Record") > 0) {
			setRecord(new ItemStack(par1NBTTagCompound.getInteger("Record"), 1, 0));
		}
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);

		if(getRecord() != null) {
			par1NBTTagCompound.setCompoundTag("RecordItem", getRecord().writeToNBT(new NBTTagCompound()));
			par1NBTTagCompound.setInteger("Record", getRecord().itemID);
		}
	}

	public ItemStack getRecord() {
		return record;
	}

	public void setRecord(ItemStack stack) {
		record = stack;
		onInventoryChanged();
	}
}
