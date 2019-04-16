package lv.id.bonne.easiervillagertrading.config;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import java.nio.file.Path;

import lv.id.bonne.easiervillagertrading.Constants;
import lv.id.bonne.easiervillagertrading.EasierVillagerTrading;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import static net.minecraftforge.fml.loading.LogMarkers.CORE;


@Mod.EventBusSubscriber(modid = Constants.MODID)
public class ConfigurationLoader
{
	public static void loadConfig(ForgeConfigSpec spec, Path path)
	{
		EasierVillagerTrading.LOGGER.debug("Loading config file {}", path);

		final CommentedFileConfig configData = CommentedFileConfig.builder(path).
			sync().
			autosave().
			writingMode(WritingMode.REPLACE).
			build();

		EasierVillagerTrading.LOGGER.debug("Built TOML config for {}", path.toString());
		configData.load();
		EasierVillagerTrading.LOGGER.debug("Loaded TOML config file {}", path.toString());
		spec.setConfig(configData);
	}


	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent)
	{
		EasierVillagerTrading.LOGGER.debug("Loaded {} config file {}",
			Constants.MODID,
			configEvent.getConfig().getFileName());
	}


	@SubscribeEvent
	public static void onFileChange(final ModConfig.ConfigReloading configEvent)
	{
		EasierVillagerTrading.LOGGER.fatal(CORE, "{} config just got changed on the file system!", Constants.MODID);
	}


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------

	static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec CLIENT_CONFIG;


// ---------------------------------------------------------------------
// Section: Static methods
// ---------------------------------------------------------------------


	static
	{
		Config.init();
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}
}