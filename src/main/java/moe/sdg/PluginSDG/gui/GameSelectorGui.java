package moe.sdg.PluginSDG.gui;

import moe.sdg.PluginSDG.MiniGame;
import moe.sdg.PluginSDG.enums.GameType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GameSelectorGui extends GuiInventory implements Listener
{

	public GameSelectorGui(GuiController invManager , String invTitle, Player player)
	{
		super(invManager, player, invTitle, 54 );
	}

	@Override
	protected void initializeItems()
	{
		ArrayList<MiniGame> gameList = this._guiController.getGameController().getGames();
		for(int i = 0; i < gameList.size() && i <27; i++)
		{
			MiniGame game = (gameList.get(i));
			MiniGame playerGame = _guiController.getGameController().GetPlayerGame(this.player);

			if(game.isJoinable())
				this.inv.setItem(i, createGuiItem(Material.WHITE_WOOL, game.getName(), game.toString()));
			else
				this.inv.setItem(i, createGuiItem(Material.RED_WOOL, game.getName(), game.toString()));
		}

		this.inv.setItem(37, createGuiItem(Material.EMERALD_BLOCK, "Create a new game"));
		this.inv.setItem(43, createGuiItem(Material.REDSTONE_BLOCK, "Delete game"));


	}

	@Override
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		event.getWhoClicked().sendMessage("event");
		if (event.getInventory() != inv) return;

		event.setCancelled(true);

		final ItemStack clickedItem = event.getCurrentItem();

		// verify current item is not null
		if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

		switch (clickedItem.getType())
		{
			case REDSTONE_BLOCK:
				break;
			case WHITE_WOOL:
				break;
			case EMERALD_BLOCK:
				break;
			case RED_WOOL:
				// deleteGame(event);
				break;
		}

		final Player player = (Player) event.getWhoClicked();

		// Using slots click is a best option for your inventory click's
		player.sendMessage("You clicked at slot " + event.getRawSlot());
	}

	@EventHandler
	public void onInventoryClick(final InventoryDragEvent event)
	{
		event.getWhoClicked().sendMessage("event");
		if (event.getInventory() == inv) {
			event.setCancelled(true);
		}
	}

	private void createGame(InventoryClickEvent event)
	{
		//TODO: add menu for selecting map and menu for selecting game type
		int i = 1;
		String name = event.getWhoClicked().getName();
		while ( _guiController.getGameController().getGameByName(name) != null)
			name =  event.getWhoClicked().getName() + "#" + i++;
		this._guiController.getGameController().createGame(GameType.DeathMatch, "", name);
	}

	private  void deleteGame(InventoryClickEvent event)
	{
		MiniGame game = (this._guiController.getGameController().getGames().get(event.getSlot())); // map inventory pos to pos in array
		game.destroy();
	}

}
