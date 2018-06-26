/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lv.id.bonne.easiervillagertrading;

import de.guntram.mcmod.easiervillagertrading.ConfigurationHandler;
import de.guntram.mcmod.easiervillagertrading.EasierVillagerTrading;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiCheckBox;

import java.io.IOException;


public class ImprovedGuiMerchant extends GuiMerchant
{

    public ImprovedGuiMerchant(InventoryPlayer inv, GuiMerchant template, World world)
    {
        super(inv, template.getMerchant(), world);

        if (ConfigurationHandler.showLeft())
        {
            this.xBase = -ConfigurationHandler.leftPixelOffset();

            if (this.xBase == 0)
			{
				this.xBase = -this.getXSize();
			}
        }
        else
        {
            this.xBase = this.getXSize() + 5;
        }
    }


	//------------------------------------------------------------------------------------------------------------------
	// Overrided methods
	//------------------------------------------------------------------------------------------------------------------


	@Override
	public void initGui()
	{
		super.initGui();

		if (ConfigurationHandler.showLeft())
		{
			this.sellAllCheckbox = new GuiCheckBox(991,
				this.getGuiLeft() - 20,
				this.guiTop - 15, "Sell All Items",
				ConfigurationHandler.isDefaultSellAll());
		}
		else
		{
			this.sellAllCheckbox = new GuiCheckBox(991,
				this.getGuiLeft() + this.getXSize(),
				this.guiTop - 15, "Sell All Items",
				ConfigurationHandler.isDefaultSellAll());
		}

		this.addButton(this.sellAllCheckbox);
		this.sellAllCheckbox.enabled = true;
	}


	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}



	@Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
	}


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        MerchantRecipeList trades = this.getMerchant().getRecipes(null);

        if (trades == null)
        {
            return;
        }

        int topAdjust = this.getTopAdjust(trades.size());

		String s = trades.size() + " trades";
        this.fontRenderer.drawString(s, this.xBase, -topAdjust, 0xff00ff);

        // First draw all items, then all tooltips. This is extra effort,
        // but we don't want any items in front of any tooltips.

        RenderHelper.enableGUIStandardItemLighting();

        for (int i = 0; i < trades.size(); i++)
        {
            MerchantRecipe trade = trades.get(i);
            ItemStack i1 = trade.getItemToBuy();
            ItemStack i2 = trade.hasSecondItemToBuy() ? trade.getSecondItemToBuy() : null;
            ItemStack o1 = trade.getItemToSell();

			this.drawItem(i1, this.xBase + this.firstBuyItemXpos, i * this.lineHeight - topAdjust + this.titleDistance);
			this.drawItem(i2, this.xBase + this.secondBuyItemXpos, i * this.lineHeight - topAdjust + this.titleDistance);
			this.drawItem(o1, this.xBase + this.sellItemXpos, i * this.lineHeight - topAdjust + this.titleDistance);

            NBTTagList enchantments;

            if (o1.getItem() instanceof ItemEnchantedBook)
            {
                enchantments = ((ItemEnchantedBook) (o1.getItem())).getEnchantments(o1);
            }
            else
            {
                enchantments = o1.getEnchantmentTagList();
            }

            if (enchantments != null)
            {
                StringBuilder enchants = new StringBuilder();

                for (int t = 0; t < enchantments.tagCount(); ++t)
                {
                    int j = enchantments.getCompoundTagAt(t).getShort("id");
                    int k = enchantments.getCompoundTagAt(t).getShort("lvl");

                    Enchantment enchant = Enchantment.getEnchantmentByID(j);

                    if (enchant != null)
                    {
                        if (t > 0)
						{
							enchants.append(", ");
						}

                        enchants.append(enchant.getTranslatedName(k));
                    }
                }

                String shownEnchants = enchants.toString();

                if (this.xBase < 0)
                {
                    shownEnchants = this.fontRenderer.trimStringToWidth(shownEnchants, -this.xBase - this.textXpos - 5);
                }

                this.fontRenderer.drawString(shownEnchants,
                        this.xBase + this.textXpos,
                        i * this.lineHeight - topAdjust + 24,
                        0xffff00);
            }
        }

        RenderHelper.disableStandardItemLighting();

        GlStateManager.color(1f, 1f, 1f, 1f);               // needed so items don't get a text color overlay
        GlStateManager.enableBlend();
        this.mc.getTextureManager().bindTexture(ImprovedGuiMerchant.icons);         // arrows; use standard item lighting for them so we need a separate loop

        for (int i = 0; i < trades.size(); i++)
        {
            MerchantRecipe trade = trades.get(i);

            if (!trade.isRecipeDisabled() &&
				this.inputSlotsAreEmpty() &&
				this.hasEnoughItemsInInventory(trade) &&
				this.canReceiveOutput(trade.getItemToSell()))
            {
                this.drawTexturedModalRect(this.xBase + this.okNokXpos,
                        i * this.lineHeight - topAdjust + this.titleDistance,
                        6 * 18,
                        2 * 18,
                        18,
                        18);   // green arrow right
            }
            else if (!trade.isRecipeDisabled())
            {
                this.drawTexturedModalRect(this.xBase + this.okNokXpos,
                	i * this.lineHeight - topAdjust + this.titleDistance,
                	5 * 18,
                	3 * 18,
         			18,
					18);       // empty arrow right
            }
            else
            {
                this.drawTexturedModalRect(this.xBase + this.okNokXpos,
                        i * this.lineHeight - topAdjust + this.titleDistance,
                        12 * 18,
                        3 * 18,
                        18,
                        18);      // red X
            }
        }

