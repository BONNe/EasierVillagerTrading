package lv.id.bonne.easiervillagertrading.gui;


import java.util.ArrayList;
import java.util.List;

import lv.id.bonne.easiervillagertrading.buttons.AutoTradeButton;
import lv.id.bonne.easiervillagertrading.buttons.CheckBoxButton;
import lv.id.bonne.easiervillagertrading.buttons.IRecipeButton;
import lv.id.bonne.easiervillagertrading.buttons.IRecipeButton.ButtonType;
import lv.id.bonne.easiervillagertrading.buttons.PageButton;
import lv.id.bonne.easiervillagertrading.buttons.recipebuttons.RecipeButton;
import lv.id.bonne.easiervillagertrading.buttons.recipebuttons.RecipeTextButton;
import lv.id.bonne.easiervillagertrading.config.Config;
import lv.id.bonne.easiervillagertrading.config.Config.SideType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
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
		SideType side = Config.getPanelSide();
		int offsetFromMainPanel = Config.getOffsetFromGUI();

		int offsetFromWindowsTopSide = Config.getOffsetFromTopSide();
		int offsetFromWindowsBottomSide = Config.getOffsetFromBottomSide();
		int offsetFromWindowRightSide = Config.getOffsetFromRightSide();
		int offsetFromWindowLeftSide = Config.getOffsetFromLeftSide();

		boolean alignWithGUI = Config.isAlignWithGUI();;

		int merchantGuiLeftSide = this.merchantGui.getGuiLeft();
		int merchantGuiTopSide = this.merchantGui.getGuiTop();

		int merchantGuiHeight = this.merchantGui.getYSize();
		int merchantGuiWidth = this.merchantGui.getXSize();

		int windowWidth = this.merchantGui.width;
		int windowHeight = this.merchantGui.height;

		int pageIndexingWidth = 56;
		int pageIndexingHeight = 15;

		if (side == SideType.LEFT_SIDE)
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
		else if (side == SideType.RIGHT_SIDE)
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
		else if (side == SideType.TOP_SIDE)
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
		if (side != SideType.TOP_SIDE)
		{
			this.previousPageButton = new PageButton(this.lastUnusedButtonId++,
				this.xPosition,
				this.yPosition - pageIndexingHeight,
				false)
			{
				@Override
				public void onClick(double mouseX, double mouseY)
				{
					ButtonPanel.this.currentPageIndex--;
				}
			};
			this.nextPageButton = new PageButton(this.lastUnusedButtonId++,
				this.xPosition + pageIndexingWidth - 10,
				this.yPosition - pageIndexingHeight,
				true)
			{
				@Override
				public void onClick(double mouseX, double mouseY)
				{
					ButtonPanel.this.currentPageIndex++;
				}
			};

			if (side == SideType.LEFT_SIDE)
			{
				this.sellAllCheckbox = new CheckBoxButton(this.lastUnusedButtonId++,
					merchantGuiLeftSide + 5,
					merchantGuiTopSide - pageIndexingHeight,
					"Sell All Items",
					Config.isSellAll())
				{
					@Override
					public void onClick(double mouseX, double mouseY)
					{
						super.onClick(mouseX, mouseY);
						this.processButton();
					}
				};

				int offset = this.sellAllCheckbox.width + 15;

				this.autoTradeButton = new AutoTradeButton(this.lastUnusedButtonId++,
					merchantGuiLeftSide + 5 + offset,
					merchantGuiTopSide - pageIndexingHeight,
					this.merchantGui.getFontRender().getStringWidth("Auto Trade") + 10,
					this.sellAllCheckbox.height + 5,
					"Auto Trade",
					this.merchantGui);
			}
			else
			{
				int buttonWidth =
					this.merchantGui.getFontRender().getStringWidth("Sell All Items") + 15;

				this.sellAllCheckbox = new CheckBoxButton(this.lastUnusedButtonId++,
					merchantGuiLeftSide + merchantGuiWidth - buttonWidth,
					merchantGuiTopSide - pageIndexingHeight,
					"Sell All Items",
					Config.isSellAll())
				{
					@Override
					public void onClick(double mouseX, double mouseY)
					{
						super.onClick(mouseX, mouseY);
						this.processButton();
					}
				};

				// TODO FIX THIS
				this.autoTradeButton = new AutoTradeButton(this.lastUnusedButtonId++,
					merchantGuiLeftSide + 5 + 2 * buttonWidth,
					merchantGuiTopSide - pageIndexingHeight,
					11,
					11,
					"Auto Trade",
					this.merchantGui);
			}
		}
		else
		{
			this.previousPageButton = new PageButton(this.lastUnusedButtonId++,
				merchantGuiLeftSide + merchantGuiWidth + offsetFromMainPanel,
				merchantGuiTopSide + offsetFromMainPanel,
				false)
			{
				@Override
				public void onClick(double mouseX, double mouseY)
				{
					ButtonPanel.this.currentPageIndex--;
				}
			};
			this.nextPageButton = new PageButton(this.lastUnusedButtonId++,
				merchantGuiLeftSide + merchantGuiWidth + offsetFromMainPanel + pageIndexingWidth - 10,
				merchantGuiTopSide + offsetFromMainPanel,
				true)
			{
				@Override
				public void onClick(double mouseX, double mouseY)
				{
					ButtonPanel.this.currentPageIndex++;
				}
			};

			this.sellAllCheckbox = new CheckBoxButton(this.lastUnusedButtonId++,
				merchantGuiLeftSide + merchantGuiWidth + offsetFromMainPanel,
				merchantGuiTopSide + offsetFromMainPanel + pageIndexingHeight + 2,
				"Sell All Items",
				Config.isSellAll())
			{
				@Override
				public void onClick(double mouseX, double mouseY)
				{
					super.onClick(mouseX, mouseY);
					this.processButton();
				}
			};

			// TODO FIX THIS
			this.autoTradeButton = new AutoTradeButton(this.lastUnusedButtonId++,
				merchantGuiLeftSide + merchantGuiWidth + offsetFromMainPanel,
				merchantGuiTopSide + offsetFromMainPanel + pageIndexingHeight + 20,
				11,
				11,
				"Auto Trade",
				this.merchantGui);
		}

		this.merchantGui.addGuiButton(this.previousPageButton);
		this.merchantGui.addGuiButton(this.nextPageButton);
		this.merchantGui.addGuiButton(this.sellAllCheckbox);
		this.merchantGui.addGuiButton(this.autoTradeButton);

		this.sellAllCheckbox.visible = true;

		this.validateButtonType();

		// Validate column count.
		int maxColumnCount;

		if (this.buttonType == ButtonType.COMPACT_BUTTON)
		{
			maxColumnCount = this.width / 58;

			if (maxColumnCount == 1 && side == SideType.RIGHT_SIDE)
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
		this.buttonType = Config.getButtonType();

		if (this.width < 58 || this.height < 20)
		{
			this.buttonType = null;

			// TODO: Necessary to notify user about current issue.

			// Cannot create button panel. Buttons are larger then available space.
			this.showButtons = false;
		}
		else if (this.width < 80)
		{
			// TODO: Necessary to notify user why button type is changed.

			this.buttonType = ButtonType.COMPACT_BUTTON;
		}
		else if (buttonType == ButtonType.ORIGINAL_BUTTON_NO_ENCHANT && this.width < 90)
		{
			// TODO: Necessary to notify user why button type is changed.

			this.buttonType = ButtonType.ORIGINAL_BUTTON;
		}
	}


