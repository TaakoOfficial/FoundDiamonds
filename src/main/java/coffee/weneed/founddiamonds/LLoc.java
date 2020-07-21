package coffee.weneed.founddiamonds;

import org.bukkit.Location;
import org.bukkit.World;

public class LLoc extends Location {
	private String worldName;

	public LLoc(String worldname, World world, double x, double y, double z) {
		super(world, x, y, z);
		setWorldName(worldName);
	}

	public LLoc(Location loc) {
		this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
	}

	public LLoc(World world, double x, double y, double z) {
		this(world.getName(), world, x, y, z);
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

}