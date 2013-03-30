package elcon.mods.soundcraft;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class SoundCableType {

	public SoundCableType[] soundCables = new SoundCableType[16];
	
	public int id;
	public String name;
	public Icon[] textures = new Icon[17];
	
	public SoundCableType(int i, String n) {
		id = i;
		name = n;
	}
	
	public void registerIcon(IconRegister iconRegister) {
		textures[0] = iconRegister.registerIcon("soundcraft:soundCable" + name);
		for(int i = 1; i < 17; i++) {
			textures[i] = iconRegister.registerIcon("soundcraft:soundCable" + name + Integer.toString(i));
		}
	}
}
