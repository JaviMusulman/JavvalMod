package com.chunkrab.javval.entities;

import com.chunkrab.javval.Javval;
import com.chunkrab.javval.items.MagicStrawberry;
import com.chunkrab.javval.tools.ModItemTier;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class StrawberryEntity extends AnimalEntity {
    private EatGrassGoal eatGrassGoal;
    private int hogTimer;

    public static final Ingredient TEMPTATION_ITEMS = Ingredient.fromItems(Items.CARROT);

    public StrawberryEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }
    //func_233666_p_ --> registerAttributes()
    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_().func_233815_a_(Attributes.field_233818_a_, 10.0D).func_233815_a_(Attributes.field_233821_d_, 0.25D)
                .func_233815_a_(Attributes.field_233819_b_, 10.0F);


    }
    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this ));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this,1.0D));
        this.goalSelector.addGoal(3,new TemptGoal(this, 1.1D,TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this,1.1D));
        this.goalSelector.addGoal(5,new WaterAvoidingRandomWalkingGoal(this,1.0D));
        this.goalSelector.addGoal(6, this.eatGrassGoal);
        this.goalSelector.addGoal(7, new LookAtGoal(this,PlayerEntity.class,6.0F));
        this.goalSelector.addGoal(8,new LookRandomlyGoal(this));
    }
    @Override
    protected int getExperiencePoints(PlayerEntity player){
        return 1;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_DONKEY_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_COW_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        super.playSound(SoundEvents.ENTITY_PIG_STEP,0.15F,1.0F);
    }



    @Override
    protected void updateAITasks() {
        this.hogTimer = eatGrassGoal.getEatingGrassTimer();
        super.updateAITasks();
    }

    @Override
    public void livingTick() {
        if(this.world.isRemote){
            this.hogTimer = Math.max(0, hogTimer-1);
        }
        super.livingTick();
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id){
        if(id == 10){ this.hogTimer = 40;}
        else{super.handleStatusUpdate(id);}
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }
}
