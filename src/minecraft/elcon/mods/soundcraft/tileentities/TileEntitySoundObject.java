package elcon.mods.soundcraft.tileentities;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import elcon.mods.soundcraft.SoundCraftConfig;
import elcon.mods.soundcraft.blocks.BlockSoundCable;
import elcon.mods.soundcraft.blocks.BlockSpeaker;

public class TileEntitySoundObject extends TileEntity {

	public TileEntitySoundObject[] neighbors = new TileEntitySoundObject[6];
	
	public int group;
	public int node;
	
	public boolean checkedNeighbors = false;
	
	public void check(int x, int y, int z, int direction) {
		if(worldObj.blockHasTileEntity(x, y, z)) {
			TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
			if(tile instanceof TileEntitySoundObject) {
				TileEntitySoundObject obj = (TileEntitySoundObject) tile;
				boolean connect = true;
				if(obj instanceof TileEntitySoundCable) {
					if(!((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).canConnectToCable(worldObj, x, y, z, xCoord, yCoord, zCoord, getOppositeSide(direction), direction)) {
						connect = false;
					}
				} else if(obj instanceof TileEntityAdvancedJukebox) {
					if(direction == 1) {
						connect = false;
					}
				} else if(obj instanceof TileEntitySpeaker) {
					int meta = worldObj.getBlockMetadata(x, y, z);
					if(BlockSpeaker.isFront(directionToBlockSide(direction), meta)) {
						connect = false;
					}
				}
				if(connect) {
					neighbors[direction] = obj;
				}
			}
		}
	}
	
	public int directionToBlockSide(int i) {
		switch(i) {
		case 0: return 5;
		case 1: return 4;
		case 2: return 1;
		case 3: return 0;
		case 4: return 3;
		case 5: return 2;
		}
		return 0;
	}
	
	public int getOppositeSide(int i) {
		switch(i) {
			case 0: return 1;
			case 1: return 0;
			case 2: return 3;
			case 3: return 2;
			case 4: return 5;
			case 5: return 4;
		}
		return 0;
	}
	
	@Override
	public void updateEntity() {
		if(!checkedNeighbors) {
			check(xCoord - 1, yCoord, zCoord, 0);
			check(xCoord + 1, yCoord, zCoord, 1);
			check(xCoord, yCoord - 1, zCoord, 2);
			check(xCoord, yCoord + 1, zCoord, 3);
			check(xCoord, yCoord, zCoord - 1, 4);
			check(xCoord, yCoord, zCoord + 1, 5);
			checkedNeighbors = true;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		group = nbt.getInteger("SoundGroupID");
		node = nbt.getInteger("SoundNodeID");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("SoundGroupID", group);
		nbt.setInteger("SoundNodeID", node);
	}
}
