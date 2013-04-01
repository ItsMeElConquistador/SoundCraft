package elcon.mods.soundcraft.tileentities;

import elcon.mods.soundcraft.gui.ContainerAdvancedJukebox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAdvancedJukebox extends TileEntitySoundSource implements IInventory {

	public ItemStack[] stacks = new ItemStack[8];
	
	public ItemStack record;
	
	public ContainerAdvancedJukebox container;
	
	public boolean[] next = new boolean[8];
	public boolean[] loop = new boolean[8];

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		for(int i = 0; i < 8; i++) {
			next[i] = nbt.getBoolean("next" + Integer.toString(i));
			loop[i] = nbt.getBoolean("loop" + Integer.toString(i));
		}
		
		if(nbt.hasKey("RecordItem")) {
			setRecord(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("RecordItem")));
		} else if(nbt.getInteger("Record") > 0) {
			setRecord(new ItemStack(nbt.getInteger("Record"), 1, 0));
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		for(int i = 0; i < 8; i++) {
			nbt.setBoolean("next" + Integer.toString(i), next[i]);
			nbt.setBoolean("loop" + Integer.toString(i), loop[i]);
		}
		
		if(getRecord() != null) {
			nbt.setCompoundTag("RecordItem", getRecord().writeToNBT(new NBTTagCompound()));
			nbt.setInteger("Record", getRecord().itemID);
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
		} else {
			stack = stacks[i].splitStack(j);

			if(stacks[i].stackSize == 0) {
				stacks[i] = null;
			}
		}
		if(container != null) {
			container.onCraftMatrixChanged(this);
		}
		return stack;
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
		if(container != null) {
			container.onCraftMatrixChanged(this);
		}
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
		if(itemstack != null && itemstack.getItem() != null) {
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
