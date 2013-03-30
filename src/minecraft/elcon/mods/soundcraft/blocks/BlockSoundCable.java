package elcon.mods.soundcraft.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.soundcraft.SoundCableType;
import elcon.mods.soundcraft.SoundCraftConfig;

public class BlockSoundCable extends BlockContainer {

	public BlockSoundCable(int i) {
		super(i, Material.cloth);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySoundCable();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		SoundCableType.registerIcons(iconRegister);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int i) {
		return SoundCableType.soundCables[blockAccess.getBlockMetadata(x, y, z)].textures[((TileEntitySoundCable) blockAccess.getBlockTileEntity(x, y, z)).color];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int i, int j) {
		return SoundCableType.soundCables[j].textures[0];
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return SoundCraftConfig.soundCableRenderID;
	}
	
	public void notifyCableNeighbors(World world, int x, int y, int z) {
		if (world.getBlockId(x, y, z) == blockID) {
			world.notifyBlocksOfNeighborChange(x, y, z, blockID);
			world.notifyBlocksOfNeighborChange(x - 1, y, z, blockID);
			world.notifyBlocksOfNeighborChange(x + 1, y, z, blockID);
			world.notifyBlocksOfNeighborChange(x, y, z - 1, blockID);
			world.notifyBlocksOfNeighborChange(x, y, z + 1, blockID);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, blockID);
			world.notifyBlocksOfNeighborChange(x, y + 1, z, blockID);
		}
	}
	
	private static boolean charToBoolean(char c) {
		if(c == '0') {
			return false;
		} else if(c == '1') {
			return true;
		}
		return false;
	}
	
	public boolean isCableEqual(int data1, int c1, int data2, int c2) {
		if(data1 == data2 && c1 == c2) {
			return true;
		}
		return false;
	}
	
	public void updateCable(World world, int x, int y, int z) {
		int data = world.getBlockMetadata(x, y, z);
		TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z);
		int color = te.color;
		TileEntitySoundCable t = null;
		
		if(world.getBlockId(x - 1, y, z) == blockID ) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x - 1, y, z);
			if(isCableEqual(data, color, world.getBlockMetadata(x - 1, y, z), t.color)) {
				t.directions[0] = true;
				world.setBlockTileEntity(x, y, z, t);
			}
		}
		if(world.getBlockId(x + 1, y, z) == blockID) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x - 1, y, z);
			if(isCableEqual(data, color, world.getBlockMetadata(x + 1, y, z), t.color)) {
				t.directions[1] = true;
				world.setBlockTileEntity(x, y, z, t);
			}
		}
		if(world.getBlockId(x, y - 1, z) == blockID) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x - 1, y, z);
			if(isCableEqual(data, color, world.getBlockMetadata(x, y - 1, z), t.color)) {
				t.directions[2] = true;
				world.setBlockTileEntity(x, y, z, t);
			}
		}
		if(world.getBlockId(x, y + 1, z) == blockID) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x - 1, y, z);
			if(isCableEqual(data, color, world.getBlockMetadata(x, y + 1, z), t.color)) {
				t.directions[3] = true;
				world.setBlockTileEntity(x, y, z, t);
			}
		}
		if(world.getBlockId(x, y, z - 1) == blockID) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x - 1, y, z);
			if(isCableEqual(data, color, world.getBlockMetadata(x, y, z - 1), t.color)) {
				t.directions[4] = true;
				world.setBlockTileEntity(x, y, z, t);
			} 
		}
		if(world.getBlockId(x, y, z + 1) == blockID) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x - 1, y, z);
			if(isCableEqual(data, color, world.getBlockMetadata(x, y, z + 1), t.color)) {
				t.directions[5] = true;
				world.setBlockTileEntity(x, y, z, t);
			}
		}
		
		//notifyCableNeighbors(world, x, y, z);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isRemote) {
			updateCable(world, x, y, z);
		}
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
		if(!world.isRemote) {
			updateCable(world, x, y, z);
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int par5) {
		if(!world.isRemote) {
			updateCable(world, x, y, z);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
		if(!world.isRemote) {
			//updateCable(world, x, y, z);
		}
	}
}
