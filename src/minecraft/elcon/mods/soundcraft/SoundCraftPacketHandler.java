package elcon.mods.soundcraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import elcon.mods.soundcraft.sounds.Sound;
import elcon.mods.soundcraft.sounds.SoundDisc;
import elcon.mods.soundcraft.tileentities.TileEntitySoundCable;

public class SoundCraftPacketHandler implements IPacketHandler {
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		byte packetID = dat.readByte();

		switch(packetID) {
		case 0:
			handlePlaySound(dat);
			break;
		case 50: 
			handleTileEntitySoundCable(dat);
			break;
		}
	}
	
	public void handlePlaySound(ByteArrayDataInput dat) {
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		
		int type = dat.readInt();
		String name = dat.readUTF();
		
		float volume = dat.readFloat();
		float pitch = dat.readFloat();
		
		if(type == 0) {
			Minecraft.getMinecraft().theWorld.playSound(x, y, z, name, volume, pitch, false);
		} else if(type == 1) {
			if(name.equalsIgnoreCase("stop")) {
				int discID = dat.readInt();				
				Minecraft.getMinecraft().sndManager.playStreaming((String) null, (float)x, (float)y, (float)z);
			} else {
				int discID = dat.readInt();
				Minecraft.getMinecraft().sndManager.playStreaming(name, (float)x, (float)y, (float)z);
			}
		} else if(type == 2) {
			Minecraft.getMinecraft().theWorld.playSound(x, y, z, name, volume, pitch, false);
		} else if(type == 3) {
			
		}
	}
	
	public static void sendSound(EntityPlayerMP player, int xCoord, int yCoord, int zCoord, int type, Sound sound) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(0);
			dos.writeInt(xCoord);
			dos.writeInt(yCoord);
			dos.writeInt(zCoord);
			
			dos.writeInt(type);
			dos.writeUTF(sound.name);
			
			dos.writeFloat(sound.volume);
			dos.writeFloat(sound.pitch);
			
			if(type == 1) {
				dos.writeInt(((SoundDisc) sound).discID);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "SCSound";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		packet.isChunkDataPacket = true;
		player.playerNetServerHandler.sendPacketToPlayer(packet);
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
