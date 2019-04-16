package lv.id.bonne.easiervillagertrading.config;


import lv.id.bonne.easiervillagertrading.buttons.IRecipeButton.ButtonType;
import net.minecraftforge.common.ForgeConfigSpec;


public class Config
{
	static void init()
	{
		ConfigurationLoader.CLIENT_BUILDER.
				comment("Easier Villager Trading configuration").
				push("EasierVillagerTrading");

		sellAll = ConfigurationLoader.CLIENT_BUILDER.
			comment("Should sell all be enabled on GUI opening.").
			define("default_sell_all", false);

		buttonType = ConfigurationLoader.CLIENT_BUILDER.
			comment("There are 3 types of button: " +
                "\tOriginal button, that was implemented in original mod. " +
                "\tOriginal button without enchant text. " +
                "\tCompact button with Minecraft button design.").
			defineEnum("button_type", ButtonType.ORIGINAL_BUTTON, ButtonType.values());

		panelSide = ConfigurationLoader.CLIENT_BUILDER.
			comment("All recipe buttons can be positioned in 3 sides: " +
                "Left side, Right side and Top side.").
			defineEnum("panel_side", SideType.LEFT_SIDE, SideType.values());

		alignWithGUI = ConfigurationLoader.CLIENT_BUILDER.
			comment("Should buttons be aligned with MerchantGUI.").
			define("align_with_gui", false);

		offsetFromGUI = ConfigurationLoader.CLIENT_BUILDER.
			comment("How many pixels from all sides of the GUI the trades list will be shown.").
			defineInRange("offset_from_gui", 5, 0, Integer.MAX_VALUE);

		offsetFromTopSide = ConfigurationLoader.CLIENT_BUILDER.
			comment("How many pixels from Top side of the Window the trades list will be shown. " + "\n" +
				"If aligning with GUI is enabled, then this option is ignored for Left and Right button position.").
			defineInRange("offset_from_top", 5, 0, Integer.MAX_VALUE);

		offsetFromBottomSide = ConfigurationLoader.CLIENT_BUILDER.
			comment("How many pixels from Bottom side of the Window the trades list will be shown. " + "\n" +
				"If aligning with GUI is enabled, then this option is ignored for Left and Right button position.").
			defineInRange("offset_from_bottom", 5, 0, Integer.MAX_VALUE);

		offsetFromRightSide = ConfigurationLoader.CLIENT_BUILDER.
			comment("How many pixels from Right side of the Window the trades list will be shown. " + "\n" +
				"If aligning with GUI is enabled, then this option is ignored for Top button position.").
			defineInRange("offset_from_right", 5, 0, Integer.MAX_VALUE);

		offsetFromLeftSide = ConfigurationLoader.CLIENT_BUILDER.
			comment("How many pixels from Left side of the Window the trades list will be shown. " + "\n" +
				"If aligning with GUI is enabled, then this option is ignored for Top button position.").
			defineInRange("offset_from_left", 5, 0, Integer.MAX_VALUE);

		delayBetweenActions = ConfigurationLoader.CLIENT_BUILDER.
			comment("This allows to bypass NoCheatPlus defense. Recipes will be processed slower, but they will work.").
			defineInRange("delay_between_actions", 100, 0, 10000);

		ConfigurationLoader.CLIENT_BUILDER.pop();
	}


// ---------------------------------------------------------------------
// Section: Getters
// ---------------------------------------------------------------------


	public static boolean isSellAll()
	{
		return sellAll.get();
	}


	public static ButtonType getButtonType()
	{
		return buttonType.get();
	}


	public static SideType getPanelSide()
	{
		return panelSide.get();
	}


	public static boolean isAlignWithGUI()
	{
		return alignWithGUI.get();
	}


	public static int getOffsetFromGUI()
	{
		return offsetFromGUI.get();
	}


	public static int getOffsetFromTopSide()
	{
		return offsetFromTopSide.get();
	}


