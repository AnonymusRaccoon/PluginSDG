package moe.sdg.PluginSDG;

import moe.sdg.PluginSDG.Commands.HubCommand;
import moe.sdg.PluginSDG.Commands.SetHubCommand;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameManager extends JavaPlugin
{

	private ArrayList<MiniGame> _games;


	@Override
	public void onEnable()
	{
		FileConfiguration config = getConfig();
		config.addDefault("hub_pos", new Location(getServer().getWorld("hub"), 0, 0, 0));
		config.options().copyDefaults(true);
		saveConfig();

		getCommand("hub").setExecutor(new HubCommand(config));
		getCommand("sethub").setExecutor(new SetHubCommand(config));
		_games = new ArrayList<MiniGame>();
		getLogger().info("Game manager loaded.");
	}

	public Location getHubLocation()
	{
		throw new NotImplementedException();
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
	}

	//! @brief factory for creating new game
	//! @param type The type of game to create
	//! @param map the name of the map to use
	//! @return
	public MiniGame createGame(GameType type, String map)
	{
		switch (type) {
			// TODO: create a new game and add it the array list
		}
		return null;
	}

	//! @brief delete a game
	//! @param game the game to be deleted
	public void deleteGame(MiniGame game)
	{
		this._games.remove(game);
	}

	public ArrayList<MiniGame> getByType(final GameType type)
	{
		return this._games.stream().filter(e -> e.getType() == type).collect(Collectors.toCollection(ArrayList::new));
	}
}
