//
// Tom Sawyer Software
// Copyright 2007 - 2018
// All rights reserved.
//
// www.tomsawyer.com
//


package lv.id.bonne.easiervillagertrading.buttons;


import de.guntram.mcmod.easiervillagertrading.ConfigurationHandler;
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
		ConfigurationHandler.setDefaultSellAll(this.isChecked());
	}
}
