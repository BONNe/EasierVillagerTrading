package de.guntram.mcmod.easiervillagertrading;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import java.io.File;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public class ConfigurationHandler
{

    public static ConfigurationHandler getInstance()
    {
        if (ConfigurationHandler.instance == null)
        {
            ConfigurationHandler.instance = new ConfigurationHandler();
        }

        return  ConfigurationHandler.instance;
    }


    public void load(final File configFile)
    {
        if (this.config == null)
        {
            this.config = new Configuration(configFile);
            this.configFileName = configFile.getPath();
            this. loadConfig();
        }
    }


    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        // System.out.println("OnConfigChanged for "+event.getModID());
        if (event.getModID().equalsIgnoreCase(EasierVillagerTrading.MODID))
        {
            this.loadConfig();
        }
    }


    private void loadConfig()
    {
        this.showLeft = this.config.getBoolean("Trades list left", Configuration.CATEGORY_CLIENT,
                Loader.isModLoaded("jei"),
                "Show trades list to the left, for Just Enough Items compatibility");
        this.leftPixelOffset = this.config.getInt("Trades left pixel offset", Configuration.CATEGORY_CLIENT,
                0, 0, Integer.MAX_VALUE,
                "How many pixels left of the GUI the trades list will be shown. Use 0 for auto detect. " +
                        "Only used if Trades list left is true.");

        if (this.config.hasChanged())
        {
            this.config.save();
        }
    }


    public static Configuration getConfig()
    {
        return ConfigurationHandler.getInstance().config;
    }


    public static String getConfigFileName()
    {
        return ConfigurationHandler.getInstance().configFileName;
    }


    public static boolean showLeft()
    {
        return ConfigurationHandler.getInstance().showLeft;
    }


    public static int leftPixelOffset()
    {
        return ConfigurationHandler.getInstance().leftPixelOffset;
    }


    public static boolean autoFocusSearch()
    {
        return ConfigurationHandler.getInstance().autoFocusSearch;
    }


    //------------------------------------------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------------------------------------------


    private static ConfigurationHandler instance;

    private Configuration config;

    private String configFileName;

    private boolean showLeft;

    private int leftPixelOffset;

    private boolean autoFocusSearch;
}
