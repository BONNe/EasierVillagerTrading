package lv.id.bonne.easiervillagertrading;


import java.util.ArrayList;
import java.util.List;

import lv.id.bonne.easiervillagertrading.buttons.RecipeButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.village.MerchantRecipeList;


public class ButtonPanel
{
	public ButtonPanel(ImprovedGuiMerchant merchantGui, int x, int y, int width, int height)
	{
		this.merchantGui = merchantGui;
	}



	// ---------------------------------------------------------------------
	// Section: Methods
	// ---------------------------------------------------------------------


	public void validateTradingButtons()
	{
		MerchantRecipeList merchantRecipes = this.merchantGui.getRecipes();

		final int size = merchantRecipes.size();

		int btnH = 20, btnW = 56;
		int x;
		int y;

		int currRow;
		int currCol;

		for (int i = 0; i < size; i++) {
			if (i >= this.tradingButtons.size()) {
				currRow = i / this.btnColCount;
				currCol = i % this.btnColCount;
				x = this.x + currCol * btnW;
				y = this.y + currRow * btnH;

				RecipeButton newRecipeButton =
					new RecipeButton(this.lastUnusedButtonId,
						x,
						y,
						btnW,
						btnH,
						i,
						this.merchantGui);

				this.merchantGui.addRecipeButton(newRecipeButton);
				this.tradingButtons.add(newRecipeButton);
				this.lastUnusedButtonId++;
			}
		}
	}


	public void drawAllButtons(int mouseX, int mouseY, float partialTicks)
	{
		for (RecipeButton button : this.tradingButtons)
		{
			button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks);
		}
	}


	public int getRecipeFromButton(GuiButton inputButton)
	{
		int recipeIndex = -1;

		for (RecipeButton button : this.tradingButtons)
		{
			if (button == inputButton)
			{
				// Not nice to quit loop like this, but ... :)

				return button.getRecipeIndex();
			}
		}

		return recipeIndex;
	}


	// ---------------------------------------------------------------------
	// Section: Variables
	// ---------------------------------------------------------------------

	int lastUnusedButtonId = 50;

	private int btnColCount = 4;

	int width;

	int height;

	int x;

	int y;


	private final ImprovedGuiMerchant merchantGui;

	private List<RecipeButton> tradingButtons = new ArrayList<RecipeButton>();
}
