package elcon.mods.soundcraft.sounds;

import net.minecraft.world.World;

public class SoundType {

	public static SoundType[] types = new SoundType[16];
	
	public SoundType sound = new SoundType(0, "sound");
	public SoundType disc = new SoundType(1, "disc");
	public SoundType effect = new SoundType(2, "effect");
	public SoundType cd = new SoundType(3, "cd");
	
	public int id = 0;
	public String name = "sound";
	
	public SoundType(int i, String n) {
		id = i;
		name = n;
		
		types[id] = this;
	}
	
	public void playSound(World world, int x, int y, int z, Sound sound) {
		
	}
}
