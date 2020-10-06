package coffee.weneed.founddiamonds.handlers;

import java.util.List;
import java.util.Random;
import org.bukkit.entity.Player;
import coffee.weneed.founddiamonds.FoundDiamonds;
import coffee.weneed.founddiamonds.file.Config;

public class CommandRewardHandler {
	public static int randInt(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

	public static List<String> randomItems() {
		return (List<String>) FoundDiamonds.fd.getConfig().getList(Config.possibleCommands);
	}

	private FoundDiamonds fd;

	public CommandRewardHandler(FoundDiamonds fd) {
		this.fd = fd;
	}


	//@SuppressWarnings("deprecation")
	private void runCommand(Player player, String command) {
		fd.getServer().dispatchCommand(fd.getServer().getConsoleSender(), command.replace("@player@", player.getName()));
	}

	public void handleRandomItems(final Player player) {
		int randomInt = (int) (Math.random() * 100);
		if (randomInt <= fd.getConfig().getInt(Config.chanceToRunCommand)) {
			selectRandomItem(player);
		}
	}

	private void selectRandomItem(Player player) {
		List<String> coms = randomItems();
		runCommand(player, coms.get(randInt(1, coms.size() - 1)));
	}
}
