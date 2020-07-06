package coffee.weneed.founddiamonds;

import java.util.Date;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Trap {

	private final byte type;
	private final Material mat;
	private Material[] oldMat; // the blocks that were replaced
	private final String placer; // name of the player who set the trap
	private final Location location; // the 'middle' of the trap
	private final Date time; // the date the trap was added;
	private boolean persistent; // will the trap persist when broken

	public Trap(byte type, Material mat, Material[] oldMat, String player, Location loc, long time, boolean persistent) {
		this.type = type;
		this.mat = mat;
		this.oldMat = oldMat;
		placer = player;
		location = loc;
		this.time = new Date(time);
		this.persistent = persistent;
		//trapList.add(this);
		refillInverse();
	}

	public Trap(byte type, Material mat, String player, Location location, boolean persistent) {
		this.type = type;
		this.mat = mat;
		placer = player;
		this.location = location;
		time = new Date(System.currentTimeMillis());
		this.persistent = persistent;
	}

	public boolean createBlocks() {
		Block[] locations = returnLocations(location.getWorld());
		oldMat = new Material[locations.length];
		for (Block block : locations) {
			//if (inverseList.containsKey(block)) {
			//return false;
			//}
		}
		if (mat == Material.EMERALD_ORE) {
			oldMat[0] = location.getBlock().getType();
			//inverseList.put(location.getBlock(), this);
			location.getBlock().setType(mat);
		} else {
			oldMat[0] = locations[0].getType();
			oldMat[1] = locations[1].getType();
			oldMat[2] = locations[2].getType();
			oldMat[3] = locations[3].getType();
			//inverseList.put(locations[0], this);
			//inverseList.put(locations[1], this);
			//inverseList.put(locations[2], this);
			//inverseList.put(locations[3], this);
			locations[0].setType(mat);
			locations[1].setType(mat);
			locations[2].setType(mat);
			locations[3].setType(mat);
		}
		return true;
	}

	public int getID() {
		//return TrapHandler.getTrapList().indexOf(this);
		return 1; //FIXME bogus
	}

	public Location getLocation() {
		return location;
	}

	public Material getMaterial() {
		return mat;
	}

	public String getPlacer() {
		return placer;
	}

	public Date getTime() {
		return time;
	}

	public String getTrapSummary() { // method to summarize the trap object, for saving
		String oldMatString = "";
		for (Material material : oldMat) {
			oldMatString += material.getId() + ";";
		}
		return type + ";" + mat.getId() + ";" + oldMatString + placer + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ() + ";" + location.getWorld().getName() + ";" + time.getTime() + ";" + persistent;
	}

	public boolean isPersistent() {
		return persistent;
	}

	private void refillInverse() {
		Block[] temp = returnLocations(location.getWorld());
		for (Block block : temp) {
			//inverseList.put(block, this);
		}
	}

	private Block[] returnLocations(World world) {
		Block block1;
		Block block2;
		Block block3;
		Block block4;
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();

		switch (type) {
			case 1:
				block1 = world.getBlockAt(x, y - 1, z);
				block2 = world.getBlockAt(x, y - 2, z + 1);
				block3 = world.getBlockAt(x - 1, y - 2, z);
				block4 = world.getBlockAt(x, y - 2, z);
				return new Block[] { block1, block2, block3, block4 };
			case 2:
				block1 = world.getBlockAt(x, y - 1, z);
				block2 = world.getBlockAt(x - 1, y - 2, z);
				block3 = world.getBlockAt(x, y - 2, z);
				block4 = world.getBlockAt(x - 1, y - 1, z);
				return new Block[] { block1, block2, block3, block4 };
			case 3:
				return new Block[] { location.getBlock() }; // emeralds
			default:
				System.out.println("FoundDiamonds: Trap has no type!");
				return null; // aliens
		}
	}
}
