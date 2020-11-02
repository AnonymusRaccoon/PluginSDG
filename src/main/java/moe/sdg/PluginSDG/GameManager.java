package moe.sdg.PluginSDG;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import moe.sdg.PluginSDG.commands.SDGCommand;
import moe.sdg.PluginSDG.exceptions.InvalidMapException;
import moe.sdg.PluginSDG.exceptions.MapNotFoundException;
import moe.sdg.PluginSDG.games.DeathMatch;
import moe.sdg.PluginSDG.commands.HubCommand;
import moe.sdg.PluginSDG.commands.SetHubCommand;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameManager extends JavaPlugin
{
	private ArrayList<MiniGame> _games;
	private WorldEditPlugin _worldEdit;
	private Vector nextGameLocation = new Vector(0, 150, 0);

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
				DeathMatch match = new DeathMatch(this, map, gameName);
				this._games.add(match);

				return match;
			default:
				return null;
		}
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

	public GameMap generateMap(String name)
	{
		File dir = new File(this.getDataFolder(), "map");
		if (!dir.exists())
			throw new MapNotFoundException();

		File file = new File(dir, name + ".schematic");
		if (!file.exists())
			throw new MapNotFoundException();
		ClipboardFormat format = ClipboardFormats.findByFile(file);
		if (format == null)
			throw new InvalidMapException();
		try (ClipboardReader reader = format.getReader(new FileInputStream(file)))
		{
			Clipboard clipboard = reader.read();
			World world = this._getGameWorld();
			try (EditSession editSession = WorldEdit.getInstance()
				.getEditSessionFactory()
				.getEditSession(BukkitAdapter.adapt(world), -1))
			{
				Operation operation = new ClipboardHolder(clipboard)
					.createPaste(editSession)
					.to(BlockVector3.at(nextGameLocation.getX(), nextGameLocation.getY(), nextGameLocation.getZ()))
					.build();
				Operations.complete(operation);
				clipboard.setOrigin(BlockVector3.at(nextGameLocation.getX(), nextGameLocation.getY(), nextGameLocation.getZ()));
				nextGameLocation.add(new Vector(200, 0, 0));
				return new GameMap(clipboard, world);
			}
		}
		catch (IOException | WorldEditException e)
		{
			e.printStackTrace();
			throw new InvalidMapException();
		}
	}

	private World _getGameWorld()
	{
		World world = Bukkit.getWorld("sdg_games");
		if (world != null)
			return world;
		return Bukkit.createWorld(new WorldCreator("sdg_games")
			.type(WorldType.FLAT)
			.generator(new ChunkGenerator()
			{
				@Override
				public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome)
				{
					return createChunkData(world);
				}

				@Override
				public boolean shouldGenerateMobs()
				{
					return false;
				}

				@Override
				public List<BlockPopulator> getDefaultPopulators(World world)
				{
					return new ArrayList<>();
				}
			}));
	}
}
