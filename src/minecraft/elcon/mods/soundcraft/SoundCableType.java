package elcon.mods.soundcraft;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class SoundCableType {

	public static SoundCableType[] soundCables = new SoundCableType[16];
	
	public static SoundCableType copper = new SoundCableType(0, "Copper", 16);
	public static SoundCableType tin = new SoundCableType(1, "Tin", 16);
	public static SoundCableType silver = new SoundCableType(2, "Silver", 32);
	public static SoundCableType iron = new SoundCableType(3, "Iron", 64);
	public static SoundCableType gold = new SoundCableType(4, "Gold", 128);
	
	public int id;
	public String name;
	public Icon[] textures = new Icon[16];
	
	public int maxBlockDistance = 16;
	
	public SoundCableType(int i, String n, int m) {
		id = i;
		name = n;
		
		maxBlockDistance = m;
		
		soundCables[i] = this;
	}
	
	public static void registerIcons(IconRegister iconRegister) {
		for(int i = 0; i < soundCables.length; i++) {
			if(soundCables[i] != null) {
				soundCables[i].registerIcon(iconRegister);
			}
		}
	}
	
	public void registerIcon(IconRegister iconRegister) {
		for(int i = 0; i < 16; i++) {
			textures[i] = iconRegister.registerIcon("soundcraft:soundCable" + name + Integer.toString(i));
		}
	}
}
