package elcon.mods.soundcraft.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

import elcon.mods.soundcraft.SoundCraft;
import elcon.mods.soundcraft.SoundCraftPacketHandler;
import elcon.mods.soundcraft.tileentities.TileEntityAdvancedJukebox;

public class GuiAdvancedJukebox extends GuiContainer {

	public EntityPlayer player;
	public TileEntityAdvancedJukebox tile;
	
	public GuiAdvancedJukebox(Container container, EntityPlayer p, TileEntityAdvancedJukebox t) {
		super(container);
		player = p;
		tile = t;
		allowUserInput = true;
		ySize = 200;
	}
	
	@Override
	protected void mouseClicked(int xx, int yy, int key) {
		int x = xx - guiLeft;
		int y = yy - guiTop;
		if(y > 16 && y < 172) {
			if(x > 61 && x < 77) {
				int i = (int) Math.floor((y - 16) / 20);
				tile.next[i] = !tile.next[i];
			} else if(x > 109 && x < 125) {
				int i = (int) Math.floor((y - 16) / 20);
				tile.loop[i] = !tile.loop[i];
			} else if(x > 153 && x < 171) {
				int i = (int) Math.floor((y - 16) / 20);
				boolean flag = key == 1;				
				if(flag) {
					if(isShiftKeyDown()) {
						tile.loopTimes[i] = 0;
					} else {
						tile.loopTimes[i]--;
					}
				} else {
					if(isShiftKeyDown()) {
						tile.loopTimes[i] = 99;
					} else {
						tile.loopTimes[i]++;
					}
				}
				if(tile.loopTimes[i] < 0) {
					tile.loopTimes[i] = 0;
				}
				if(tile.loopTimes[i] > 99) {
					tile.loopTimes[i] = 99;
				}
			}
		}
		super.mouseClicked(xx, yy, key);
	}
	
	@Override
	public void onGuiClosed() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(50);
			dos.writeInt(tile.xCoord);
			dos.writeInt(tile.yCoord);
			dos.writeInt(tile.zCoord);
			dos.writeInt(tile.worldObj.provider.dimensionId);
			
			for(int i = 0; i < 8; i++) {
				dos.writeBoolean(tile.next[i]);
				dos.writeBoolean(tile.loop[i]);
				dos.writeByte(tile.loopTimes[i]);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "SoundCraft";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		packet.isChunkDataPacket = true;
		PacketDispatcher.sendPacketToServer(packet);
		
		super.onGuiClosed();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		fontRenderer.drawString(StatCollector.translateToLocal("soundcraft.advancedJukebox"), 16 + 8, 4, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture("/mods/soundcraft/textures/gui/advanced_jukebox.png");
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		for(int ii = 0; ii < 8; ii++) {
			if(tile.next[ii]) {
				drawTexturedModalRect(guiLeft + 64, guiTop + 19 + (ii * 20), 186, 0, 10, 10);
			}
			if(tile.loop[ii]) {
				//drawTexturedModalRect(guiLeft + 112, guiTop + 19 + (ii * 20), 186, 0, 10, 10);
				drawTexturedModalRect(guiLeft + 112, guiTop + 19 + (ii * 20), 186, 0, 10, 10);
			}
			mc.fontRenderer.drawString(Integer.toString(tile.loopTimes[ii]), guiLeft + 156, guiTop + 19 + (ii * 20), 16777215, false);
		}
	}
}
