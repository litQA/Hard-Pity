package cn.hard_pity.server;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import cn.hard_pity.configuration.HardConfigConfiguration;

@EventBusSubscriber
public class PlayerDeth {
	@SubscribeEvent
	public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (HardConfigConfiguration.CLEAR_PROGRESS_ON_DEATH.get()) {
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