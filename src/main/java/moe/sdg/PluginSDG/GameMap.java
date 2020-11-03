package moe.sdg.PluginSDG;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.ArrayList;

public class GameMap
{
	public Location lobbyLocation;

	public ArrayList<Location> spawnLocations;
	private String _mapName;

	public GameMap() { }

	public GameMap(Clipboard clipboard, World world, String mapName)
	{
		this._mapName = mapName;
		BlockVector3 o = clipboard.getOrigin();
		this.lobbyLocation = new Location(world, o.getX(), o.getY(), o.getZ());
		for (BlockVector3 b : clipboard.getRegion())
		{
			BlockState state = clipboard.getBlock(b);
			if (state.getBlockType() == BlockType.REGISTRY.get("minecraft:respawn_anchor"))
			{
				System.out.println("RESPAWN BLOCK");
				this.spawnLocations.add(new Location(world, b.getX(), b.getY(), b.getZ()));
			}
		}
	}
}
