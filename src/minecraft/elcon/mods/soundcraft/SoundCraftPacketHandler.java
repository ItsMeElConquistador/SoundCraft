package elcon.mods.soundcraft;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

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
		
	}
}
