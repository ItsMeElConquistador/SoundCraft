package elcon.mods.soundcraft.tileentities;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import elcon.mods.soundcraft.RecordRegistry;
import elcon.mods.soundcraft.gui.ContainerAdvancedJukebox;
import elcon.mods.soundcraft.sounds.SoundDisc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

public class TileEntityAdvancedJukebox extends TileEntitySoundSource implements IInventory {

	public ItemStack[] stacks = new ItemStack[8];

	public ContainerAdvancedJukebox container;

	public int currentSong = 0;
	public boolean playing = false;
	public long startTime = 0;
	public long totalTime = 0;
	public long timePlaying = 0;
	public boolean[] next = new boolean[8];
	public boolean[] loop = new boolean[8];
	public byte[] loopTimes = new byte[8];

	@Override
	public Packet getDescriptionPacket() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(21);
			dos.writeInt(xCoord);
			dos.writeInt(yCoord);
			dos.writeInt(zCoord);
			
			for(int i = 0; i < 8; i++) {
				dos.writeBoolean(next[i]);
				dos.writeBoolean(loop[i]);
				dos.writeByte(loopTimes[i]);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "SCTile";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;
		return pkt;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if(playing) {
			if(timePlaying <= 0) {
				startTime = 0;
				totalTime = 0;
				timePlaying = 0;
				playing = false;
				playNext();
			} else {
				timePlaying = totalTime - (System.currentTimeMillis() - startTime);
			}
		}
	}

	public void play() {
		if(!playing && stacks[currentSong] != null) {
			String recordName = ((ItemRecord) stacks[currentSong].getItem()).recordName;
			sendSound(new SoundDisc(recordName, stacks[currentSong].itemID, 1.0F, 1.0F));
			startTime = System.currentTimeMillis();
			totalTime = (long) (RecordRegistry.recordTime.get(recordName) * 1000);
			timePlaying = totalTime;
			playing = true;
		}
	}

	public void play(int i) {
		if(!playing) {
			currentSong = i;
			play();
		}
	}

	public void forcePlay(int i) {
		stop();
		currentSong = i;
		play();
	}

	public void playNext() {
		if(next[currentSong]) {
			currentSong++;
			play();
		}
	}

	public void stop() {
		playing = false;
		timePlaying = 0;
		startTime = 0;
		totalTime = 0;
		sendSound(new SoundDisc("stop", 0, 1.0F, 1.0F));
	}

	public void onInventoryChange() {
		if(playing && stacks[currentSong] == null) {
			stop();
		}
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList nbtList = nbt.getTagList("Records");
		for(int i = 0; i < 8; i++) {
			next[i] = nbt.getBoolean("next" + Integer.toString(i));
			loop[i] = nbt.getBoolean("loop" + Integer.toString(i));
			loopTimes[i] = nbt.getByte("loopTimes" + Integer.toString(i));
		}
		for(int i = 0; i < nbtList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbtList.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			if(j >= 0 && j < 8) {
				stacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList nbtList = new NBTTagList();
		for(int i = 0; i < 8; i++) {
			nbt.setBoolean("next" + Integer.toString(i), next[i]);
			nbt.setBoolean("loop" + Integer.toString(i), loop[i]);
			nbt.setByte("loopTimes" + Integer.toString(i), loopTimes[i]);

			if(stacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				stacks[i].writeToNBT(nbttagcompound1);
				nbtList.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("Records", nbtList);
	}

	@Override
	public int getSizeInventory() {
		return 8;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack stack;

		if(stacks[i].stackSize <= j) {
			stack = stacks[i];
			stacks[i] = null;
		} else {
			stack = stacks[i].splitStack(j);

			if(stacks[i].stackSize == 0) {
				stacks[i] = null;
			}
		}
		if(container != null) {
			container.onCraftMatrixChanged(this);
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if(stacks[i] != null) {
			return stacks[i];
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if(container != null) {
			container.onCraftMatrixChanged(this);
		}
		stacks[i] = itemstack;
	}

	@Override
	public String getInvName() {
		return "soundcraft.advancedJukebox";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		if(itemstack != null && itemstack.getItem() != null) {
			if(itemstack.getItem() instanceof ItemRecord) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}
}
