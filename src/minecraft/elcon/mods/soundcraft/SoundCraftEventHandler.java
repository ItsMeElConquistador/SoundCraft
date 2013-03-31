package elcon.mods.soundcraft;

import net.minecraftforge.client.event.sound.PlayBackgroundMusicEvent;
import net.minecraftforge.client.event.sound.PlaySoundEffectEvent;
import net.minecraftforge.client.event.sound.PlaySoundEffectSourceEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.client.event.sound.PlayStreamingEvent;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.world.WorldEvent;

public class SoundCraftEventHandler {

	/*@ForgeSubscribe
	public void worldLoad(WorldEvent.Load event) {
		if(!event.world.isRemote) {
			SoundCraftSaveHandler sh = new SoundCraftSaveHandler(event.world.getSaveHandler(), event.world);
			sh.load();
		}
	}
	
	@ForgeSubscribe
	public void worldSave(WorldEvent.Save event) {
		if(!event.world.isRemote) {
			SoundCraftSaveHandler sh = new SoundCraftSaveHandler(event.world.getSaveHandler(), event.world);
			sh.save();
		}
	}*/
	
	@ForgeSubscribe
	public void onPlayBackgroundMusic(PlayBackgroundMusicEvent event) {
		
	}
	
	@ForgeSubscribe
	public void onPlaySoundEffect(PlaySoundEffectEvent event) {
		
	}
	
	@ForgeSubscribe
	public void onPlaySoundEffectSource(PlaySoundEffectSourceEvent event) {
		
	}
	
	@ForgeSubscribe
	public void onPlaySound(PlaySoundEvent event) {
		
	}
	
	@ForgeSubscribe
	public void onPlaySoundSource(PlaySoundSourceEvent event) {
		
	}
	
	@ForgeSubscribe
	public void onPlayStreaming(PlayStreamingEvent event) {
		
	}
	
	@ForgeSubscribe
	public void onPlayStreaming(PlayStreamingSourceEvent event) {
		
	}
	
	@ForgeSubscribe
	public void onPlaySoundAtEntity(PlaySoundAtEntityEvent event) {
		
	}
}
