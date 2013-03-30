package elcon.mods.soundcraft.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import elcon.mods.soundcraft.network.ISoundSource;

public class BlockAdvancedJukebox extends BlockContainer {

	protected BlockAdvancedJukebox(int i) {
		super(i, Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityAdvancedJukebox();
	}
}
