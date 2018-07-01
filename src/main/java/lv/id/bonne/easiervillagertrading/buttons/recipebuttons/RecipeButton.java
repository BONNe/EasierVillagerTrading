package lv.id.bonne.easiervillagertrading.buttons.recipebuttons;

import de.guntram.mcmod.easiervillagertrading.EasierVillagerTrading;
import lv.id.bonne.easiervillagertrading.ImprovedGuiMerchant;
import lv.id.bonne.easiervillagertrading.buttons.IRecipeButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;

/**
 * This Class creates RecipeButton that is like standard Minecraft button but with ItemStacks on it.
 */
public class RecipeButton extends IRecipeButton
{
	public RecipeButton(int buttonId,
		int x,
		int y,
		int widthIn,
		int heightIn,
		int recipeIndex,
		ImprovedGuiMerchant merchantGui)
	{
		super(buttonId, x, y, widthIn, heightIn, recipeIndex, merchantGui, 2, 20, 38);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawTooltips(int mouseX, int mouseY)
	{
		MerchantRecipe recipe = this.merchantGui.getMerchantRecipe(this.recipeIndex);

		// TODO: Performance can be improved if do not check buttons that is outside MouseX and MouseY.

		if (recipe != null)
		{
			this.drawTooltip(recipe.getItemToBuy(),
				this.x + this.firstBuyItemOffset,
				this.y + 2,
				mouseX,
				mouseY);

			if (recipe.hasSecondItemToBuy())
			{
				this.drawTooltip(recipe.getSecondItemToBuy(),
					this.x + this.secondBuyItemOffset,
					this.y + 2,
					mouseX,
					mouseY);
			}

			this.drawTooltip(recipe.getItemToSell(),
				this.x + this.sellItemOffset,
				this.y + 2,
				mouseX,
				mouseY);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float p)
	{
		super.drawButton(minecraft, mouseX, mouseY, p);

		if (this.visible)
		{
			MerchantRecipe recipe = this.merchantGui.getMerchantRecipe(this.recipeIndex);

			if (recipe == null)
			{
				// Recipe not found. Do not draw it.

				this.visible = false;
				return;
			}

			GlStateManager.pushMatrix();

			RenderItem itemRender = this.merchantGui.getItemRender();
			FontRenderer fontRender = this.merchantGui.getFontRender();

			this.renderItem(recipe.getItemToBuy(),
				itemRender,
				fontRender,
				this.x + this.firstBuyItemOffset,
				this.y + 2);

			if (recipe.hasSecondItemToBuy())
			{
				this.renderItem(recipe.getSecondItemToBuy(),
					itemRender,
					fontRender,
					this.x + this.secondBuyItemOffset,
					this.y + 2);
			}

			this.renderItem(recipe.getItemToSell(),
				itemRender,
				fontRender,
				this.x + this.sellItemOffset,
				this.y + 2);

			GlStateManager.popMatrix();

			//IMPORTANT: without this, any button with transparent item (glass) well have messed up shading
			RenderHelper.enableGUIStandardItemLighting();

			if (recipe.isRecipeDisabled())
			{
				minecraft.getTextureManager().bindTexture(DISABLE_RECIPE);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableLighting();

				// real physical texture is 30x30 pixels, and we just reduce it a touch
				int sizeOfX = 30 / 2;

				Gui.drawModalRectWithCustomSizedTexture(this.x + this.secondBuyItemOffset,
					this.y + 2,
					0,
					0,
					sizeOfX,
					sizeOfX,
					sizeOfX,
					sizeOfX);
			}
		}
	}


// ---------------------------------------------------------------------
// Section: Constants
// ---------------------------------------------------------------------


	private static final ResourceLocation DISABLE_RECIPE =
		new ResourceLocation(EasierVillagerTrading.MODID, "textures/cross.png");
}
