package elcon.mods.soundcraft.tileentities;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import elcon.mods.soundcraft.SoundCraftConfig;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class TileEntitySoundCable extends TileEntitySoundConductor {

	public int color = 0;
	public boolean[] directions = new boolean[6];
	
	public boolean isDetector = false;
	public boolean emitRedstone = false;
	public int emitTicks = 0;
	
	@Override
	public void updateEntity() {
		if(isDetector && emitRedstone) {
			System.out.println("tick: " + emitTicks);
			emitTicks--;
			if(emitTicks <= 0) {
				emitRedstone = false;
				emitTicks = 0;
				//worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, SoundCraftConfig.soundCableID);
				/*worldObj.notifyBlocksOfNeighborChange(xCoord - 1, yCoord, zCoord, SoundCraftConfig.soundCableID);
				worldObj.notifyBlocksOfNeighborChange(xCoord + 1, yCoord, zCoord, SoundCraftConfig.soundCableID);
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, SoundCraftConfig.soundCableID);
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord + 1, zCoord, SoundCraftConfig.soundCableID);
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord - 1, SoundCraftConfig.soundCableID);
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord + 1, SoundCraftConfig.soundCableID);*/
			}
		}
	}
	
	@Override
	public Packet getDescriptionPacket() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(50);
			dos.writeInt(xCoord);
			dos.writeInt(yCoord);
			dos.writeInt(zCoord);
			
			dos.writeInt(color);
			for(int i = 0; i < 6; i++) {
				dos.writeBoolean(directions[i]);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "SCCable";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;
		return pkt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		color = nbt.getInteger("color");
		for(int i = 0; i < 6; i++) {
			directions[i] = nbt.getBoolean("direction" + Integer.toString(i));
		}
		 isDetector = nbt.getBoolean("isDetector");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("color", color);
		for(int i = 0; i < 6; i++) {
			nbt.setBoolean("direction" + Integer.toString(i), directions[i]);
		}
		nbt.setBoolean("isDetector", isDetector);
	}
}
