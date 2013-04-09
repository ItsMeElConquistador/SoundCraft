package elcon.mods.soundcraft.mus;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MusOutputStream extends FilterOutputStream {

	private int hash;

	public MusOutputStream(OutputStream out, int hashCode) {
		super(out);
		hash = hashCode;
	}

	private byte encode(byte b) {
		byte b2 = (byte) (b ^ hash >> 8);
		hash = hash * 498729871 + 85731 * b;
		return b2;
	}

	@Override
	public void write(int b) throws IOException {
		out.write(encode((byte) b));
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		for(int i = 0; i < len; ++i) {
			b[off + i] = encode(b[off + i]);
		}
		out.write(b, off, len);
	}
}
