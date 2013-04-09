package elcon.mods.soundcraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.WorldServer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import elcon.mods.soundcraft.sounds.Sound;
import elcon.mods.soundcraft.sounds.SoundDisc;
import elcon.mods.soundcraft.tileentities.TileEntityAdvancedJukebox;
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
		case 20: 
			handleTileEntitySoundCable(dat);
			break;
		case 21:
			handleTileEntityAdvancedJukebox(dat);
			break;
		case 40:
			handleTileEntityUpdate(dat);
			break;
		//server side
		case 50:
			handleJukeboxUpdate(dat);
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
			//Minecraft.getMinecraft().theWorld.playSound(x, y, z, name, volume, pitch, false);
		} else if(type == 1) {
			if(name.equalsIgnoreCase("stop")) {
				int discID = dat.readInt();				
				Minecraft.getMinecraft().sndManager.playStreaming((String) null, (float)x, (float)y, (float)z);
			} else {
				int discID = dat.readInt();
				Minecraft.getMinecraft().sndManager.playStreaming(name, (float)x, (float)y, (float)z);
			}
		} else if(type == 2) {
			//Minecraft.getMinecraft().theWorld.playSound(x, y, z, name, volume, pitch, false);
		} else if(type == 3) {
			
		} else if(type == 4) {
			Minecraft.getMinecraft().sndManager.playStreaming((String) null, (float)x, (float)y, (float)z);
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
		
		TileEntitySoundCable te = (TileEntitySoundCable) Minecraft.getMinecraft().theWorld.getBlockTileEntity(x, y, z);
		if(te == null) {
			te = new TileEntitySoundCable();
		}
		
		te.color = dat.readInt();
		for(int i = 0; i < 6; i++) {
			te.directions[i] = dat.readBoolean();
		}
		
		Minecraft.getMinecraft().theWorld.setBlockTileEntity(x, y, z, te);
	}
	
	public void handleTileEntityAdvancedJukebox(ByteArrayDataInput dat) {
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		
		TileEntityAdvancedJukebox te = (TileEntityAdvancedJukebox) Minecraft.getMinecraft().theWorld.getBlockTileEntity(x, y, z);
		if(te == null) {
			te = new TileEntityAdvancedJukebox();
		}		
		for(int i = 0; i < 8; i++) {
			te.next[i] = dat.readBoolean();
			te.loop[i] = dat.readBoolean();
			te.loopTimes[i] = dat.readByte();
		}
		Minecraft.getMinecraft().theWorld.setBlockTileEntity(x, y, z, te);		
	}
	
	public void handleTileEntityUpdate(ByteArrayDataInput dat) {
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		
		Minecraft.getMinecraft().theWorld.markBlockForUpdate(x, y, z);
	}
	
	public static void sendTileEntityUpdate(EntityPlayerMP player, int x, int y, int z) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(40);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "SoundCraft";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		packet.isChunkDataPacket = true;
		player.playerNetServerHandler.sendPacketToPlayer(packet);
	}
	
	
	public void handleJukeboxUpdate(ByteArrayDataInput dat) {
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		int dim = dat.readInt();
		
		WorldServer world = SoundCraft.proxy.getMCServer().worldServerForDimension(dim);
		TileEntityAdvancedJukebox te = (TileEntityAdvancedJukebox) world.getBlockTileEntity(x, y, z);
		if(te == null) {
			te = new TileEntityAdvancedJukebox();
		}		
		for(int i = 0; i < 8; i++) {
			te.next[i] = dat.readBoolean();
			te.loop[i] = dat.readBoolean();
			te.loopTimes[i] = dat.readByte();
		}
		world.setBlockTileEntity(x, y, z, te);
		
		if(world != null && world.playerEntities != null) {
			for(Object o : world.playerEntities) {
				if(o != null && o instanceof EntityPlayerMP) {
					EntityPlayerMP player = (EntityPlayerMP) o;
					sendJukeboxUpdate(player, x, y, z, te.next, te.loop, te.loopTimes);
				}
			}
		}
	}
	
	public void sendJukeboxUpdate(EntityPlayerMP player, int x, int y, int z, boolean[] next, boolean[] loop, byte[] loopTimes) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(21);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			
			for(int i = 0; i < 8; i++) {
				dos.writeBoolean(next[i]);
				dos.writeBoolean(loop[i]);
				dos.writeByte(loopTimes[i]);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "SCTile";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;
		player.playerNetServerHandler.sendPacketToPlayer(pkt);		
	}
}
