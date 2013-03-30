package elcon.mods.soundcraft.network;

import java.util.HashMap;

import elcon.mods.soundcraft.Coordinates;

public class SoundNetwork {

	public static HashMap<Coordinates, SoundNetworkGroup> groups = new HashMap<Coordinates, SoundNetworkGroup>();
	
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
	
	public static void removeGroup(int x, int y, int z) {
		Coordinates c = new Coordinates(x, y, z);
		if(groups.containsKey(c)) {
			groups.remove(c);
		}
	}
	
	public static void mergeGroups(SoundNetworkGroup group1, SoundNetworkGroup group2) {
		for(ISoundSource source : group2.sources) {
			group1.sources.add(source);
		}
		for(ISoundAcceptor acceptor : group2.acceptors) {
			group1.acceptors.add(acceptor);
		}
		group2.sources = null;
		group2.acceptors = null;
	}
	
	public static void connectGroups(int x1, int y1, int z1, int x2, int y2, int z2) {
		
	}
}
