package elcon.mods.soundcraft.sounds;

public class Sound {
	
	public int type = 0;
	public String name = "";
	public float volume = 1.0F;
	public float pitch = 1.0F;
	
	public Sound(String n, float v, float p) {
		this(0, n, v, p);
	}
	
	public Sound(int t, String n, float v, float p) {
		type = t;
		name = n;
		volume = v;
		pitch = p;
	}
}
