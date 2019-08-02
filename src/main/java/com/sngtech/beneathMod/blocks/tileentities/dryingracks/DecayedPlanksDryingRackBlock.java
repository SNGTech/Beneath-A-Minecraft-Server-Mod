package com.sngtech.beneathMod.blocks.tileentities.dryingracks;

import com.sngtech.beneathMod.tileentities.crates.SpruceCrateTileEntity;
import com.sngtech.beneathMod.tileentities.dryingracks.DecayedPlanksDryingRackTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class DecayedPlanksDryingRackBlock extends AbstractDryingRackBlock 
{
	public DecayedPlanksDryingRackBlock(Properties builder) 
	{
		super(builder);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) 
	{
		return new DecayedPlanksDryingRackTileEntity();
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(world.isRemote)
		{
			return true;
		}
		else
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof DecayedPlanksDryingRackTileEntity)
			{
				DecayedPlanksDryingRackTileEntity dryingrack = (DecayedPlanksDryingRackTileEntity) te;
				ItemStack stack = player.getHeldItem(handIn);
				dryingrack.addItem(stack);
			}
		}
		
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) 
	{
		if (stack.hasDisplayName()) 
		{
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity instanceof DecayedPlanksDryingRackTileEntity) 
			{
				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) 
	{
		TileEntity te = world.getTileEntity(pos);
		
		if(te instanceof SpruceCrateTileEntity)
		{
			ItemStack stack;
			for(int i = 0; i < ((SpruceCrateTileEntity) te).getInventory().getSlots(); i++)
			{
				stack = ((SpruceCrateTileEntity) te).getInventory().getStackInSlot(i);
				if(stack != null)
				{
					double d0 = (double)EntityType.ITEM.getWidth();
			        double d1 = 1.0D - d0;
			        double d2 = d0 / 2.0D;
			        double d3 = Math.floor((double)pos.getX()) + RANDOM.nextDouble() * d1 + d2;
			        double d4 = Math.floor((double)pos.getY()) + RANDOM.nextDouble() * d1;
			        double d5 = Math.floor((double)pos.getZ()) + RANDOM.nextDouble() * d1 + d2;

			        while(!stack.isEmpty()) 
			        {
			           ItemEntity itementity = new ItemEntity(world, d3, d4, d5, stack.split(RANDOM.nextInt(21) + 10));
			           itementity.setMotion(RANDOM.nextGaussian() * (double)0.05F, RANDOM.nextGaussian() * (double)0.05F + (double)0.2F, RANDOM.nextGaussian() * (double)0.05F);
			           world.addEntity(itementity);
			        }
				}
			}
			
			world.updateComparatorOutputLevel(pos, this);
		}
			
		super.onReplaced(state, world, pos, newState, isMoving);
	}
}