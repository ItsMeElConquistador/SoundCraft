package elcon.mods.soundcraft.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
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
	
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z){
        TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z);
		if(te == null) {
			te = new TileEntitySoundCable();
			world.setBlockTileEntity(x, y, z, te);
		}
		double minX = 0.0625F * 6;
		double minY = 0.0625F * 6;
		double minZ = 0.0625F * 6;
		double maxX = 0.0625F * 10;
		double maxY = 0.0625F * 10;
		double maxZ = 0.0625F * 10;
		
		if(te.directions[0]) {
			minX = 0.0;
		}
		if(te.directions[1]) {
			maxX = 1.0;
		}
		if(te.directions[2]) {
			minY = 0.0;
		}
		if(te.directions[3]) {
			maxY = 1.0;
		}
		if(te.directions[4]) {
			minZ = 0.0;
		}
		if(te.directions[5]) {
			maxZ = 1.0;
		}
		return AxisAlignedBB.getAABBPool().getAABB(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
    }
	
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		TileEntitySoundCable te = (TileEntitySoundCable) blockAccess.getBlockTileEntity(x, y, z);
		if(te == null) {
			te = new TileEntitySoundCable();
		}
		float minX = 0.0625F * 6;
		float minY = 0.0625F * 6;
		float minZ = 0.0625F * 6;
		float maxX = 0.0625F * 10;
		float maxY = 0.0625F * 10;
		float maxZ = 0.0625F * 10;
		
		if(te.directions[0]) {
			minX = 0.0F;
		}
		if(te.directions[1]) {
			maxX = 1.0F;
		}
		if(te.directions[2]) {
			minY = 0.0F;
		}
		if(te.directions[3]) {
			maxY = 1.0F;
		}
		if(te.directions[4]) {
			minZ = 0.0F;
		}
		if(te.directions[5]) {
			maxZ = 1.0F;
		}
		setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int i, CreativeTabs creativeTabs, List list) {
		for(SoundCableType type : SoundCableType.soundCables) {
			list.add(new ItemStack(blockID, 1, type.id));
		}
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
	
	public void connectCable(World world, int x1, int y1, int z1, int x2, int y2, int z2, int direction1, int direction2) {
		int id1 = world.getBlockId(x1, y1, z1);
		int id2 = world.getBlockId(x2, y2, z2);
		if(id1 == blockID) {
			TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x1, y1, z1);
			if(te == null) {
				te = new TileEntitySoundCable();
				world.setBlockTileEntity(x1, y1, z1, te);
			}
			te.directions[direction1] = true;
			world.setBlockTileEntity(x1, y1, z1, te);
			
			world.markBlockForUpdate(x1, y1, z1);
		}
		if(id2 == blockID) {
			TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x2, y2, z2);
			if(te == null) {
				te = new TileEntitySoundCable();
				world.setBlockTileEntity(x2, y2, z2, te);
			}
			te.directions[direction2] = true;
			world.setBlockTileEntity(x2, y2, z2, te);
			
			world.markBlockForUpdate(x2, y2, z2);
		}
	}
	
	public void unconnectCable(World world, int x1, int y1, int z1, int x2, int y2, int z2, int direction1, int direction2) {
		int id1 = world.getBlockId(x1, y1, z1);
		int id2 = world.getBlockId(x2, y2, z2);
		if(id1 == blockID) {
			TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x1, y1, z1);
			if(te == null) {
				te = new TileEntitySoundCable();
				world.setBlockTileEntity(x1, y1, z1, te);
			}
			te.directions[direction1] = false;
			world.setBlockTileEntity(x1, y1, z1, te);
			
			world.markBlockForUpdate(x1, y1, z1);
		}
		if(id2 == blockID) {
			TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x2, y2, z2);
			if(te == null) {
				te = new TileEntitySoundCable();
				world.setBlockTileEntity(x2, y2, z2, te);
			}
			te.directions[direction2] = false;
			world.setBlockTileEntity(x2, y2, z2, te);
			
			world.markBlockForUpdate(x2, y2, z2);
		}
	}
	
	public boolean canConnectToCable(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		int id1 = world.getBlockId(x1, y1, z1);
		int id2 = world.getBlockId(x2, y2, z2);
		if(id1 == id2) {
			TileEntitySoundCable t1 = (TileEntitySoundCable) world.getBlockTileEntity(x1, y1, z1);
			TileEntitySoundCable t2 = (TileEntitySoundCable) world.getBlockTileEntity(x2, y2, z2);
			if(t1 == null) {
				t1 = new TileEntitySoundCable();
				world.setBlockTileEntity(x1, y1, z1, t1);
			}
			if(t2 == null) {
				t2 = new TileEntitySoundCable();
				world.setBlockTileEntity(x2, y2, z2, t2);
			}
			if(world.getBlockMetadata(x1, y1, z1) == world.getBlockMetadata(x2, y2, z2) && t1.color == t2.color) {
				return true;
			}
		} else {
			if(SoundCraftConfig.blockConnectsToCable[id1] && SoundCraftConfig.blockConnectsToCable[id2]) {
				return true;
			}
		}
		return false;
	}
	
	public void updateCableConnections(World world, int x, int y, int z) {
		int data = world.getBlockMetadata(x, y, z);
		TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z);
		if(te == null) {
			te = new TileEntitySoundCable();
			world.setBlockTileEntity(x, y, z, te);
		}
		int color = te.color;
		
		if(canConnectToCable(world, x, y, z, x - 1, y, z)) {
			connectCable(world, x, y, z, x - 1, y, z, 0, 1);
		} else {
			unconnectCable(world, x, y, z, x - 1, y, z, 0, 1);
		}
		if(canConnectToCable(world, x, y, z, x + 1, y, z)) {
			connectCable(world, x, y, z, x + 1, y, z, 1, 0);
		} else {
			unconnectCable(world, x, y, z, x + 1, y, z, 1, 0);
		}
		if(canConnectToCable(world, x, y, z, x, y - 1, z)) {
			connectCable(world, x, y, z, x, y - 1, z, 2, 3);
		} else {
			unconnectCable(world, x, y, z, x, y - 1, z, 2, 3);
		}
		if(canConnectToCable(world, x, y, z, x, y + 1, z)) {
			connectCable(world, x, y, z, x, y + 1, z, 3, 2);
		} else {
			unconnectCable(world, x, y, z, x, y + 1, z, 3, 2);
		}
		if(canConnectToCable(world, x, y, z, x, y, z - 1)) {
			connectCable(world, x, y, z, x, y, z - 1, 4, 5);
		} else {
			unconnectCable(world, x, y, z, x, y, z - 1, 4, 5);
		}
		if(canConnectToCable(world, x, y, z, x, y, z + 1)) {
			connectCable(world, x, y, z, x, y, z + 1, 5, 4);
		} else {
			unconnectCable(world, x, y, z, x, y, z + 1, 5, 4);
		}
	}
	
	/*public void updateCable(World world, int x, int y, int z) {
		int data = world.getBlockMetadata(x, y, z);
		TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z);
		if(te == null) {
			te = new TileEntitySoundCable();
			world.setBlockTileEntity(x, y, z, te);
		}
		int color = te.color;
		TileEntitySoundCable t = null;
		
		if(world.getBlockId(x - 1, y, z) == blockID ) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x - 1, y, z);
			if(t == null) {
				t = new TileEntitySoundCable();
				world.setBlockTileEntity(x - 1, y, z, t);
			}			
			if(isCableEqual(data, color, world.getBlockMetadata(x - 1, y, z), t.color)) {
				te.directions[0] = true;
				t.directions[1] = true;
				world.setBlockTileEntity(x - 1, y, z, t);
				world.setBlockTileEntity(x, y, z, te);
				world.markBlockForRenderUpdate(x, y, z);
				world.markBlockForUpdate(x - 1, y, z);
			}
		} else {
			te.directions[0] = false;
			world.setBlockTileEntity(x, y, z, te);
			world.markBlockForUpdate(x, y, z);
		}
		if(world.getBlockId(x + 1, y, z) == blockID) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x + 1, y, z);
			if(t == null) {
				t = new TileEntitySoundCable();
				world.setBlockTileEntity(x + 1, y, z, t);
			}		
			if(isCableEqual(data, color, world.getBlockMetadata(x + 1, y, z), t.color)) {
				te.directions[1] = true;
				t.directions[0] = true;
				world.setBlockTileEntity(x + 1, y, z, t);
				world.setBlockTileEntity(x, y, z, te);
				world.markBlockForRenderUpdate(x, y, z);
				world.markBlockForUpdate(x + 1, y, z);
			}
		} else {
				te.directions[1] = false;
				world.setBlockTileEntity(x, y, z, te);
				world.markBlockForUpdate(x, y, z);
			}
		if(world.getBlockId(x, y - 1, z) == blockID) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x, y - 1, z);
			if(t == null) {
				t = new TileEntitySoundCable();
				world.setBlockTileEntity(x, y - 1, z, t);
			}		
			if(isCableEqual(data, color, world.getBlockMetadata(x, y - 1, z), t.color)) {
				te.directions[2] = true;
				t.directions[3] = true;
				world.setBlockTileEntity(x, y - 1, z, t);
				world.setBlockTileEntity(x, y, z, te);
				world.markBlockForRenderUpdate(x, y, z);
				world.markBlockForUpdate(x, y - 1, z);
			}
		} else {
			te.directions[2] = false;
			world.setBlockTileEntity(x, y, z, te);
			world.markBlockForUpdate(x, y, z);
		}
		if(world.getBlockId(x, y + 1, z) == blockID) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x, y + 1, z);
			if(t == null) {
				t = new TileEntitySoundCable();
				world.setBlockTileEntity(x, y + 1, z, t);
			}		
			if(isCableEqual(data, color, world.getBlockMetadata(x, y + 1, z), t.color)) {
				te.directions[3] = true;
				t.directions[2] = true;
				world.setBlockTileEntity(x, y + 1, z, t);
				world.setBlockTileEntity(x, y, z, te);
				world.markBlockForRenderUpdate(x, y, z);
				world.markBlockForUpdate(x, y + 1, z);
			}
		} else {
				te.directions[3] = false;
				world.setBlockTileEntity(x, y, z, te);
				world.markBlockForUpdate(x, y, z);
			}
		if(world.getBlockId(x, y, z - 1) == blockID) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z - 1);
			if(t == null) {
				t = new TileEntitySoundCable();
				world.setBlockTileEntity(x, y, z - 1, t);
			}		
			if(isCableEqual(data, color, world.getBlockMetadata(x, y, z - 1), t.color)) {
				te.directions[4] = true;
				t.directions[5] = true;
				world.setBlockTileEntity(x, y, z - 1, t);
				world.setBlockTileEntity(x, y, z, te);
				world.markBlockForRenderUpdate(x, y, z);
				world.markBlockForUpdate(x, y, z - 1);
			} 
		} else {
			te.directions[4] = false;
			world.setBlockTileEntity(x, y, z, te);
			world.markBlockForUpdate(x, y, z);
		}
		if(world.getBlockId(x, y, z + 1) == blockID) {
			t = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z + 1);
			if(t == null) {
				t = new TileEntitySoundCable();
				world.setBlockTileEntity(x, y, z + 1, t);
			}
			if(isCableEqual(data, color, world.getBlockMetadata(x, y, z + 1), t.color)) {
				te.directions[5] = true;
				t.directions[4] = true;
				world.setBlockTileEntity(x, y, z + 1, t);
				world.setBlockTileEntity(x, y, z, te);
				world.markBlockForRenderUpdate(x, y, z);
				world.markBlockForUpdate(x, y, z + 1);
			}
		} else {
			te.directions[5] = false;
			world.setBlockTileEntity(x, y, z, te);
			world.markBlockForUpdate(x, y, z);
		}
		world.setBlockTileEntity(x, y, z, te);
		world.markBlockForUpdate(x, y, z);
		
		//notifyCableNeighbors(world, x, y, z);
	}*/
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isRemote) {
			updateCableConnections(world, x, y, z);
		}
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
		if(!world.isRemote) {
			updateCableConnections(world, x, y, z);
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int par5) {
		if(!world.isRemote) {
			updateCableConnections(world, x, y, z);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
		if(!world.isRemote) {
			//updateCable(world, x, y, z);
		} 
	}
}
