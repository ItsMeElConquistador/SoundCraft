package elcon.mods.soundcraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.RenderingRegistry;
import elcon.mods.soundcraft.gui.ContainerAdvancedJukebox;
import elcon.mods.soundcraft.gui.GuiAdvancedJukebox;
import elcon.mods.soundcraft.tileentities.TileEntityAdvancedJukebox;

public class ClientProxy extends CommonProxy {

	public static Icon crossIcon;
	
	public SoundCraftBlockRenderingHandler blockRenderingHandler;
	
	@Override
	public void registerRenderInformation() {
		blockRenderingHandler = new SoundCraftBlockRenderingHandler();
		
		RenderingRegistry.registerBlockHandler(SoundCraftConfig.soundCableRenderID, blockRenderingHandler);
	}
	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if(id == 0) {
			TileEntityAdvancedJukebox tile = (TileEntityAdvancedJukebox) world.getBlockTileEntity(x, y, z);
			if(tile != null) {
				return new GuiAdvancedJukebox(new ContainerAdvancedJukebox(tile, player.inventory, world, x, y, z));
			}
		}
		return null;
	}
}