// ---------------------------------------------------------------------
// Section: Setters
// ---------------------------------------------------------------------


	/**
	 * This method sets button type;
	 * @param buttonType Integer that represents type of button that should be used in current panel.
	 * ButtonType.ORIGINAL_BUTTON_NO_ENCHANT - Old button design.
	 * ButtonType.ORIGINAL_BUTTON - Old button design without enchant name.
	 * ButtonType.COMPACT_BUTTON - New button design.
	 */
	public void setButtonType(ButtonType buttonType)
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
			int buttonWidth = this.buttonType == ButtonType.COMPACT_BUTTON ? 58 : 80;

			// process paging
			this.elementsPerPage = this.maxColumnCount * (int) Math.floor((double) this.height / 20);

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

					if (this.buttonType == ButtonType.COMPACT_BUTTON)
					{
						newRecipeButton = new RecipeButton(this.lastUnusedButtonId,
							x,
							y,
							buttonWidth,
							buttonHigh,
							index,
							this.merchantGui)
						{
							@Override
							public void onClick(double mouseX, double mouseY)
							{
								ButtonPanel.this.merchantGui.startRecipeTrading(ButtonPanel.this.getRecipeFromButton(this));
							}
						};
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
							this.buttonType == ButtonType.ORIGINAL_BUTTON_NO_ENCHANT)
						{
							@Override
							public void onClick(double mouseX, double mouseY)
							{
								ButtonPanel.this.merchantGui.startRecipeTrading(ButtonPanel.this.getRecipeFromButton(this));
							}
						};
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
			RenderHelper.enableGUIStandardItemLighting();

			if (this.tradingButtons.size() <= this.elementsPerPage)
			{
				// Single page. Easy.

				for (IRecipeButton button : this.tradingButtons)
				{
					button.render(mouseX, mouseY, partialTicks);
				}

				for (IRecipeButton button : this.tradingButtons)
				{
					button.drawTooltips(mouseX, mouseY);
				}

				this.nextPageButton.visible = false;
				this.previousPageButton.visible = false;

				// Push up sellAllCheckBox, as index buttons are not visible.
				if (Config.getPanelSide() == SideType.TOP_SIDE)
				{
					this.sellAllCheckbox.y = this.merchantGui.getGuiTop() +
						Config.getOffsetFromGUI();
				}
			}
			else
			{
				// Per page.

				int firstElement = this.currentPageIndex * this.elementsPerPage;

				for (int index = 0; index < this.tradingButtons.size(); index++)
				{
					GuiButton button = this.tradingButtons.get(index);

					button.visible = index >= firstElement && index < firstElement + this.elementsPerPage;
					button.render(mouseX, mouseY, partialTicks);
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

				if (Config.getPanelSide() == SideType.TOP_SIDE &&
					this.sellAllCheckbox.y <
						(this.nextPageButton.y + this.nextPageButton.height))
				{
					this.sellAllCheckbox.y =
						this.nextPageButton.y + this.nextPageButton.height + 2;
				}

				this.nextPageButton.visible = true;
				this.previousPageButton.visible = true;
			}

			this.previousPageButton.render(mouseX, mouseY, partialTicks);
			this.nextPageButton.render(mouseX, mouseY, partialTicks);
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
	 * This method returns if sellAllCheckbox isChecked.
	 * @return
	 * 		<code>true</code> if sellAllCheckbox is checked.
	 * 		<code>false</code> if sellAllCheckbox is not checked.
	 */
	public boolean isSellAllChecked()
	{
		return this.sellAllCheckbox.enabled && this.sellAllCheckbox.isChecked();
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
	private ButtonType buttonType;

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

	/**
	 * This button allows to use recipe till all items are sold or recipe is not available.
	 */
	private CheckBoxButton sellAllCheckbox;

	/**
	 * This button allows to detect all tradable items in inventory and sell them all.
	 * ITEM->EMERALD
	 */
	private AutoTradeButton autoTradeButton;
}
