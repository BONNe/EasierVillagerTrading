package lv.id.bonne.easiervillagertrading;


import java.util.ArrayList;
import java.util.List;

import de.guntram.mcmod.easiervillagertrading.ConfigurationHandler;
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
		int side = ConfigurationHandler.getPanelSide();
		int offsetFromMainPanel = ConfigurationHandler.getOffsetFromGUI();

		int offsetFromWindowsTopSide = ConfigurationHandler.getOffsetFromTopSide();
		int offsetFromWindowsBottomSide = ConfigurationHandler.getOffsetFromBottomSide();
		int offsetFromWindowRightSide = ConfigurationHandler.getOffsetFromRightSide();
		int offsetFromWindowLeftSide = ConfigurationHandler.getOffsetFromLeftSide();

		boolean alignWithGUI = ConfigurationHandler.alignWithGUI();;

		int merchantGuiLeftSide = this.merchantGui.getGuiLeft();
		int merchantGuiTopSide = this.merchantGui.getGuiTop();

		int merchantGuiHeight = this.merchantGui.getYSize();
		int merchantGuiWidth = this.merchantGui.getXSize();

		int windowWidth = this.merchantGui.width;
		int windowHeight = this.merchantGui.height;

		int pageIndexingWidth = 56;
		int pageIndexingHeight = 15;

		if (side == LEFT_SIDE)
		{
			this.width = merchantGuiLeftSide - offsetFromMainPanel - offsetFromWindowLeftSide;
			this.xPosition = offsetFromWindowLeftSide;

			if (alignWithGUI)
			{
				this.height = merchantGuiHeight;
				this.yPosition = merchantGuiTopSide;
			}
			else
			{
				this.height = windowHeight - offsetFromWindowsTopSide -
					offsetFromWindowsBottomSide - pageIndexingHeight;
				this.yPosition = offsetFromWindowsTopSide + pageIndexingHeight;
			}
		}
		else if (side == RIGHT_SIDE)
		{
			this.width = merchantGuiLeftSide - offsetFromMainPanel - offsetFromWindowRightSide;
			this.xPosition = merchantGuiLeftSide + merchantGuiWidth + offsetFromMainPanel;

			if (alignWithGUI)
			{
				this.height = merchantGuiHeight;
				this.yPosition = merchantGuiTopSide;
			}
			else
			{
				this.height = windowHeight - offsetFromWindowsTopSide -
					offsetFromWindowsBottomSide - pageIndexingHeight;
				this.yPosition = offsetFromWindowsTopSide + pageIndexingHeight;
			}
		}
		else if (side == TOP_SIDE)
		{
			this.height = merchantGuiTopSide - offsetFromWindowsTopSide - offsetFromMainPanel;
			this.yPosition = offsetFromWindowsTopSide;

			if (alignWithGUI)
			{
				this.width = merchantGuiWidth;
				this.xPosition = merchantGuiLeftSide;
			}
			else
			{
				this.xPosition = offsetFromWindowLeftSide + pageIndexingWidth;
				this.width = windowWidth - offsetFromWindowLeftSide -
					offsetFromWindowsBottomSide - pageIndexingWidth;
			}
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
		if (side != TOP_SIDE)
		{
			this.previousPageButton = new PageButton(this.lastUnusedButtonId++,
				this.xPosition,
				this.yPosition - pageIndexingHeight,
				false);
			this.nextPageButton = new PageButton(this.lastUnusedButtonId++,
				this.xPosition + pageIndexingWidth - 10,
				this.yPosition - pageIndexingHeight,
				true);
		}
		else
		{
			this.previousPageButton = new PageButton(this.lastUnusedButtonId++,
				this.xPosition - pageIndexingWidth,
				this.yPosition + this.height / 2,
				false);
			this.nextPageButton = new PageButton(this.lastUnusedButtonId++,
				this.xPosition - pageIndexingWidth,
				this.yPosition + this.height / 2,
				true);
		}

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
		this.buttonType = ConfigurationHandler.getButtonType();

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
			int buttonWidth = this.buttonType == IRecipeButton.BUTTON_TYPE_COMPACT ? 58 : 80;

			// process paging

			this.elementsPerPage = this.maxColumnCount * this.height / 20;

			if (this.elementsPerPage < size)
			{
				this.maxPageCount = (int) Math.ceil((double) size / this.elementsPerPage);
			}

			// process button layout

			int x;
			int y;

			int currRow;
			int currCol;

			for (int index = 0; index < size; index++)
			{
				if (index >= this.tradingButtons.size())
				{
					int elementIndex = index;

					while (elementIndex >= this.elementsPerPage)
					{
						elementIndex = elementIndex - this.elementsPerPage;
					}

					currRow = elementIndex / this.maxColumnCount;
					currCol = elementIndex % this.maxColumnCount;
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
							index,
							this.merchantGui);
					}
					else
					{
						newRecipeButton = new RecipeTextButton(this.lastUnusedButtonId,
							x,
							y,
							buttonWidth,
							buttonHigh,
							index,
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
			if (this.tradingButtons.size() <= this.elementsPerPage)
			{
				// Single page. Easy.

				for (IRecipeButton button : this.tradingButtons)
				{
					button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks);
				}

				for (IRecipeButton button : this.tradingButtons)
				{
					button.drawTooltips(mouseX, mouseY);
				}

				this.nextPageButton.visible = false;
				this.previousPageButton.visible = false;
			}
			else
			{
				// Per page.

				int firstElement = this.currentPageIndex * this.elementsPerPage;

				for (int index = 0; index < this.tradingButtons.size(); index++)
				{
					GuiButton button = this.tradingButtons.get(index);

					button.visible = index >= firstElement && index < firstElement + this.elementsPerPage;
					button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks);
				}

				for (int index = firstElement;
					index < this.tradingButtons.size() && index < firstElement + this.elementsPerPage;
					index++)
				{
					this.tradingButtons.get(index).drawTooltips(mouseX, mouseY);
				}

				this.previousPageButton.enabled = this.currentPageIndex != 0;
				this.nextPageButton.enabled = this.currentPageIndex < this.maxPageCount - 1;

				// Page index String
				String pageIndexString = (this.currentPageIndex + 1) + " / " + this.maxPageCount;

				int stringWidth = this.merchantGui.getFontRender().getStringWidth(pageIndexString);
				int xPosition = this.previousPageButton.x + (this.nextPageButton.x -
					this.previousPageButton.x + this.previousPageButton.width - stringWidth) / 2;
				int stringHeight = this.merchantGui.getFontRender().FONT_HEIGHT;
				int yPosition = this.previousPageButton.y +
					(this.previousPageButton.height - stringHeight) / 2;

				// TODO: Probably User want to change color manually?
				int fontColor = 0xffffff;

				this.merchantGui.getFontRender().drawString(pageIndexString,
					xPosition,
					yPosition,
					fontColor);
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


	/**
	 * This method returns if given GuiButton is RecipeButton.
	 * @param button GuiButton that must be checked.
	 * @return
	 * 		<code>true</code> if given button is RecipeButton.
	 * 		<code>false</code> if given button is not RecipeButton.
	 */
	public boolean isRecipeButton(GuiButton button)
	{
		return button instanceof IRecipeButton;
	}


	/**
	 * This method process given button pressing action.
	 * @param button GuiButton.
	 */
	public void actionPerformed(GuiButton button)
	{
		if (button == this.nextPageButton)
		{
			this.currentPageIndex++;
		}
		else if (button == this.previousPageButton)
		{
			this.currentPageIndex--;
		}
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

	/**
	 * This variable holds current page index.
	 */
	private int currentPageIndex = 0;

	/**
	 * This variable hold max page count.
	 */
	private int maxPageCount = 1;

	/**
	 * This variable holds how much elements are in single page.
	 */
	private int elementsPerPage;

	/**
	 * This button allows to get next recipe button page.
	 */
	private PageButton nextPageButton;

	/**
	 * This button allows to get previous recipe button page.
	 */
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
