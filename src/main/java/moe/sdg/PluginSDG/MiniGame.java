package moe.sdg.PluginSDG;

import moe.sdg.PluginSDG.enums.GameType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.ArrayList;

public abstract class MiniGame
{
	private final ArrayList<Player> _players;
	private final GameManager _manager;
	private final GameMap _map;
	private final String name;
	protected int _maxPlayer = 4;
	protected boolean enforceMaxPlayer = true;


	public MiniGame(GameManager manager, String mapName, String name)
	{
		this._manager = manager;
		this._map = this._manager.generateMap(mapName);;
		this._players = new ArrayList<>();
		this.name = name;
	}

	public abstract GameType getType();
	
	//! @brief Return the current count of players in the game.
	public int getCurrentPlayers()
	{
		return this._players.size();
	}

	public ArrayList<Player> getPlayerList()
	{
		return _players;
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
		player.teleport(this._map.lobbyLocation);
		_manager.addPlayer(player, this);

		return true;
	}

	//! @brief Can a player join this game
	public boolean isJoinable()
	{
		return this.getMaxPlayers() < this.getCurrentPlayers() + 1;
	}


	public boolean isPlayerInGame(Player player)
	{
		return _players.contains(player);
	}

	//! @brief Method called when a player wants to leave the game.
	//! @param player The player who will leave.
	public void removePlayer(Player player)
	{
		this._players.remove(player);
		_manager.removePlayer(player);
		player.teleport(_manager.getHubLocation());
		if(this.getCurrentPlayers() == 0)
			this.destroy();
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

	public void setEnforceMaxPlayer(boolean enforceMaxPlayer)
	{
		this.enforceMaxPlayer = enforceMaxPlayer;
	}

	public String getName()
	{
		return this.name;
	}

	@Override
	public String toString()
	{
		return getType().toString() + " " + getCurrentPlayers() + "/" + getMaxPlayers() + " " + getName();
	}
}
