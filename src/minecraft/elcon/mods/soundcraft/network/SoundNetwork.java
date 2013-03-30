package elcon.mods.soundcraft.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import cpw.mods.fml.common.FMLCommonHandler;
import elcon.mods.soundcraft.Coordinates;
import elcon.mods.soundcraft.blocks.TileEntitySoundObject;

public class SoundNetwork {

	public static HashMap<Coordinates, SoundNetworkGroup> groups = new HashMap<Coordinates, SoundNetworkGroup>();
	
	public static int findGroupID() {
		int id = 0;
		int oldID = 0;
		int highest = 0;
		for(SoundNetworkGroup group : groups.values()) {
			if(group != null) {
				id = group.id;
				if(id != 0 && id != (oldID + 1)) {
					return oldID + 1;
				}
				oldID = id;
				if(id > highest) {
					highest = id;
				}
			}
		}
		return highest + 1;
	}
	
	public static void addGroup(int x, int y, int z, SoundNetworkGroup group) {
		groups.put(new Coordinates(x, y, z), group);
	}
	
	public static SoundNetworkGroup getGroup(int x, int y, int z) {
		Coordinates c = new Coordinates(x, y, z);
		if(groups.containsKey(c)) {
			return groups.get(c);
		}
		return null;
	}
	
	public static SoundNetworkGroup getGroup(int id) {
		for(SoundNetworkGroup group : groups.values()) {
			if(group != null) {
				if(id == group.id) {
					return group;
				}
			}
		}
		return null;
	}
	
	public static void removeGroup(int x, int y, int z) {
		Coordinates c = new Coordinates(x, y, z);
		if(groups.containsKey(c)) {
			groups.remove(c);
		}
	}
	
	public static SoundNetworkGroup mergeGroups(SoundNetworkGroup group1, SoundNetworkGroup group2) {
		for(ISoundSource source : group2.sources) {
			group1.sources.add(source);
		}
		for(ISoundAcceptor acceptor : group2.acceptors) {
			group1.acceptors.add(acceptor);
		}
		group2.sources = null;
		group2.acceptors = null;
		return group1;
	}
	
	public static boolean hasGroup(int x, int y, int z) {
		return getGroup(x, y, z) != null;
	}
	
	public static void connectGroups(TileEntitySoundObject obj1, int x1, int y1, int z1, TileEntitySoundObject obj2, int x2, int y2, int z2) {
		System.out.println("connecting " + obj1 + " to " + obj2);
		if(obj1 == null || obj2 == null) {
			return;
		}
		boolean hasGroup1 = hasGroup(x1, y1, z1);
		boolean hasGroup2 = hasGroup(x2, y2, z2);
		if(hasGroup1 && hasGroup2) {
			SoundNetworkGroup group1 = getGroup(x1, y1, z1);
			SoundNetworkGroup group2 = getGroup(x2, y2, z2);
			if(group1.id < group2.id) {
				SoundNetworkGroup group = mergeGroups(group1, group2);
				obj2.group = group.id;
			} else {
				SoundNetworkGroup group = mergeGroups(group2, group1);
				obj1.group = group.id;
			}
		} else if(!hasGroup1 && !hasGroup2) {
			SoundNetworkGroup group = new SoundNetworkGroup(findGroupID());
			if(obj1 instanceof ISoundAcceptor) {
				group.acceptors.add((ISoundAcceptor) obj1);
			} else if(obj1 instanceof ISoundSource) {
				group.sources.add((ISoundSource) obj1);
			}
			if(obj2 instanceof ISoundAcceptor) {
				group.acceptors.add((ISoundAcceptor) obj2);
			} else if(obj2 instanceof ISoundSource) {
				group.sources.add((ISoundSource) obj2);
			}
			obj1.group = group.id;
			obj2.group = group.id;
		} else if(hasGroup1 && !hasGroup2) {
			SoundNetworkGroup group = getGroup(x1, y1, z1);
			
			if(obj2 instanceof ISoundAcceptor) {
				group.acceptors.add((ISoundAcceptor) obj2);
			} else if(obj2 instanceof ISoundSource) {
				group.sources.add((ISoundSource) obj2);
			}
			obj2.group = group.id;
		} else if(!hasGroup1 && hasGroup2) {
			SoundNetworkGroup group = getGroup(x2, y2, z2);
			
			if(obj1 instanceof ISoundAcceptor) {
				group.acceptors.add((ISoundAcceptor) obj1);
			} else if(obj1 instanceof ISoundSource) {
				group.sources.add((ISoundSource) obj1);
			}
			obj1.group = group.id;
		}
	}
	
	public static void unconnectGroups() {
		//TODO: check if two groups should be split
	}
	
	public static void unconnectFromGroup(TileEntitySoundObject obj, int x, int y, int z) {
		boolean hasGroup = hasGroup(x, y, z);
		if(hasGroup) {
			SoundNetworkGroup group = getGroup(x, y, z);
			if(obj instanceof ISoundAcceptor) {
				group.acceptors.remove(obj);
			} else if(obj instanceof ISoundSource) {
				group.sources.remove(obj);
			}
			obj.group = 0;
		}
	}
	
	public static void load(File file) {
		try {
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(gzis);
			List<String> loaded = (List<String>) in.readObject();
			
			for(String key : loaded) {
				
			}
			FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[SoundCraft] Loaded the sound network");
			
			in.close();
			gzis.close();
			fis.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void save(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			ObjectOutputStream out = new ObjectOutputStream(gzos);
			out.writeObject(groups);
			out.flush();
			out.close();
			gzos.close();
			fos.close();
		} catch (Exception e) {
			FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "[AgeCraft] A major error has been encountered while trying to save the Tech Tree.", e);
		}
	}
}
