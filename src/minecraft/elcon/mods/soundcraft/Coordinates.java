package elcon.mods.soundcraft;

public class Coordinates {

	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	public Coordinates(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Coordinates) {
			return equals((Coordinates) obj);
		}
		return false;
	}
	
	public boolean equals(Coordinates c) {
		if(x == c.x && y == c.y && z == c.z) {
			return true;
		}
		return false;
	}
}
