package coffee.weneed.founddiamonds.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class BlockColor {

	public static ChatColor getBlockColor(Material mat) {
		switch (mat) {
			case DIAMOND_ORE:
			case DIAMOND_BLOCK:
			case DEEPSLATE_DIAMOND_ORE:
				return ChatColor.AQUA;
			case NETHER_WART:
			case REDSTONE_TORCH:
				return ChatColor.DARK_RED;
			case GOLD_ORE:
			case PUMPKIN:
			case JACK_O_LANTERN:
			case GLOWSTONE:
			case WHEAT:
			case GOLD_BLOCK:
			case DEEPSLATE_GOLD_ORE:
			case GILDED_BLACKSTONE:
			case NETHER_GOLD_ORE:
				return ChatColor.GOLD;
			case MOSSY_COBBLESTONE:
			case OAK_LEAVES:
			case VINE:
			case TALL_GRASS:
			case LILY_PAD:
				return ChatColor.DARK_GREEN;
			case IRON_ORE:
			case CLAY:
			case CAULDRON:
			case IRON_BARS:
			case STONE:
			case CHISELED_STONE_BRICKS:
			case COBBLESTONE:
			case COBBLESTONE_STAIRS:
			case CLAY_BALL:
			case GRAVEL:
			case DISPENSER:
			case FURNACE:
			case IRON_DOOR:
			case STONE_BUTTON:
			case DEEPSLATE_IRON_ORE:
				return ChatColor.GRAY;
			case LAPIS_BLOCK:
			case LAPIS_ORE:
			case DEEPSLATE_LAPIS_ORE:
				return ChatColor.BLUE;
			case SPAWNER:
			case BROWN_MUSHROOM:
			case SOUL_SAND:
				return ChatColor.DARK_GRAY;
			case OBSIDIAN:
			case MYCELIUM:
			case END_PORTAL:
				return ChatColor.DARK_PURPLE;
			case MELON:
			case SUGAR_CANE:
			case CACTUS:
			case GRASS:
			case OAK_SAPLING:
			case EMERALD_BLOCK:
			case EMERALD_ORE:
			case DEEPSLATE_COPPER_ORE:
			case COPPER_ORE:
			case DEEPSLATE_EMERALD_ORE:
				return ChatColor.GREEN;
			case BRICK:
			case BRICK_STAIRS:
			case RED_MUSHROOM:
			case NETHERRACK:
			case TNT:
			case REDSTONE_ORE:
			case DEEPSLATE_REDSTONE_ORE:
				return ChatColor.RED;
			case SPONGE:
			case SUNFLOWER:
			case SAND:
			case SANDSTONE:
			case TORCH:
				return ChatColor.YELLOW;
			case DEEPSLATE_COAL_ORE:
			case COAL_ORE:
				return ChatColor.BLACK;
			default:
				return ChatColor.WHITE;
		}
	}
}
