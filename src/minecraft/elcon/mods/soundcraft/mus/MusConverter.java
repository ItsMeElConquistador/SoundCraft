package elcon.mods.soundcraft.mus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import elcon.mods.soundcraft.SoundCraftConfig;

public class MusConverter {

	public static void decodeFolder(File dir) {
		File[] files = dir.listFiles();

		for(File file : files) {
			String name = file.getName();
			if(!file.isFile() || (!name.endsWith(".mus") && !name.endsWith(".ogg")) || name.endsWith(".mus")) {
				continue;
			}

			decode(file);
		}
	}

	public static void encodeFolder(File dir) {
		File[] files = dir.listFiles();

		for(File file : files) {
			String name = file.getName();
			if(!file.isFile() || (!name.endsWith(".mus") && !name.endsWith(".ogg")) || name.endsWith(".ogg")) {
				continue;
			}

			encode(file);
		}
	}

	public static void decode(File musFile) {
		File oggFile = new File(musFile.getPath().replaceFirst("\\.mus$", ".ogg"));
		boolean delete = false;

		InputStream is = null;
		OutputStream os = null;

		try {
			is = new MusInputStream(new FileInputStream(musFile), musFile.getName().hashCode());
			os = new FileOutputStream(oggFile);

			byte[] buffer = new byte[4096];
			int n = 0;
			while(-1 != (n = is.read(buffer))) {
				os.write(buffer, 0, n);
			}
			if(SoundCraftConfig.debugMode) {
				FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[SoundCraft] [MusConverter] Successfully decoded " + musFile.getName());
			}
		} catch(IOException e) {
			if(SoundCraftConfig.debugMode) {
				FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "[SoundCraft] [MusConverter] Decoding error: " + e.getMessage());
			}
		} finally {
			try {
				is.close();
				os.close();
			} catch(Exception e) {
			}
		}
	}

	public static void encode(File oggFile) {
		File musFile = new File(oggFile.getPath().replaceFirst("\\.ogg$", ".mus"));
		boolean delete = false;

		InputStream is = null;
		OutputStream os = null;

		try {
			is = new FileInputStream(oggFile);
			os = new MusOutputStream(new FileOutputStream(musFile), musFile.getName().hashCode());

			byte[] buffer = new byte[4096];
			int n = 0;
			while(-1 != (n = is.read(buffer))) {
				os.write(buffer, 0, n);
			}
			if(SoundCraftConfig.debugMode) {
				FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[SoundCraft] [MusConverter] Successfully encoded " + oggFile.getName());
			}
		} catch(IOException e) {
			if(SoundCraftConfig.debugMode) {	
				FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "[SoundCraft] [MusConverter] Encoding error: " + e.getMessage());
			}
		} finally {
			try {
				is.close();
				os.close();
			} catch(Exception e) {
			}
		}
	}
}
