package moe.sdg.PluginSDG;

import org.bukkit.entity.Player;
import java.util.ArrayList;

class Team
{
	public String name;
	private final ArrayList<Player> _players;
	public int maxPlayer;
	
	public Team(String name, int maxPlayer)
	{
		super();
		this.name = name;
		this.maxPlayer = maxPlayer;
		this._players = new ArrayList<Player>();
	}
	
	//! @brief Make a player join the team.
	//! @param player The player who will join.
	public boolean join(Player player)
	{
		if (this.playerCount() + 1 > this.maxPlayer)
			return false;
		this._players.add(player);
		return true;
	}
	
	public int playerCount()
	{
		return this._players.size();
	}
}

public abstract class TeamMiniGame extends MiniGame
{
	private final ArrayList<Team> _teams;
	
	
	public TeamMiniGame(GameManager manager, String gameName)
	{
		super(manager, gameName);
		this._teams = new ArrayList<Team>();
	}
	
	//! @brief Get the number of teams.
	public int getTeamCount()
	{
		return this._teams.size();
	}
}
