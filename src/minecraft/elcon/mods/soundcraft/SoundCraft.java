package elcon.mods.soundcraft;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
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
import cpw.mods.fml.relauncher.Side;
import elcon.mods.soundcraft.blocks.BlockAdvancedJukebox;
import elcon.mods.soundcraft.blocks.BlockSoundCable;
import elcon.mods.soundcraft.blocks.BlockSpeaker;
import elcon.mods.soundcraft.items.ItemSoundCable;
import elcon.mods.soundcraft.items.ItemSoundCraft;
import elcon.mods.soundcraft.tileentities.TileEntityAdvancedJukebox;
import elcon.mods.soundcraft.tileentities.TileEntitySoundCable;
import elcon.mods.soundcraft.tileentities.TileEntitySpeaker;

@Mod(modid = "SoundCraft", name = "SoundCraft", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, packetHandler = SoundCraftPacketHandler.class, channels = {"SoundCraft", "SCTile", "SCCable", "SCSound"})
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
	
	public static Item circuit;
	public static Item speakerItem;
	public static Item soundCableCopper;
	public static Item soundCableTin;
	public static Item soundCableSilver;
	public static Item soundCableIron;
	public static Item soundCableGold;
	
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
		circuit = new ItemSoundCraft(SoundCraftConfig.circuitID, "circuit").setCreativeTab(tabSoundCraft).setUnlocalizedName("circuit");
		speakerItem = new ItemSoundCraft(SoundCraftConfig.speakerItemID, "speaker").setCreativeTab(tabSoundCraft).setUnlocalizedName("speaker");
		soundCableCopper = new ItemSoundCable(SoundCraftConfig.soundCableCopperID, 0).setCreativeTab(tabSoundCraft).setUnlocalizedName("soundCableCopper");
		soundCableTin = new ItemSoundCable(SoundCraftConfig.soundCableTinID, 1).setCreativeTab(tabSoundCraft).setUnlocalizedName("soundCableTin");
		soundCableSilver = new ItemSoundCable(SoundCraftConfig.soundCableSilverID, 2).setCreativeTab(tabSoundCraft).setUnlocalizedName("soundCableSilver");
		soundCableIron = new ItemSoundCable(SoundCraftConfig.soundCableIronID, 3).setCreativeTab(tabSoundCraft).setUnlocalizedName("soundCableIron");
		soundCableGold = new ItemSoundCable(SoundCraftConfig.soundCableGoldID, 4).setCreativeTab(tabSoundCraft).setUnlocalizedName("soundCableGold");
		
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
		LanguageRegistry.addName(speakerItem, "Speaker");
		LanguageRegistry.addName(circuit, "Sound Circiut");
		LanguageRegistry.addName(soundCableCopper, "Copper Sound Cable");
		LanguageRegistry.addName(soundCableTin, "Tin Sound Cable");
		LanguageRegistry.addName(soundCableSilver, "Silver Sound Cable");
		LanguageRegistry.addName(soundCableIron, "Iron Sound Cable");
		LanguageRegistry.addName(soundCableGold, "Gold Sound Cable");
		
		//add localizations
		LanguageRegistry.instance().addStringLocalization("itemGroup.SoundCraft", "SoundCraft");
		LanguageRegistry.instance().addStringLocalization("soundcraft.advancedJukebox", "en_US", "Advanced Jukebox");
		
		eventHandler = new SoundCraftEventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);
		
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			for(int i = 0; i < Item.itemsList.length; i++) {
				if(Item.itemsList[i] != null) {
					if(Item.itemsList[i] instanceof ItemRecord) {
						RecordRegistry.registerRecord(((ItemRecord) Item.itemsList[i]).recordName);
					}
				}
			}
		} else {
			RecordRegistry.registerRecord("11", 71.11);
			RecordRegistry.registerRecord("13", 178.09);
			RecordRegistry.registerRecord("cat", 185.34);
			RecordRegistry.registerRecord("blocks", 345.91);
			RecordRegistry.registerRecord("chirp", 185.58);
			RecordRegistry.registerRecord("far", 174.46);
			RecordRegistry.registerRecord("mall", 197.22);
			RecordRegistry.registerRecord("mellohi", 96.20);
			RecordRegistry.registerRecord("stal", 150.86);
			RecordRegistry.registerRecord("strad", 188.17);
			RecordRegistry.registerRecord("ward", 251.39);
			RecordRegistry.registerRecord("wait", 237.89);
		}
		
		GameRegistry.addRecipe(new ItemStack(circuit), 
			" i ", "rgr", " i ", 'i', Item.ingotIron, 'r', Item.redstone, 'g', Item.ingotGold 
		);
		GameRegistry.addRecipe(new ItemStack(speakerItem),
			" i ", "ici", " i ", 'i', Item.ingotIron, 'c', circuit
		);
		GameRegistry.addRecipe(new ItemStack(speaker),
			"isi", "aca", "iri", 'i', Item.ingotIron, 'c', circuit, 'a', soundCableCopper, 'r', Item.redstone, 's', speakerItem
		);
		GameRegistry.addRecipe(new ItemStack(advancedJukebox),
			"iji", "aca", "iri", 'i', Item.ingotIron, 'c', circuit, 'a', soundCableCopper, 'r', Item.redstone, 'j', Block.jukebox
		);
		/*GameRegistry.addRecipe(new ItemStack(soundCableCopper, 6, 0),
			"www", "ccc", "www", 'c', "ingotCopper", 'w', Block.cloth
		);*/
		/*GameRegistry.addRecipe(new ItemStack(soundCableTin, 6, 1),
			"www", "ttt", "www", 't', "ingotTin", 'w', Block.cloth
		);*/
		/*GameRegistry.addRecipe(new ItemStack(soundCableSilver, 6, 2),
			"www", "sss", "www", 's', "ingotSilver", 'w', Block.cloth
		);*/
		GameRegistry.addRecipe(new ItemStack(soundCableIron, 6, 3),
			"www", "iii", "www", 'i', Item.ingotIron, 'w', Block.cloth
		);
		GameRegistry.addRecipe(new ItemStack(soundCableGold, 6, 4),
			"www", "ggg", "www", 'g', Item.ingotGold, 'w', Block.cloth
		);		
		
		for(int i = 0; i < 16; i++) {
			GameRegistry.addShapelessRecipe(new ItemStack(soundCableCopper, 1, i), new ItemStack(Item.dyePowder, 1, i), new ItemStack(soundCableCopper));
			GameRegistry.addShapelessRecipe(new ItemStack(soundCableTin, 1, i), new ItemStack(Item.dyePowder, 1, i), new ItemStack(soundCableTin));
			GameRegistry.addShapelessRecipe(new ItemStack(soundCableSilver, 1, i), new ItemStack(Item.dyePowder, 1, i), new ItemStack(soundCableSilver));
			GameRegistry.addShapelessRecipe(new ItemStack(soundCableIron, 1, i), new ItemStack(Item.dyePowder, 1, i), new ItemStack(soundCableIron));
			GameRegistry.addShapelessRecipe(new ItemStack(soundCableGold, 1, i), new ItemStack(Item.dyePowder, 1, i), new ItemStack(soundCableGold));
		}
	}
}
