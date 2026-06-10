package cn.hard_pity.configuration;

import net.neoforged.neoforge.common.ModConfigSpec;

public class HardConfigConfiguration {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;
	public static final ModConfigSpec.ConfigValue<Boolean> ONLY_PLAYER_TRIGGER;
	public static final ModConfigSpec.ConfigValue<Boolean> MAIN_HAND_LOOTING_EXTRA_PULLS;
	public static final ModConfigSpec.ConfigValue<Double> LOOTING_MAX_EXTRA_PULL;
	public static final ModConfigSpec.ConfigValue<Double> ENTITY_TRIGGER_DETECTION_RANGE;
	public static final ModConfigSpec.ConfigValue<Boolean> CLEAR_PROGRESS_ON_DEATH;
	public static final ModConfigSpec.ConfigValue<Double> HIGH_REWARD_GUARANTEE_PULL_COUNT;
	public static final ModConfigSpec.ConfigValue<Double> MINI_GUARANTEE_BASE_SUCCESS_RATE;
	public static final ModConfigSpec.ConfigValue<String> PROBABILITY_GROWTH_MODE;
	public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_NORMAL_REWARD;
	public static final ModConfigSpec.ConfigValue<Double> NORMAL_REWARD_GUARANTEE_PULL_COUNT;
	static {
		BUILDER.push("general");
		ONLY_PLAYER_TRIGGER = BUILDER.comment("Controls whether only players are allowed to activate the lottery pull system. Other mobs or entities cannot trigger pulls when enabled.").define("onlyplayercantriggerpulls", true);
		MAIN_HAND_LOOTING_EXTRA_PULLS = BUILDER.comment("Allow extra pull counts when holding an item with Looting enchantment in the main hand.").define("mainhandlootingenchantextrapulls", true);
		LOOTING_MAX_EXTRA_PULL = BUILDER.comment("Set the upper limit of extra pull counts granted by Looting enchantment. Valid value: positive number.").define("maxextrapullsfromlooting", (double) 3);
		ENTITY_TRIGGER_DETECTION_RANGE = BUILDER.comment("Set the block range to detect target entities for triggering lottery pulls.").define("entitytriggerdetectionrange", (double) 8);
		CLEAR_PROGRESS_ON_DEATH = BUILDER.comment("Reset all pull progress and guarantee counts when the player dies.").define("clearprogressonplayerdeath", false);
		BUILDER.pop();
		BUILDER.push("advanced_reward_settings");
		HIGH_REWARD_GUARANTEE_PULL_COUNT = BUILDER.comment("Guaranteed pull count for advanced loot. Range: >=10").define("highrewardguaranteepullcount", (double) 40);
		MINI_GUARANTEE_BASE_SUCCESS_RATE = BUILDER.comment("Base non-offset probability for mini guarantee. It represents the probability of dropping resident pool items when the first mini guarantee is offset or the capture light value is 0.")
				.define("miniguaranteebasesuccessprobability", (double) 0.5);
		PROBABILITY_GROWTH_MODE = BUILDER.comment("Defines the probability calculation model as pull count increases. Optional values: mihoyo, linear, exp.").define("probabilitygrowthmode", "mihoyo");
		BUILDER.pop();
		BUILDER.push("normal_reward_settings");
		ENABLE_NORMAL_REWARD = BUILDER.comment("Enable the normal reward pool function. All card pools share the same pull progress and normal reward pool.").define("enablenormalrewardsystem", true);
		NORMAL_REWARD_GUARANTEE_PULL_COUNT = BUILDER.comment("Guaranteed pull count for normal loot. Normal reward will be triggered definitely when the cumulative pull count reaches this value.").define("normalrewardguaranteepullcount",
				(double) 20);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}