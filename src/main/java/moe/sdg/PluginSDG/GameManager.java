package moe.sdg.PluginSDG;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class GameManager extends JavaPlugin 
{
	@Override
	public void onEnable()
	{
		getLogger().info("Game manager loaded.");
	}
	
	public Location getHubLocation() 
	{
		throw new NotImplementedException();
	}
}
