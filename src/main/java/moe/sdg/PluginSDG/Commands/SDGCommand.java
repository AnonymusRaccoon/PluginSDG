package moe.sdg.PluginSDG.Commands;

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

	public SDGCommand(GameManager gameManager){
		this.gameManager = gameManager;
	}

	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

		if(args.length == 0)
		{
			commandSender.sendMessage(ChatColor.BLUE + "usage: /sdg <action>");
			return true;
		}
		//parse sdg argument
		switch (args[0])
		{
			case "create":
				if(args.length != 4 && args.length != 3)
				{
					commandSender.sendMessage(ChatColor.BLUE + "Usage /sdg create <gameType> <mapName> <gameName>");
					break;
				}

					//parse create argument
					if (args[1].equals("deathmatch"))
					{
						//TODO: check for map name
						//check if a gameName was given
						if (args.length == 3) {

							gameManager.createGame(GameType.DeathMatch, args[2]);
							commandSender.sendMessage(ChatColor.BLUE + "Created deathmatch game with a default name since no name where precised");
						} else {

							//check if game name is already used
							if (gameManager.getGamesByName(args[3]).size() != 0) {
								commandSender.sendMessage(ChatColor.BLUE + "Name is already used" + args[0]);
							}else {
								gameManager.createGame(GameType.DeathMatch, args[2], args[3]);
								commandSender.sendMessage(ChatColor.BLUE + "Created deathmatch game");
							}
						}
						break;
					}
					commandSender.sendMessage(ChatColor.BLUE + "Unknown game type " + args[1]);
					break;

			case "remove":

				if(gameManager.getGamesByName(args[1]).size() == 0)
				{
					commandSender.sendMessage(ChatColor.BLUE + "Invalid game name: " + args[1]);
				}else{
					gameManager.getGameByName(args[1]).destroy();
					commandSender.sendMessage(ChatColor.BLUE + "removed game " + args[1]);
				}
				break;

			case "list":

				ArrayList<MiniGame>  games = gameManager.getGames();

				commandSender.sendMessage(ChatColor.BLUE + "====="+ ChatColor.DARK_RED + "Game list" + ChatColor.BLUE +  "=====" );

				if(games.size() == 0){
					commandSender.sendMessage("There is no game");
				}else {
					for (MiniGame game : games)
					{
						commandSender.sendMessage(game.getType() + " " + game.getCurrentPlayers() + "/" + game.getMaxPlayers() + " " + game.getName());
					}
				}
				commandSender.sendMessage(ChatColor.BLUE +"===================");
				break;

			case "modify":

				//check if number of args match required args
				if(args.length < 3)
				{
					commandSender.sendMessage(ChatColor.BLUE +"usage: /sdg modify <gameName> <action> ");
					break;
				}
				MiniGame game = gameManager.getGameByName(args[1]);

				if(game == null)
				{
					commandSender.sendMessage(ChatColor.BLUE + "Invalid game name: " + args[1]);
				}

				if (args[2].equals("enforcePlayerLimit"))
				{
					if(args[3].equals("true"))
					{
						game.setEnforceMaxPlayer(true);
						commandSender.sendMessage(ChatColor.BLUE + "Set property enforceMaxPlayer to " + ChatColor.DARK_RED + "TRUE");
					}
					if(args[3].equals("false"))
					{
						game.setEnforceMaxPlayer(false);
						commandSender.sendMessage(ChatColor.BLUE + "Set property enforceMaxPlayer to " + ChatColor.DARK_RED + "FALSE");
					}
					break;
				}
				commandSender.sendMessage(ChatColor.BLUE + "Unknown command: " + args [2]);
				break;

			case "join":
				if(args.length != 2)
				{
					commandSender.sendMessage(ChatColor.BLUE +"usage: /sdg join <gameName> ");
					break;
				}

				game = gameManager.getGameByName(args[1]);

				if(game == null)
				{
					commandSender.sendMessage(ChatColor.BLUE + "Invalid game name: " + args[1]);
				}

				if (commandSender instanceof Player) {

					if(game.isPlayerInGame((Player) commandSender))
					{
						commandSender.sendMessage(ChatColor.BLUE + "You are already in that game");
					}

					//join game and check result
					if(game.join((Player) commandSender))
					{
						commandSender.sendMessage(ChatColor.BLUE + "Joined game " + game.getName() );
					}
					else
						{
							commandSender.sendMessage(ChatColor.BLUE + "Game "+ game.getName() + "is full");
						}
					break;
				}
				commandSender.sendMessage(ChatColor.BLUE + "This command can only be performed by player");
				break;

			case "leave":
				if(args.length != 2)
				{
					commandSender.sendMessage(ChatColor.BLUE +"usage: /sdg join <gameName> ");
					break;
				}

				game = gameManager.getGameByName(args[1]);

				if(game == null)
				{
					commandSender.sendMessage(ChatColor.BLUE + "Invalid game name: " + args[1]);
				}
				if (commandSender instanceof Player) {

					if(game.isPlayerInGame((Player) commandSender))
					{
						game.removePlayer((Player) commandSender);
						commandSender.sendMessage(ChatColor.BLUE + "Left game " + game.getName());
					}
					else
					{
						commandSender.sendMessage(ChatColor.BLUE + "You can't leave " +  game.getName() + "because you are not in it");
					}
					break;
				}
				commandSender.sendMessage(ChatColor.BLUE + "This command can only be performed by player");
				break;
		}
		return true;
	}



}
