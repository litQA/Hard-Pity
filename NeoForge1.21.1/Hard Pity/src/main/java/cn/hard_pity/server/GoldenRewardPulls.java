package cn.hard_pity.server;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

import cn.hard_pity.configuration.HardConfigConfiguration;

public class GoldenRewardPulls {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity sourceentity, double pulls, String entityType) {
        if (sourceentity == null || entityType == null)
            return;

        String modelType = "";
        boolean ifGetAdvancedReward = false;
        boolean ifFailedThisTime = false;
        double guaruntee = 0;
        double currentPullCount = 0;
        double brightCaptureCount = 0;
        double successRate = 0;

        modelType = HardConfigConfiguration.PROBABILITY_GROWTH_MODE.get();
        guaruntee = Math.max(10, (double) HardConfigConfiguration.HIGH_REWARD_GUARANTEE_PULL_COUNT.get());
        currentPullCount = sourceentity.getPersistentData().getDouble((entityType + "Count"));

        if (currentPullCount + pulls < guaruntee) {
            for (int index0 = 0; index0 < (int) Math.round(pulls); index0++) {
                if (("exp").equals(modelType)) {
                    if (Random01.execute() < Math.pow(0.3679, (guaruntee - currentPullCount) - 1)) {
                        ifGetAdvancedReward = true;
                        break;
                    } else {
                        currentPullCount = currentPullCount + 1;
                    }
                } else if (("linear").equals(modelType)) {
                    if (Random01.execute() < (currentPullCount + 1) / guaruntee) {
                        ifGetAdvancedReward = true;
                        break;
                    } else {
                        currentPullCount = currentPullCount + 1;
                    }
                } else {
                    if (currentPullCount + 1 == guaruntee - 1) {
                        if (Random01.execute() < 0.8) {
                            ifGetAdvancedReward = true;
                            break;
                        } else {
                            currentPullCount = currentPullCount + 1;
                        }
                    } else if (currentPullCount + 1 == guaruntee - 2) {
                        if (Random01.execute() < 0.51) {
                            ifGetAdvancedReward = true;
                            break;
                        } else {
                            currentPullCount = currentPullCount + 1;
                        }
                    } else if (currentPullCount + 1 == guaruntee - 3) {
                        if (Random01.execute() < 0.33) {
                            ifGetAdvancedReward = true;
                            break;
                        } else {
                            currentPullCount = currentPullCount + 1;
                        }
                    } else if (currentPullCount + 1 == guaruntee - 4) {
                        if (Random01.execute() < 0.16) {
                            ifGetAdvancedReward = true;
                            break;
                        } else {
                            currentPullCount = currentPullCount + 1;
                        }
                    } else {
                        if (Random01.execute() < 0.21 / (guaruntee - 5)) {
                            ifGetAdvancedReward = true;
                            break;
                        } else {
                            currentPullCount = currentPullCount + 1;
                        }
                    }
                }
            }
        } else {
            ifGetAdvancedReward = true;
        }

        if (!ifGetAdvancedReward) {
            sourceentity.getPersistentData().putDouble((entityType + "Count"), Math.round(currentPullCount));
        } else {
            sourceentity.getPersistentData().putDouble((entityType + "Count"), 0);

            if (!sourceentity.getPersistentData().getBoolean((entityType + "IfGuarenteed"))) {
                successRate = (double) HardConfigConfiguration.MINI_GUARANTEE_BASE_SUCCESS_RATE.get();
                brightCaptureCount = sourceentity.getPersistentData().getDouble("brightLightCaptureCount");

                if (brightCaptureCount <= -1) {
                    if (Random01.execute() < successRate) {
                        sourceentity.getPersistentData().putDouble("brightLightCaptureCount", (-1));
                    } else {
                        ifFailedThisTime = true;
                        sourceentity.getPersistentData().putDouble("brightLightCaptureCount", 0);
                    }
                } else if (brightCaptureCount == 1) {
                    if (Random01.execute() < 0.006) {
                        sourceentity.getPersistentData().putDouble("brightLightCaptureCount", 0);
                    } else {
                        if (brightCaptureCount < successRate) {
                            sourceentity.getPersistentData().putDouble("brightLightCaptureCount", 0);
                        } else {
                            ifFailedThisTime = true;
                            sourceentity.getPersistentData().putDouble("brightLightCaptureCount", 2);
                        }
                    }
                } else if (brightCaptureCount >= 2) {
                    sourceentity.getPersistentData().putDouble("brightLightCaptureCount", 0);
                    
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 5, 0, 0.5, 0, 0.1);
                } else {
                    if (Random01.execute() < successRate) {
                        sourceentity.getPersistentData().putDouble("brightLightCaptureCount", (-1));
                    } else {
                        ifFailedThisTime = true;
                        sourceentity.getPersistentData().putDouble("brightLightCaptureCount", 1);
                    }
                }
            } else {
                sourceentity.getPersistentData().putBoolean((entityType + "IfGuarenteed"), false);
            }

            if (ifFailedThisTime) {
                sourceentity.getPersistentData().putBoolean((entityType + "IfGuarenteed"), true);
                
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, RewardConfig.execute(true, false, entityType));
                    entityToSpawn.setPickUpDelay(10);
                    _level.addFreshEntity(entityToSpawn);
                }

                if (world instanceof ServerLevel _level)
                    _level.sendParticles(ParticleTypes.ANGRY_VILLAGER, x, y, z, 5, 0, 0.5, 0, 0.1);
            } else {
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, RewardConfig.execute(true, true, entityType));
                    entityToSpawn.setPickUpDelay(10);
                    _level.addFreshEntity(entityToSpawn);
                }

                if (world instanceof ServerLevel _level)
                    _level.sendParticles(ParticleTypes.HEART, x, y, z, 5, 0, 0.5, 0, 0.1);
            }
        }
    }
}