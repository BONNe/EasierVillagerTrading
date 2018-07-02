package de.guntram.mcmod.easiervillagertrading;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import java.io.File;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;


/**
 * ConfigurationHandler allows to access and modify mod configuration.
 */
public class ConfigurationHandler
{
    /**
     * This method returns ConfigurationHandler instance. If handler is not initialized, then it creates new
     * one.
     * @return ConfigurationHandler object.
     */
    public static ConfigurationHandler getInstance()
    {
        if (ConfigurationHandler.instance == null)
        {
            ConfigurationHandler.instance = new ConfigurationHandler();
        }

        return  ConfigurationHandler.instance;
    }


    /**
     * This method loads and parse configuration from given file.
     * @param configFile File that contains mod options.
     */
    public void load(final File configFile)
    {
        if (this.config == null)
        {
            this.config = new Configuration(configFile);
            this.configFileName = configFile.getPath();
            this.loadConfig();
        }
    }


    /**
     * This method parse and store current mod options.
     */
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


    /**
     * This method detects configuration changing.
     * @param event ConfigChangedEvent that is fired if configuration is changed.
     */
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(EasierVillagerTrading.MODID))
        {
            this.loadConfig();
        }
    }


// ---------------------------------------------------------------------
// Section: Getters
// ---------------------------------------------------------------------


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


    public static boolean isDefaultSellAll()
    {
        return ConfigurationHandler.getInstance().sellAll;
    }


// ---------------------------------------------------------------------
// Section: Setters
// ---------------------------------------------------------------------


    public static void setDefaultSellAll(boolean checked)
    {
        ConfigurationHandler.getInstance().sellAll = checked;
    }


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------

    /**
     * Single ConfigurationHandler instance.
     */
    private static ConfigurationHandler instance;

    /**
     * Configuration that contains all mod options.
     */
    private Configuration config;

    /**
     * File where mod options are saved.
     */
    private String configFileName;

    private boolean showLeft;

    private int leftPixelOffset;

    private boolean autoFocusSearch;

    private boolean sellAll;
}
