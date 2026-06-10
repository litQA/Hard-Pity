package cn.hard_pity.server;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.Registries;

import javax.annotation.Nullable;

import cn.hard_pity.configuration.HardConfigConfiguration;

@EventBusSubscriber
public class EntityDie {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntity() != null) {
            execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getEntity());
        }
    }

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
        execute(null, world, x, y, z, entity, sourceentity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;

        double pullBasedOnLooting = 0;
        String type = "";
        Vec3 dLocation = Vec3.ZERO;
        type = "null";

        if (!HardConfigConfiguration.ONLY_PLAYER_TRIGGER.get() && !(sourceentity == null) || HardConfigConfiguration.ONLY_PLAYER_TRIGGER.get() && sourceentity instanceof Player) {
            if (entity instanceof WitherSkeleton) {
                type = "WitherSkeleton";
            } else if (entity instanceof Drowned) {
                type = "Drowned";
            } else if (entity instanceof Piglin || entity instanceof PiglinBrute) {
                type = "Piglin";
            } else if (entity instanceof EnderDragon) {
                type = "Dragon";
            }
        }

        if (!("null").equals(type)) {
            dLocation = (entity.position()).subtract((sourceentity.position()));

            if (dLocation.x() * dLocation.x() + dLocation.y() * dLocation.y() + dLocation.z() * dLocation.z() <= Math.pow((double) HardConfigConfiguration.ENTITY_TRIGGER_DETECTION_RANGE.get(), 2)) {
                if (HardConfigConfiguration.MAIN_HAND_LOOTING_EXTRA_PULLS.get()) {
                    pullBasedOnLooting = Math.min(
                        (double) HardConfigConfiguration.LOOTING_MAX_EXTRA_PULL.get() + 1, 
                        1 + Math.round(Random01.execute() * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(
                            world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING)
                        ))
                    );
                } else {
                    pullBasedOnLooting = 1;
                }

                GoldenRewardPulls.execute(world, x, y, z, sourceentity, pullBasedOnLooting, type);

                if (HardConfigConfiguration.ENABLE_NORMAL_REWARD.get()) {
                    NormalRewardPulls.execute(world, x, y, z, sourceentity, pullBasedOnLooting, type);
                }
            }
        }
    }
}