package moe.sdg.PluginSDG.Commands;

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
					if (args[1].equals("deathmatch")) {
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
					}
					commandSender.sendMessage(ChatColor.BLUE + "Unknown game type " + args[1]);
					break;


			case "remove":
				//check if entered name exist
				if(gameManager.getGamesByName(args[1]).size() == 0)
				{
					commandSender.sendMessage(ChatColor.BLUE + "Invalid game name: " + args[1]);
				}else{
					gameManager.getGamesByName(args[1]).get(0).destroy();
					commandSender.sendMessage(ChatColor.BLUE + "removed game " + args[1]);
				}
				break;

			case "list":

				ArrayList<MiniGame>  games = gameManager.getGames();

				commandSender.sendMessage(ChatColor.BLUE + "====="+ ChatColor.DARK_RED + "Game list" + ChatColor.BLUE +  "=====" );

				//list game name
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
		}
		return true;
	}



}
