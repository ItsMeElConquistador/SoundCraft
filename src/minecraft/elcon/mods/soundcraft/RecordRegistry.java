package elcon.mods.soundcraft;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.FMLCommonHandler;
import elcon.mods.soundcraft.mus.MusConverter;
import elcon.mods.soundcraft.ogg.Ogg;

public class RecordRegistry {

	public static ArrayList<String> records = new ArrayList<String>();
	public static HashMap<String, Double> recordTime = new HashMap<String, Double>();
	
	public static void registerRecord(String name, double time) {
		records.add(name);
		recordTime.put(name, time);
	}
	
	public static void registerRecord(String name) {
		records.add(name);
		double time = calculateDiscTime(name);
		if(SoundCraftConfig.debugMode) {
			FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[SoundCraft] Record \"" + name + "\" plays for " + Double.toString(time) + " seconds.");
		}
		recordTime.put(name, time);
	}
	
	public static double calculateDiscTime(File file) {
		if(file.exists()) {
			if(file.getName().endsWith(".mus")) {
				MusConverter.decode(file);
				file = new File(file.getPath().replaceAll(".mus", ".ogg"));
			}
			if(file.getName().endsWith(".ogg")) {
				Ogg ogg = new Ogg(file);
				String sec = Double.toString(ogg.getSeconds());
				double i = Double.parseDouble(sec.substring(0, sec.indexOf('.') + 3));
				int j = Integer.parseInt(sec.substring(sec.indexOf('.') + 3, sec.indexOf('.') + 4));
				return i + (j >= 5 ? 0.01 : 0);
			}
		}		
		return 0;
	}
	
	public static double calculateDiscTime(String name) {
		File file = new File(Minecraft.getMinecraftDir(), "/resources/streaming/" + name + ".mus");
		return calculateDiscTime(file);
	}
}
