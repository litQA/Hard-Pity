package cn.hard_pity.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class HardConfigConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ONLY_PLAYER_TRIGGER;
	public static final ForgeConfigSpec.ConfigValue<Boolean> MAIN_HAND_LOOTING_EXTRA_PULLS;
	public static final ForgeConfigSpec.ConfigValue<Double> LOOTING_MAX_EXTRA_PULL;
	public static final ForgeConfigSpec.ConfigValue<Double> ENTITY_TRIGGER_DETECTION_RANGE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> CLEAR_PROGRESS_ON_DEATH;
	public static final ForgeConfigSpec.ConfigValue<Double> HIGH_REWARD_GUARANTEE_PULL_COUNT;
	public static final ForgeConfigSpec.ConfigValue<Double> MINI_GUARANTEE_BASE_SUCCESS_RATE;
	public static final ForgeConfigSpec.ConfigValue<String> PROBABILITY_GROWTH_MODE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_NORMAL_REWARD;
	public static final ForgeConfigSpec.ConfigValue<Double> NORMAL_REWARD_GUARANTEE_PULL_COUNT;
	static {
		BUILDER.push("General");
		ONLY_PLAYER_TRIGGER = BUILDER.comment("Controls whether only players are allowed to activate the lottery pull system. Other mobs or entities cannot trigger pulls when enabled.").define("only player can trigger pulls", true);
		MAIN_HAND_LOOTING_EXTRA_PULLS = BUILDER.comment("Allow extra pull counts when holding an item with Looting enchantment in the main hand.").define("main hand looting enchant extra pulls", true);
		LOOTING_MAX_EXTRA_PULL = BUILDER.comment("Set the upper limit of extra pull counts granted by Looting enchantment. Valid value: positive number.").define("max extra pulls from looting", (double) 3);
		ENTITY_TRIGGER_DETECTION_RANGE = BUILDER.comment("Set the block range to detect target entities for triggering lottery pulls.").define("entity trigger detection range", (double) 8);
		CLEAR_PROGRESS_ON_DEATH = BUILDER.comment("Reset all pull progress and guarantee counts when the player dies.").define("clear progress on player death", false);
		BUILDER.pop();
		BUILDER.push("Advanced Reward Settings");
		HIGH_REWARD_GUARANTEE_PULL_COUNT = BUILDER.comment("Guaranteed pull count for advanced loot. Range: >=10").define("high reward guarantee pull count", (double) 40);
		MINI_GUARANTEE_BASE_SUCCESS_RATE = BUILDER.comment("Base non-offset probability for mini guarantee. It represents the probability of dropping resident pool items when the first mini guarantee is offset or the capture light value is 0.")
				.define("mini guarantee base success probability", (double) 0.5);
		PROBABILITY_GROWTH_MODE = BUILDER.comment("Defines the probability calculation model as pull count increases. Optional values: mihoyo, linear, exp.").define("probability growth mode", "mihoyo");
		BUILDER.pop();
		BUILDER.push("Normal Reward Settings");
		ENABLE_NORMAL_REWARD = BUILDER.comment("Enable the normal reward pool function. All card pools share the same pull progress and normal reward pool.").define("enable normal reward system", true);
		NORMAL_REWARD_GUARANTEE_PULL_COUNT = BUILDER.comment("Guaranteed pull count for normal loot. Normal reward will be triggered definitely when the cumulative pull count reaches this value.").define("normal reward guarantee pull count",
				(double) 20);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}