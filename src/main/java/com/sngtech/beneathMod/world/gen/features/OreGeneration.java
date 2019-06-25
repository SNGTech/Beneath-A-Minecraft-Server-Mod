package com.sngtech.beneathMod.world.gen.features;

import com.sngtech.beneathMod.init.BlockInit;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class OreGeneration
{
	private static CountRangeConfig copper_ore_gen;
	private static CountRangeConfig bauxite_ore_gen;
	
	public static void registerOreGeneration()
	{
		for(Biome biome : ForgeRegistries.BIOMES)
		{
			addOreGen(copper_ore_gen, 10, 10, 10, 75, biome, BlockInit.COPPER_ORE.getDefaultState(), 6);
			addOreGen(bauxite_ore_gen, 5, 10, 10, 40, biome, BlockInit.BAUXITE_ORE.getDefaultState(), 4);
		}
	}
	
	private static void addOreGen(CountRangeConfig placement, int chance, int minHeight, int maxHeightBase, int MaxHeight, Biome biome, BlockState block, int veinSize) 
	{
		placement = new CountRangeConfig(chance, minHeight, maxHeightBase, MaxHeight);
		biome.addFeature(Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, block, 8), Placement.COUNT_RANGE, placement));
	}
}
