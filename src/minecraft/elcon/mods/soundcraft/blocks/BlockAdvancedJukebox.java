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
import elcon.mods.soundcraft.SoundCraftConfig;
import elcon.mods.soundcraft.sounds.Sound;
import elcon.mods.soundcraft.sounds.SoundDisc;
import elcon.mods.soundcraft.tileentities.TileEntityAdvancedJukebox;

public class BlockAdvancedJukebox extends BlockContainer {

	public Icon jukeboxSide;
	public Icon jukeboxTop;

	public BlockAdvancedJukebox(int i) {
		super(i, Material.iron);
	}

	public boolean onBlockActivated(World world, int par2, int par3, int par4, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
		if(world.getBlockMetadata(par2, par3, par4) == 0) {
			if(entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof ItemRecord) {
				insertRecord(world, par2, par3, par4, entityPlayer.getHeldItem());
				
				String recordName = ((ItemRecord) entityPlayer.getHeldItem().getItem()).recordName;
				
				TileEntityAdvancedJukebox te = (TileEntityAdvancedJukebox) world.getBlockTileEntity(par2, par3, par4);
				if(te == null) {
					te = new TileEntityAdvancedJukebox();
					world.setBlockTileEntity(par2, par3, par4, te);
				}
				
				te.sendSound(new SoundDisc(recordName, entityPlayer.getHeldItem().itemID, 1.0F, 1.0F));
				
				return true;
			}			
			return false;
		} else {
			ejectRecord(world, par2, par3, par4);
			
			TileEntityAdvancedJukebox te = (TileEntityAdvancedJukebox) world.getBlockTileEntity(par2, par3, par4);
			if(te == null) {
				te = new TileEntityAdvancedJukebox();
				world.setBlockTileEntity(par2, par3, par4, te);
			}
			te.sendSound(new SoundDisc("stop", 0, 1.0F, 1.0F));
			
			return true;
		}
	}

	public void insertRecord(World world, int par2, int par3, int par4, ItemStack par5ItemStack) {
		if(!world.isRemote) {
			TileEntityAdvancedJukebox tileentityrecordplayer = (TileEntityAdvancedJukebox) world.getBlockTileEntity(par2, par3, par4);

			if(tileentityrecordplayer != null) {
				tileentityrecordplayer.setRecord(par5ItemStack.copy());
				
				world.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
			}
		}
	}

	public void ejectRecord(World par1World, int par2, int par3, int par4) {
		if(!par1World.isRemote) {
			TileEntityAdvancedJukebox tileentityrecordplayer = (TileEntityAdvancedJukebox) par1World.getBlockTileEntity(par2, par3, par4);

			if(tileentityrecordplayer != null) {
				ItemStack itemstack = tileentityrecordplayer.getRecord();

				if(itemstack != null) {
					//par1World.playAuxSFX(1005, par2, par3, par4, 0);
					//par1World.playRecord((String) null, par2, par3, par4);
					tileentityrecordplayer.setRecord((ItemStack) null);
					par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 2);
					float f = 0.7F;
					double d0 = (double) (par1World.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					double d1 = (double) (par1World.rand.nextFloat() * f) + (double) (1.0F - f) * 0.2D + 0.6D;
					double d2 = (double) (par1World.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					ItemStack itemstack1 = itemstack.copy();
					EntityItem entityitem = new EntityItem(par1World, (double) par2 + d0, (double) par3 + d1, (double) par4 + d2, itemstack1);
					entityitem.delayBeforeCanPickup = 10;
					par1World.spawnEntityInWorld(entityitem);
				}
			}
		}
	}

	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		this.ejectRecord(par1World, par2, par3, par4);
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
		if(!par1World.isRemote) {
			super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, 0);
		}
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	public int getComparatorInputOverride(World world, int par2, int par3, int par4, int par5) {
		ItemStack itemstack = ((TileEntityAdvancedJukebox) world.getBlockTileEntity(par2, par3, par4)).getRecord();
		return itemstack == null ? 0 : itemstack.itemID + 1 - Item.record13.itemID;
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
