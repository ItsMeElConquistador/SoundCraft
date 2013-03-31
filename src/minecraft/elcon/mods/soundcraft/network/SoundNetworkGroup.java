package elcon.mods.soundcraft.network;

import java.io.Serializable;
import java.util.ArrayList;

import elcon.mods.soundcraft.tileentities.TileEntitySoundAcceptor;
import elcon.mods.soundcraft.tileentities.TileEntitySoundSource;

public class SoundNetworkGroup implements Serializable {

	public int id;
	
	public ArrayList<SoundNetworkNode> nodes = new ArrayList<SoundNetworkNode>();
	public ArrayList<SoundNetworkConnection> connections = new ArrayList<SoundNetworkConnection>();
	
	public ArrayList<TileEntitySoundSource> sources = new ArrayList<TileEntitySoundSource>();
	public ArrayList<TileEntitySoundAcceptor> acceptors = new ArrayList<TileEntitySoundAcceptor>();	
	
	public SoundNetworkGroup(int i, SoundNetworkConnection... cs) {
		id = i;
		for(SoundNetworkConnection c : cs) {
			connections.add(c);
			SoundNetworkNode n1;
			SoundNetworkNode n2;
			if(!containsNode(c.node1)) {
				n1 = new SoundNetworkNode(c.node1);
				nodes.add(n1);
			} else {
				n1 = getNode(c.node1);
			}
			if(!containsNode(c.node2)) {
				n2 = new SoundNetworkNode(c.node2);
				nodes.add(n2);
			} else {
				n2 = getNode(c.node2);
			}
		}
	}

	public void sendSound(TileEntitySoundSource source, Sound sound) {
		if(sources.contains(source)) {
			for(TileEntitySoundAcceptor acceptor : acceptors) {
				if(acceptor.canAcceptSound(sound)) {
					acceptor.receiveSound(sound);
				}
			}
		}
	}
	
	public int getNodeID() {
		int highest = 0;
		for(int i = 0; i < nodes.size(); i++) {
			if(getNode(i) == null) {
				return i;
			} else if(i > highest) {
				highest = i;
			}
		}
		return 0;
	}
	
	public int[] searchComponent(int[] comps, int i, int count, SoundNetworkGroup g) {
		comps[i] = count;
		g.nodes.add(getNode(i));
		
		for(int j = 0; j < nodes.size(); j++) {
			if(comps[j] == 0 && isConnected(i, j)) {
				g.nodes.add(getNode(j));
				g.connections.add(getConnectionObject(i, j));
				comps = searchComponent(comps, j, count, g);
			}
		}
		return comps;
	}
	
	public SoundNetworkGroup[] countComponents() {
		SoundNetworkGroup[] newGroups = new SoundNetworkGroup[8];
		int newGroupCount = 0;
		int count = -1;
		int[] comps = new int[nodes.size()];
		for(int i = 0; i < nodes.size(); i++) {
			if(comps[i] == 0) {
				count++;
				newGroupCount++;
				newGroups[newGroupCount] = new SoundNetworkGroup(SoundNetwork.findGroupID());
				comps = searchComponent(comps, i, count, newGroups[newGroupCount]);
			}
		}
		return newGroups;
	}
	
	public boolean hasPath(int n1, int n2) {
		return false;
	}
	
	public void printAllNodes() {
		for(SoundNetworkNode n : nodes) {
			System.out.println(n.id);
		}
	}
	
	public void printAllConnections() {
		for(SoundNetworkConnection c : connections) {
			System.out.println(Character.toString((char) (c.node1 + 'A')) + " to " + Character.toString((char) (c.node2 + 'A')) + " with value " + c.value);
		}
	}
	
	public SoundNetworkNode getNode(int i) {
		for(SoundNetworkNode n : nodes) {
			if(n.id == i) {
				return n;
			}
		}
		return null;
	}
	
	public int getConnection(int n1, int n2) {
		for(SoundNetworkConnection c : connections) {
			if(c.node1 == n1 && c.node2 == n2) {
				return c.value;
			}
		}
		return -1;
	}
	
	public SoundNetworkConnection getConnectionObject(int n1, int n2) {
		for(SoundNetworkConnection c : connections) {
			if(c.node1 == n1 && c.node2 == n2) {
				return c;
			}
		}
		return null;
	}
	
	public boolean isConnected(int n1, int n2) {
		for(SoundNetworkConnection c : connections) {
			if(c.node1 == n1 && c.node2 == n2) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsNode(int id) {
		for(SoundNetworkNode n : nodes) {
			if(n.id == id) {
				return true;
			}
		}
		return false;
	}
}
