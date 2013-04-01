package elcon.mods.soundcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import elcon.mods.soundcraft.tileentities.TileEntitySoundCable;

public class SoundCraftPacketHandler implements IPacketHandler {
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		byte packetID = dat.readByte();

		switch(packetID) {
		case 50: 
			handleTileEntitySoundCable(dat);
			break;
		}
	}
	
	public void handleTileEntitySoundCable(ByteArrayDataInput dat) {
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		
		TileEntitySoundCable te = new TileEntitySoundCable();
		te.color = dat.readInt();
		for(int i = 0; i < 6; i++) {
			te.directions[i] = dat.readBoolean();
		}
		
		Minecraft.getMinecraft().theWorld.setBlockTileEntity(x, y, z, te);
	}
	
	public void handleTileEntityUpdate(ByteArrayDataInput dat) {
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		
		Minecraft.getMinecraft().theWorld.markBlockForRenderUpdate(x, y, z);
	}
}
