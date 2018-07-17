package de.guntram.mcmod.easiervillagertrading;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import java.io.File;
import java.util.Arrays;

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

        // new options

        this.buttonType = this.config.getString("Recipe Button type",
            Configuration.CATEGORY_CLIENT,
            VALID_BUTTONS[0],
            "There are 3 types of button: " +
                "Original button, that was implemented in original mod. " +
                "Original button without enchant text. " +
                "Compact button with Minecraft button design.",
            VALID_BUTTONS);

        this.panelSide = this.config.getString("Recipe Button position",
            Configuration.CATEGORY_CLIENT,
            VALID_SIDES[0],
            "All recipe buttons can be positioned in 3 sides: " +
                "Left side, Right side and Top side.",
            VALID_SIDES);

        this.alignWithGUI = this.config.getBoolean("Align buttons with MerchantGUI",
            Configuration.CATEGORY_CLIENT,
            false,
            "Should buttons be aligned with MerchantGUI.");

        this.offsetFromGUI = this.config.getInt("Offset From GUI Menu",
            Configuration.CATEGORY_CLIENT,
            5,
            0,
            Integer.MAX_VALUE,
            "How many pixels from all sides of the GUI the trades list will be shown.");

        this.offsetFromTopSide = this.config.getInt("Offset From Window Top",
            Configuration.CATEGORY_CLIENT,
            5,
            0,
            Integer.MAX_VALUE,
            "How many pixels from Top side of the Window the trades list will be shown. " +
            "If aligning with GUI is enabled, then this option is ignored for Left and Right button position.");

        this.offsetFromBottomSide = this.config.getInt("Offset From Window Bottom",
            Configuration.CATEGORY_CLIENT,
            5,
            0,
            Integer.MAX_VALUE,
            "How many pixels from Bottom side of the Window the trades list will be shown. " +
                "If aligning with GUI is enabled, then this option is ignored for Left and Right button position.");

        this.offsetFromRightSide = this.config.getInt("Offset From Window Right",
            Configuration.CATEGORY_CLIENT,
            5,
            0,
            Integer.MAX_VALUE,
            "How many pixels from Right side of the Window the trades list will be shown. " +
                "If aligning with GUI is enabled, then this option is ignored for Top button position.");

        this.offsetFromLeftSide = this.config.getInt("Offset From Window Left",
            Configuration.CATEGORY_CLIENT,
            5,
            0,
            Integer.MAX_VALUE,
            "How many pixels from Left side of the Window the trades list will be shown. " +
                "If aligning with GUI is enabled, then this option is ignored for Top button position.");

        this.delayBetweenActions = this.config.getInt("MS Delay between Actions",
            Configuration.CATEGORY_CLIENT,
            100,
            0,
            10000,
            "This allows to bypass NoCheatPlus defense. Recipes will be processed slower, but +" +
                "they will work.");

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
// Section: New Getters
// ---------------------------------------------------------------------


    /**
     * This method returns button type that current mod should use.
     * @return Integer that represents button type.
     *      0 - Original EasierVillagerTrading button.
     *      1 - Original EasierVillagerTrading button without Enchant Text.
     *      2 - Compact Button that looks like Minecraft button.
     */
    public static int getButtonType()
    {
        return Arrays.asList(VALID_BUTTONS).indexOf(ConfigurationHandler.getInstance().buttonType);
    }


    /**
     * This method returns Integer that represents in which side buttons should be placed.
     * @return Integer that represents button placement side.
     *      0 - Left Side.
     *      1 - Right Side.
     *      2 - Top Side.
     */
    public static int getPanelSide()
    {
        return Arrays.asList(VALID_SIDES).indexOf(ConfigurationHandler.getInstance().panelSide);
    }


    /**
     * This method returns Integer that represents how large distance should be from buttons till main
     * MerchantGUI.
     * @return Integer that represents distance from button panel till MerchantGUI.
     */
    public static int getOffsetFromGUI()
    {
        return ConfigurationHandler.getInstance().offsetFromGUI;
    }


    /**
     * This method returns Integer that represents how large distance should be from buttons till Game window
     * TOP Side. This option is ignored if placement side is Left or Right and alignment with GUI is enabled.
     * @return Integer that represents distance from button panel till Game window top side.
     */
    public static int getOffsetFromTopSide()
    {
        return ConfigurationHandler.getInstance().offsetFromTopSide;
    }


    /**
     * This method returns Integer that represents how large distance should be from buttons till Game window
     * Bottom Side. This option is ignored if placement side is Left or Right and alignment with GUI is
     * enabled.
     * @return Integer that represents distance from button panel till Game window bottom side.
     */
    public static int getOffsetFromBottomSide()
    {
        return ConfigurationHandler.getInstance().offsetFromBottomSide;
    }


    /**
     * This method returns Integer that represents how large distance should be from buttons till Game window
     * Right Side. This option is ignored if placement side is TOP and alignment with GUI is enabled.
     * @return Integer that represents distance from button panel till Game window right side.
     */
    public static int getOffsetFromRightSide()
    {
        return ConfigurationHandler.getInstance().offsetFromRightSide;
    }


    /**
     * This method returns Integer that represents how large distance should be from buttons till Game window
     * Left Side. This option is ignored if placement side is TOP and alignment with GUI is enabled.
     * @return Integer that represents distance from button panel till Game window left side.
     */
    public static int getOffsetFromLeftSide()
    {
        return ConfigurationHandler.getInstance().offsetFromLeftSide;
    }


    /**
     * This method returns boolean that indicate if buttons should be aligned with MerchantGui.
     * @return
     *      <code>true</code> if buttons must be aligned with MerchantGUI.
     *      <code>false</code> if buttons should not be aligned with MerchantGUI.
     */
    public static boolean alignWithGUI()
    {
        return ConfigurationHandler.getInstance().alignWithGUI;
    }


    /**
     * This method returns integer that indicate a delay between automated mouse click actions.
     * @return integer that represents milliseconds which will be used in delay between mouse clicks.
     */
    public static int getDelayBetweenActions()
    {
        return ConfigurationHandler.getInstance().delayBetweenActions;
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

// ---------------------------------------------------------------------
// Section: New Variables
// ---------------------------------------------------------------------

    /**
     * String that stores selected button type.
     */
    private String buttonType;

    /**
     * String that stores selected button panel side.
     */
    private String panelSide;

    /**
     * Boolean that indicate if button panel should be aligned with MerchantGui.
     */
    private boolean alignWithGUI;

    /**
     * Integer that stores distance from button panel till MerchantGui.
     */
    private int offsetFromGUI;

    /**
     * Integer that stores minimal distance from Games Window Top Side till button panel.
     */
    private int offsetFromTopSide;

    /**
     * Integer that stores minimal distance from Games Window Bottom Side till button panel.
     */
    private int offsetFromBottomSide;

    /**
     * Integer that stores minimal distance from Games Window Right Side till button panel.
     */
    private int offsetFromRightSide;

    /**
     * Integer that stores minimal distance from Games Window Left Side till button panel.
     */
    private int offsetFromLeftSide;

    /**
     * Integer that stores milliseconds delay between actions.
     */
    private int delayBetweenActions;

// ---------------------------------------------------------------------
// Section: Constants
// ---------------------------------------------------------------------

    /**
     * Variable that holds all available sides.
     */
    private static final String[] VALID_SIDES = {"Left Side", "Right Side", "Top Side"};

    /**
     * Variable that holds all available buttons.
     */
    private static final String[] VALID_BUTTONS = {"Original Button", "Original without Enchant", "Compact button"};
}
