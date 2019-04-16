package lv.id.bonne.easiervillagertrading.events;

import lv.id.bonne.easiervillagertrading.gui.ImprovedGuiMerchant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.GuiOpenEvent;

public class OpenTradeEventHandler
{
    public OpenTradeEventHandler(Minecraft minecraft)
    {
        this.mc = minecraft;
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


    private Minecraft mc;
}
