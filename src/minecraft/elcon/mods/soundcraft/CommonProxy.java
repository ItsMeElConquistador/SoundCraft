package elcon.mods.soundcraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import elcon.mods.soundcraft.gui.ContainerAdvancedJukebox;
import elcon.mods.soundcraft.gui.GuiAdvancedJukebox;
import elcon.mods.soundcraft.tileentities.TileEntityAdvancedJukebox;

public class CommonProxy implements IGuiHandler {

	public void registerRenderInformation() {
		
	}
	
	public MinecraftServer getMCServer() {
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if(id == 0) {
			TileEntityAdvancedJukebox tile = (TileEntityAdvancedJukebox) world.getBlockTileEntity(x, y, z);
			if(tile != null) {
				return new ContainerAdvancedJukebox(tile, player.inventory, world, x, y, z);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
}
