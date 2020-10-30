package moe.sdg.PluginSDG;

public interface MiniGame
{
	//! @brief Return the current count of players in the game.
	int getCurrentPlayers();
	//! @brief Maximum number of player in the game.
	int getMaxPlayers();
	
	///! @brief Method called when a player wants to join the game.
	//! @return True if the player has joined the game, false otherwise.
	boolean join();
	//! @brief Method called when a player wants to leave the game.
	void leave();
	
	//! @brief Start the game.
	void start();
	//! @brief End the game.
	void end();
}
