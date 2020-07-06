package coffee.weneed.founddiamonds;

import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import coffee.weneed.founddiamonds.file.Config;
import coffee.weneed.founddiamonds.file.FileHandler;
import coffee.weneed.founddiamonds.file.FileUtils;
import coffee.weneed.founddiamonds.handlers.AdminMessageHandler;
import coffee.weneed.founddiamonds.handlers.BroadcastHandler;
import coffee.weneed.founddiamonds.handlers.CommandHandler;
import coffee.weneed.founddiamonds.handlers.ItemHandler;
import coffee.weneed.founddiamonds.handlers.LightLevelHandler;
import coffee.weneed.founddiamonds.handlers.LoggingHandler;
import coffee.weneed.founddiamonds.handlers.MapHandler;
import coffee.weneed.founddiamonds.handlers.MenuHandler;
import coffee.weneed.founddiamonds.handlers.PotionHandler;
import coffee.weneed.founddiamonds.handlers.TrapHandler;
import coffee.weneed.founddiamonds.handlers.WorldHandler;
import coffee.weneed.founddiamonds.listeners.BlockBreakListener;
import coffee.weneed.founddiamonds.listeners.BlockDamageListener;
import coffee.weneed.founddiamonds.listeners.BlockPlaceListener;
import coffee.weneed.founddiamonds.listeners.PistonListener;
import coffee.weneed.founddiamonds.listeners.PlayerDamageListener;
import coffee.weneed.founddiamonds.listeners.TrapListener;
import coffee.weneed.founddiamonds.metrics.MetricsLite;
import coffee.weneed.founddiamonds.sql.MySQL;

/*
Copyright 2011-2013 Blake Bartenbach

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

public class FoundDiamonds extends JavaPlugin {

	private static Logger log;
	public static FoundDiamonds fd;
	private final BlockPlaceListener blockPlaceListener = new BlockPlaceListener(this);
	private final BlockBreakListener blockBreakListener = new BlockBreakListener(this);
	private final PlayerDamageListener playerDamageListener = new PlayerDamageListener(this);
	private final PistonListener pistonListener = new PistonListener(this);
	private final TrapListener trapListener = new TrapListener(this);
	private final BroadcastHandler broadcastHandler = new BroadcastHandler(this);
	private final AdminMessageHandler adminMessageHandler = new AdminMessageHandler(this);
	private final BlockDamageListener lightLevelListener = new BlockDamageListener(this);
	private final LightLevelHandler lightLevelHandler = new LightLevelHandler(this);
	private final FileUtils fileUtils = new FileUtils(this);
	private final MySQL mysql = new MySQL(this);
	private final MapHandler mapHandler = new MapHandler(this);
	private final Permissions permissions = new Permissions(this);
	private final WorldHandler worldHandler = new WorldHandler(this);
	private final LoggingHandler loggingHandler = new LoggingHandler(this);
	private final TrapHandler trapHandler = new TrapHandler(this);
	private final FileHandler fileHandler = new FileHandler(this);
	private final PotionHandler potionHandler = new PotionHandler(this);
	private final ItemHandler itemHandler = new ItemHandler(this);
	private final MenuHandler menuHandler = new MenuHandler(this);
	private final BlockCounter blockTotal = new BlockCounter(this);

	/*
	TODO:
	MenuHandler set area?
	Is clean logging in SQL a popular request?
	Customizable admin message formats!
	Customizable light level message formats!
	*/

	/*
	Changelog:
	
	*/

	/*
	Test:
	Ideally?  Everything.
	*/

	public void debug(String msg) {
		if (getConfig().getBoolean(Config.debug)) {
			log.info("[FoundDiamonds-Debug] " + msg);
		}

	}

	public AdminMessageHandler getAdminMessageHandler() {
		return adminMessageHandler;
	}

	public BlockCounter getBlockCounter() {
		return blockTotal;
	}

	public BlockPlaceListener getBlockPlaceListener() {
		return blockPlaceListener;
	}

	public BroadcastHandler getBroadcastHandler() {
		return broadcastHandler;
	}

	public FileHandler getFileHandler() {
		return fileHandler;
	}

	public FileUtils getFileUtils() {
		return fileUtils;
	}

	public ItemHandler getItemHandler() {
		return itemHandler;
	}

	public LightLevelHandler getLightLevelHandler() {
		return lightLevelHandler;
	}

	public Logger getLog() {
		return log;
	}

	public LoggingHandler getLoggingHandler() {
		return loggingHandler;
	}

	public MapHandler getMapHandler() {
		return mapHandler;
	}

	public MenuHandler getMenuHandler() {
		return menuHandler;
	}

	public MySQL getMySQL() {
		return mysql;
	}

	public PluginDescriptionFile getPdf() {
		return getDescription();
	}

	public Permissions getPermissions() {
		return permissions;
	}

	public PotionHandler getPotionHandler() {
		return potionHandler;
	}

	public TrapHandler getTrapHandler() {
		return trapHandler;
	}

	public WorldHandler getWorldHandler() {
		return worldHandler;
	}

	@Override
	public void onDisable() {
		log.info("Saving all data...");
		fileHandler.saveFlatFileData();
		log.info("Disabled");
	}

	@Override
	public void onEnable() {
		log = getLogger();
		fileHandler.initFileVariables();
		fileHandler.checkFiles();
		potionHandler.getPotionList();
		worldHandler.checkWorlds();
		mapHandler.loadAllBlocks();
		getCommand("fd").setExecutor(new CommandHandler(this));
		registerEvents();
		startMetrics();
		mysql.getConnection();
		FoundDiamonds.fd = this;
		log.info("Enabled");
	}

	public void registerEvents() {
		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerDamageListener, this);
		pm.registerEvents(blockBreakListener, this);
		pm.registerEvents(blockPlaceListener, this);
		pm.registerEvents(pistonListener, this);
		pm.registerEvents(trapListener, this);
		pm.registerEvents(lightLevelListener, this);
		System.out.println(getConfig().getBoolean(Config.useBungeeCord) + " bungeecord");
		if (getConfig().getBoolean(Config.useBungeeCord)) {
			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		}
	}

	private void startMetrics() {
		if (getConfig().getBoolean(Config.metrics)) {
			try {
				MetricsLite metrics = new MetricsLite(this);
				metrics.start();
			} catch (IOException e) {
				log.warning("Metrics failed to start - Ignoring.");
			}
		}
	}
}
