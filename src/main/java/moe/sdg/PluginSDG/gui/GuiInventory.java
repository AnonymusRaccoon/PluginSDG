package moe.sdg.PluginSDG.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class GuiInventory implements Listener
{
	protected final Inventory inv;
	protected final GuiController _guiController;
	protected final Player player;

	public GuiInventory(GuiController invManager, Player player, String invTitle, int size)
	{
		this._guiController = invManager;
		inv = Bukkit.createInventory(null, size, invTitle);
		this.player = player;
		// Put the items into the inventory
		initializeItems();
	}

	//! @brief create the inventory by placing block inside the inventory. The structure of the inventory is detailled here
	protected abstract void initializeItems();

	//! @brief helper method for creating an item
	//! @return the created item
	protected ItemStack createGuiItem(final Material material, final String name, final String... lore)
	{
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();

		// Set the name of the item
		meta.setDisplayName(name);

		// Set the lore of the item
		meta.setLore(Arrays.asList(lore));

		item.setItemMeta(meta);

		return item;
	}

	//! @brief open the inventory for the selected player
	public void openInventory(final HumanEntity ent) {
		ent.openInventory(inv);
	}

	// Check for clicks on items
	@EventHandler
	public abstract void onInventoryClick(final InventoryClickEvent event);

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		_guiController.deleteInventory(this);
	}

}
