package cn.hard_pity.server;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

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

import javax.annotation.Nullable;

import cn.hard_pity.configuration.HardConfigConfiguration;

/**
 * 实体死亡事件处理类
 * 监听实体死亡事件，触发奖励抽取逻辑（金色+普通）
 */
@Mod.EventBusSubscriber
public class EntityDie {
    /**
     * 监听实体死亡事件
     * @param event 实体死亡事件对象
     */
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(
                event, 
                event.getEntity().level(), 
                event.getEntity().getX(), 
                event.getEntity().getY(), 
                event.getEntity().getZ(), 
                event.getEntity(), 
                event.getSource().getEntity()
            );
        }
    }

    /**
     * 重载执行方法（简化参数）
     * @param world 世界对象
     * @param x 实体死亡X坐标
     * @param y 实体死亡Y坐标
     * @param z 实体死亡Z坐标
     * @param entity 死亡的实体
     * @param sourceentity 造成死亡的实体（攻击者）
     */
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
        execute(null, world, x, y, z, entity, sourceentity);
    }

    /**
     * 核心执行逻辑：判定触发条件，计算抽取次数，执行奖励抽取
     * @param event 事件对象（可为空）
     * @param world 世界对象
     * @param x 实体死亡X坐标
     * @param y 实体死亡Y坐标
     * @param z 实体死亡Z坐标
     * @param entity 死亡的实体
     * @param sourceentity 造成死亡的实体（攻击者）
     */
    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
        // 空值校验
        if (entity == null || sourceentity == null) {
            return;
        }
        
        // 基于抢夺附魔的额外抽取次数
        double pullBasedOnLooting = 0;
        // 死亡实体的类型标识
        String type = "";
        // 攻击者与死亡实体的位置向量
        Vec3 dLocation = Vec3.ZERO;
        
        // 初始化类型为null（未匹配）
        type = "null";
        
        // 触发条件校验：配置是否仅玩家触发 | 攻击者是否为玩家/非空
        if (!HardConfigConfiguration.ONLY_PLAYER_TRIGGER.get() && !(sourceentity == null) || HardConfigConfiguration.ONLY_PLAYER_TRIGGER.get() && sourceentity instanceof Player) {
            // 判定死亡实体类型
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
        
        // 匹配到有效实体类型
        if (!("null").equals(type)) {
            // 计算攻击者与死亡实体的位置差向量
            dLocation = (entity.position()).subtract((sourceentity.position()));
            // 距离校验：欧几里得距离平方 <= 配置的检测范围平方（避免开根号，提升性能）
            if (dLocation.x() * dLocation.x() + dLocation.y() * dLocation.y() + dLocation.z() * dLocation.z() <= Math.pow((double) HardConfigConfiguration.ENTITY_TRIGGER_DETECTION_RANGE.get(), 2)) {
                // 计算基于抢夺附魔的抽取次数
                if (HardConfigConfiguration.MAIN_HAND_LOOTING_EXTRA_PULLS.get()) {
                    // 抢夺附魔额外次数：1 + 随机数取整后的附魔等级，且不超过配置的最大额外次数+1
                    pullBasedOnLooting = Math.min(
                        (double) HardConfigConfiguration.LOOTING_MAX_EXTRA_PULL.get() + 1,
                        1 + Math.round(Random01.execute() * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(Enchantments.MOB_LOOTING))
                    );
                } else {
                    // 不启用抢夺额外次数：默认1次
                    pullBasedOnLooting = 1;
                }
                
                // 执行金色高级奖励抽取
                GoldenRewardPulls.execute(world, x, y, z, sourceentity, pullBasedOnLooting, type);
                
                // 配置启用普通奖励：执行普通奖励抽取
                if (HardConfigConfiguration.ENABLE_NORMAL_REWARD.get()) {
                    NormalRewardPulls.execute(world, x, y, z, sourceentity, pullBasedOnLooting, type);
                }
            }
        }
    }
}