package lv.id.bonne.easiervillagertrading.buttons;

import lv.id.bonne.easiervillagertrading.gui.ImprovedGuiMerchant;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;


/**
 * This abstract method allows to draw different types of recipe buttons in MerchantGui.
 */
public abstract class IRecipeButton extends GuiButton
{

	/**
	 * Simple constructor that inits GuiButton and sets recipeIndex and merchantGui.
	 *
	 * @param buttonId Unique button index.
	 * @param x Position on X axis.
	 * @param y Position on Y axis.
	 * @param widthIn Button width.
	 * @param heightIn Button height.
	 * @param recipeIndex Recipe Index.
	 * @param merchantGui ImprovedMerchantGui.
	 * @param firstBuyItemOffset First Item offset.
	 * @param secondBuyItemOffset Second Item offset.
	 * @param sellItemOffset Sell Item Offset.
	 */
	protected IRecipeButton(int buttonId,
		int x,
		int y,
		int widthIn,
		int heightIn,
		int recipeIndex,
		ImprovedGuiMerchant merchantGui,
		int firstBuyItemOffset,
		int secondBuyItemOffset,
		int sellItemOffset)
	{
		super(buttonId, x, y, widthIn, heightIn, "");

		this.recipeIndex = recipeIndex;
		this.merchantGui = merchantGui;

		this.firstBuyItemOffset = firstBuyItemOffset;
		this.secondBuyItemOffset = secondBuyItemOffset;
		this.sellItemOffset = sellItemOffset;
	}


	// ---------------------------------------------------------------------
	// Section: Public methods
	// ---------------------------------------------------------------------


	/**
	 * This method calls ToolTip updating for button with current mouse location.
	 *
	 * @param mouseX Mouse X location.
	 * @param mouseY Mouse Y location.
	 */
	public abstract void drawTooltips(int mouseX, int mouseY);


	/**
	 * This method returns current button recipe index.
	 *
	 * @return Integer that represents recipe ID.
	 */
	public int getRecipeIndex()
	{
		return this.recipeIndex;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		MerchantRecipe recipe = this.merchantGui.getMerchantRecipe(this.recipeIndex);

		this.enabled = !recipe.isRecipeDisabled() &&
				this.merchantGui.inputSlotsAreEmpty() &&
				this.merchantGui.hasEnoughItemsInInventory(recipe) &&
				this.merchantGui.canReceiveOutput(recipe.getItemToSell());

		super.render(mouseX, mouseY, partialTicks);
	}


	// ---------------------------------------------------------------------
	// Section: Protected methods
	// ---------------------------------------------------------------------


	/**
	 * This method draws tool tip for current ItemStack if mouseX and mouseY is over ItemStack.
	 *
	 * @param stack ItemStack which tool tip must be drawed.
	 * @param x ItemStack X position.
	 * @param y ItemStack Y position.
	 * @param mouseX Mouse X position.
	 * @param mouseY Mouse Y position.
	 */
	protected void drawTooltip(ItemStack stack, int x, int y, int mouseX, int mouseY)
	{
		if (stack != null)
		{
			// ItemStack should be drawn only if mouse is over it. ItemStack width and high is 16.

			if (mouseX >= x && mouseX <= x + ITEM_STACK_WIDTH && mouseY >= y && mouseY <= y + ITEM_STACK_HIGH)
			{
				this.merchantGui.callToolTipRendering(stack, mouseX, mouseY);
			}
		}
	}


	/**
	 * This method renders given ItemStack in given X and Y position.
	 *
	 * @param itemStack ItemStack that must be rendered.
	 * @param itemRender RenderItem object.
	 * @param fontRender FontRender object.
	 * @param x ItemStack X position.
	 * @param y ItemStack Y position.
	 */
	protected void renderItem(ItemStack itemStack,
		ItemRenderer itemRender,
		FontRenderer fontRender,
		int x,
		int y)
	{
		if (itemStack != null)
		{
			itemRender.renderItemAndEffectIntoGUI(itemStack, x, y);
			itemRender.renderItemOverlays(fontRender, itemStack, x, y);
		}
	}


	// ---------------------------------------------------------------------
	// Section: Variables
	// ---------------------------------------------------------------------

	/**
	 * This variable holds index of recipe from all merchant recipes.
	 */
	protected int recipeIndex;

	/**
	 * This variable allows to easier access to some GUI and merchant methods.
	 */
	protected ImprovedGuiMerchant merchantGui;

	/**
	 * This variable holds first buy item offset size.
	 */
	protected final int firstBuyItemOffset;

	/**
	 * This variable holds second buy item offset size.
	 */
	protected final int secondBuyItemOffset;

	/**
	 * This variable holds sell item offset size.
	 */
	protected final int sellItemOffset;


	// ---------------------------------------------------------------------
	// Section: Constants
	// ---------------------------------------------------------------------


	private static final int ITEM_STACK_WIDTH = 16;

	private static final int ITEM_STACK_HIGH = 16;

	/**
	 * Variable that holds all available buttons.
	 */
	public enum ButtonType
	{
		ORIGINAL_BUTTON,
		ORIGINAL_BUTTON_NO_ENCHANT,
		COMPACT_BUTTON
	}
}
