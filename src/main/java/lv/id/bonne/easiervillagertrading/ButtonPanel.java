package lv.id.bonne.easiervillagertrading;


import java.util.ArrayList;
import java.util.List;

import lv.id.bonne.easiervillagertrading.buttons.IRecipeButton;
import lv.id.bonne.easiervillagertrading.buttons.PageButton;
import lv.id.bonne.easiervillagertrading.buttons.recipebuttons.RecipeButton;
import lv.id.bonne.easiervillagertrading.buttons.recipebuttons.RecipeTextButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.village.MerchantRecipeList;


/**
 * This class is used to store and position all recipe buttons.
 */
public class ButtonPanel
{
	/**
	 * This constructor is used to create default Button Panel.
	 * @param merchantGui ImprovedGuiMerchant where current panel is located.
	 */
	public ButtonPanel(ImprovedGuiMerchant merchantGui)
	{
		this.merchantGui = merchantGui;

		this.lastUnusedButtonId = 50;
		this.tradingButtons = new ArrayList<>();

		this.showButtons = true;

		this.initPanel();
	}


	/**
	 * This method inits ButtonPanel parameters.
	 */
	private void initPanel()
	{
		// TODO: Read side from Configuration.
		int side = 1;

		// TODO: Read distance from Configuration.
		int offsetFromMainPanel = 5;

		// TODO: Read distances from Configuration.
		int offsetFromWindowsTopSide = 5;
		int offsetFromWindowsBottomSide = 5;
		int offsetFromWindowRightSide = 5;
		int offsetFromWindowLeftSide = 5;

		int merchantGuiLeftSide = this.merchantGui.getGuiLeft();
		int merchantGuiTopSide = this.merchantGui.getGuiTop();

		int merchantGuiHeight = this.merchantGui.getYSize();
		int merchantGuiWidth = this.merchantGui.getXSize();

		int windowWidth = this.merchantGui.width;
		int windowHeight = this.merchantGui.height;

		if (side == LEFT_SIDE)
		{
			this.width = merchantGuiLeftSide - offsetFromMainPanel - offsetFromWindowLeftSide;
			this.height = windowHeight - merchantGuiTopSide - offsetFromWindowsBottomSide;

			this.xPosition = offsetFromWindowLeftSide;
			this.yPosition = merchantGuiTopSide;
		}
		else if (side == RIGHT_SIDE)
		{
			// TODO: Probably not correct. I assume that left and right side are with equal size.
			this.width = merchantGuiLeftSide - offsetFromMainPanel - offsetFromWindowRightSide;
			this.height = windowHeight - merchantGuiTopSide - offsetFromWindowsBottomSide;

			this.xPosition = merchantGuiLeftSide + merchantGuiWidth + offsetFromMainPanel;
			this.yPosition = merchantGuiTopSide;
		}
		else if (side == TOP_SIDE)
		{
			this.width = windowWidth - offsetFromWindowLeftSide - offsetFromWindowsBottomSide;
			this.height = merchantGuiTopSide - offsetFromWindowsTopSide - offsetFromMainPanel;

			this.xPosition = offsetFromWindowLeftSide;
			this.yPosition = offsetFromWindowsTopSide;
		}
		else
		{
			// There are no space for buttons.
			this.showButtons = false;

			this.width = 0;
			this.height = 0;

			this.xPosition = 0;
			this.yPosition = 0;
		}

		// Add new PageButtons above current button panel.
		this.previousPageButton =
			new PageButton(this.lastUnusedButtonId++, this.xPosition, this.yPosition - 15, false);
		this.nextPageButton =
			new PageButton(this.lastUnusedButtonId++, this.xPosition + 46, this.yPosition - 15, true);

		this.merchantGui.addGuiButton(this.previousPageButton);
		this.merchantGui.addGuiButton(this.nextPageButton);

		this.validateButtonType();

		// Validate column count.
		int maxColumnCount;

		if (this.buttonType == IRecipeButton.BUTTON_TYPE_COMPACT)
		{
			maxColumnCount = this.width / 58;

			if (maxColumnCount == 1 && side == RIGHT_SIDE)
			{
				// set better position for compact button.

				this.xPosition = windowWidth - offsetFromWindowRightSide - 58;
			}
		}
		else
		{
			maxColumnCount = 1;
		}

		this.setColumnCount(maxColumnCount);
	}


	/**
	 * This method validate button type. If type is not available, then sets different.
	 */
	private void validateButtonType()
	{
		// TODO: Get button type from Configuration
		this.buttonType = 2;

		if (this.width < 58 || this.height < 20)
		{
			this.buttonType = -1;

			// TODO: Necessary to notify user about current issue.

			// Cannot create button panel. Buttons are larger then available space.
			this.showButtons = false;
		}
		else if (this.width < 80)
		{
			// TODO: Necessary to notify user why button type is changed.

			this.buttonType = IRecipeButton.BUTTON_TYPE_COMPACT;
		}
		else if (buttonType == IRecipeButton.BUTTON_TYPE_TEXT_WITH_ENCHANTS && this.width < 90)
		{
			// TODO: Necessary to notify user why button type is changed.

			this.buttonType = IRecipeButton.BUTTON_TYPE_TEXT;
		}
	}


// ---------------------------------------------------------------------
// Section: Setters
// ---------------------------------------------------------------------


