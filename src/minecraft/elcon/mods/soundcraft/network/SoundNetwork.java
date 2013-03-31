package elcon.mods.soundcraft.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.FMLCommonHandler;
import elcon.mods.soundcraft.tileentities.TileEntitySoundAcceptor;
import elcon.mods.soundcraft.tileentities.TileEntitySoundObject;
import elcon.mods.soundcraft.tileentities.TileEntitySoundSource;

public class SoundNetwork {

	public static ArrayList<SoundNetworkGroup> groups = new ArrayList<SoundNetworkGroup>();
	
	public static int findGroupID() {
		int id = 0;
		int oldID = 0;
		int highest = 0;
		for(SoundNetworkGroup group : groups) {
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
	
	public static void printGroups() {
		System.out.println("Printing groups");
		for(SoundNetworkGroup g : groups) {
			if(g != null) {
				if(g.nodes != null && g.connections != null && g.acceptors != null && g.sources != null) {
					System.out.println(g.id + " - nodes: " + g.nodes.size() + " - connections: " + g.connections.size() + " - a: " + g.acceptors.size() + " - s:" + g.sources.size());
				}
			}
		}
	}
	
	public static void addGroup(SoundNetworkGroup group) {
		groups.add(group);
	}
	
	public static SoundNetworkGroup getGroup(int id) {
		for(SoundNetworkGroup group : groups) {
			if(group != null) {
				if(id == group.id) {
					return group;
				}
			}
		}
		return null;
	}
	
	public static void removeGroup(int id) {
		for(SoundNetworkGroup group : groups) {
			if(group != null) {
				if(id == group.id) {
					groups.remove(group);
				}
			}
		}
	}
	
	public static boolean groupExists(int id) {
		for(SoundNetworkGroup group : groups) {
			if(group != null) {
				if(id == group.id) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static SoundNetworkGroup mergeGroups(SoundNetworkGroup group1, SoundNetworkGroup group2) {
		for(TileEntitySoundSource source : group2.sources) {
			if(group1.sources == null) {
				group1.sources = new ArrayList<TileEntitySoundSource>();
			}
			group1.sources.add(source);
		}
		for(TileEntitySoundAcceptor acceptor : group2.acceptors) {
			if(group1.acceptors == null) {
				group1.acceptors = new ArrayList<TileEntitySoundAcceptor>();
			}
			group1.acceptors.add(acceptor);
		}
		group2.sources = null;
		group2.acceptors = null;
		return group1;
	}
	
	public static void connectGroups(TileEntitySoundObject obj1, TileEntitySoundObject obj2) {
		System.out.println("connecting " + obj1 + " to " + obj2);
		if(obj1 == null || obj2 == null) {
			return ;
		}
		boolean hasGroup1 = groupExists(obj1.group);
		boolean hasGroup2 = groupExists(obj2.group);
			
		if(hasGroup1 && hasGroup2) {
			SoundNetworkGroup group1 = getGroup(obj1.group);
			SoundNetworkGroup group2 = getGroup(obj2.group);
			if(group1.id < group2.id) {
				SoundNetworkGroup group = mergeGroups(group1, group2);
				obj2.group = group.id;
				
				int oldID = obj1.node;
				int newID = obj2.node;

				group.nodes.add(getGroup(obj2.group).getNode(newID));
				obj2.node = newID;
				
				group.connections.add(new SoundNetworkConnection(oldID, newID, 1));
				group.connections.add(new SoundNetworkConnection(newID, oldID, 1));
			} else {
				SoundNetworkGroup group = mergeGroups(group2, group1);
				obj1.group = group.id;
				
				int oldID = obj2.node;
				int newID = obj1.node;

				group.nodes.add(getGroup(obj1.group).getNode(newID));
				obj1.node = newID;
				
				group.connections.add(new SoundNetworkConnection(oldID, newID, 1));
				group.connections.add(new SoundNetworkConnection(newID, oldID, 1));
			}
		} else if(!hasGroup1 && !hasGroup2) {
			SoundNetworkGroup group = new SoundNetworkGroup(findGroupID());
			if(obj1 instanceof TileEntitySoundAcceptor) {
				group.acceptors.add((TileEntitySoundAcceptor) obj1);
			} else if(obj1 instanceof TileEntitySoundSource) {
				group.sources.add((TileEntitySoundSource) obj1);
			}
			if(obj2 instanceof TileEntitySoundAcceptor) {
				group.acceptors.add((TileEntitySoundAcceptor) obj2);
			} else if(obj2 instanceof TileEntitySoundSource) {
				group.sources.add((TileEntitySoundSource) obj2);
			}
			groups.add(group);
			obj1.group = group.id;
			obj2.group = group.id;
			
			int oldID = group.getNodeID();
			group.nodes.add(new SoundNetworkNode(oldID));
			obj1.node = oldID;
			
			int newID = group.getNodeID();
			group.nodes.add(new SoundNetworkNode(newID));
			obj2.node = newID;
			
			group.connections.add(new SoundNetworkConnection(oldID, newID, 1));
			group.connections.add(new SoundNetworkConnection(newID, oldID, 1));
		} else if(hasGroup1 && !hasGroup2) {
			SoundNetworkGroup group = getGroup(obj1.group);
			
			if(obj2 instanceof TileEntitySoundAcceptor) {
				group.acceptors.add((TileEntitySoundAcceptor) obj2);
			} else if(obj2 instanceof TileEntitySoundSource) {
				group.sources.add((TileEntitySoundSource) obj2);
			}
			obj2.group = group.id;
			
			int oldID = obj1.node;
			int newID = group.getNodeID();
			obj2.node = newID;
			group.nodes.add(new SoundNetworkNode(newID));
			group.connections.add(new SoundNetworkConnection(oldID, newID, 1));
			group.connections.add(new SoundNetworkConnection(newID, oldID, 1));
		} else if(!hasGroup1 && hasGroup2) {
			SoundNetworkGroup group = getGroup(obj2.group);
			
			if(obj1 instanceof TileEntitySoundAcceptor) {
				group.acceptors.add((TileEntitySoundAcceptor) obj1);
			} else if(obj1 instanceof TileEntitySoundSource) {
				group.sources.add((TileEntitySoundSource) obj1);
			}
			obj1.group = group.id;
			
			int oldID = obj2.node;
			int newID = group.getNodeID();
			obj1.node = newID;
			group.nodes.add(new SoundNetworkNode(newID));
			group.connections.add(new SoundNetworkConnection(oldID, newID, 1));
			group.connections.add(new SoundNetworkConnection(newID, oldID, 1));
		}
		printGroups();
	}
	
	public static void unconnectGroups(TileEntitySoundObject obj1, TileEntitySoundObject obj2) {
		System.out.println("unconnecting " + obj1 + " to " + obj2);
		if(obj1 == null || obj2 == null) {
			return;
		}
		SoundNetworkGroup group = getGroup(obj1.group);
		if(group == null) {
			group = getGroup(obj2.group);
			if(group == null) {
				return;
			}
		}
		
		//remove connection
		SoundNetworkConnection connection = group.getConnectionObject(obj1.node, obj2.node);
		group.connections.remove(connection);
		group.nodes.remove(group.getNode(obj1.node));
		
		//check group count
		SoundNetworkGroup[] newGroups = group.countComponents();
		if(newGroups.length > 1) {
			groups.remove(group);
			for(int i = 0; i < newGroups.length; i++) {
				groups.add(newGroups[i]);
			}
		}
		obj1.group = -1;
		obj1.node = -1;
		printGroups();
	}
	
	public static void load(File file) {
		try {
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			GZIPInputStream gzis = new GZIPInputStream(fis);
			ObjectInputStream in = new ObjectInputStream(gzis);
			ArrayList<SoundNetworkNode> saves = (ArrayList<SoundNetworkNode>) in.readObject();
			
			HashMap<Integer, SoundNetworkGroup> tempGroups = new HashMap<Integer, SoundNetworkGroup>();
			
			for(SoundNetworkNode save : saves) {
				
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
			
			ArrayList<SoundNetworkNode> saves = new ArrayList<SoundNetworkNode>();
			
			for(SoundNetworkGroup group : groups) {
				if(group != null) {
					
				}
			}
			
			out.writeObject(saves);
			out.flush();
			out.close();
			gzos.close();
			fos.close();
		} catch (Exception e) {
			FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "[AgeCraft] A major error has been encountered while trying to save the Tech Tree.", e);
		}
	}
}
