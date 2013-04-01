package elcon.mods.soundcraft.sounds;

public class SoundDisc extends Sound {

	public int discID = 0;
	
	public SoundDisc(String n, int id, float v, float p) {
		super(1, n, v, p);
		discID = id;
	}
}
