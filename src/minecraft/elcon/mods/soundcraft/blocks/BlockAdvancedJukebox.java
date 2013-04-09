package elcon.mods.soundcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.soundcraft.SoundCraft;
import elcon.mods.soundcraft.SoundCraftConfig;
import elcon.mods.soundcraft.tileentities.TileEntityAdvancedJukebox;

public class BlockAdvancedJukebox extends BlockContainer {

	public Icon jukeboxSide;
	public Icon jukeboxTop;

	public BlockAdvancedJukebox(int i) {
		super(i, Material.iron);
	}

	public static boolean isTop(int i, int j) {
		return i == 1;
	}

	public void onPoweredBlockChange(World world, int x, int y, int z, boolean par5) {
		TileEntityAdvancedJukebox tile = (TileEntityAdvancedJukebox) world.getBlockTileEntity(x, y, z);
		if(tile == null) {
			tile = new TileEntityAdvancedJukebox();
			world.setBlockTileEntity(x, y, z, tile);
		}
		tile.play();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
		boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);

		if(flag || par5 > 0 && Block.blocksList[par5].canProvidePower()) {
			onPoweredBlockChange(world, x, y, z, flag);
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
		if(entityPlayer.isSneaking()) {
			if(!world.isRemote) {
				TileEntityAdvancedJukebox tile = (TileEntityAdvancedJukebox) world.getBlockTileEntity(x, y, z);
				if(tile == null) {
					tile = new TileEntityAdvancedJukebox();
					world.setBlockTileEntity(x, y, z, tile);
				}
				entityPlayer.openGui(SoundCraft.instance, 0, world, x, y, z);
				return true;
			}
		}
		if(entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof ItemRecord) {
			TileEntityAdvancedJukebox tile = (TileEntityAdvancedJukebox) world.getBlockTileEntity(x, y, z);
			if(tile == null) {
				tile = new TileEntityAdvancedJukebox();
				world.setBlockTileEntity(x, y, z, tile);
			}
			tile.stop();
			tile.stacks[0] = new ItemStack(entityPlayer.getHeldItem().itemID, 1, 0);
			entityPlayer.getHeldItem().stackSize--;
			tile.play(0);
		}

		return false;
	}

	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {		
		TileEntityAdvancedJukebox tile = (TileEntityAdvancedJukebox) world.getBlockTileEntity(x, y, z);
		if(tile != null) {
			for(int i = 0; i < 8; i++) {
				float f = 0.7F;
                double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.2D + 0.6D;
                double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                ItemStack itemstack1 = tile.stacks[i].copy();
                EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, itemstack1);
                entityitem.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(entityitem);
			}
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
		if(!par1World.isRemote) {
			super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, 0);
		}
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	/*public int getComparatorInputOverride(World world, int par2, int par3, int par4, int par5) {
		ItemStack itemstack = ((TileEntityAdvancedJukebox) world.getBlockTileEntity(par2, par3, par4)).getRecord();
		return itemstack == null ? 0 : itemstack.itemID + 1 - Item.record13.itemID;
	}*/

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

	public void updateConnections(World world, int x, int y, int z) {
		if(world.getBlockId(x - 1, y, z) == SoundCraftConfig.soundCableID) {
			((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x - 1, y, z);
		}
		if(world.getBlockId(x + 1, y, z) == SoundCraftConfig.soundCableID) {
			((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x + 1, y, z);
		}
		if(world.getBlockId(x, y - 1, z) == SoundCraftConfig.soundCableID) {
			((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y - 1, z);
		}
		if(world.getBlockId(x, y + 1, z) == SoundCraftConfig.soundCableID) {
			((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y + 1, z);
		}
		if(world.getBlockId(x, y, z - 1) == SoundCraftConfig.soundCableID) {
			((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y, z - 1);
		}
		if(world.getBlockId(x, y, z + 1) == SoundCraftConfig.soundCableID) {
			((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y, z + 1);
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isRemote) {
			updateConnections(world, x, y, z);
		}
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
		if(!world.isRemote) {
			updateConnections(world, x, y, z);
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int par5) {
		if(!world.isRemote) {
			updateConnections(world, x, y, z);
		}
	}
}
