package moe.sdg.PluginSDG;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.ArrayList;

public abstract class MiniGame
{
	private final ArrayList<Player> _players;
	private final GameManager _manager;
	private final Location _lobbyLocation;
	
	
	public MiniGame(GameManager manager)
	{
		this._manager = manager;
		this._lobbyLocation = null;
		this._players = new ArrayList<Player>();
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
	public void leave(Player player)
	{
		this._players.remove(player);
		player.teleport(_manager.getHubLocation());
	}
	
	//! @brief Start the game.
	public abstract void start();
	//! @brief End the game.
	public abstract void end();
}
