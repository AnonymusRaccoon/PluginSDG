package moe.sdg.PluginSDG;

import moe.sdg.PluginSDG.Commands.HubCommand;
import moe.sdg.PluginSDG.Commands.SetHubCommand;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class GameManager extends JavaPlugin 
{
	@Override
	public void onEnable()
	{
		FileConfiguration config = getConfig();
		config.addDefault("hub_pos", new Location(getServer().getWorld("hub"), 0, 0, 0));
		config.options().copyDefaults(true);
		saveConfig();
		
		getCommand("hub").setExecutor(new HubCommand(config));
		getCommand("sethub").setExecutor(new SetHubCommand(config));
	}
	
	public Location getHubLocation() 
	{
		throw new NotImplementedException();
	}
}
