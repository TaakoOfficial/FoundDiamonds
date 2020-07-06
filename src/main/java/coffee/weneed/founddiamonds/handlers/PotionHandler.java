package coffee.weneed.founddiamonds.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import coffee.weneed.founddiamonds.FoundDiamonds;
import coffee.weneed.founddiamonds.file.Config;

public class PotionHandler {

	private FoundDiamonds fd;
	private final HashMap<String, Boolean> jumpPotion = new HashMap<>();
	private List<PotionEffectType> potionList;

	public PotionHandler(FoundDiamonds fd) {
		this.fd = fd;
	}

	public void getPotionList() {
		List<String> configList = (List<String>) fd.getConfig().getList(Config.potionList);
		potionList = new ArrayList<>();
		for (String potion : configList) {
			PotionEffectType p = PotionEffectType.getByName(potion);
			if (p != null) {
				potionList.add(PotionEffectType.getByName(potion));
			} else {
				fd.getLogger().warning("Unrecognized potion in config: " + potion);
			}
		}
	}

	private void givePotions(Player player, PotionEffect potion) {
		if (fd.getConfig().getBoolean(Config.awardAllPotions)) {
			for (Player p : fd.getServer().getOnlinePlayers()) {
				if (!p.hasPotionEffect(potion.getType()) && fd.getWorldHandler().isEnabledWorld(p)) {
					p.addPotionEffect(potion);
					if (potion.getType() == PotionEffectType.JUMP) {
						jumpPotion.put(p.getName(), true);
					}
					p.sendMessage(ChatColor.DARK_RED + "Potion awarded");
				}
			}
		} else {
			player.addPotionEffect(potion);
			if (potion.getType() == PotionEffectType.JUMP) {
				jumpPotion.put(player.getName(), true);
			}
			player.sendMessage(ChatColor.DARK_RED + "Potion awarded");
		}
	}

	public void handlePotions(final Player player) {
		int randomInt = (int) (Math.random() * 100);
		if (randomInt <= fd.getConfig().getInt(Config.chanceToGetPotion)) {
			selectRandomPotion(player);
		}
	}

	public boolean playerHasJumpPotion(final Player player) {
		if (jumpPotion.containsKey(player.getName()) && jumpPotion.get(player.getName()) && player.hasPotionEffect(PotionEffectType.JUMP))
			return true;
		else {
			jumpPotion.put(player.getName(), false);
			return false;
		}
	}

	private void selectRandomPotion(Player player) {
		int randomPotionNumber = (int) (Math.random() * potionList.size());
		PotionEffectType p = potionList.get(randomPotionNumber);
		PotionEffect potion = new PotionEffect(p, 3000, fd.getConfig().getInt(Config.potionStrength));
		givePotions(player, potion);
	}
}
