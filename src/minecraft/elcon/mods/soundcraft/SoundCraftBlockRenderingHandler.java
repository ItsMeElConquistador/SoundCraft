package elcon.mods.soundcraft;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import elcon.mods.soundcraft.blocks.BlockSoundCable;

public class SoundCraftBlockRenderingHandler implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int renderID, RenderBlocks renderer) {
		if(renderID == SoundCraftConfig.soundCableRenderID) {
			BlockSoundCable blockSoundCable = (BlockSoundCable) block;
			
			int meta = world.getBlockMetadata(x, y, z);
			
			//renderer.setRenderBounds(1 / 16);
		}
		
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	@Override
	public int getRenderId() {
		return SoundCraftConfig.soundCableRenderID;
	}

}
