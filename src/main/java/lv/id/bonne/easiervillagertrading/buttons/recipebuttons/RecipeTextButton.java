package lv.id.bonne.easiervillagertrading.buttons.recipebuttons;

import de.guntram.mcmod.easiervillagertrading.EasierVillagerTrading;
import lv.id.bonne.easiervillagertrading.ImprovedGuiMerchant;
import lv.id.bonne.easiervillagertrading.buttons.IRecipeButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;


/**
 * This is Button that is design like original EasierVillagerTrading button.
 */
public class RecipeTextButton  extends IRecipeButton
{
	/**
	 * {@inheritDoc}
	 */
	public RecipeTextButton(int buttonId,
		int x,
		int y,
		int widthIn,
		int heightIn,
		int recipeIndex,
		ImprovedGuiMerchant merchantGui,
		boolean showEnchants)
	{
		super(buttonId, x, y, widthIn, heightIn, recipeIndex, merchantGui, 0, 18, 60);

		this.okOrNotOffset = 40;
		this.textOffset = 85;

		this.showEnchants = showEnchants;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawTooltips(int mouseX, int mouseY)
	{
		MerchantRecipe recipe = this.merchantGui.getMerchantRecipe(this.recipeIndex);

		if (recipe != null && this.visible)
		{
			this.drawTooltip(recipe.getItemToBuy(),
				this.x + this.firstBuyItemOffset,
				this.y,
				mouseX,
				mouseY);

			if (recipe.hasSecondItemToBuy())
			{
				this.drawTooltip(recipe.getSecondItemToBuy(),
					this.x + this.secondBuyItemOffset,
					this.y,
					mouseX,
					mouseY);
			}

			this.drawTooltip(recipe.getItemToSell(),
				this.x + this.sellItemOffset,
				this.y,
				mouseX,
				mouseY);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(int mouseX, int mouseY, float p)
	{
		if (this.visible)
		{
			RenderHelper.enableGUIStandardItemLighting();

			MerchantRecipe recipe = this.merchantGui.getMerchantRecipe(this.recipeIndex);

			this.renderItem(recipe.getItemToBuy(),
				this.merchantGui.getItemRender(),
				this.merchantGui.getFontRender(),
				this.x + this.firstBuyItemOffset,
				this.y);

			if (recipe.hasSecondItemToBuy())
			{
				this.renderItem(recipe.getSecondItemToBuy(),
					this.merchantGui.getItemRender(),
					this.merchantGui.getFontRender(),
					this.x + this.secondBuyItemOffset,
					this.y);
			}

			ItemStack sellItem = recipe.getItemToSell();

			this.renderItem(sellItem,
				this.merchantGui.getItemRender(),
				this.merchantGui.getFontRender(),
				this.x + this.sellItemOffset,
				this.y);

			if (this.showEnchants)
			{
				NBTTagList enchantments;

				if (sellItem.getItem() instanceof ItemEnchantedBook)
				{
					enchantments = ItemEnchantedBook.getEnchantments(sellItem);
				}
				else
				{
					enchantments = sellItem.getEnchantmentTagList();
				}

				if (enchantments != null)
				{
					StringBuilder enchants = new StringBuilder();

					for (int index = 0; index < enchantments.size(); ++index)
					{
						int enchantID = enchantments.getCompound(index).getShort("id");
						int enchantLevel = enchantments.getCompound(index).getShort("lvl");

						Enchantment enchant = Enchantment.getEnchantmentByID(enchantID);

						if (enchant != null)
						{
							if (index > 0)
							{
								enchants.append(", ");
							}

							enchants.append(enchant.getName() + ":" + enchantLevel);
						}
					}

					String shownEnchants = enchants.toString();

					if (this.x < 0)
					{
						// TODO: This must be reworked.

						shownEnchants = this.merchantGui.getFontRender().trimStringToWidth(
							shownEnchants,
							-this.x - this.textOffset - 5);
					}

					this.merchantGui.getFontRender().drawString(shownEnchants,
						this.x + this.textOffset,
						this.y,
						0xffff00);
				}
			}

			RenderHelper.disableStandardItemLighting();

			// needed so items don't get a text color overlay
			GlStateManager.color4f(1f, 1f, 1f, 1f);
			GlStateManager.enableBlend();

			// arrows; use standard item lighting for them so we need a separate loop
			Minecraft.getInstance().getTextureManager().bindTexture(RecipeTextButton.icons);

			if (!recipe.isRecipeDisabled() &&
				this.merchantGui.inputSlotsAreEmpty() &&
				this.merchantGui.hasEnoughItemsInInventory(recipe) &&
				this.merchantGui.canReceiveOutput(recipe.getItemToSell()))
			{
				// green arrow right
				this.drawTexturedModalRect(this.x + this.okOrNotOffset,
					this.y,
					6 * 18,
					2 * 18,
					18,
					18);
			}
			else if (!recipe.isRecipeDisabled())
			{
				// empty arrow right
				this.drawTexturedModalRect(this.x + this.okOrNotOffset,
					this.y,
					5 * 18,
					3 * 18,
					18,
					18);
			}
			else
			{
				// red X
				this.drawTexturedModalRect(this.x + this.okOrNotOffset,
					this.y,
					12 * 18,
					3 * 18,
					18,
					18);
			}
		}
	}


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------

	/**
	 * Image icon offset that shows if trading is available.
	 */
	private final int okOrNotOffset;

	/**
	 * Offset for text that shows enchantment.
	 */
	private final int textOffset;

	/**
	 * This variable holds if enchant text should be rendered.
	 */
	private final boolean showEnchants;

// ---------------------------------------------------------------------
// Section: Constants
// ---------------------------------------------------------------------


	private static final ResourceLocation icons =
		new ResourceLocation(EasierVillagerTrading.MODID, "textures/icons.png");
}
