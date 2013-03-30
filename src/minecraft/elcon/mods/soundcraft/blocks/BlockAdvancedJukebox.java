package elcon.mods.soundcraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import elcon.mods.soundcraft.network.ISoundSource;

public class BlockAdvancedJukebox extends BlockContainer {

	public Icon jukeboxSide;
	public Icon jukeboxTop;
	
	public BlockAdvancedJukebox(int i) {
		super(i, Material.iron);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int i, int j) {
		return i == 1 ? jukeboxTop : jukeboxSide;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		jukeboxSide = iconRegister.registerIcon("soundcraft:jukeboxSide");
		jukeboxTop = iconRegister.registerIcon("soundcraft:jukebox");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityAdvancedJukebox();
	}
}
