package elcon.mods.soundcraft;

import net.minecraftforge.common.Configuration;

public class SoundCraftConfig {

	public static int soundCableID = 1650;
	
	public static int soundCableRenderID = 300;
	
	public static void load(Configuration config) {
		soundCableID = config.getBlock("soundCable", 1650).getInt();
		
		soundCableRenderID = config.get("RenderID", "soundCable", 300).getInt();
	}
}