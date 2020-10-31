package moe.sdg.PluginSDG;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class MiniGame
{
	private final ArrayList<Player> _players;
	private final GameManager _manager;
	private final Location _lobbyLocation;
	private final String gameName;

	public String getName() {
		return gameName;
	}

	public MiniGame(GameManager manager, String gameName)
	{
		this._manager = manager;
		this._lobbyLocation = null;
		this._players = new ArrayList<Player>();
		this.gameName = gameName;
	}


	public abstract GameType getType();
	
	//! @brief Return the current count of players in the game.
	public int getCurrentPlayers()
	{
		return this._players.size();
	}
	
	//! @brief Maximum number of player in the game.
	public abstract int getMaxPlayers();
	
	///! @brief Method called when a player wants to join the game.
	//! @param player The player who will join.
	//! @return True if the player has joined the game, false otherwise.
	public boolean join(Player player)
	{
		if (this.getMaxPlayers() < this.getCurrentPlayers() + 1)
			return false;
		this._players.add(player);
		player.teleport(this._lobbyLocation);
		return true;
	}
	
	//! @brief Method called when a player wants to leave the game.
	//! @param player The player who will leave.
	public void removePlayer(Player player)
	{
		this._players.remove(player);
		player.teleport(_manager.getHubLocation());
		if(this.getCurrentPlayers() == 0)
		{

		}
	}
	
	//! @brief Start the game.
	public abstract void start();

	//! @brief End the game.
	public abstract void end();

	public void destroy()
	{
		if(this.getCurrentPlayers() == 0)
		{
		_manager.deleteGame(this);
		}
	}

}
