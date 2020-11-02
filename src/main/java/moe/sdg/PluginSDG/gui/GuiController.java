package moe.sdg.PluginSDG.gui;

import moe.sdg.PluginSDG.GameManager;
import moe.sdg.PluginSDG.enums.GuiType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class GuiController
{
	private ArrayList<GuiInventory> _guiList;
	private final GameManager _gameManager;


	public GameManager getGameController()
	{
		return _gameManager;
	}

	public GuiController(GameManager _gameManager)
	{
		this._gameManager = _gameManager;
		this._guiList = new ArrayList<>();
	}


	//! @brief Fabric for creating guiInventory
	public GameSelectorGui createInventory(GuiType type, Player playerUsingGui)
	{
		switch (type)
		{
			case GAMESELECTOR:
				GameSelectorGui gui = new GameSelectorGui(this, "SDG - Motivated gui selection system", playerUsingGui);
				this._guiList.add(gui);
				getGameController().getServer().getPluginManager().registerEvents(gui, _gameManager);
				return gui;
		}
		return null;
	}

	public void deleteInventory (GuiInventory inv)
	{
		HandlerList.unregisterAll(inv);
		this._guiList.remove(inv);
	}
}
