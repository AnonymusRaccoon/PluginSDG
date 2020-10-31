package moe.sdg.PluginSDG.games;

import moe.sdg.PluginSDG.GameManager;
import moe.sdg.PluginSDG.GameType;
import moe.sdg.PluginSDG.MiniGame;

public class DeathMatch extends MiniGame
{
    private int _maxPlayer = 4;
    private boolean enforceMaxPlayer = true;

    public DeathMatch(GameManager gameManager, String map, String name)
    {
        super(gameManager, name);
    }

    @Override
    public GameType getType()
    {
        return GameType.DeathMatch;
    }

    @Override
    public int getMaxPlayers()
    {
        if (this.enforceMaxPlayer)
            return _maxPlayer;
        return -1;
    }

    //! @brief Start the game.
    @Override
    public void start() { }

    //! @brief End the game.
    @Override
    public void end() { }
}