	public static int getOffsetFromBottomSide()
	{
		return offsetFromBottomSide.get();
	}


	public static int getOffsetFromRightSide()
	{
		return offsetFromRightSide.get();
	}


	public static int getOffsetFromLeftSide()
	{
		return offsetFromLeftSide.get();
	}


	public static int getDelayBetweenActions()
	{
		return delayBetweenActions.get();
	}

// ---------------------------------------------------------------------
// Section: Setters
// ---------------------------------------------------------------------


	public static void setSellAll(boolean sellAll)
	{
//		Config.sellAll;
	}


	public static void setButtonType(ForgeConfigSpec.EnumValue<ButtonType> buttonType)
	{
		Config.buttonType = buttonType;
	}


	public static void setPanelSide(ForgeConfigSpec.EnumValue<SideType> panelSide)
	{
		Config.panelSide = panelSide;
	}


	public static void setAlignWithGUI(ForgeConfigSpec.BooleanValue alignWithGUI)
	{
		Config.alignWithGUI = alignWithGUI;
	}


	public static void setOffsetFromGUI(ForgeConfigSpec.IntValue offsetFromGUI)
	{
		Config.offsetFromGUI = offsetFromGUI;
	}


	public static void setOffsetFromTopSide(ForgeConfigSpec.IntValue offsetFromTopSide)
	{
		Config.offsetFromTopSide = offsetFromTopSide;
	}


	public static void setOffsetFromBottomSide(ForgeConfigSpec.IntValue offsetFromBottomSide)
	{
		Config.offsetFromBottomSide = offsetFromBottomSide;
	}


	public static void setOffsetFromRightSide(ForgeConfigSpec.IntValue offsetFromRightSide)
	{
		Config.offsetFromRightSide = offsetFromRightSide;
	}


	public static void setOffsetFromLeftSide(ForgeConfigSpec.IntValue offsetFromLeftSide)
	{
		Config.offsetFromLeftSide = offsetFromLeftSide;
	}


	public static void setDelayBetweenActions(ForgeConfigSpec.IntValue delayBetweenActions)
	{
		Config.delayBetweenActions = delayBetweenActions;
	}


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------

	/**
	 * Booelan that indicate if sell all should be enabled by default on GUI opening.
	 */
	private static ForgeConfigSpec.BooleanValue sellAll;

	/**
	 * String that stores selected button type.
	 */
	private static ForgeConfigSpec.EnumValue<ButtonType> buttonType;

	/**
	 * String that stores selected button panel side.
	 */
	private static ForgeConfigSpec.EnumValue<SideType> panelSide;

	/**
	 * Boolean that indicate if button panel should be aligned with MerchantGui.
	 */
	private static ForgeConfigSpec.BooleanValue alignWithGUI;

	/**
	 * Integer that stores distance from button panel till MerchantGui.
	 */
	private static ForgeConfigSpec.IntValue offsetFromGUI;

	/**
	 * Integer that stores minimal distance from Games Window Top Side till button panel.
	 */
	private static ForgeConfigSpec.IntValue offsetFromTopSide;

	/**
	 * Integer that stores minimal distance from Games Window Bottom Side till button panel.
	 */
	private static ForgeConfigSpec.IntValue offsetFromBottomSide;

	/**
	 * Integer that stores minimal distance from Games Window Right Side till button panel.
	 */
	private static ForgeConfigSpec.IntValue offsetFromRightSide;

	/**
	 * Integer that stores minimal distance from Games Window Left Side till button panel.
	 */
	private static ForgeConfigSpec.IntValue offsetFromLeftSide;

	/**
	 * Integer that stores milliseconds delay between actions.
	 */
	private static ForgeConfigSpec.IntValue delayBetweenActions;


// ---------------------------------------------------------------------
// Section: Enums
// ---------------------------------------------------------------------

	/**
	 * Variable that holds all available sides.
	 */
	public enum SideType
	{
		LEFT_SIDE,
		RIGHT_SIDE,
		TOP_SIDE
	}
}
