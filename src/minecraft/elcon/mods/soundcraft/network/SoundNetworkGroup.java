package elcon.mods.soundcraft.network;

import java.io.Serializable;
import java.util.ArrayList;

public class SoundNetworkGroup implements Serializable {

	public int id;
	
	public ArrayList<ISoundSource> sources = new ArrayList<ISoundSource>();
	public ArrayList<ISoundAcceptor> acceptors = new ArrayList<ISoundAcceptor>();
	
	public SoundNetworkGroup(int i) {
		id = i;
	}
	
	public void sendSound(ISoundSource source, Sound sound) {
		if(sources.contains(source)) {
			for(ISoundAcceptor acceptor : acceptors) {
				if(acceptor.canAcceptSound(sound)) {
					acceptor.receiveSound(sound);
				}
			}
		}
	}
}
