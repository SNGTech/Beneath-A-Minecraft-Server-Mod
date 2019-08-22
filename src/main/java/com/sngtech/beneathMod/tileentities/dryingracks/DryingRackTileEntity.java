package com.sngtech.beneathMod.tileentities.dryingracks;

import java.util.Optional;

import com.sngtech.beneathMod.init.RecipeInit;
import com.sngtech.beneathMod.init.TileEntityInit;
import com.sngtech.beneathMod.recipes.DryingRecipe;
import com.sngtech.beneathMod.utils.ModItemStackHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.ItemStackHandler;

public class DryingRackTileEntity extends TileEntity implements ITickableTileEntity
{
	ItemStackHandler itemstack = new ItemStackHandler()
	{
		@Override
		protected void onContentsChanged(int slot) 
		{
			super.onContentsChanged(slot);
			markDirty();
		}
	};
	
	private int dryingTime;
	private int dryingTimeTotal;
	public boolean canRetrieve = false;
	
	public DryingRackTileEntity() 
	{
		super(TileEntityInit.DECAYED_PLANKS_DRYING_RACK);
	}

	@Override
	public void tick() 
	{
		if(this.itemstack.getStackInSlot(0).getCount() > 0 && this.dryingTime <= this.dryingTimeTotal)
		{
			++this.dryingTime;
			
			if(this.dryingTime >= this.dryingTimeTotal)
			{
				this.resetDryingTime();
				Optional<DryingRecipe> optional = this.findMatchingRecipe(itemstack.getStackInSlot(0));
				
				if(optional.isPresent())
				{
					this.itemstack.setStackInSlot(0, optional.get().getCraftingResult(new Inventory(itemstack.getStackInSlot(0))));
					this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
				}
			}
		}
		
	}
	
	@Override
	public void read(CompoundNBT compound) 
	{
		super.read(compound);
		this.itemstack.deserializeNBT(compound.getCompound("inventory"));
		ModItemStackHelper.loadAllItems(compound, itemstack);
		this.dryingTime = compound.getInt("DryingTime");
		this.dryingTimeTotal = compound.getInt("DryingTimeTotal");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) 
	{
		super.write(compound);
		compound.putString("inventory", itemstack.serializeNBT().toString());
		ModItemStackHelper.saveAllItems(compound, itemstack);
		compound.putInt("DryingTime", this.dryingTime);
		compound.putInt("DryingTimeTotal", this.dryingTimeTotal);
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag() 
	{
		return this.write(new CompoundNBT());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) 
	{
		this.read(pkt.getNbtCompound());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() 
	{
		return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() 
	{
		return super.getRenderBoundingBox();
	}
	
	public ItemStackHandler getInventory()
	{
		return this.itemstack;
	}
	
	public Optional<DryingRecipe> findMatchingRecipe(ItemStack itemStack) 
	{
		return this.itemstack.getStackInSlot(0) == null ? Optional.empty() : this.world.getRecipeManager().getRecipe(RecipeInit.DRYING, new Inventory(itemStack), this.world);
	}
	
	public void addItem(ItemStack stack, PlayerEntity player, Hand hand, int dryingTime)
	{
		if(this.itemstack.getStackInSlot(0).getCount() <= 0)
		{
			this.itemstack.setStackInSlot(0, stack.split(1));
			this.dryingTimeTotal = dryingTime;
			this.dryingTime = 0;
			this.canRetrieve = true;
			this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}
	
	public void retrieveItem(PlayerEntity player, Hand hand)
	{
		if(this.itemstack.getStackInSlot(0).getCount() > 0 && !player.getHeldItem(hand).isEmpty())
		{
			player.getHeldItem(hand).grow(1);
			this.itemstack.extractItem(0, 1, false);
			this.resetDryingTime();
			this.canRetrieve = false;
			this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
		}
		else if(this.itemstack.getStackInSlot(0).getCount() > 0 && player.getHeldItem(hand).isEmpty())
		{
			player.setHeldItem(hand, this.itemstack.getStackInSlot(0));
			this.itemstack.extractItem(0, 1, false);
			this.resetDryingTime();
			this.canRetrieve = false;
			this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
		}
		else if(this.itemstack.getStackInSlot(0).getCount() > 0 && !player.getHeldItem(hand).isEmpty())
		{
			player.getHeldItem(hand).grow(1);
			this.itemstack.extractItem(0, 1, false);
			this.resetDryingTime();
			this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
		}
			
	}
	
	private void resetDryingTime()
	{
		this.dryingTimeTotal = 0;
		this.dryingTime = 0;
	}
}
