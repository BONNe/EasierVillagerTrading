package lv.id.bonne.easiervillagertrading.buttons;


import lv.id.bonne.easiervillagertrading.gui.ImprovedGuiMerchant;
import net.minecraft.client.gui.GuiButton;


public class AutoTradeButton extends GuiButton
{
	public AutoTradeButton(int buttonId,
		int x,
		int y,
		int width,
		int height,
		String buttonText,
		ImprovedGuiMerchant guiMerchant)
	{
		super(buttonId, x, y, width, height, buttonText);
		this.visible = true;
		this.guiMerchant = guiMerchant;
	}


	@Override
	public void onClick(double mouseX, double mouseY)
	{
		super.onClick(mouseX, mouseY);
		this.guiMerchant.startAllItemTrading();
	}

	private ImprovedGuiMerchant guiMerchant;
}
