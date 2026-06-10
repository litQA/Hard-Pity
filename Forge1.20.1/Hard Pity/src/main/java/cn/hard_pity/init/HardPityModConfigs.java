package cn.hard_pity.init;

import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import cn.hard_pity.configuration.HardConfigConfiguration;
import cn.hard_pity.HardPityMod;

@Mod.EventBusSubscriber(modid = HardPityMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HardPityModConfigs {
	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, HardConfigConfiguration.SPEC, "Hard Pity.toml");
		});
	}
}