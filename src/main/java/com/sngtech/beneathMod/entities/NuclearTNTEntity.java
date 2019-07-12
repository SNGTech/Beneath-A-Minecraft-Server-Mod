package com.sngtech.beneathMod.entities;

import javax.annotation.Nullable;

import com.sngtech.beneathMod.init.EntityInit;
import com.sngtech.beneathMod.packets.ModSSpawnObjectPacket;
import com.sngtech.beneathMod.world.explosions.NuclearExplosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class NuclearTNTEntity extends Entity 
{
   private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(NuclearTNTEntity.class, DataSerializers.VARINT);
   @Nullable
   private LivingEntity placedBy;
   private int fuse = 200;
   private int displacement = 15;

   public NuclearTNTEntity(EntityType<? extends NuclearTNTEntity> p_i50216_1_, World p_i50216_2_) 
   {
      super(p_i50216_1_, p_i50216_2_);
      this.preventEntitySpawning = true;
   }

   public NuclearTNTEntity(World worldIn, double x, double y, double z, @Nullable LivingEntity igniter) 
   {
      this(EntityInit.NUCLEAR_TNT, worldIn);
      this.setPosition(x, y, z);
      double d0 = worldIn.rand.nextDouble() * (double)((float)Math.PI * 2F);
      this.setMotion(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
      this.setFuse(80);
      this.prevPosX = x;
      this.prevPosY = y;
      this.prevPosZ = z;
      this.placedBy = igniter;
   }

   @Override
   protected void registerData() 
   {
      this.dataManager.register(FUSE, 80);
   }

   /**
    * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
    * prevent them from trampling crops
    */
   @Override
   protected boolean canTriggerWalking() 
   {
      return false;
   }

   /**
    * Returns true if other Entities should be prevented from moving through this Entity.
    */
   @SuppressWarnings("deprecation")
   @Override
   public boolean canBeCollidedWith() 
   {
      return !this.removed;
   }

   /**
    * Called to update the entity's position/logic.
    */
   @Override
   public void tick() 
   {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (!this.hasNoGravity()) {
         this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
      }

      this.move(MoverType.SELF, this.getMotion());
      this.setMotion(this.getMotion().scale(0.98D));
      if (this.onGround) {
         this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
      }

      --this.fuse;
      if (this.fuse <= 0) 
      {
         this.remove();
         if (!this.world.isRemote) 
         {
            this.explode();
         }
      } 
      else 
      {
         this.handleWaterMovement();
         this.world.addParticle(ParticleTypes.SMOKE, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
      }

   }

   private void explode() 
   {
      this.createExplosion(this.world, this, this.posX, this.posY + (double)(this.getHeight() / 16.0F), this.posZ, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX - displacement, this.posY + (double)(this.getHeight() / 16.0F), this.posZ, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX - displacement, this.posY + (double)(this.getHeight() / 16.0F), this.posZ - displacement, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX + displacement, this.posY + (double)(this.getHeight() / 16.0F), this.posZ, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX + displacement, this.posY + (double)(this.getHeight() / 16.0F), this.posZ + displacement, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX, this.posY + (double)(this.getHeight() / 16.0F), this.posZ + displacement, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX, this.posY + (double)(this.getHeight() / 16.0F), this.posZ - displacement, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX, this.posY + (double)(this.getHeight() / 16.0F), this.posZ - displacement, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX - displacement, this.posY + (double)(this.getHeight() / 16.0F), this.posZ + displacement, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX + displacement, this.posY + (double)(this.getHeight() / 16.0F), this.posZ - displacement, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX - displacement, this.posY + (double)(this.getHeight() / 16.0F) - displacement * 2, this.posZ + displacement, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX + displacement, this.posY + (double)(this.getHeight() / 16.0F) - displacement * 2, this.posZ - displacement, 50.0F, NuclearExplosion.Mode.BREAK);
      this.createExplosion(this.world, this, this.posX, this.posY + (double)(this.getHeight() / 16.0F) - displacement * 2, this.posZ, 50.0F, NuclearExplosion.Mode.BREAK);
   }
   
   private NuclearExplosion createExplosion(World p_i50007_1_, @Nullable Entity p_i50007_2_, double p_i50007_3_, double p_i50007_5_, double p_i50007_7_, float p_i50007_9_, NuclearExplosion.Mode p_i50007_11_) 
   {
	   NuclearExplosion nuclearExplosion = new NuclearExplosion(p_i50007_1_, p_i50007_2_, p_i50007_3_, p_i50007_5_, p_i50007_7_, p_i50007_9_, p_i50007_11_);
	   if (ForgeEventFactory.onExplosionStart(this.world, nuclearExplosion)) 
		   return nuclearExplosion;

	   nuclearExplosion.doExplosionA();
	   nuclearExplosion.doExplosionB(true);
	   return nuclearExplosion;
   }

   @Override
   protected void writeAdditional(CompoundNBT compound) 
   {
      compound.putShort("Fuse", (short)this.getFuse());
   }

   /**
    * (abstract) Protected helper method to read subclass entity data from NBT.
    */
   @Override
   protected void readAdditional(CompoundNBT compound) 
   {
      this.setFuse(compound.getShort("Fuse"));
   }

   /**
    * returns null or the entityliving it was placed or ignited by
    */
   @Nullable
   public LivingEntity getTntPlacedBy() 
   {
      return this.placedBy;
   }

   @Override
   protected float getEyeHeight(Pose p_213316_1_, EntitySize p_213316_2_)
   {
      return 0.0F;
   }

   public void setFuse(int fuseIn) 
   {
      this.dataManager.set(FUSE, fuseIn);
      this.fuse = fuseIn;
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> key) 
   {
      if (FUSE.equals(key)) 
      {
         this.fuse = this.getFuseDataManager();
      }

   }

   /**
    * Gets the fuse from the data manager
    */
   public int getFuseDataManager() 
   {
      return this.dataManager.get(FUSE);
   }

   public int getFuse() 
   {
      return this.fuse;
   }

   @Override
   public IPacket<?> createSpawnPacket() 
   {
      return new ModSSpawnObjectPacket(this);
   }
}