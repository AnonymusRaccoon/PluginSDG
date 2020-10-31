package moe.sdg.PluginSDG.commands;

import moe.sdg.PluginSDG.GameManager;
import moe.sdg.PluginSDG.GameType;
import moe.sdg.PluginSDG.MiniGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
}
