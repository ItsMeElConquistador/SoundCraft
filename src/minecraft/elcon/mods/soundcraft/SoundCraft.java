package elcon.mods.soundcraft;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import elcon.mods.soundcraft.blocks.BlockSoundCable;
import elcon.mods.soundcraft.blocks.BlockSpeaker;
import elcon.mods.soundcraft.blocks.TileEntitySoundCable;

@Mod(modid = "SoundCraft", name = "SoundCraft", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, packetHandler = SoundCraftPacketHandler.class, channels = {"SCCable"})
public class SoundCraft {

	@Instance("SoundCraft")
	public static SoundCraft instance;
	
	@SidedProxy(clientSide = "elcon.mods.soundcraft.ClientProxy", serverSide = "elcon.mods.soundcraft.CommonProxy")
	public static CommonProxy proxy;
	
	public static SoundCraftEventHandler eventHandler;
	
	public static CreativeTabs tabSoundCraft = new CreativeTabSoundCraft("SoundCraft");
	
	public static Block soundCable;
	public static Block speaker;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		SoundCraftConfig.load(config);
		config.save();
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderInformation();
		
		//init blocks
		soundCable = new BlockSoundCable(SoundCraftConfig.soundCableID).setStepSound(Block.soundClothFootstep).setHardness(0.8F).setCreativeTab(tabSoundCraft).setUnlocalizedName("soundCable");
		speaker = new BlockSpeaker(SoundCraftConfig.speakerID).setStepSound(Block.soundMetalFootstep).setHardness(5.0F).setResistance(10.0F).setCreativeTab(tabSoundCraft).setUnlocalizedName("speaker");
		
		//register blocks
		GameRegistry.registerBlock(soundCable, "SoundCraft_soundCable");
		GameRegistry.registerBlock(speaker, "SoundCraft_speaker");
		
		//register tileentities
		GameRegistry.registerTileEntity(TileEntitySoundCable.class, "SoundCable");
		
		//add block names
		LanguageRegistry.addName(soundCable, "Sound Cable");
		LanguageRegistry.addName(speaker, "Speaker");
		
		//add localizations
		LanguageRegistry.instance().addStringLocalization("itemGroup.SoundCraft", "SoundCraft");
		
		eventHandler = new SoundCraftEventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
