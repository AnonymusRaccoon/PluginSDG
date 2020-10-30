package moe.sdg.PluginSDG.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetHubCommand implements CommandExecutor
{
	FileConfiguration config;

	public SetHubCommand(FileConfiguration config)
	{
		this.config = config;
	}
	
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
	{
		if (commandSender instanceof Player) {
			Player player = (Player)commandSender;
			config.set("hub_pos", player.getLocation());
			player.sendMessage("Hub location set to your position.");
			return true;
		}
		return false;
	}
}
