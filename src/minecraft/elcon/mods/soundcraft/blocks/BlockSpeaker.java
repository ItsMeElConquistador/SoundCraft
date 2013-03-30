package elcon.mods.soundcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpeaker extends Block {

	public Icon speakerSide;
	public Icon speakerFront;
	
	public BlockSpeaker(int i) {
		super(i, Material.iron);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int i, int j) {
		return i == 3 ? speakerFront : speakerSide;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		speakerSide = iconRegister.registerIcon("soundcraft:speakerSide");
		speakerFront = iconRegister.registerIcon("soundcraft:speaker");
	}
}
