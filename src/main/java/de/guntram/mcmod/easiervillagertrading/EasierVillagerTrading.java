package de.guntram.mcmod.easiervillagertrading;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("easiervillagertrading")
public class EasierVillagerTrading
{
    public EasierVillagerTrading()
    {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


    private void setup(final FMLCommonSetupEvent event)
    {
        ConfigurationHandler confHandler = ConfigurationHandler.getInstance();
        confHandler.load();
        MinecraftForge.EVENT_BUS.register(confHandler);
    }


    private void doClientStuff(final FMLClientSetupEvent fmlEvent)
    {
        MinecraftForge.EVENT_BUS.register(OpenTradeEventHandler.getInstance());
    }



    //------------------------------------------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Minecraft logger.
     */
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * Mod ID
     */
    public static final String MODID = "easiervillagertrading";
}
