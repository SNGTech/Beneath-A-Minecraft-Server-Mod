package com.sngtech.beneathMod.init;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.sngtech.beneathMod.Main;
import com.sngtech.beneathMod.tileentities.AcaciaCrateTileEntity;
import com.sngtech.beneathMod.tileentities.BirchCrateTileEntity;
import com.sngtech.beneathMod.tileentities.DarkOakCrateTileEntity;
import com.sngtech.beneathMod.tileentities.JungleCrateTileEntity;
import com.sngtech.beneathMod.tileentities.OakCrateTileEntity;
import com.sngtech.beneathMod.tileentities.SpruceCrateTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Main.MODID)
public class TileEntityInit 
{
	//Storage
	public static final TileEntityType<?> OAK_CRATE = null;
	public static final TileEntityType<?> SPRUCE_CRATE = null;
	public static final TileEntityType<?> BIRCH_CRATE = null;
	public static final TileEntityType<?> JUNGLE_CRATE = null;
	public static final TileEntityType<?> ACACIA_CRATE = null;
	public static final TileEntityType<?> DARK_OAK_CRATE = null;
	
	//Machines (Heat Operated)
	//public static final TileEntityType<?> BLOCK_BREAKER = null;
	
	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents
	{
		@SubscribeEvent
		public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> e)
		{
			e.getRegistry().registerAll
			(
				//Storage
				setup(TileEntityType.Builder.create(OakCrateTileEntity::new, BlockInit.OAK_CRATE).build(null), "oak_crate"),
				setup(TileEntityType.Builder.create(SpruceCrateTileEntity::new, BlockInit.SPRUCE_CRATE).build(null), "spruce_crate"),
				setup(TileEntityType.Builder.create(BirchCrateTileEntity::new, BlockInit.BIRCH_CRATE).build(null), "birch_crate"),
				setup(TileEntityType.Builder.create(JungleCrateTileEntity::new, BlockInit.JUNGLE_CRATE).build(null), "jungle_crate"),
				setup(TileEntityType.Builder.create(AcaciaCrateTileEntity::new, BlockInit.ACACIA_CRATE).build(null), "acacia_crate"),
				setup(TileEntityType.Builder.create(DarkOakCrateTileEntity::new, BlockInit.DARK_OAK_CRATE).build(null), "dark_oak_crate")
				
				//Machines (Heat Operated)
				//TileEntityType.Builder.create(TileEntityBlockBreaker::new, BlockInit.BLOCK_BREAKER).build(null).setRegistryName(Main.MODID, "block_breaker")
			);
			
			Main.logger.debug("Registered Tile Entities");
		}
	}
	
	@Nonnull
	private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final String name) 
	{
		Preconditions.checkNotNull(name, "Name to assign to entry cannot be null!");
		return setup(entry, new ResourceLocation(Main.MODID, name));
	}

	@Nonnull
	private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final ResourceLocation registryName) 
	{
		Preconditions.checkNotNull(entry, "Entry cannot be null!");
		Preconditions.checkNotNull(registryName, "Registry name to assign to entry cannot be null!");
		entry.setRegistryName(registryName);
		return entry;
	}
}
