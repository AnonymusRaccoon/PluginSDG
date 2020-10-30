package moe.sdg.PluginSDG;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class MiniGame
{
	private final ArrayList<Player> _players;
	
	public MiniGame()
	{
		this._players = new ArrayList<Player>();
	}
	
	//! @brief Return the current count of players in the game.
	public int getCurrentPlayers()
	{
		return this._players.size();
	}
	
	//! @brief Maximum number of player in the game.
	public abstract int getMaxPlayers();
	
	///! @brief Method called when a player wants to join the game.
	//! @param player The player who will join
	//! @return True if the player has joined the game, false otherwise.
	public boolean join(Player player)
	{
		if (this.getMaxPlayers() < this.getCurrentPlayers() + 1)
			return false;
		this._players.add(player);
		return true;
	}
	
	//! @brief Method called when a player wants to leave the game.
	public abstract void leave();
	
	//! @brief Start the game.
	public abstract void start();
	//! @brief End the game.
	public abstract void end();
}