// tooltips

        for (int i = 0; i < trades.size(); i++)
        {
            MerchantRecipe trade = trades.get(i);
            ItemStack i1 = trade.getItemToBuy();
            ItemStack i2 = trade.hasSecondItemToBuy() ? trade.getSecondItemToBuy() : null;
            ItemStack o1 = trade.getItemToSell();
			this.drawTooltip(i1, this.xBase + this.firstBuyItemXpos, i * this.lineHeight - topAdjust + this.titleDistance, mouseX, mouseY);
			this.drawTooltip(i2, this.xBase + this.secondBuyItemXpos, i * this.lineHeight - topAdjust + this.titleDistance, mouseX, mouseY);
			this.drawTooltip(o1, this.xBase + this.sellItemXpos, i * this.lineHeight - topAdjust + this.titleDistance, mouseX, mouseY);
        }
	}



	@Override
	protected void actionPerformed(GuiButton guiButton) throws IOException
	{
		super.actionPerformed(guiButton);

		if (guiButton == this.sellAllCheckbox)
		{
			ConfigurationHandler.setDefaultSellAll(this.sellAllCheckbox.isChecked());
		}
	}


	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException
	{
		MerchantRecipeList trades = this.getMerchant().getRecipes(null);

		if (trades == null)
		{
			return;
		}

		int numTrades = trades.size();
		int topAdjust = this.getTopAdjust(numTrades);

		// System.out.println("click at "+mouseX+"/"+mouseY);

		if (mouseButton == 0 &&
				(mouseX - this.guiLeft) >= this.xBase &&
				(mouseX - this.guiLeft) <= this.xBase + this.textXpos &&
				(mouseY - this.guiTop) >= -topAdjust + this.titleDistance)
		{
			int tradeIndex = (mouseY + topAdjust - this.guiTop - this.titleDistance) / this.lineHeight;

			if (tradeIndex >= 0 && tradeIndex < numTrades)
			{
				// System.out.println("tradeIndex="+tradeIndex+", numTrades="+numTrades);
				GuiButton myNextButton = this.buttonList.get(0);
				GuiButton myPrevButton = this.buttonList.get(1);

				for (int i = 0; i < numTrades; i++)
				{
					this.actionPerformed(myPrevButton);
				}

				for (int i = 0; i < tradeIndex; i++)
				{
					this.actionPerformed(myNextButton);
				}

				MerchantRecipe recipe = trades.get(tradeIndex);

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
		else
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}


	//------------------------------------------------------------------------------------------------------------------
	// Private methods
	//------------------------------------------------------------------------------------------------------------------


    private int getTopAdjust(int numTrades)
    {
        int topAdjust = ((numTrades * this.lineHeight + this.titleDistance) - this.ySize) / 2;

        if (topAdjust < 0)
        {
            topAdjust = 0;
        }

        return topAdjust;
    }


    private void drawItem(ItemStack stack, int x, int y)
    {
        if (stack == null)
        {
            return;
        }

        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlays(this.fontRenderer, stack, x, y);
    }


    private void drawTooltip(ItemStack stack, int x, int y, int mousex, int mousey)
    {
        if (stack == null)
        {
            return;
        }

        mousex -= this.guiLeft;
        mousey -= this.guiTop;

        if (mousex >= x && mousex <= x + 16 && mousey >= y && mousey <= y + 16)
        {
            this.renderToolTip(stack, mousex, mousey);
        }
    }


    private boolean inputSlotsAreEmpty()
    {
        return this.inventorySlots.getSlot(0).getHasStack() == false &&
                this.inventorySlots.getSlot(1).getHasStack() == false &&
                this.inventorySlots.getSlot(2).getHasStack() == false;

    }


    private boolean hasEnoughItemsInInventory(MerchantRecipe recipe)
    {
        if (!this.hasEnoughItemsInInventory(recipe.getItemToBuy()))
        {
            return false;
        }

        return !recipe.hasSecondItemToBuy() || this.hasEnoughItemsInInventory(recipe.getSecondItemToBuy());
    }


    private boolean hasEnoughItemsInInventory(ItemStack stack)
    {
        int remaining = stack.getCount();

        for (int i = this.inventorySlots.inventorySlots.size() - 36; i < this.inventorySlots.inventorySlots.size(); i++)
        {
            ItemStack invstack = this.inventorySlots.getSlot(i).getStack();

            if (invstack == null)
            {
                continue;
            }

            if (this.areItemStacksMergable(stack, invstack))
            {
                //System.out.println("taking "+invstack.getCount()+" items from slot # "+i);
                remaining -= invstack.getCount();
            }

            if (remaining <= 0)
            {
                return true;
            }
        }

        return false;
    }


    private boolean canReceiveOutput(ItemStack stack)
    {
        int remaining = stack.getCount();

        for (int i = this.inventorySlots.inventorySlots.size() - 36; i < this.inventorySlots.inventorySlots.size(); i++)
        {
            ItemStack invstack = this.inventorySlots.getSlot(i).getStack();

            if (invstack == null || invstack.isEmpty())
            {
                //System.out.println("can put result into empty slot "+i);
                return true;
            }

            if (this.areItemStacksMergable(stack, invstack) &&
                    stack.getMaxStackSize() >= stack.getCount() + invstack.getCount())
            {
                //System.out.println("Can merge "+(invstack.getMaxStackSize()-invstack.getCount())+" items with slot "+i);
                remaining -= (invstack.getMaxStackSize() - invstack.getCount());
            }

            if (remaining <= 0)
            {
                return true;
            }
        }
        return false;
    }


    private void transact(MerchantRecipe recipe)
    {
        //System.out.println("fill input slots called");

        int putback0, putback1 = -1;
        putback0 = this.fillSlot(0, recipe.getItemToBuy());

        if (recipe.hasSecondItemToBuy())
        {
            putback1 = this.fillSlot(1, recipe.getSecondItemToBuy());
        }

        this.getSlot(2, recipe.getItemToSell(), putback0, putback1);

        //System.out.println("putting back to slot "+putback0+" from 0, and to "+putback1+"from 1");

        if (putback0 != -1)
        {
            this.slotClick(0);
            this.slotClick(putback0);
        }

        if (putback1 != -1)
        {
            this.slotClick(1);
            this.slotClick(putback1);
        }
    }


    /**
     * @param slot  - the number of the (trading) slot that should receive items
     * @param stack - what the trading slot should receive
     * @return the number of the inventory slot into which these items should be put back
     * after the transaction. May be -1 if nothing needs to be put back.
     */
    private int fillSlot(int slot, ItemStack stack)
    {
        int remaining = stack.getCount();
        for (int i = this.inventorySlots.inventorySlots.size() - 36; i < this.inventorySlots.inventorySlots.size(); i++)
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

                // System.out.println("taking "+invstack.getCount()+" items from slot # "+i+", remaining is now "+remaining);

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
        // We should not be able to arrive here, since hasEnoughItemsInInventory should have been
        // called before fillSlot. But if we do, something went wrong; in this case better do a bit less.

        return -1;
    }


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


    private void getSlot(int slot, ItemStack stack, int... forbidden)
    {
        int remaining = stack.getCount();
        this.slotClick(slot);

        for (int i = this.inventorySlots.inventorySlots.size() - 36; i < this.inventorySlots.inventorySlots.size(); i++)
        {
            ItemStack invstack = this.inventorySlots.getSlot(i).getStack();

            if (invstack == null || invstack.isEmpty())
            {
                continue;
            }

            if (this.areItemStacksMergable(stack, invstack) && invstack.getCount() < invstack.getMaxStackSize())
            {
                // System.out.println("Can merge "+(invstack.getMaxStackSize()-invstack.getCount())+" items with slot "+i);

                remaining -= (invstack.getMaxStackSize() - invstack.getCount());
                this.slotClick(i);
            }

            if (remaining <= 0)
            {
                return;
            }
        }

        // When looking for an empty slot, don't take one that we want to put some input back to.
        for (int i = this.inventorySlots.inventorySlots.size() - 36; i < this.inventorySlots.inventorySlots.size(); i++)
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
                // System.out.println("putting result into empty slot "+i);
                return;
            }
        }
    }


    private void slotClick(int slot)
    {
		this.mc.playerController.windowClick(this.mc.player.openContainer.windowId,
				slot,
				0,
				ClickType.PICKUP,
				this.mc.player);
	}


	//------------------------------------------------------------------------------------------------------------------
	// Public Methods
	//------------------------------------------------------------------------------------------------------------------


	public MerchantRecipe getMerchantRecipe(int recipeIndex)
	{
		MerchantRecipeList merchantRecipeList = this.getMerchant().getRecipes(null);

		if (merchantRecipeList == null || merchantRecipeList.isEmpty() || merchantRecipeList.size() <= recipeIndex)
		{
			// Recipe not found.
			return null;
		}

		return merchantRecipeList.get(recipeIndex);
	}


	public RenderItem getItemRender()
	{
		return this.itemRender;
	}


	public FontRenderer getFontRender()
	{
		return this.fontRenderer;
	}


    //------------------------------------------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------------------------------------------

	private GuiCheckBox sellAllCheckbox;

    private int xBase = 0;

    private final int lineHeight = 18;

    private final int titleDistance = 20;

    private final int firstBuyItemXpos = 0;

    private final int secondBuyItemXpos = 18;

    private final int okNokXpos = 40;

    private final int sellItemXpos = 60;

    private final int textXpos = 85;

	private static final ResourceLocation icons = new ResourceLocation(EasierVillagerTrading.MODID, "textures/icons.png");
}