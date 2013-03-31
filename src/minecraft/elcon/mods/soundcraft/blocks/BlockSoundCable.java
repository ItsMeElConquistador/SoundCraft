package elcon.mods.soundcraft.blocks;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
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
import elcon.mods.soundcraft.network.SoundNetwork;
import elcon.mods.soundcraft.tileentities.TileEntitySoundCable;
import elcon.mods.soundcraft.tileentities.TileEntitySoundObject;

public class BlockSoundCable extends BlockContainer {

	public BlockSoundCable(int i) {
		super(i, Material.cloth);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float xpos, float ypos, float zpos) {
		if(player.getHeldItem().getItem() instanceof ItemDye) {
			TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z);
			if(te == null) {
				te = new TileEntitySoundCable();
				world.setBlockTileEntity(x, y, z, te);
			}
			te.color = player.getHeldItem().getItemDamage();
			player.getHeldItem().stackSize--;
			world.markBlockForUpdate(x, y, z);
		}
		return false;
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
			if(type != null) {
				list.add(new ItemStack(blockID, 1, type.id));
			}
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
		TileEntitySoundObject obj1 = (TileEntitySoundObject) world.getBlockTileEntity(x1, y1, z1);
		TileEntitySoundObject obj2 = (TileEntitySoundObject) world.getBlockTileEntity(x2, y2, z2);
		SoundNetwork.connectGroups(obj1, x1, y1, z1, obj2, x2, y2, z2);
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
		TileEntitySoundObject obj1 = (TileEntitySoundObject) world.getBlockTileEntity(x1, y1, z1);
		TileEntitySoundObject obj2 = (TileEntitySoundObject) world.getBlockTileEntity(x2, y2, z2);
		//SoundNetwork.unconnectGroups(obj1, x1, y1, z1, obj2, x2, y2, z2);
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
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isRemote) {
			updateCableConnections(world, x, y, z);
			world.markBlockForRenderUpdate(x, y, z);
		}
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
		if(!world.isRemote) {
			updateCableConnections(world, x, y, z);
			world.markBlockForRenderUpdate(x, y, z);
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int par5) {
		if(!world.isRemote) {
			updateCableConnections(world, x, y, z);
			world.markBlockForRenderUpdate(x, y, z);
		}
	}
}
