package elcon.mods.soundcraft;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import elcon.mods.soundcraft.blocks.BlockSoundCable;
import elcon.mods.soundcraft.tileentities.TileEntitySoundCable;

public class SoundCraftBlockRenderingHandler implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int renderID, RenderBlocks renderer) {
		if(renderID == SoundCraftConfig.soundCableRenderID) {
			BlockSoundCable blockSoundCable = (BlockSoundCable) block;
			int meta = blockAccess.getBlockMetadata(x, y, z);
			TileEntitySoundCable te = (TileEntitySoundCable) blockAccess.getBlockTileEntity(x, y, z);
			
			renderer.renderAllFaces = true;
			renderer.setOverrideBlockTexture(SoundCableType.soundCables[meta].textures[te.color]);

			renderer.setRenderBounds(pixels(6), pixels(6), pixels(6), pixels(10), pixels(10), pixels(10));
			renderer.renderStandardBlock(blockSoundCable, x, y, z);
			
			if(te.directions[0]) {
				renderer.setRenderBounds(pixels(0), pixels(6), pixels(6), pixels(6), pixels(10), pixels(10));
				renderer.renderStandardBlock(blockSoundCable, x, y, z);
			}
			if(te.directions[1]) {
				renderer.setRenderBounds(pixels(10), pixels(6), pixels(6), pixels(16), pixels(10), pixels(10));
				renderer.renderStandardBlock(blockSoundCable, x, y, z);
			}
			if(te.directions[2]) {
				renderer.setRenderBounds(pixels(6), pixels(0), pixels(6), pixels(10), pixels(6), pixels(10));
				renderer.renderStandardBlock(blockSoundCable, x, y, z);
			}
			if(te.directions[3]) {
				renderer.setRenderBounds(pixels(6), pixels(10), pixels(6), pixels(10), pixels(16), pixels(10));
				renderer.renderStandardBlock(blockSoundCable, x, y, z);
			}
			if(te.directions[4]) {
				renderer.setRenderBounds(pixels(6), pixels(6), pixels(0), pixels(10), pixels(10), pixels(6));
				renderer.renderStandardBlock(blockSoundCable, x, y, z);
			}
			if(te.directions[5]) {
				renderer.setRenderBounds(pixels(6), pixels(6), pixels(10), pixels(10), pixels(10), pixels(16));
				renderer.renderStandardBlock(blockSoundCable, x, y, z);
			}
			
			renderer.renderAllFaces = false;
			renderer.clearOverrideBlockTexture();
			
			blockSoundCable.setBlockBoundsBasedOnState(blockAccess, x, y, z);
			
			return true;
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

	public float pixels(int i) {
		return (float) (0.0625F * i);
	}
}
