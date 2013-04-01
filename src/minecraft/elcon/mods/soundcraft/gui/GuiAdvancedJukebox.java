package elcon.mods.soundcraft.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiAdvancedJukebox extends GuiContainer {

	public GuiAdvancedJukebox(Container container) {
		super(container);
		allowUserInput = true;
		ySize = 200;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		fontRenderer.drawString(StatCollector.translateToLocal("soundcraft.advancedJukebox"), 16 + 8, 4, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture("/mods/soundcraft/textures/gui/advanced_jukebox.png");
		int xx = (width - xSize) / 2;
		int yy = (height - ySize) / 2;
		this.drawTexturedModalRect(xx, yy, 0, 0, xSize, ySize);
	}
}
