package elcon.mods.soundcraft;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import elcon.mods.soundcraft.blocks.BlockAdvancedJukebox;
import elcon.mods.soundcraft.blocks.BlockSoundCable;
import elcon.mods.soundcraft.blocks.BlockSpeaker;
import elcon.mods.soundcraft.items.ItemSoundCable;
import elcon.mods.soundcraft.items.ItemSoundCraft;
import elcon.mods.soundcraft.tileentities.TileEntityAdvancedJukebox;
import elcon.mods.soundcraft.tileentities.TileEntitySoundCable;
import elcon.mods.soundcraft.tileentities.TileEntitySpeaker;

@Mod(modid = "SoundCraft", name = "SoundCraft", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, packetHandler = SoundCraftPacketHandler.class, channels = {"SoundCraft", "SCCable", "SCSound"})
public class SoundCraft {

	@Instance("SoundCraft")
	public static SoundCraft instance;
	
	@SidedProxy(clientSide = "elcon.mods.soundcraft.ClientProxy", serverSide = "elcon.mods.soundcraft.CommonProxy")
	public static CommonProxy proxy;
	
	public static SoundCraftEventHandler eventHandler;
	
	public static CreativeTabs tabSoundCraft = new CreativeTabSoundCraft("SoundCraft");
	
	public static Block soundCable;
	public static Block speaker;
	public static Block advancedJukebox;
	
	public static Item soundCableItem;
	public static Item circuit;
	public static Item speakerItem;
	
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
		advancedJukebox = new BlockAdvancedJukebox(SoundCraftConfig.advancedJukeboxID).setStepSound(Block.soundMetalFootstep).setHardness(5.0F).setResistance(10.0F).setCreativeTab(tabSoundCraft).setUnlocalizedName("advancedJukebox");
		
		//init items
		soundCableItem = new ItemSoundCable(SoundCraftConfig.soundCableID - 256, soundCable);
		circuit = new ItemSoundCraft(SoundCraftConfig.circuitID, "circuit").setUnlocalizedName("circuit");
		speakerItem = new ItemSoundCraft(SoundCraftConfig.speakerItemID, "speaker").setUnlocalizedName("speaker");
		
		//register blocks
		GameRegistry.registerBlock(soundCable, "SoundCraft_soundCable");
		GameRegistry.registerBlock(speaker, "SoundCraft_speaker");
		GameRegistry.registerBlock(advancedJukebox, "SoundCraft_advancedJukebox");
		
		//register tileentities
		GameRegistry.registerTileEntity(TileEntitySoundCable.class, "SoundCable");
		GameRegistry.registerTileEntity(TileEntitySpeaker.class, "Speaker");
		GameRegistry.registerTileEntity(TileEntityAdvancedJukebox.class, "AdvancedJukebox");
		
		//add block names
		LanguageRegistry.addName(soundCable, "Sound Cable");
		LanguageRegistry.addName(speaker, "Speaker");
		LanguageRegistry.addName(advancedJukebox, "Advanced Jukebox");
		
		//add item names
		LanguageRegistry.addName(soundCableItem, "Sound Cable");
		LanguageRegistry.addName(speakerItem, "Speaker");
		LanguageRegistry.addName(circuit, "Sound Circiut");
		
		//add localizations
		LanguageRegistry.instance().addStringLocalization("itemGroup.SoundCraft", "SoundCraft");
		LanguageRegistry.instance().addStringLocalization("soundcraft.advancedJukebox", "en_US", "Advanced Jukebox");
		
		eventHandler = new SoundCraftEventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);
		
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		GameRegistry.addRecipe(new ItemStack(circuit), 
			" i ", "rgr", " i ", 'i', Item.ingotIron, 'r', Item.redstone, 'g', Item.ingotGold 
		);
		GameRegistry.addRecipe(new ItemStack(speakerItem),
			" i ", "ici", " i ", 'i', Item.ingotIron, 'c', circuit
		);
		GameRegistry.addRecipe(new ItemStack(speaker),
			"isi", "aca", "iri", 'i', Item.ingotIron, 'c', circuit, 'a', soundCableItem, 'r', Item.redstone, 's', speakerItem
		);
		GameRegistry.addRecipe(new ItemStack(advancedJukebox),
			"iji", "aca", "iri", 'i', Item.ingotIron, 'c', circuit, 'a', soundCableItem, 'r', Item.redstone, 'j', Block.jukebox
		);
		/*GameRegistry.addRecipe(new ItemStack(soundCableItem, 1, 0),
			"www", "iii", "ww", 'i', "ingotCopper", 'w', Block.cloth
		);*/
		/*GameRegistry.addRecipe(new ItemStack(soundCableItem, 1, 1),
			"www", "iii", "ww", 'i', "ingotTin", 'w', Block.cloth
		);*/
		/*GameRegistry.addRecipe(new ItemStack(soundCableItem, 1, 2),
			"www", "iii", "ww", 'i', "ingotSilver", 'w', Block.cloth
		);*/
		GameRegistry.addRecipe(new ItemStack(soundCableItem, 1, 3),
			"www", "iii", "ww", 'i', Item.ingotIron, 'w', Block.cloth
		);
		GameRegistry.addRecipe(new ItemStack(soundCableItem, 1, 4),
			"www", "iii", "ww", 'i', Item.ingotGold, 'w', Block.cloth
		);
		
		//GameRegistry.addRecipe();
	}
}
