package lv.id.bonne.easiervillagertrading;

import lv.id.bonne.easiervillagertrading.config.ConfigurationLoader;
import lv.id.bonne.easiervillagertrading.events.OpenTradeEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Constants.MODID)
public class EasierVillagerTrading
{
    public EasierVillagerTrading()
    {
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        ModLoadingContext.get().
            registerConfig(ModConfig.Type.CLIENT, ConfigurationLoader.CLIENT_CONFIG);

        ConfigurationLoader.loadConfig(
            ConfigurationLoader.CLIENT_CONFIG,
            FMLPaths.CONFIGDIR.get().resolve(Constants.MODID + "-client.toml"));
    }


    private void doClientStuff(final FMLClientSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(
            new OpenTradeEventHandler(event.getMinecraftSupplier().get()));
    }


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------


    /**
     * Minecraft logger.
     */
    public static final Logger LOGGER = LogManager.getLogger();
}
