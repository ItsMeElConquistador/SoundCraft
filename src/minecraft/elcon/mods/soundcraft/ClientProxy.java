package elcon.mods.soundcraft;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	public SoundCraftBlockRenderingHandler blockRenderingHandler;
	
	@Override
	public void registerRenderInformation() {
		blockRenderingHandler = new SoundCraftBlockRenderingHandler();
		
		RenderingRegistry.registerBlockHandler(SoundCraftConfig.soundCableRenderID, blockRenderingHandler);
	}
}
