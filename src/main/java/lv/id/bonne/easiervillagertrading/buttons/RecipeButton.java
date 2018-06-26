package lv.id.bonne.easiervillagertrading.buttons;

import de.guntram.mcmod.easiervillagertrading.EasierVillagerTrading;
import lv.id.bonne.easiervillagertrading.ImprovedGuiMerchant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeButton extends GuiButton
{
	/**
	 *
	 * @param buttonId
	 * @param x
	 * @param y
	 * @param widthIn
	 * @param heightIn
	 * @param recipeIndex
	 * @param merchantGui
	 */
	public RecipeButton(int buttonId,
						int x,
						int y,
						int widthIn,
						int heightIn,
						int recipeIndex,
						ImprovedGuiMerchant merchantGui)
	{
		super(buttonId, x, y, widthIn, heightIn, "");
		this.recipeIndex = recipeIndex;
		this.merchantGui = merchantGui;
	}


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

			int x = this.x + 2;
			int y = this.y + 1;
			GlStateManager.pushMatrix();

			RenderItem itemRender = this.merchantGui.getItemRender();
			FontRenderer fontRender = this.merchantGui.getFontRender();

			this.renderItem(recipe.getItemToBuy(), itemRender, fontRender, x, y);
			x += SPACING_DISTANCE;
			this.renderItem(recipe.getSecondItemToBuy(), itemRender, fontRender, x, y);
			x += SPACING_DISTANCE;
			this.renderItem(recipe.getItemToSell(), itemRender, fontRender, x, y);

			GlStateManager.popMatrix();

			//IMPORTANT: without this, any button with transparent item (glass) well have messed up shading
			RenderHelper.enableGUIStandardItemLighting();

			if (recipe.isRecipeDisabled())
			{
				minecraft.getTextureManager().bindTexture(TRADE_REDX);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableLighting();

				// real physical texture is 30x30 pixels, and we just reduce it a touch
				int sizeOfX = 30 / 2;

				Gui.drawModalRectWithCustomSizedTexture(x - 18,
						y + 1,
						0,
						0,
						sizeOfX,
						sizeOfX,
						sizeOfX,
						sizeOfX);
			}
		}
	}


	private void renderItem(ItemStack itemStack, RenderItem itemRender, FontRenderer fontRender, int x, int y)
	{
		if (itemStack != null)
		{
			itemRender.renderItemAndEffectIntoGUI(itemStack, x, y);
			itemRender.renderItemOverlays(fontRender, itemStack, x, y);
		}
	}


	//------------------------------------------------------------------------------------------------------------------
	// Public getters
	//------------------------------------------------------------------------------------------------------------------


	/**
	 * Return recipe index for current button
	 * @return Recipe index.
	 */
	public int getRecipeIndex()
	{
		return this.recipeIndex;
	}


	/**
	 * String list with possible tooltips.
	 * @return
	 */
	public List<String> getTooltips()
	{
		List<String> toolTips = new ArrayList<String>();

		MerchantRecipe recipe = this.merchantGui.getMerchantRecipe(this.recipeIndex);

		if (recipe != null)
		{
			if (recipe.isRecipeDisabled())
			{
				toolTips.add("deprecated");
			}
			else
			{
				toolTips.add("tooltip");
			}

			if (recipe.getItemToSell() != null)
			{
				// && recipe.getItemToSell().getItem() == Items.ENCHANTED_BOOK

				Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(recipe.getItemToSell());

				for (Map.Entry<Enchantment, Integer> entry : map.entrySet())
				{
					toolTips.add(entry.getKey().getTranslatedName(entry.getValue()));
				}
			}
		}

		return toolTips;
	}


	//------------------------------------------------------------------------------------------------------------------
	// Variables
	//------------------------------------------------------------------------------------------------------------------


	private int recipeIndex;

	private ImprovedGuiMerchant merchantGui;


	//------------------------------------------------------------------------------------------------------------------
	// Constants
	//------------------------------------------------------------------------------------------------------------------


	public final static int ROW_COUNT = 5;

	public final static int SPACING_DISTANCE = 18;

	public static final ResourceLocation TRADE_REDX = new ResourceLocation(EasierVillagerTrading.MODID, "textures/cross.png");
}
