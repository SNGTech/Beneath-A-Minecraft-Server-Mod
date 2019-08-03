package com.sngtech.beneathMod.blocks.tileentities.dryingracks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public abstract class AbstractDryingRackBlock extends Block
{
	protected static final VoxelShape RACK_NORTH_AABB = Block.makeCuboidShape(0.0D, 16.0D, 0.0D, 16.0D, 13.0D, 3.0D);
	protected static final VoxelShape RACK_EAST_AABB = Block.makeCuboidShape(13.0D, 16.0D, 0.0D, 16.0D, 13.0D, 16.0D);
	protected static final VoxelShape RACK_SOUTH_AABB = Block.makeCuboidShape(0.0D, 16.0D, 13.0D, 16.0D, 13.0D, 16.0D);
	protected static final VoxelShape RACK_WEST_AABB = Block.makeCuboidShape(0.0D, 16.0D, 0.0D, 3.0D, 13.0D, 16.0D);
	
	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	
	public AbstractDryingRackBlock(Properties builder) 
	{
		super(builder);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) 
	{
		return true;
	}
	
	public TileEntity createTileEntity(BlockState state, IBlockReader world) 
	{
		return null;
	}
	
	public BlockRenderType getRenderType(BlockState state) 
	{
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean hasCustomBreakingProgress(BlockState state) 
	{
		return true;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) 
	{
		return this.getDefaultState().with(FACING, context.getNearestLookingDirection());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot)
	{
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) 
	{
		switch(state.get(FACING)) 
		{
	      case NORTH:
	      default:
	    	  return RACK_NORTH_AABB;
	      case EAST:
	    	  return RACK_EAST_AABB;
	      case SOUTH:
	    	  return RACK_SOUTH_AABB;
	      case WEST:
	    	  return RACK_WEST_AABB;
	    }
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) 
	{
		builder.add(FACING);
	}
	
	@Override
	public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) 
	{
		return false;
	}

	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) 
	{
		return false;
	}
   
	@Override
	public boolean isSolid(BlockState state) 
	{
		return false;
	}
	
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) 
	{
		return 1;
	}
	
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) 
	{
		return true;
	}
   
	@Override
   	public BlockRenderLayer getRenderLayer() 
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}
