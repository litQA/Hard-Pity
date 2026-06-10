package cn.hard_pity.server;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import cn.hard_pity.configuration.HardConfigConfiguration;

public class NormalRewardPulls {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity sourceentity, double pulls, String entityType) {
        if (sourceentity == null || entityType == null)
            return;

        double guarantee = 0;
        double currentPullNormal = 0;
        double rateBeforeGuarantee = 0;

        guarantee = Math.max(2, (double) HardConfigConfiguration.NORMAL_REWARD_GUARANTEE_PULL_COUNT.get());
        currentPullNormal = sourceentity.getPersistentData().getDouble((entityType + "NormalCount"));
        rateBeforeGuarantee = 0.5 / guarantee;

        for (int index0 = 0; index0 < (int) Math.round(pulls); index0++) {
            if (currentPullNormal + 1 >= guarantee) {
                currentPullNormal = 0;
                
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, RewardConfig.execute(false, true, entityType));
                    entityToSpawn.setPickUpDelay(10);
                    _level.addFreshEntity(entityToSpawn);
                }
            } else {
                if (Random01.execute() < rateBeforeGuarantee) {
                    currentPullNormal = 0;
                    
                    if (world instanceof ServerLevel _level) {
                        ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, RewardConfig.execute(false, true, entityType));
                        entityToSpawn.setPickUpDelay(10);
                        _level.addFreshEntity(entityToSpawn);
                    }
                } else {
                    currentPullNormal = currentPullNormal + 1;
                }
            }
        }

        sourceentity.getPersistentData().putDouble((entityType + "NormalCount"), currentPullNormal);
    }
}