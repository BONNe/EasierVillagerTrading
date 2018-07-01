/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lv.id.bonne.easiervillagertrading;

import de.guntram.mcmod.easiervillagertrading.ConfigurationHandler;
import lv.id.bonne.easiervillagertrading.buttons.CheckBoxButton;
import lv.id.bonne.easiervillagertrading.buttons.IRecipeButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

import java.io.IOException;


/**
 * This is new Merchant Gui that contains custom panel with all Merchant Recipes.
 */
public class ImprovedGuiMerchant extends GuiMerchant
{
	/**
	 * {@inheritDoc}
	 */
    public ImprovedGuiMerchant(InventoryPlayer inv, GuiMerchant template, World world)
    {
        super(inv, template.getMerchant(), world);
    }


// ---------------------------------------------------------------------
// Section: Overrided methods
// ---------------------------------------------------------------------


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initGui()
	{
		super.initGui();

		// Set up as local variables to avoid list getters.
		this.nextRecipeButton = this.buttonList.get(0);
		this.previousRecipeButton = this.buttonList.get(1);

		this.buttonPanel = new ButtonPanel(this);

		if (ConfigurationHandler.showLeft())
		{
			this.sellAllCheckbox = new CheckBoxButton(991,
				this.getGuiLeft() - 20,
				this.guiTop - 15, "Sell All Items",
				ConfigurationHandler.isDefaultSellAll());
		}
		else
		{
			this.sellAllCheckbox = new CheckBoxButton(991,
				this.getGuiLeft() + this.getXSize(),
				this.guiTop - 15, "Sell All Items",
				ConfigurationHandler.isDefaultSellAll());
		}

		this.addButton(this.sellAllCheckbox);
		this.sellAllCheckbox.enabled = true;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		this.buttonPanel.validateTradingButtons();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void actionPerformed(GuiButton guiButton) throws IOException
	{
		if (this.nextRecipeButton == guiButton || this.previousRecipeButton == guiButton)
		{
			// Original buttons. Not overriding.
			super.actionPerformed(guiButton);
		}
		else
		{
			// All other buttons.

			if (guiButton == this.sellAllCheckbox)
			{
				this.sellAllCheckbox.processButton();
			}
			else
			{
				this.processRecipeTrading(this.buttonPanel.getRecipeFromButton(guiButton));
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

		this.buttonPanel.drawAllButtons(mouseX, mouseY, partialTicks);
	}


// ---------------------------------------------------------------------
// Section: Process Trading operation
// ---------------------------------------------------------------------


	/**
	 * This method process trading. It selects correct recipe (by recipe index) and
	 * uses it, if it is possible.
	 * @param recipeIndex Index of recipe that should be used.
	 * @throws IOException Exception.
	 */
	private void processRecipeTrading(int recipeIndex) throws IOException
	{
		MerchantRecipeList trades = this.getRecipes();

		if (recipeIndex < 0 || trades == null)
		{
			// nothing to process.
			return;
		}

		int numTrades = trades.size();

		if (recipeIndex < numTrades)
		{
			for (int i = 0; i < numTrades; i++)
			{
				this.actionPerformed(this.previousRecipeButton);
			}

			for (int i = 0; i < recipeIndex; i++)
			{
				this.actionPerformed(this.nextRecipeButton);
			}

			MerchantRecipe recipe = trades.get(recipeIndex);

			if (this.sellAllCheckbox.isChecked())
			{
				while (!recipe.isRecipeDisabled() &&
					this.inputSlotsAreEmpty() &&
					this.hasEnoughItemsInInventory(recipe) &&
					this.canReceiveOutput(recipe.getItemToSell()))
				{
					this.transact(recipe);
				}
			}
			else
			{
				if (!recipe.isRecipeDisabled() &&
					this.inputSlotsAreEmpty() &&
					this.hasEnoughItemsInInventory(recipe) &&
					this.canReceiveOutput(recipe.getItemToSell()))
				{
					this.transact(recipe);
				}
			}
		}
	}


	/**
	 * This method returns if all MerchantGui input slots are empty.
	 * @return
	 * 		<code>true</code> if all slots is empty.
	 * 		<code>false</code> if at least one slot is not empty.
	 */
	public boolean inputSlotsAreEmpty()
	{
		return !this.inventorySlots.getSlot(0).getHasStack() &&
			!this.inventorySlots.getSlot(1).getHasStack() &&
			!this.inventorySlots.getSlot(2).getHasStack();
	}


	/**
	 * This method returns if user has enough items in inventory for given recipe.
	 * @param recipe Recipe that contains information about necessary items.
	 * @return
	 * 		<code>true</code> if user has enough items to use current recipe.
	 * 		<code>false</code> if user has not enough items to use current recipe.
	 */
	public boolean hasEnoughItemsInInventory(MerchantRecipe recipe)
	{
		if (!this.hasEnoughItemsInInventory(recipe.getItemToBuy()))
		{
			return false;
		}

		return !recipe.hasSecondItemToBuy() ||
			this.hasEnoughItemsInInventory(recipe.getSecondItemToBuy());
	}


	/**
	 * This method iterates through player inventory slots and search given item. It
	 * returns if user has enough given item.
	 * @param stack ItemStack that contains necessary item and how much of it is needed.
	 * @return
	 * 		<code>true</code> if user has necessary count of input item.
	 * 		<code>false</code> if item is missing or necessary count is not in inventory.
	 */
	private boolean hasEnoughItemsInInventory(ItemStack stack)
	{
		int remaining = stack.getCount();

		for (int i = this.inventorySlots.inventorySlots.size() - 36;
			i < this.inventorySlots.inventorySlots.size();
			i++)
		{
			ItemStack invstack = this.inventorySlots.getSlot(i).getStack();

			if (invstack == null)
			{
				continue;
			}

			if (this.areItemStacksMergable(stack, invstack))
			{
				remaining -= invstack.getCount();
			}

			if (remaining <= 0)
			{
				return true;
			}
		}

		return false;
	}


	/**
	 * This method returns if two of given items are equal and stackable.
	 * @param a First item.
	 * @param b Second item.
	 * @return
	 * 		<code>true</code> if two given ItemStack are equal and stackable.
	 * 		<code>false</code> if ItemStacks are not equal or sackable.
	 */
	private boolean areItemStacksMergable(ItemStack a, ItemStack b)
	{
		if (a == null || b == null)
		{
			return false;
		}

		return a.getItem() == b.getItem()
			&& (!a.getHasSubtypes() || a.getItemDamage() == b.getItemDamage())
			&& ItemStack.areItemStackTagsEqual(a, b);
	}


	/**
	 * This method returns if user has enough space in inventory to collect trading
	 * output.
	 * @param stack ItemStack that contains output Item and it's count.
	 * @return
	 * 		<code>true</code> if user's inventory has space for trading output.
	 * 		<code>false</code> if user's inventory cannot accept trading output.
	 */
	public boolean canReceiveOutput(ItemStack stack)
	{
		int remaining = stack.getCount();

		for (int i = this.inventorySlots.inventorySlots.size() - 36;
			i < this.inventorySlots.inventorySlots.size();
			i++)
		{
			ItemStack invstack = this.inventorySlots.getSlot(i).getStack();

			if (invstack == null || invstack.isEmpty())
			{
				// Empty slot
				return true;
			}

			if (this.areItemStacksMergable(stack, invstack) &&
				stack.getMaxStackSize() >= stack.getCount() + invstack.getCount())
			{
				remaining -= (invstack.getMaxStackSize() - invstack.getCount());
			}

			if (remaining <= 0)
			{
				return true;
			}
		}

		return false;
	}


	/**
	 * This method process given recipe trading. It fill and click necessary slots.
	 * @param recipe MerchantRecipe that must be processed.
	 */
	private void transact(MerchantRecipe recipe)
	{
		int putBack0 = this.fillSlot(0, recipe.getItemToBuy());
		int putBack1 = recipe.hasSecondItemToBuy() ?
			this.fillSlot(1, recipe.getSecondItemToBuy()) : -1;

		// Collect result.
		this.getSlot(2, recipe.getItemToSell(), putBack0, putBack1);

		// Empty first slot.
		if (putBack0 != -1)
		{
			this.slotClick(0);
			this.slotClick(putBack0);
		}

		// Empty second slot.
		if (putBack1 != -1)
		{
			this.slotClick(1);
			this.slotClick(putBack1);
		}
	}


	/**
	 * This method places given ItemStack from trading inventory into user's inventory.
	 * @param slot  - the number of the (trading) slot that should receive items
	 * @param stack - what the trading slot should receive
	 * @return the number of the inventory slot into which these items should be put back
	 * after the transaction. May be -1 if nothing needs to be put back.
	 */
	private int fillSlot(int slot, ItemStack stack)
	{
		int remaining = stack.getCount();

		for (int i = this.inventorySlots.inventorySlots.size() - 36;
			i < this.inventorySlots.inventorySlots.size();
			i++)
		{
			ItemStack invstack = this.inventorySlots.getSlot(i).getStack();

			if (invstack == null)
			{
				continue;
			}

			boolean needPutBack = false;

			if (this.areItemStacksMergable(stack, invstack))
			{
				if (stack.getCount() + invstack.getCount() > stack.getMaxStackSize())
				{
					needPutBack = true;
				}

				remaining -= invstack.getCount();

				this.slotClick(i);
				this.slotClick(slot);
			}

			if (needPutBack)
			{
				this.slotClick(i);
			}

			if (remaining <= 0)
			{
				return remaining < 0 ? i : -1;
			}
		}

		// We should not be able to arrive here, since hasEnoughItemsInInventory should
		// have been called before fillSlot. But if we do, something went wrong; in this
		// case better do a bit less.

		return -1;
	}


	/**
	 * @param slot
	 * @param stack
	 * @param forbidden
	 */
	private void getSlot(int slot, ItemStack stack, int... forbidden)
	{
		int remaining = stack.getCount();
		this.slotClick(slot);

		for (int i = this.inventorySlots.inventorySlots.size() - 36;
			i < this.inventorySlots.inventorySlots.size();
			i++)
		{
			ItemStack invstack = this.inventorySlots.getSlot(i).getStack();

			if (invstack == null || invstack.isEmpty())
			{
				continue;
			}

			if (this.areItemStacksMergable(stack, invstack) &&
				invstack.getCount() < invstack.getMaxStackSize())
			{
				remaining -= (invstack.getMaxStackSize() - invstack.getCount());
				this.slotClick(i);
			}

			if (remaining <= 0)
			{
				return;
			}
		}

		// When looking for an empty slot, don't take one that we want to put some input
		// back to.

		for (int i = this.inventorySlots.inventorySlots.size() - 36;
			i < this.inventorySlots.inventorySlots.size();
			i++)
		{
			boolean isForbidden = false;

			for (int f : forbidden)
			{
				if (i == f)
				{
					isForbidden = true;
				}
			}

			if (isForbidden)
			{
				continue;
			}

			ItemStack invstack = this.inventorySlots.getSlot(i).getStack();

			if (invstack == null || invstack.isEmpty())
			{
				this.slotClick(i);

				return;
			}
		}
	}


	/**
	 * This method process slot clicking operation.
	 * @param slot Integer that represents slot in inventory that must be clicked.
	 */
	private void slotClick(int slot)
	{
		this.mc.playerController.windowClick(this.mc.player.openContainer.windowId,
			slot,
			0,
			ClickType.PICKUP,
			this.mc.player);
	}


// ---------------------------------------------------------------------
// Section: Getters
// ---------------------------------------------------------------------

	/**
	 * This method returns current gui item render.
	 * @return RenderItem object for current Gui.
	 */
	public RenderItem getItemRender()
	{
		return this.itemRender;
	}


	/**
	 * This method returns current gui font render.
	 * @return FontRenderer object for current Gui.
	 */
	public FontRenderer getFontRender()
	{
		return this.fontRenderer;
	}


	/**
	 * This method returns recipes from current merchant.
	 * @return MerchantRecipeList object that contains all recipes for current merchant.
	 */
	public MerchantRecipeList getRecipes()
	{
		return this.getMerchant().getRecipes(null);
	}


	/**
	 * This method returns recipe with given recipe index.
	 * @param recipeIndex Index of recipe that should be returned.
	 * @return MerchantRecipe from MerchantRecipes or null, if recipe does not exist.
	 */
	public MerchantRecipe getMerchantRecipe(int recipeIndex)
	{
		MerchantRecipeList merchantRecipeList = this.getRecipes();

		if (merchantRecipeList == null ||
			merchantRecipeList.isEmpty() ||
			merchantRecipeList.size() <= recipeIndex)
		{
			// Recipe not found.
			return null;
		}

		return merchantRecipeList.get(recipeIndex);
	}


// ---------------------------------------------------------------------
// Section: Other methods
// ---------------------------------------------------------------------


	/**
	 * This method adds given IRecipeButton to buttonList.
	 * @param button New Recipe Button.
	 */
	public void addGuiButton(GuiButton button)
	{
		this.addButton(button);
	}


	/**
	 * This method removes given IRecipeButton to buttonList.
	 * @param button Recipe Button that must be removed.
	 */
	public void removeButton(GuiButton button)
	{
		this.buttonList.remove(button);
	}


	/**
	 * This method calls toolTip rendering for given item stack in given X and Y position.
	 * @param itemStack ItemStack which tooltip must be rendered.
	 * @param x Tooltip X position.
	 * @param y Tooltip Y position.
	 */
	public void callToolTipRendering(ItemStack itemStack, int x, int y)
	{
		this.renderToolTip(itemStack, x, y);
	}


 // ---------------------------------------------------------------------
 // Section: Variables
 // ---------------------------------------------------------------------


	/**
	 * This is original Merchant Gui button.
	 */
	private GuiButton nextRecipeButton;

	/**
	 * This is original Merchant Gui button
	 */
	private GuiButton previousRecipeButton;


	private CheckBoxButton sellAllCheckbox;

	/**
	 * Panel with recipe buttons.
	 */
    private ButtonPanel buttonPanel;
}
