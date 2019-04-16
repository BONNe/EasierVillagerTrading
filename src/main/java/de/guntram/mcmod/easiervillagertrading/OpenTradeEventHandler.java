package de.guntram.mcmod.easiervillagertrading;

import lv.id.bonne.easiervillagertrading.ImprovedGuiMerchant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.GuiOpenEvent;

public class OpenTradeEventHandler
{
    public static OpenTradeEventHandler getInstance()
    {
        if (OpenTradeEventHandler.instance == null)
        {
            OpenTradeEventHandler.instance = new OpenTradeEventHandler();
            OpenTradeEventHandler.instance.mc = Minecraft.getInstance();
        }

        return OpenTradeEventHandler.instance;
    }


    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event)
    {
        if (event.getGui() instanceof GuiMerchant)
        {
            event.setGui(new ImprovedGuiMerchant(this.mc.player.inventory, (GuiMerchant) event.getGui(), this.mc.world));
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------------------------------------------


    static private OpenTradeEventHandler instance;

    private Minecraft mc;
}
