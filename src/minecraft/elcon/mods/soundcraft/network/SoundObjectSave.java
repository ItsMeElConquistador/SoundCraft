package elcon.mods.soundcraft.network;

import java.io.Serializable;

public class SoundObjectSave implements Serializable {

	public int x;
	public int y;
	public int z;
	
	public int groupID;
	
	public boolean isSource;
	
	public String tileEntity = "";
	
	public SoundObjectSave(int x, int y, int z, int id, String tileEntity, boolean isSource) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.groupID = id;
		this.tileEntity = tileEntity;
		this.isSource = isSource;
	}
}
