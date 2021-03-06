package elcon.mods.soundcraft.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import elcon.mods.soundcraft.ClientProxy;
import elcon.mods.soundcraft.SoundCableType;
import elcon.mods.soundcraft.SoundCraft;
import elcon.mods.soundcraft.SoundCraftConfig;
import elcon.mods.soundcraft.SoundCraftPacketHandler;
import elcon.mods.soundcraft.tileentities.TileEntitySoundCable;
import elcon.mods.soundcraft.tileentities.TileEntitySoundObject;

public class BlockSoundCable extends BlockContainer {

	public BlockSoundCable(int i) {
		super(i, Material.cloth);
		setTickRandomly(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		
	}
	
	@Override
	public int getDamageValue(World world, int x, int y, int z) {
		TileEntitySoundCable tile = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z);
		if(tile == null) {
			tile = new TileEntitySoundCable();
			world.setBlockTileEntity(x, y, z, tile);
		}
		return tile.color;
	}

	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		TileEntitySoundCable tile = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z);
		if(tile == null) {
			tile = new TileEntitySoundCable();
			world.setBlockTileEntity(x, y, z, tile);
		}		
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		int count = quantityDropped(metadata, fortune, world.rand);
		for(int i = 0; i < count; i++) {
			ret.add(new ItemStack(getIDForMeta(world.getBlockMetadata(x, y, z)), 1, tile.color));
		}
		return ret;
	}
	
	public int getIDForMeta(int i) {
		switch(i) {
		case 0: return SoundCraftConfig.soundCableCopperID + 256;
		case 1: return SoundCraftConfig.soundCableTinID + 256;
		case 2: return SoundCraftConfig.soundCableSilverID + 256;
		case 3: return SoundCraftConfig.soundCableIronID + 256;
		case 4: return SoundCraftConfig.soundCableGoldID + 256;
		}
		return SoundCraftConfig.soundCableCopperID + 256;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int par5) {
		/*
		 * TileEntitySoundCable tile = (TileEntitySoundCable) blockAccess.getBlockTileEntity(x, y, z); if(tile != null) { if(tile.isDetector && tile.emitRedstone) { return 15; } }
		 */
		return 0;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int par5) {
		/*
		 * TileEntitySoundCable tile = (TileEntitySoundCable) blockAccess.getBlockTileEntity(x, y, z); if(tile != null) { if(tile.isDetector && tile.emitRedstone) { return 15; } }
		 */
		return 0;
		/*
		 * int i1 = blockAccess.getBlockMetadata(x, y, z);
		 * 
		 * if ((i1 & 8) == 0) { return 0; } else { int j1 = i1 & 7; return j1 == 0 && par5 == 0 ? 15 : (j1 == 7 && par5 == 0 ? 15 : (j1 == 6 && par5 == 1 ? 15 : (j1 == 5 && par5 == 1 ? 15 : (j1 == 4
		 * && par5 == 2 ? 15 : (j1 == 3 && par5 == 3 ? 15 : (j1 == 2 && par5 == 4 ? 15 : (j1 == 1 && par5 == 5 ? 15 : 0))))))); }
		 */
	}

	@Override
	public boolean canProvidePower() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float xpos, float ypos, float zpos) {
		if(player.getHeldItem() != null) {
			if(player.getHeldItem().getItem() instanceof ItemDye) {
				TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z);
				if(te == null) {
					te = new TileEntitySoundCable();
					world.setBlockTileEntity(x, y, z, te);
				}
				te.color = player.getHeldItem().getItemDamage();
				player.getHeldItem().stackSize--;

				updateCableConnections(world, x, y, z);
			}/*
			 * else if(player.getHeldItem().getItem() instanceof ItemRedstone) { TileEntitySoundCable te = (TileEntitySoundCable) world.getBlockTileEntity(x, y, z); if(te == null) { te = new
			 * TileEntitySoundCable(); world.setBlockTileEntity(x, y, z, te); } te.isDetector = true; System.out.println("create detector"); world.markBlockForUpdate(x, y, z);
			 * player.getHeldItem().stackSize--; }
			 */
		}
		world.markBlockForRenderUpdate(x, y, z);
		return false;
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
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
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySoundCable();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		ClientProxy.crossIcon = iconRegister.registerIcon("soundcraft:cross");
		ClientProxy.emptyIcon = iconRegister.registerIcon("soundcraft:empty");
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
		if(world.getBlockId(x, y, z) == blockID) {
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
		if(obj1 != null) {
			obj1.updateEntity();
			//obj1.neighbors[direction1] = obj2;
		}
		if(obj2 != null) {
			obj2.updateEntity();
			//obj2.neighbors[direction2] = obj1;
		}
		world.markBlockForRenderUpdate(x1, y1, z1);
		world.markBlockForRenderUpdate(x2, y2, z2);

		sendTileEntityUpdate(world, x1, y1, z1);
		sendTileEntityUpdate(world, x2, y2, z2);
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
		if(obj1 != null) {
			obj1.updateEntity();
			//obj1.neighbors[direction1] = null;
		}
		if(obj2 != null) {
			obj2.updateEntity();
			//obj2.neighbors[direction2] = null;
		}
		
		world.markBlockForRenderUpdate(x1, y1, z1);
		world.markBlockForRenderUpdate(x2, y2, z2);

		sendTileEntityUpdate(world, x1, y1, z1);
		sendTileEntityUpdate(world, x2, y2, z2);
	}

	public boolean canConnectToCable(World world, int x1, int y1, int z1, int x2, int y2, int z2, int direction1, int direction2) {
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
				boolean ret = true;
				if(id1 == SoundCraftConfig.speakerID) {
					int meta = world.getBlockMetadata(x1, y1, z1);
					if(BlockSpeaker.isFront(directionToBlockSide(direction2), meta)) {
						ret = false;
					}
				}
				if(id2 == SoundCraftConfig.speakerID) {
					int meta = world.getBlockMetadata(x2, y2, z2);
					if(BlockSpeaker.isFront(directionToBlockSide(direction1), meta)) {
						ret = false;
					}
				}
				if(id1 == SoundCraftConfig.advancedJukeboxID) {
					if(BlockAdvancedJukebox.isTop(directionToBlockSide(direction2), 0)) {
						ret = false;
					}
				}
				if(id2 == SoundCraftConfig.advancedJukeboxID) {
					if(BlockAdvancedJukebox.isTop(directionToBlockSide(direction1), 0)) {
						ret = false;
					}
				}
				return ret;
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

		if(canConnectToCable(world, x, y, z, x - 1, y, z, 0, 1)) {
			connectCable(world, x, y, z, x - 1, y, z, 0, 1);
		} else {
			unconnectCable(world, x, y, z, x - 1, y, z, 0, 1);
		}
		if(canConnectToCable(world, x, y, z, x + 1, y, z, 1, 0)) {
			connectCable(world, x, y, z, x + 1, y, z, 1, 0);
		} else {
			unconnectCable(world, x, y, z, x + 1, y, z, 1, 0);
		}
		if(canConnectToCable(world, x, y, z, x, y - 1, z, 2, 3)) {
			connectCable(world, x, y, z, x, y - 1, z, 2, 3);
		} else {
			unconnectCable(world, x, y, z, x, y - 1, z, 2, 3);
		}
		if(canConnectToCable(world, x, y, z, x, y + 1, z, 3, 2)) {
			connectCable(world, x, y, z, x, y + 1, z, 3, 2);
		} else {
			unconnectCable(world, x, y, z, x, y + 1, z, 3, 2);
		}
		if(canConnectToCable(world, x, y, z, x, y, z - 1, 4, 5)) {
			connectCable(world, x, y, z, x, y, z - 1, 4, 5);
		} else {
			unconnectCable(world, x, y, z, x, y, z - 1, 4, 5);
		}
		if(canConnectToCable(world, x, y, z, x, y, z + 1, 5, 4)) {
			connectCable(world, x, y, z, x, y, z + 1, 5, 4);
		} else {
			unconnectCable(world, x, y, z, x, y, z + 1, 5, 4);
		}
	}

	public int directionToBlockSide(int i) {
		switch(i) {
		case 0:
			return 5;
		case 1:
			return 4;
		case 2:
			return 1;
		case 3:
			return 0;
		case 4:
			return 3;
		case 5:
			return 2;
		}
		return 0;
	}

	public void sendTileEntityUpdate(World world, int x, int y, int z) {
		if(SoundCraft.proxy.getMCServer().worldServerForDimension(world.provider.dimensionId) != null) {
			for(Object o : SoundCraft.proxy.getMCServer().worldServerForDimension(world.provider.dimensionId).playerEntities) {
				EntityPlayerMP player = null;
				if(o instanceof EntityPlayerMP) {
					player = (EntityPlayerMP) o;
					SoundCraftPacketHandler.sendTileEntityUpdate(player, x, y, z);
				}
			}
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

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
		if(!world.isRemote) {
			updateCableConnections(world, x, y, z);
			world.markBlockForRenderUpdate(x, y, z);
		}
	}
}
