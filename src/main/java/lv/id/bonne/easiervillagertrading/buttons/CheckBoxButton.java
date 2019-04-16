package lv.id.bonne.easiervillagertrading.buttons;


import lv.id.bonne.easiervillagertrading.config.Config;
import net.minecraftforge.fml.client.config.GuiCheckBox;


public class CheckBoxButton extends GuiCheckBox
{
	public CheckBoxButton(int id,
		int xPos,
		int yPos,
		String displayString,
		boolean isChecked)
	{
		super(id, xPos, yPos, displayString, isChecked);
	}


	public void processButton()
	{
		Config.setSellAll(this.isChecked());
	}
}
