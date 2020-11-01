package moe.sdg.PluginSDG;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import moe.sdg.PluginSDG.commands.SDGCommand;
import moe.sdg.PluginSDG.games.DeathMatch;
import moe.sdg.PluginSDG.commands.HubCommand;
import moe.sdg.PluginSDG.commands.SetHubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameManager extends JavaPlugin
{
	private ArrayList<MiniGame> _games;
	private WorldEditPlugin _worldEdit;

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onEnable()
	{
		this._worldEdit = (WorldEditPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

		FileConfiguration config = getConfig();
		config.addDefault("hub_pos", new Location(getServer().getWorld("hub"), 0, 0, 0));
		config.options().copyDefaults(true);
		saveConfig();

		getCommand("hub").setExecutor(new HubCommand(config));
		getCommand("sethub").setExecutor(new SetHubCommand(config));
		getCommand("sdg").setExecutor(new SDGCommand(this, this._worldEdit));
		_games = new ArrayList<>();
		getLogger().info("Game manager loaded.");
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
	}

	//! @brief return hub location
	//! @return return a Location containing the server hub
	public Location getHubLocation()
	{
		return getConfig().getLocation("hub_pos");
	}

	//! @brief factory for creating new game
	//! @param type The type of game to create
	//! @param map the name of the map to use
	//! @param gameName the name of the new game
	//! @return
	public MiniGame createGame(GameType type, String map, String gameName)
	{
		switch (type)
		{
			case DeathMatch:
				DeathMatch match = new DeathMatch(this, map, gameName != null ? gameName : this.generateNewName());
				this._games.add(match);

				return match;
			default:
				return null;
		}
	}

	public MiniGame createGame(GameType type, String map)
	{
		return this.createGame(type,map,null);
	}

	//! @brief generate a new possible game name of the form <unnamed: number>
	//! @return
	private String generateNewName()
	{
		String name = "Unnamed " + Math.random();
		if (this.getGameByName(name) != null)
			return this.generateNewName();
		return name;
	}

	//! @brief delete a game
	//! @param game the game to be deleted
	public void deleteGame(MiniGame game)
	{
		this._games.remove(game);
	}

	//! @brief Return a list of game whose GameType match a given type
	public ArrayList<MiniGame> getGamesByType(final GameType type)
	{
		return this._games.stream().filter(e -> e.getType() == type)
			.collect(Collectors.toCollection(ArrayList::new));
	}

	//! @brief Return the list of game created
	public ArrayList<MiniGame> getGames()
	{
		return this._games;
	}

	//! @brief return an single game witch name match a given name. If there is multiple candidate the first one is returned
	//! @param name The name of the game to get
	//! @return
	public MiniGame getGameByName(String name)
	{
		return this._games.stream().filter(e -> e.getName().equals(name))
			.findFirst()
			.orElse(null);
	}
}
