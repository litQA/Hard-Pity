package cn.hard_pity.server;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;

public class RewardConfig {
	public static ItemStack execute(boolean golden, boolean success, String entityType) {
		if (entityType == null)
			return ItemStack.EMPTY;
		ItemStack drops = ItemStack.EMPTY;
		String type = "";
		String list = "";
		double order = 0;
		double location = 0;
		double endComma = 0;
		type = entityType;
		if (golden) {
			if ((type).equals("WitherSkeleton")) {
				if (success) {
					drops = new ItemStack(Blocks.WITHER_SKELETON_SKULL).copy();
				} else {
					drops = new ItemStack(Blocks.SKELETON_SKULL).copy();
				}
			} else if ((type).equals("Drowned")) {
				if (success) {
					drops = new ItemStack(Items.TRIDENT).copy();
				} else {
					drops = new ItemStack(Items.DIAMOND_SWORD).copy();
				}
			} else if ((type).equals("Piglin")) {
				if (success) {
					drops = new ItemStack(Items.NETHERITE_SCRAP).copy();
				} else {
					drops = new ItemStack(Items.BROWN_DYE).copy();
				}
			} else if ((type).equals("Dragon")) {
				if (success) {
					drops = new ItemStack(Blocks.DRAGON_EGG).copy();
				} else {
					drops = new ItemStack(Items.EGG).copy();
				}
			}
		} else {
			if ((type).equals("WitherSkeleton")) {
				drops = new ItemStack(Blocks.WITHER_ROSE).copy();
			} else if ((type).equals("Drowned")) {
				drops = new ItemStack(Items.COPPER_INGOT).copy();
			} else if ((type).equals("Piglin")) {
				drops = new ItemStack(Items.GOLD_INGOT).copy();
			} else if ((type).equals("Dragon")) {
				drops = new ItemStack(Blocks.DRAGON_HEAD).copy();
			}
		}
		return drops;
	}
}