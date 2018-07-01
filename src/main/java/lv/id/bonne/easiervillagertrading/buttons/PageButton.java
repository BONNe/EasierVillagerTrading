package lv.id.bonne.easiervillagertrading.buttons;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;


public class PageButton extends GuiButton
{
	public PageButton(int buttonID, int x, int y, boolean forward)
	{
		super(buttonID, x, y, 12, 19, "");
		this.forward = forward;
	}

	/**
	 * {@inheritDoc}
	 */
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks)
	{
		if (this.visible)
		{
			minecraft.getTextureManager().bindTexture(RESOURCE_ICONS);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width &&
				mouseY < this.y + this.height;
			int i = 0;
			int j = 176;

			if (!this.enabled)
			{
				j += this.width * 2;
			}
			else if (flag)
			{
				j += this.width;
			}

			if (!this.forward)
			{
				i += this.height;
			}

			this.drawTexturedModalRect(this.x, this.y, j + 1, i + 2, 10, 15);
		}
	}


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------

	private boolean forward;

// ---------------------------------------------------------------------
// Section: Constants
// ---------------------------------------------------------------------

	private static final ResourceLocation RESOURCE_ICONS =
		new ResourceLocation("textures/gui/container/villager.png");
}