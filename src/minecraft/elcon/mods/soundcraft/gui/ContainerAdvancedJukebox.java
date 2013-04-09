package elcon.mods.soundcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import elcon.mods.soundcraft.tileentities.TileEntityAdvancedJukebox;

public class ContainerAdvancedJukebox extends Container {
	
	public TileEntityAdvancedJukebox tileEntity;
	public World world;
	public int posX;
	public int posY;
	public int posZ;
	
	public ContainerAdvancedJukebox(TileEntityAdvancedJukebox tile, InventoryPlayer inv, World w, int x, int y, int z) {
		tileEntity = tile;
		world = w;
		posX = x;
		posY = y;
		posZ = z;
		
		for(int i = 0; i < 8; i++) {
			addSlotToContainer(new SlotRecord(tileEntity, i, 8, 16 + (i * 20)));
		}
		
		for(int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inv, i, 8 + (i * 18), 176));
		}
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		super.onCraftMatrixChanged(inventory);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		ItemStack stack = null;
		Slot slot = (Slot) inventorySlots.get(i);

		if(slot != null && slot.getHasStack()) {
			ItemStack var5 = slot.getStack();
			stack = var5.copy();
			return null;
		}
		return stack;
	}
}
