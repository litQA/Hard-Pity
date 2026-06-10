package cn.hard_pity.server;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import cn.hard_pity.configuration.HardConfigConfiguration;

/**
 * 玩家死亡事件处理类
 * 处理玩家重生时的进度清空逻辑
 */
@Mod.EventBusSubscriber
public class PlayerDeth {
    /**
     * 监听玩家重生事件
     * @param event 玩家重生事件对象
     */
    @SubscribeEvent
    public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        execute(event, event.getEntity());
    }

    /**
     * 重载执行方法（简化参数）
     * @param entity 玩家实体
     */
    public static void execute(Entity entity) {
        execute(null, entity);
    }

    /**
     * 核心执行逻辑：清空玩家各类进度数据
     * @param event 事件对象（可为空）
     * @param entity 玩家实体
     */
    private static void execute(@Nullable Event event, Entity entity) {
        // 空值校验
        if (entity == null) {
            return;
        }
        
        // 检查配置：死亡时是否清空进度
        if (HardConfigConfiguration.CLEAR_PROGRESS_ON_DEATH.get()) {
            // 清空各类实体对应的累计进度/保底标记
            entity.getPersistentData().putDouble("brightLightCaptureCount", 0);
            entity.getPersistentData().putDouble("WitherSkeletonCount", 0);
            entity.getPersistentData().putDouble("WitherSkeletonIfGuaranteed", 0);
            entity.getPersistentData().putDouble("DrownedCount", 0);
            entity.getPersistentData().putDouble("DrownedIfGuaranteed", 0);
            entity.getPersistentData().putDouble("PiglinCount", 0);
            entity.getPersistentData().putDouble("PiglinIfGuaranteed", 0);
            entity.getPersistentData().putDouble("DragonCount", 0);
            entity.getPersistentData().putDouble("DragonIfGuaranteed", 0);
        }
    }
}