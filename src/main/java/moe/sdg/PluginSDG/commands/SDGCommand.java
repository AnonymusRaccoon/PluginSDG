package moe.sdg.PluginSDG.commands;

import moe.sdg.PluginSDG.GameManager;
import moe.sdg.PluginSDG.GameType;
import moe.sdg.PluginSDG.MiniGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class SDGCommand implements CommandExecutor
{
	GameManager gameManager;

	public SDGCommand(GameManager gameManager)
	{
		this.gameManager = gameManager;
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
			case "join":
				if (commandSender instanceof Player)
					this.joinCommand((Player)commandSender, args);
				else
					commandSender.sendMessage(ChatColor.BLUE + "This command can only be performed by player.");
				break;
			case "leave":
				if (commandSender instanceof Player)
					this.leaveCommand((Player)commandSender, args);
				else
					commandSender.sendMessage(ChatColor.BLUE + "This command can only be performed by player.");
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
					gameManager.createGame(GameType.DeathMatch, args[2]);
					commandSender.sendMessage(ChatColor.BLUE + "Created deathmatch game with a default name since no name where precised");
				}
				else
				{
					if (gameManager.getGameByName(args[3]) != null)
						commandSender.sendMessage(ChatColor.BLUE + "Name is already used" + args[0]);
					else
					{
						gameManager.createGame(GameType.DeathMatch, args[2], args[3]);
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
		MiniGame game = gameManager.getGameByName(args[1]);
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
		ArrayList<MiniGame> games = gameManager.getGames();
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

		MiniGame game = gameManager.getGameByName(args[1]);
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

		MiniGame game = gameManager.getGameByName(args[1]);
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

		MiniGame game = gameManager.getGameByName(args[1]);
		if(game == null)
		{
			player.sendMessage(ChatColor.BLUE + "Invalid game name: " + args[1]);
			return;
		}
		if(game.isPlayerInGame(player))
		{
			game.removePlayer(player);
			player.sendMessage(ChatColor.BLUE + "Left game " + game.getName());
		}
		else
			player.sendMessage(ChatColor.BLUE + "You can't leave " +  game.getName() + "because you are not in it");
	}
}
