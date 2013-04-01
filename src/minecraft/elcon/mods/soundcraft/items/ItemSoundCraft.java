package elcon.mods.soundcraft.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemSoundCraft extends Item {

	public String name = "";
	
	public ItemSoundCraft(int i, String name) {
		super(i);
		this.name = name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister) {
		iconIndex = iconRegister.registerIcon("soundcraft:" + name);
	}
}
