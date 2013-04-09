package elcon.mods.soundcraft;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;

public class SoundCraftConfig {

	public static boolean[] blockConnectsToCable = new boolean[Block.blocksList.length];
	
	public static boolean debugMode = false;
	
	public static int soundCableID = 1650;
	public static int speakerID = 1651;
	public static int advancedJukeboxID = 1652;
	
	public static int soundCableRenderID = 300;

	public static int circuitID = 28500;
	public static int speakerItemID = 28501;
	public static int soundCableCopperID = 28502;
	public static int soundCableTinID = 28503;
	public static int soundCableSilverID = 28504;
	public static int soundCableIronID = 28505;
	public static int soundCableGoldID = 28506;
	
	public static void load(Configuration config) {
		debugMode = config.get("General", "debugMode", false).getBoolean(false);
		
		soundCableID = config.getBlock("soundCable", 1650).getInt();
		speakerID = config.getBlock("speaker", 1651).getInt();
		advancedJukeboxID = config.getBlock("advancedJukebox", 1652).getInt();
		
		soundCableRenderID = config.get("RenderID", "soundCable", 300).getInt();
		
		circuitID = config.getItem("circuit", 28500).getInt();
		speakerItemID = config.getItem("speaker", 28501).getInt();
		soundCableCopperID = config.getItem("soundCableCopper", 28502).getInt();
		soundCableTinID = config.getItem("soundCableTin", 28503).getInt();
		soundCableSilverID = config.getItem("soundCableSilver", 28504).getInt();
		soundCableIronID = config.getItem("soundCableIron", 28505).getInt();
		soundCableGoldID = config.getItem("soundCableGold", 28506).getInt();
		
		setBlockConnectsToCable(soundCableID, true);
		setBlockConnectsToCable(speakerID, true);
		setBlockConnectsToCable(advancedJukeboxID, true);
	}
	
	public static void setBlockConnectsToCable(int i, boolean b) {
		blockConnectsToCable[i] = b;
	}
}