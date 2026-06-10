package cn.hard_pity.server;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

import cn.hard_pity.configuration.HardConfigConfiguration;

/**
 * 金色高级奖励抽取逻辑处理类
 * 处理不同实体对应的高级奖励抽取、保底机制、概率增长模型
 */
public class GoldenRewardPulls {
    /**
     * 执行金色高级奖励抽取
     * @param world 世界对象
     * @param x 奖励生成X坐标
     * @param y 奖励生成Y坐标
     * @param z 奖励生成Z坐标
     * @param sourceentity 触发奖励的实体（通常是玩家）
     * @param pulls 抽取次数
     * @param entityType 触发的实体类型（如WitherSkeleton、Drowned等）
     */
    public static void execute(LevelAccessor world, double x, double y, double z, Entity sourceentity, double pulls, String entityType) {
        // 空值校验
        if (sourceentity == null || entityType == null) {
            return;
        }
        
        // 概率增长模型类型（exp/linear/默认）
        String modelType = "";
        // 是否抽到高级奖励
        boolean ifGetAdvancedReward = false;
        // 本次抽取是否失败（用于保底标记）
        boolean ifFailedThisTime = false;
        // 高级奖励保底次数（最小为10）
        double guaruntee = 0;
        // 当前高级奖励累计抽取次数
        double currentPullCount = 0;
        // 捕获明光计数（防止非酋连歪）
        double brightCaptureCount = 0;
        // 基础成功率
        double successRate = 0;
        
        // 获取配置中的概率增长模型类型
        modelType = HardConfigConfiguration.PROBABILITY_GROWTH_MODE.get();
        // 获取配置中的保底次数，确保最小值为10
        guaruntee = Math.max(10, (double) HardConfigConfiguration.HIGH_REWARD_GUARANTEE_PULL_COUNT.get());
        // 从实体持久化数据中读取当前累计抽取次数
        currentPullCount = sourceentity.getPersistentData().getDouble((entityType + "Count"));
        
        // 未达到保底次数：按概率模型抽取
        if (currentPullCount + pulls < guaruntee) {
            // 循环执行抽取逻辑
            for (int index0 = 0; index0 < (int) Math.round(pulls); index0++) {
                // 指数增长模型（exp）
                if (("exp").equals(modelType)) {
                    // 指数概率公式：Math.pow(0.3679, (保底次数 - 当前次数) - 1)
                    if (Random01.execute() < Math.pow(0.3679, (guaruntee - currentPullCount) - 1)) {
                        ifGetAdvancedReward = true;
                        break;
                    } else {
                        currentPullCount = currentPullCount + 1;
                    }
                } 
                // 线性增长模型（linear）
                else if (("linear").equals(modelType)) {
                    // 线性概率公式：(当前次数 + 1) / 保底次数
                    if (Random01.execute() < (currentPullCount + 1) / guaruntee) {
                        ifGetAdvancedReward = true;
                        break;
                    } else {
                        currentPullCount = currentPullCount + 1;
                    }
                } 
                // 默认概率模型（米哈游式分段固定概率）
                else {
                    if (currentPullCount + 1 == guaruntee - 1) {
                        // 保底前1次：80%概率
                        if (Random01.execute() < 0.8) {
                            ifGetAdvancedReward = true;
                            break;
                        } else {
                            currentPullCount = currentPullCount + 1;
                        }
                    } else if (currentPullCount + 1 == guaruntee - 2) {
                        // 保底前2次：51%概率
                        if (Random01.execute() < 0.51) {
                            ifGetAdvancedReward = true;
                            break;
                        } else {
                            currentPullCount = currentPullCount + 1;
                        }
                    } else if (currentPullCount + 1 == guaruntee - 3) {
                        // 保底前3次：33%概率
                        if (Random01.execute() < 0.33) {
                            ifGetAdvancedReward = true;
                            break;
                        } else {
                            currentPullCount = currentPullCount + 1;
                        }
                    } else if (currentPullCount + 1 == guaruntee - 4) {
                        // 保底前4次：16%概率
                        if (Random01.execute() < 0.16) {
                            ifGetAdvancedReward = true;
                            break;
                        } else {
                            currentPullCount = currentPullCount + 1;
                        }
                    } else {
                        // 其他次数：0.21 / (保底次数 - 5) 概率
                        if (Random01.execute() < 0.21 / (guaruntee - 5)) {
                            ifGetAdvancedReward = true;
                            break;
                        } else {
                            currentPullCount = currentPullCount + 1;
                        }
                    }
                }
            }
        } 
        // 达到保底次数：必出金
        else {
            ifGetAdvancedReward = true;
        }
        
        // 未出金：更新累计次数
        if (!ifGetAdvancedReward) {
            sourceentity.getPersistentData().putDouble((entityType + "Count"), Math.round(currentPullCount));
        } 
        // 抽到高级奖励：重置累计次数，处理奖励发放
        else {
            // 重置累计抽取次数
            sourceentity.getPersistentData().putDouble((entityType + "Count"), 0);
            
            // 未触发保底标记：按基础成功率判定最终奖励
            if (!sourceentity.getPersistentData().getBoolean((entityType + "IfGuarenteed"))) {
                // 获取配置中的基础保底成功率
                successRate = (double) HardConfigConfiguration.MINI_GUARANTEE_BASE_SUCCESS_RATE.get();
                // 读取捕获明光计数
                brightCaptureCount = sourceentity.getPersistentData().getDouble("brightLightCaptureCount");
                
                // 捕获明光计数 <= -1：特殊状态判定
                if (brightCaptureCount <= -1) {
                    if (Random01.execute() < successRate) {
                        sourceentity.getPersistentData().putDouble("brightLightCaptureCount", (-1));
                    } else {
                        ifFailedThisTime = true;
                        sourceentity.getPersistentData().putDouble("brightLightCaptureCount", 0);
                    }
                } 
                // 捕获明光计数 = 1：低概率特殊判定
                else if (brightCaptureCount == 1) {
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
                } 
                // 捕获明光计数 >= 2：重置计数并生成特效
                else if (brightCaptureCount >= 2) {
                    sourceentity.getPersistentData().putDouble("brightLightCaptureCount", 0);
                    if (world instanceof ServerLevel _level) {
                        _level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 5, 0, 0.5, 0, 0.1);
                    }
                } 
                // 捕获明光计数 = 0：默认状态判定
                else {
                    if (Random01.execute() < successRate) {
                        sourceentity.getPersistentData().putDouble("brightLightCaptureCount", (-1));
                    } else {
                        ifFailedThisTime = true;
                        sourceentity.getPersistentData().putDouble("brightLightCaptureCount", 1);
                    }
                }
            } 
            // 已触发保底标记：重置保底标记
            else {
                sourceentity.getPersistentData().putBoolean((entityType + "IfGuarenteed"), false);
            }
            
            // 本次抽取失败（歪了）：发放保底奖励，标记保底，生成愤怒粒子
            if (ifFailedThisTime) {
                sourceentity.getPersistentData().putBoolean((entityType + "IfGuarenteed"), true);
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, RewardConfig.execute(true, false, entityType));
                    entityToSpawn.setPickUpDelay(10);
                    _level.addFreshEntity(entityToSpawn);
                }
                if (world instanceof ServerLevel _level) {
                    _level.sendParticles(ParticleTypes.ANGRY_VILLAGER, x, y, z, 5, 0, 0.5, 0, 0.1);
                }
            } 
            // 本次抽取成功：发放高级奖励，生成爱心粒子
            else {
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, RewardConfig.execute(true, true, entityType));
                    entityToSpawn.setPickUpDelay(10);
                    _level.addFreshEntity(entityToSpawn);
                }
                if (world instanceof ServerLevel _level) {
                    _level.sendParticles(ParticleTypes.HEART, x, y, z, 5, 0, 0.5, 0, 0.1);
                }
            }
        }
    }
}