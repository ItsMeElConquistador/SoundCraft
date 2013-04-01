package elcon.mods.soundcraft;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;

public class SoundCraftConfig {

	public static boolean[] blockConnectsToCable = new boolean[Block.blocksList.length];
	
	public static int soundCableID = 1650;
	public static int speakerID = 1651;
	public static int advancedJukeboxID = 1652;
	
	public static int soundCableRenderID = 300;

	//public static int circuitID = ;
	
	public static void load(Configuration config) {
		soundCableID = config.getBlock("soundCable", 1650).getInt();
		speakerID = config.getBlock("speaker", 1651).getInt();
		advancedJukeboxID = config.getBlock("advancedJukebox", 1652).getInt();
		
		soundCableRenderID = config.get("RenderID", "soundCable", 300).getInt();
		
		setBlockConnectsToCable(soundCableID, true);
		setBlockConnectsToCable(speakerID, true);
		setBlockConnectsToCable(advancedJukeboxID, true);
	}
	
	public static void setBlockConnectsToCable(int i, boolean b) {
		blockConnectsToCable[i] = b;
	}
}