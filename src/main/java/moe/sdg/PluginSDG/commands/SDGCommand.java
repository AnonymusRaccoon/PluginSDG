package moe.sdg.PluginSDG.commands;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.io.Closer;
import moe.sdg.PluginSDG.GameManager;
import moe.sdg.PluginSDG.GameType;
import moe.sdg.PluginSDG.MiniGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class SDGCommand implements CommandExecutor
{
	private final GameManager _gameManager;
	private final WorldEditPlugin _worldEdit;


	public SDGCommand(GameManager gameManager, WorldEditPlugin worldEdit)
	{
		this._gameManager = gameManager;
		this._worldEdit = worldEdit;
	}

	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		if (args.length == 0)
		{
			commandSender.sendMessage(ChatColor.BLUE + "usage: /sdg <action>");
			return true;
		}

		switch (args[0])
		{
			case "create":
				this.createCommand(commandSender, args);
				return true;
			case "remove":
				this.removeCommand(commandSender, args);
				break;
			case "list":
				this.listCommand(commandSender, args);
				break;
			case "modify":
				this.modifyCommand(commandSender, args);
				break;
		}

		if (!(commandSender instanceof Player))
		{
			commandSender.sendMessage(ChatColor.BLUE + "This command can only be performed by player.");
			return true;
		}
		Player player = (Player)commandSender;
		switch (args[0])
		{
			case "join":
				this.joinCommand(player, args);
				break;
			case "leave":
				this.leaveCommand(player, args);
				break;
			case "admin":
				this.adminCommand(player, args);
				break;
		}
		return true;
	}

	private void createCommand(CommandSender commandSender, String[] args)
	{
		if (args.length != 4 && args.length != 3)
		{
			commandSender.sendMessage(ChatColor.BLUE + "Usage /sdg create <gameType> <mapName> <gameName>");
			return;
		}

		switch (args[1].toLowerCase())
		{
			case "deathmatch":
				if (args.length == 3)
				{
					_gameManager.createGame(GameType.DeathMatch, args[2]);
					commandSender.sendMessage(ChatColor.BLUE + "Created deathmatch game with a default name since no name where precised");
				}
				else
				{
					if (_gameManager.getGameByName(args[3]) != null)
						commandSender.sendMessage(ChatColor.BLUE + "Name is already used" + args[0]);
					else
					{
						_gameManager.createGame(GameType.DeathMatch, args[2], args[3]);
						commandSender.sendMessage(ChatColor.BLUE + "Created deathmatch game");
					}
				}
				break;
			default:
				commandSender.sendMessage(ChatColor.BLUE + "Unknown game type " + args[1]);
				break;
		}
	}

	private void removeCommand(CommandSender commandSender, String[] args)
	{
		MiniGame game = _gameManager.getGameByName(args[1]);
		if (game == null)
			commandSender.sendMessage(ChatColor.BLUE + "Invalid game name: " + args[1]);
		else
		{
			game.destroy();
			commandSender.sendMessage(ChatColor.BLUE + "removed game " + args[1]);
		}
	}

	private void listCommand(CommandSender commandSender, String[] args)
	{
		ArrayList<MiniGame> games = _gameManager.getGames();
		commandSender.sendMessage(ChatColor.BLUE + "=====" + ChatColor.DARK_RED + "Game list" + ChatColor.BLUE + "=====");

		if (games.size() == 0)
			commandSender.sendMessage("There is no game");
		else
		{
			for (MiniGame game : games)
				commandSender.sendMessage(game.toString());
		}
		commandSender.sendMessage(ChatColor.BLUE + "===================");
	}

	private void modifyCommand(CommandSender commandSender, String[] args)
	{
		if (args.length < 3)
		{
			commandSender.sendMessage(ChatColor.BLUE + "Usage: /sdg modify <gameName> <action>");
			return;
		}

		MiniGame game = _gameManager.getGameByName(args[1]);
		if(game == null)
		{
			commandSender.sendMessage(ChatColor.BLUE + "Invalid game name: " + args[1]);
			return;
		}

		switch (args[2])
		{
			case "enforcePlayerLimit":
				boolean value = Boolean.getBoolean(args[3]);
				game.setEnforceMaxPlayer(value);
				commandSender.sendMessage(ChatColor.BLUE + "Set property enforceMaxPlayer to " + ChatColor.DARK_RED + value);
				break;
			default:
				break;
		}
		commandSender.sendMessage(ChatColor.BLUE + "Unknown command: " + args [2]);
	}

	private void joinCommand(Player player, String[] args)
	{
		if (args.length != 2)
		{
			player.sendMessage(ChatColor.BLUE +"usage: /sdg join <gameName>");
			return;
		}

		MiniGame game = _gameManager.getGameByName(args[1]);
		if(game == null)
		{
			player.sendMessage(ChatColor.BLUE + "Invalid game name: " + args[1]);
			return;
		}

		if (game.isPlayerInGame(player))
		{
			player.sendMessage(ChatColor.BLUE + "You are already in that game");
			return;
		}

		if (game.join(player))
			player.sendMessage(ChatColor.BLUE + "Joined game " + game.getName() );
		else
			player.sendMessage(ChatColor.BLUE + "Game "+ game.getName() + "is full");
	}

	private void leaveCommand(Player player, String[] args)
	{
		if (args.length != 2)
		{
			player.sendMessage(ChatColor.BLUE + "usage: /sdg leave <gameName>");
			return;
		}

		MiniGame game = _gameManager.getGameByName(args[1]);
		if (game == null)
		{
			player.sendMessage(ChatColor.BLUE + "Invalid game name: " + args[1]);
			return;
		}

		if (game.isPlayerInGame(player))
		{
			game.removePlayer(player);
			player.sendMessage(ChatColor.BLUE + "Left game " + game.getName());
		}
		else
			player.sendMessage(ChatColor.BLUE + "You can't leave " +  game.getName() + "because you are not in it");
	}

	private void adminCommand(Player player, String[] args)
	{
		if (args.length != 3 || (!args[1].equals("map") && args[1].equals("lobby")))
		{
			player.sendMessage(ChatColor.BLUE + "usage: /sdg admin <map|lobby> <name>");
			return;
		}

		LocalSession session = this._worldEdit.getSession(player);
		com.sk89q.worldedit.entity.Player wePlayer = this._worldEdit.wrapPlayer(player);
		EditSession editSession = session.createEditSession(wePlayer);
		Closer closer = Closer.create();

		try
		{
			Region region = session.getSelection(wePlayer.getWorld());
			Clipboard cb = new BlockArrayClipboard(region);
			ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, cb, region.getMinimumPoint());
			Operations.complete(copy);
			LocalConfiguration config = this._worldEdit.getWorldEdit().getConfiguration();
			File dir = new File(this._gameManager.getDataFolder(), args[1]);
			if (!dir.exists() && !dir.mkdirs())
				throw new IOException("Could not create directory " + config.saveDir);
			File schematicFile = new File(dir, args[2] + ".schematic");
			schematicFile.createNewFile();

			FileOutputStream fos = closer.register(new FileOutputStream(schematicFile));
			BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
			ClipboardWriter writer = closer.register(BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(bos));
			writer.write(cb);
		}
		catch (IOException | WorldEditException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				closer.close();
				editSession.flushSession();
			}
			catch (IOException ignore) { }
		}
		player.sendMessage(Color.BLUE + "Map saved successfully.");
	}
}
