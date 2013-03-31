package elcon.mods.soundcraft.network;

import java.io.Serializable;

public class SoundNetworkNode implements Serializable {

	public int id;
	
	public int x;
	public int y;
	public int z;
	
	public boolean isSource;
	public boolean isAcceptor;
	
	public SoundNetworkNode(int id) {
		this.id = id;
	}
	
	public SoundNetworkNode(int id, int x, int y, int z, boolean isSource, boolean isAcceptor) {
		this(id);
		this.x = x;
		this.y = y;
		this.z = z;
		this.isSource = isSource;
		this.isAcceptor = isAcceptor;
	}
}
