package elcon.mods.soundcraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabSoundCraft extends CreativeTabs {

	public CreativeTabSoundCraft(String label) {
		super(label);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getTabIconItemIndex() {
		return Item.record13.itemID;
	}
}
