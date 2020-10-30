package moe.sdg.PluginSDG.games;

import moe.sdg.PluginSDG.GameManager;
import moe.sdg.PluginSDG.GameType;
import moe.sdg.PluginSDG.MiniGame;

public class DeathMatch extends MiniGame {

    private int _maxPlayer = 4;
    private boolean enforceMaxPlayer = false;

    public DeathMatch(GameManager gameManager)
    {
        super(gameManager);
    }

    @Override
    public GameType getType() {
        return GameType.DeathMatch;
    }

    @Override
    public int getMaxPlayers() {
        if (enforceMaxPlayer)
            return _maxPlayer;
        return 0;
    }

    //! @brief Start the game.
    @Override
    public void start() {

    }

    //! @brief End the game.
    @Override
    public void end() {

    }

}
