package moe.sdg.PluginSDG.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor
{
	FileConfiguration config;
	
	public HubCommand(FileConfiguration config)
	{
		this.config = config;
	}
	
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
	{
		if (commandSender instanceof Player)
		{
			Player player = (Player)commandSender;
			Location hub = config.getLocation("hub_pos");
			if (hub != null && hub.getWorld() != null)
				player.teleport(hub);
			else
				player.sendMessage("No hub location set.");
			return true;
		}
		return false;
	}
}
