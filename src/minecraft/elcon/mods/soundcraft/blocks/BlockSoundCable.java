package elcon.mods.soundcraft.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSoundCable extends BlockContainer {

	protected BlockSoundCable(int i) {
		super(i, Material.cloth);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

}
