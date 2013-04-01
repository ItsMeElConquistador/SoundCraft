package elcon.mods.soundcraft.items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.soundcraft.SoundCableType;

public class ItemSoundCable extends ItemBlockWithMetadata {

	public ItemSoundCable(int i, Block block) {
		super(i, block);
	}
	
	@Override
	public String getLocalizedName(ItemStack stack) {
		int i = stack.getItemDamage();
		String s = getUnlocalizedName(stack);
        return s == null ? "" : SoundCableType.soundCables[i].name + " " + StatCollector.translateToLocal(s);
	}
}
