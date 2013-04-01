package elcon.mods.soundcraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.RenderingRegistry;
import elcon.mods.soundcraft.gui.ContainerAdvancedJukebox;
import elcon.mods.soundcraft.gui.GuiAdvancedJukebox;

public class ClientProxy extends CommonProxy {

	public SoundCraftBlockRenderingHandler blockRenderingHandler;
	
	@Override
	public void registerRenderInformation() {
		blockRenderingHandler = new SoundCraftBlockRenderingHandler();
		
		RenderingRegistry.registerBlockHandler(SoundCraftConfig.soundCableRenderID, blockRenderingHandler);
	}
	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if(id == 0) {
			return new GuiAdvancedJukebox(new ContainerAdvancedJukebox());
		}
		return null;
	}
}
