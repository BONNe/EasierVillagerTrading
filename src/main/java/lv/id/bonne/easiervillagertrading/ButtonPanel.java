package lv.id.bonne.easiervillagertrading;


import java.util.ArrayList;
import java.util.List;

import lv.id.bonne.easiervillagertrading.buttons.IRecipeButton;
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
	 * @param x X start Position.
	 * @param y Y start Position.
	 * @param width Panel width.
	 * @param height Panel High.
	 */
	public ButtonPanel(ImprovedGuiMerchant merchantGui, int x, int y, int width, int height)
	{
		this.merchantGui = merchantGui;

		this.lastUnusedButtonId = 50;
		this.tradingButtons = new ArrayList<>();

		this.useOldDesign = false;

		if (this.useOldDesign)
		{
			this.maxColumnCount = 1;
		}
		else
		{
			this.maxColumnCount = 4;
		}

		// Position ?

		this.xPosition = x;
		this.yPosition = y;

		this.width = width;
		this.height = height;
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

		if (merchantRecipes != null)
		{
			final int size = merchantRecipes.size();

			int btnH = 20, btnW = 56;
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
					x = this.xPosition + currCol * btnW;
					y = this.yPosition + currRow * btnH;

					IRecipeButton newRecipeButton;

					if (this.useOldDesign)
					{
						newRecipeButton = new RecipeTextButton(this.lastUnusedButtonId,
							x,
							y,
							btnW,
							btnH,
							i,
							this.merchantGui);
					}
					else
					{
						newRecipeButton = new RecipeButton(this.lastUnusedButtonId,
							x,
							y,
							btnW,
							btnH,
							i,
							this.merchantGui);
					}


					this.merchantGui.addRecipeButton(newRecipeButton);
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
		for (IRecipeButton button : this.tradingButtons)
		{
			button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks);
		}

		for (IRecipeButton button : this.tradingButtons)
		{
			button.drawTooltips(mouseX, mouseY);
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
	 * Just an integer that allows to init unique buttons.
	 */
	private int lastUnusedButtonId;

	/**
	 * List with Recipe buttons.
	 */
	private List<IRecipeButton> tradingButtons;

	/**
	 * This boolean holds which button design should be used in current panel.
	 */
	private final boolean useOldDesign;

	/**
	 * ImprovedGuiMerchant.
	 */
	private final ImprovedGuiMerchant merchantGui;

	/**
	 * This variable holds how much columns can be used.
	 */
	private final int maxColumnCount;

	/**
	 * This variable holds how wide current panel can be.
	 */
	private final int width;

	/**
	 * This variable holds how high current panel can be.
	 */
	private final int height;

	/**
	 * This variable holds X position where panel starts.
	 */
	private final int xPosition;

	/**
	 * This variable holds Y position where panel starts.
	 */
	private final int yPosition;
}
