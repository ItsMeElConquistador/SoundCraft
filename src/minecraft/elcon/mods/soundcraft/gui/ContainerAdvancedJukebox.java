package elcon.mods.soundcraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerAdvancedJukebox extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}
