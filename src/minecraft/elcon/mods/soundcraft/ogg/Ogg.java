package elcon.mods.soundcraft.ogg;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import elcon.mods.soundcraft.SoundCraftConfig;

public class Ogg {

	public int audio_channels;
	public int audio_sample_rate;
	public long dataLength;
	public long headerStart;
	public long sampleNum;
	public int vorbis_version;
	public FileInputStream inStream;

	public Ogg(File file) {
		this(file.getPath());
	}

	public Ogg(String file) {
		try {
			dataLength = new File(file).length();
			inStream = new FileInputStream(file);
	
			int pos = 0;
			while(true) {
				int packet_type = 0;
				char[] capture_pattern1 = {'v', 'o', 'r', 'b', 'i', 's'};
				for(int i = 0; i < capture_pattern1.length; i++) {
					int b = inStream.read();
					if(b == -1)
						throw new Exception("No Vorbis Identification Header");
					pos++;
					if(b != capture_pattern1[i]) {
						packet_type = b;
						i = -1;
					}
				}
	
				if(packet_type == 1)
					break;
			}
	
			vorbis_version = read32Bits(inStream);
			if(vorbis_version > 0)
				throw new Exception("Unknown Vorbis Version: " + vorbis_version);
			audio_channels = inStream.read();
			audio_sample_rate = read32Bits(inStream);
			pos += 4 + 1 + 4;
	
	
			headerStart = dataLength - 16 * 1024;
			inStream.skip(headerStart - pos);
			int count = 0;
			while(true) {
				char[] capture_pattern = {'O', 'g', 'g', 'S', 0};
				int i;
				for(i = 0; i < capture_pattern.length; i++) {
					int b = inStream.read();
					if(b == -1)
						break;
					if(b != capture_pattern[i]) {
						headerStart += i + 1;
						i = -1;
					}
				}
				if(i < capture_pattern.length)
					break;
	
				count++;
				//System.out.println(count + " OggS at " + headerStart);
	
				int header_type_flag = inStream.read();
				if(header_type_flag == -1)
					break;
	
				long absolute_granule_position = 0;
				for(i = 0; i < 8; i++) {
					long b = inStream.read();
					if(b == -1)
						break;
	
					absolute_granule_position |= b << (8 * i);
				}
				if(i < 8)
					break;
	
				if((header_type_flag & 0x06) != 0) {
					//System.out.print("OggS " + count + " at " + headerStart);
					//if((header_type_flag & 0x01) != 0)
						//System.out.print(": continued");
					//if((header_type_flag & 0x02) != 0)
						//System.out.print(": first");
					//if((header_type_flag & 0x04) != 0)
						//System.out.print(": last");
					//System.out.println(" " + absolute_granule_position);
					sampleNum = absolute_granule_position;
				}
			}
		} catch(Exception e) {
			if(SoundCraftConfig.debugMode) {
				FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "[SoundCraft] [Ogg] Reading error: " + e.getMessage());
			}
		}
	}

	public double getSeconds() {
		if(audio_sample_rate > 0) {
			return ((double) sampleNum) / ((double) audio_sample_rate);
		}
		return 0;
	}

	public int read32Bits(InputStream inStream) throws Exception {
		int n = 0;
		for(int i = 0; i < 4; i++) {
			int b = inStream.read();
			if(b == -1)
				throw new Exception("Unexpected end of input stream");
			n |= b << (8 * i);
		}
		return n;
	}

	public void showInfo() {
		System.out.println("audio_channels = " + audio_channels);
		System.out.println("audio_sample_rate = " + audio_sample_rate);
		System.out.println("dataLength = " + dataLength);
		System.out.println("seconds = " + getSeconds());
		System.out.println("headerStart = " + headerStart);
		System.out.println("vorbis_version = " + vorbis_version);
	}
}
