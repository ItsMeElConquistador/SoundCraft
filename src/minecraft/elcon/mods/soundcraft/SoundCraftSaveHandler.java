package elcon.mods.soundcraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.ISaveHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import elcon.mods.soundcraft.network.SoundNetwork;

public class SoundCraftSaveHandler {

	public ISaveHandler saveHandler;
	public World world;
	
	public SoundCraftSaveHandler(ISaveHandler saveHandler, World world) {
		this.saveHandler = saveHandler;
		this.world = world;
	}
	
	public void load() {
		if(world.provider.dimensionId == 0 && !saveHandler.getSaveDirectoryName().equalsIgnoreCase("none")) {
			try {
				File file = getNetworkSaveFile(saveHandler, world);
				if(file != null) {
					try {	
						SoundNetwork.load(file);
					} catch(Exception e) {
						e.printStackTrace();
						
						File file2 = new File(new StringBuilder().append(file.getAbsolutePath()).append(".bak").toString());
						if(file2.exists()) {
							SoundNetwork.load(file2);
						} else {
							file.createNewFile();
							SoundNetwork.save(file);
						}
					}	
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}				
		}
	}
	
	public void save() {
		if(world.provider.dimensionId == 0) {
			try {
				File file = getNetworkSaveFile(saveHandler, world, false);
				if(file != null) {
					SoundNetwork.save(file);
					copyFile(file, getNetworkSaveFile(saveHandler, world, true));
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static File getNetworkSaveFile(ISaveHandler saveHandler, World world) {
		File worldDir = new File(saveHandler.getSaveDirectoryName());
		IChunkLoader loader = saveHandler.getChunkLoader(world.provider);
		if ((loader instanceof AnvilChunkLoader)) {
			worldDir = ((AnvilChunkLoader) loader).chunkSaveLocation;
		}
		File file = new File(worldDir, "soundcraft_network");
		return file;
	}

	public File getNetworkSaveFile(ISaveHandler saveHandler, World world, boolean backup) throws Exception {
		File worldDir = new File(saveHandler.getSaveDirectoryName());
		IChunkLoader loader = saveHandler.getChunkLoader(world.provider);
		if ((loader instanceof AnvilChunkLoader)) {
			worldDir = ((AnvilChunkLoader) loader).chunkSaveLocation;
		}
		return new File(worldDir, new StringBuilder().append("soundcraft_network").append(backup ? ".bak" : "").toString());
	}

	public void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0L, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
}
