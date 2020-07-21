package coffee.weneed.founddiamonds.listeners;

import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import coffee.weneed.founddiamonds.FoundDiamonds;
import coffee.weneed.founddiamonds.LLoc;
import coffee.weneed.founddiamonds.file.Config;

public class BlockPlaceListener implements Listener {



	private HashSet<LLoc> placed = new HashSet<>();
	private FoundDiamonds fd;

	public BlockPlaceListener(FoundDiamonds fd) {
		this.fd = fd;
	}

	public void addBlock(BlockPlaceEvent event) {
		if (fd.getConfig().getBoolean(Config.mysqlEnabled)) {
			fd.getMySQL().updatePlacedBlockinSQL(event.getBlock().getLocation());
		} else {
			placed.add(new LLoc(event.getBlock().getLocation()));
		}
	}

	public void clearPlaced() {
		placed.clear();
	}

	public HashSet<LLoc> getFlatFilePlacedBlocks() {
		return placed;
	}

	public boolean isMonitoredBlock(BlockPlaceEvent event) {
		final Material mat = event.getBlock().getType();
		return fd.getMapHandler().getAdminMessageBlocks().containsKey(mat) || fd.getMapHandler().getBroadcastedBlocks().containsKey(mat) || fd.getMapHandler().getLightLevelBlocks().containsKey(mat);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (fd.getWorldHandler().isEnabledWorld(event.getPlayer())) {
			if (isMonitoredBlock(event)) {
				addBlock(event);
			}
		}
	}

}
