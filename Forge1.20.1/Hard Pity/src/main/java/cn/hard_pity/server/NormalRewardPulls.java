package cn.hard_pity.server;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import cn.hard_pity.configuration.HardConfigConfiguration;

/**
 * 普通奖励抽取逻辑处理类
 * 处理不同实体对应的普通奖励抽取、保底机制
 */
public class NormalRewardPulls {
    /**
     * 执行普通奖励抽取
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
        
        // 保底次数（最小为2）
        double guarantee = 0;
        // 当前普通奖励累计抽取次数
        double currentPullNormal = 0;
        // 保底前的基础抽取概率
        double rateBeforeGuarantee = 0;
        
        // 获取配置中的保底次数，确保最小值为2
        guarantee = Math.max(2, (double) HardConfigConfiguration.NORMAL_REWARD_GUARANTEE_PULL_COUNT.get());
        // 从实体持久化数据中读取当前累计抽取次数
        currentPullNormal = sourceentity.getPersistentData().getDouble((entityType + "NormalCount"));
        // 计算保底前的抽取概率：0.5 / 保底次数
        rateBeforeGuarantee = 0.5 / guarantee;
        
        // 循环执行抽取逻辑
        for (int index0 = 0; index0 < (int) Math.round(pulls); index0++) {
            // 触发保底：累计次数+1 >= 保底次数
            if (currentPullNormal + 1 >= guarantee) {
                // 重置累计次数
                currentPullNormal = 0;
                // 生成奖励物品实体
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, RewardConfig.execute(false, true, entityType));
                    entityToSpawn.setPickUpDelay(10);
                    _level.addFreshEntity(entityToSpawn);
                }
            } 
            // 未触发保底，按概率抽取
            else {
                // 随机数小于抽取概率，抽取成功
                if (Random01.execute() < rateBeforeGuarantee) {
                    // 重置累计次数
                    currentPullNormal = 0;
                    // 生成奖励物品实体
                    if (world instanceof ServerLevel _level) {
                        ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, RewardConfig.execute(false, true, entityType));
                        entityToSpawn.setPickUpDelay(10);
                        _level.addFreshEntity(entityToSpawn);
                    }
                } 
                // 抽取失败，累计次数+1
                else {
                    currentPullNormal = currentPullNormal + 1;
                }
            }
        }
        
        // 将最终的累计次数写回实体持久化数据
        sourceentity.getPersistentData().putDouble((entityType + "NormalCount"), currentPullNormal);
    }
}