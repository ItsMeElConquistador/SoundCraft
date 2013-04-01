package elcon.mods.soundcraft.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAdvancedJukebox extends TileEntitySoundSource implements IInventory {

	public ItemStack[] stacks = new ItemStack[8];
	
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

	@Override
	public int getSizeInventory() {
		return 8;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack stack;

		if(stacks[i].stackSize <= j) {
			stack = stacks[i];
			stacks[i] = null;
			return stack;
		} else {
			stack = stacks[i].splitStack(j);

			if(stacks[i].stackSize == 0) {
				stacks[i] = null;
			}
			return stack;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if(stacks[i] != null) {
			return stacks[i];
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		stacks[i] = itemstack;
	}

	@Override
	public String getInvName() {
		return "soundcraft.advancedJukebox";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		if(itemstack != null) {
			if(itemstack.getItem() instanceof ItemRecord) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}
}
