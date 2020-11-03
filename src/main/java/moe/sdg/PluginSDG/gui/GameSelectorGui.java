package moe.sdg.PluginSDG.gui;

import moe.sdg.PluginSDG.GameMap;
import moe.sdg.PluginSDG.MiniGame;
import moe.sdg.PluginSDG.enums.GameType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GameSelectorGui extends GuiInventory implements Listener
{
	private String _guiMode = "default";

	private final int gameDisplayEndSlot = 27;
	private final int gameDisplayStartSlot = 0;

	//this is horrible we store potential map name and type in hope that we will eventually use it
	private String _map;
	private GameType _type;

	public GameSelectorGui(GuiController invManager , String invTitle, Player player)
	{
		super(invManager, player, invTitle, 54 );
	}

	@Override
	protected void initializeItems()
	{
		ArrayList<MiniGame> gameList = this._guiController.getGameController().getGames();
		this._guiMode = "default";

		blankMenu();

		for(int i = gameDisplayStartSlot; i < gameList.size() && i < gameDisplayEndSlot; i++)
		{
			MiniGame game = (gameList.get(i));
			player.sendMessage("game name" + game.getName());
			if(game.isJoinable())
				this.inv.setItem(i, createGuiItem(Material.WHITE_WOOL, game.getName(), game.toString()));
			else
				this.inv.setItem(i, createGuiItem(Material.RED_WOOL, game.getName(), game.toString()));
		}

		this.inv.setItem(37, createGuiItem(Material.EMERALD_BLOCK, "Create a new game"));
		this.inv.setItem(43, createGuiItem(Material.REDSTONE_BLOCK, "Delete game"));
	}

	//! @brief Empty the top part of the menu
	private void blankMenu()
	{
		for(int i = gameDisplayStartSlot; i < gameDisplayEndSlot; i++ )
		{
			this.inv.setItem(i, createGuiItem(Material.AIR,"a","a"));
		}
	}


	@Override
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getInventory() != inv) return;

		event.setCancelled(true);

		final ItemStack clickedItem = event.getCurrentItem();

		if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

		player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10, 29);

		switch (clickedItem.getType())
		{
			case WHITE_WOOL:
			case RED_WOOL:
				if(_guiMode.equals("delete")) deleteGame(event);
				break;
			case EMERALD_BLOCK:
			case GREEN_STAINED_GLASS_PANE:
			case MAP:
				createGame(event);
				break;
			case REDSTONE_BLOCK:
			case BONE_BLOCK:
				switchDeleteMode();
				break;
		}
	}

	@EventHandler
	public void onInventoryClick(final InventoryDragEvent event)
	{
		if (event.getInventory() == inv) {
			event.setCancelled(true);
		}
	}

	private void createGame(InventoryClickEvent event)
	{
		//TODO: add menu for selecting map and menu for selecting game type
		int i = 1;

		switch(_guiMode)
		{
			case "default":
				showGameModeList();
				break;
			case "mapSelect":
				_type = GameType.valueOf(event.getCurrentItem().getItemMeta().getDisplayName());
				showMapList();
				break;
			case "createGame":
				this._map = event.getCurrentItem().getItemMeta().getDisplayName();
				String name = event.getWhoClicked().getName();
				while ( _guiController.getGameController().getGameByName(name) != null)
					name =  event.getWhoClicked().getName() + "#" + i++;

				this._guiController.getGameController().createGame(GameType.DeathMatch, this._map, name);
				initializeItems();
				break;
		}

	}

	private void showGameModeList()
	{
		blankMenu();
		for (GameType type : GameType.values())
		{
			if(type == GameType.None);
			// NOTE : Adding color to type.name will break the menu TOO BAD!
			inv.addItem( createGuiItem(Material.GREEN_STAINED_GLASS_PANE, type.name()));
		}
		_guiMode = "modeSelect";
	}

	private void showMapList()
	{
		// NOTE : delete this once map list is properly implemented
		inv.addItem( createGuiItem(Material.MAP, "mapName"));

		for (GameMap map : _guiController.getGameController().getGamesMap(this._type))
		{
			inv.addItem( createGuiItem(Material.MAP, this._type.name()));
		}
		_guiMode = "createGame";
	}


	//! @brief switch between deleting game on click and normal mode
	private void switchDeleteMode()
	{
		if(_guiMode.equals("delete"))
		{
			this.initializeItems();
		}
		else if(_guiMode.equals("default"))
		{
			this.initializeItems();
			this._guiMode = "delete";
			this.inv.setItem(43, createGuiItem(Material.BONE_BLOCK, "Delete game"));
		}
	}

	//! @brief delete a game that was clicked on
	private void deleteGame(InventoryClickEvent event)
	{
		MiniGame game = mapInventoryToGameList(event.getRawSlot(), event);
		if(game != null)
		{
			game.destroy();
		} else {
			event.getWhoClicked().sendMessage(ChatColor.RED +"game does not exist");
		}
		this.initializeItems();

	}

	private void joinGame()
	{

	}

	//! @brief return a game from an inventory position. Return null if the slot position does not map to anything
	private MiniGame mapInventoryToGameList(int slotNumber, InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		if(slotNumber < this._guiController.getGameController().getGames().size() && slotNumber >= gameDisplayStartSlot)
		{
			return this._guiController.getGameController().getGames().get(slotNumber); // map inventory pos to pos in array
		}
		return null;
	}


}
