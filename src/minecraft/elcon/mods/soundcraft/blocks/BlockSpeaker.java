package elcon.mods.soundcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.soundcraft.SoundCraftConfig;
import elcon.mods.soundcraft.tileentities.TileEntitySpeaker;

public class BlockSpeaker extends BlockContainer {

	public Icon speakerSide;
	public Icon speakerFront;

	public BlockSpeaker(int i) {
		super(i, Material.iron);
	}
	
	public static int getDirection(int i) {
        return i & 3;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int i, int j) {
		if(j == 0) {
			return i == 3 ? speakerFront : speakerSide;
		} else if(j == 1) {
			return i == 4 ? speakerFront : speakerSide;
		} else if(j == 2) {
			return i == 2 ? speakerFront : speakerSide;
		} else if(j == 3) {
			return i == 5 ? speakerFront : speakerSide;
		}
		return i == 3 ? speakerFront : speakerSide;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack itemStack) {
		int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, l, 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		speakerSide = iconRegister.registerIcon("soundcraft:speakerSide");
		speakerFront = iconRegister.registerIcon("soundcraft:speaker");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySpeaker();
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isRemote) {
			if(world.getBlockId(x - 1, y, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x - 1, y, z);
			}
			if(world.getBlockId(x + 1, y, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x + 1, y, z);
			}
			if(world.getBlockId(x, y - 1, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y - 1, z);
			}
			if(world.getBlockId(x, y + 1, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y + 1, z);
			}
			if(world.getBlockId(x, y, z - 1) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y, z - 1);
			}
			if(world.getBlockId(x, y, z + 1) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y, z + 1);
			}
		}
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
		if(!world.isRemote) {
			if(world.getBlockId(x - 1, y, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x - 1, y, z);
			}
			if(world.getBlockId(x + 1, y, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x + 1, y, z);
			}
			if(world.getBlockId(x, y - 1, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y - 1, z);
			}
			if(world.getBlockId(x, y + 1, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y + 1, z);
			}
			if(world.getBlockId(x, y, z - 1) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y, z - 1);
			}
			if(world.getBlockId(x, y, z + 1) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y, z + 1);
			}
		}
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int par5) {
		if(!world.isRemote) {
			if(world.getBlockId(x - 1, y, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x - 1, y, z);
			}
			if(world.getBlockId(x + 1, y, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x + 1, y, z);
			}
			if(world.getBlockId(x, y - 1, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y - 1, z);
			}
			if(world.getBlockId(x, y + 1, z) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y + 1, z);
			}
			if(world.getBlockId(x, y, z - 1) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y, z - 1);
			}
			if(world.getBlockId(x, y, z + 1) == SoundCraftConfig.soundCableID) {
				((BlockSoundCable) Block.blocksList[SoundCraftConfig.soundCableID]).updateCableConnections(world, x, y, z + 1);
			}
		}
	}
}