	/**
	 * This method sets button type;
	 * @param buttonType Integer that represents type of button that should be used in current panel.
	 * IRecipeButton.BUTTON_TYPE_TEXT_WITH_ENCHANTS - Old button design.
	 * IRecipeButton.BUTTON_TYPE_TEXT - Old button design without enchant name.
	 * IRecipeButton.BUTTON_TYPE_COMPACT - New button design.
	 */
	public void setButtonType(int buttonType)
	{
		this.buttonType = buttonType;
	}


	/**
	 * This method sets maximal number of columns that should be used.
	 * @param maxColumnCount column count.
	 */
	public void setColumnCount(int maxColumnCount)
	{
		this.maxColumnCount = maxColumnCount;
	}


// ---------------------------------------------------------------------
// Section: Methods
// ---------------------------------------------------------------------


	/**
	 * This method is used to add and validate all merchant recipe buttons.
	 */
	public void validateTradingButtons()
	{
		MerchantRecipeList merchantRecipes = this.merchantGui.getRecipes();

		if (merchantRecipes != null && this.showButtons)
		{
			final int size = merchantRecipes.size();

			int buttonHigh = 20;
			int buttonWidth;

			if (this.buttonType == IRecipeButton.BUTTON_TYPE_COMPACT)
			{
				buttonWidth = 58;
			}
			else
			{
				buttonWidth = 80;
			}

			int x;
			int y;

			int currRow;
			int currCol;

			for (int i = 0; i < size; i++)
			{
				if (i >= this.tradingButtons.size())
				{
					currRow = i / this.maxColumnCount;
					currCol = i % this.maxColumnCount;
					x = this.xPosition + currCol * buttonWidth;
					y = this.yPosition + currRow * buttonHigh;

					IRecipeButton newRecipeButton;

					if (this.buttonType == IRecipeButton.BUTTON_TYPE_COMPACT)
					{
						newRecipeButton = new RecipeButton(this.lastUnusedButtonId,
							x,
							y,
							buttonWidth,
							buttonHigh,
							i,
							this.merchantGui);
					}
					else
					{
						newRecipeButton = new RecipeTextButton(this.lastUnusedButtonId,
							x,
							y,
							buttonWidth,
							buttonHigh,
							i,
							this.merchantGui,
							this.buttonType == IRecipeButton.BUTTON_TYPE_TEXT_WITH_ENCHANTS);
					}


					this.merchantGui.addGuiButton(newRecipeButton);
					this.tradingButtons.add(newRecipeButton);
					this.lastUnusedButtonId++;
				}
			}
		}
	}


	/**
	 * This method draws all buttons and their tooltips.
	 * @param mouseX Cursor X position.
	 * @param mouseY Cursor Y position.
	 * @param partialTicks Partial Ticks.
	 */
	public void drawAllButtons(int mouseX, int mouseY, float partialTicks)
	{
		if (this.showButtons)
		{
			for (IRecipeButton button : this.tradingButtons)
			{
				button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks);
			}

			for (IRecipeButton button : this.tradingButtons)
			{
				button.drawTooltips(mouseX, mouseY);
			}

			this.previousPageButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks);
			this.nextPageButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks);
		}
	}


	/**
	 * This method returns Recipe index that is stored in given GuiButton.
	 * @param inputButton Button which recipe must be searched.
	 * @return Index of merchant recipe.
	 */
	public int getRecipeFromButton(GuiButton inputButton)
	{
		int recipeIndex;

		if (inputButton instanceof IRecipeButton)
		{
			recipeIndex = ((IRecipeButton) inputButton).getRecipeIndex();
		}
		else
		{
			recipeIndex = -1;
		}

		return recipeIndex;
	}


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------


	/**
	 * ImprovedGuiMerchant.
	 */
	private final ImprovedGuiMerchant merchantGui;

	/**
	 * This variable holds how wide current panel can be.
	 */
	private int width;

	/**
	 * This variable holds how high current panel can be.
	 */
	private int height;

	/**
	 * This variable holds X position where panel starts.
	 */
	private int xPosition;

	/**
	 * This variable holds Y position where panel starts.
	 */
	private int yPosition;

	/**
	 * Just an integer that allows to init unique buttons.
	 */
	private int lastUnusedButtonId;

	/**
	 * List with Recipe buttons.
	 */
	private List<IRecipeButton> tradingButtons;

	/**
	 * This variable holds which button design should be used in current panel.
	 */
	private int buttonType;

	/**
	 * This variable holds how much columns can be used.
	 */
	private int maxColumnCount;

	/**
	 * This variable holds if recipe buttons can be showed.
	 */
	private boolean showButtons;

	private PageButton nextPageButton;

	private PageButton previousPageButton;

// ---------------------------------------------------------------------
// Section: Constants
// ---------------------------------------------------------------------

	/**
	 * Integer that represents left side.
	 */
	private static final int LEFT_SIDE = 0;

	/**
	 * Integer that represents right side.
	 */
	private static final int RIGHT_SIDE = 1;

	/**
	 * Integer that represents top side.
	 */
	private static final int TOP_SIDE = 2;
}
