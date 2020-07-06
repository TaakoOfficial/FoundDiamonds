package coffee.weneed.founddiamonds;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import coffee.weneed.founddiamonds.file.Config;
import coffee.weneed.founddiamonds.util.Prefix;

public class Permissions {

	private FoundDiamonds fd;

	public Permissions(FoundDiamonds fd) {
		this.fd = fd;
	}

	public boolean hasAdminManagementPerm(CommandSender sender) {
		return hasPerm(sender, "fd.manage.admin.add") || hasPerm(sender, "fd.manage.admin.remove") || hasPerm(sender, "fd.manage.admin.list");
	}

	public boolean hasAdminMessagePerm(CommandSender sender) {
		return hasPerm(sender, "fd.admin");
	}

	public boolean hasAnyMenuPerm(CommandSender sender) {
		return hasPerm(sender, "fd.manage.config") || hasPerm(sender, "fd.manage.reload") || hasPerm(sender, "fd.manage.toggle") || hasPerm(sender, "fd.manage.admin.add") || hasPerm(sender, "fd.manage.admin.remove") || hasPerm(sender, "fd.manage.world") || hasPerm(sender, "fd.manage.admin.list") || hasPerm(sender, "fd.manage.broadcast.add") || hasPerm(sender, "fd.manage.broadcast.remove") || hasPerm(sender, "fd.manage.broadcast.list") || hasPerm(sender, "fd.manage.light.add") || hasPerm(sender, "fd.manage.light.list") || hasPerm(sender, "fd.manage.light.remove") || hasPerm(sender, "fd.trap");
	}

	public boolean hasBroadcastManagementPerm(CommandSender sender) {
		return hasPerm(sender, "fd.manage.broadcast.add") || hasPerm(sender, "fd.broadcast.remove") || hasPerm(sender, "fd.broadcast.list");
	}

	public boolean hasBroadcastPerm(CommandSender sender) {
		return hasPerm(sender, "fd.broadcast");
	}

	public boolean hasConfigPerm(CommandSender sender) {
		return hasPerm(sender, "fd.manage.config");
	}

	public boolean hasLightManagementPerm(CommandSender sender) {
		return hasPerm(sender, "fd.manage.light.add") || hasPerm(sender, "fd.manage.light.remove") || hasPerm(sender, "fd.manage.light.list");
	}

	public boolean hasMonitorPerm(CommandSender sender) {
		return hasPerm(sender, "fd.monitor");
	}

	public boolean hasNotifyPerm(CommandSender sender) {
		return hasPerm(sender, "fd.notify");
	}

	public boolean hasNotifySelfPerm(CommandSender sender) {
		return hasPerm(sender, "fd.notify.self");
	}

	public boolean hasPerm(CommandSender sender, String permission) {
		return sender.hasPermission(permission) || fd.getConfig().getBoolean(Config.opsAsFDAdmin) && sender.isOp();
	}

	public boolean hasReloadPerm(CommandSender sender) {
		return hasPerm(sender, "fd.manage.reload");
	}

	public boolean hasTogglePerm(CommandSender sender) {
		return hasPerm(sender, "fd.manage.toggle");
	}

	public boolean hasTrapPerm(CommandSender sender) {
		return hasPerm(sender, "fd.trap");
	}

	public boolean hasTrapRemovalPerm(CommandSender sender) {
		return hasPerm(sender, "fd.trap.remove.self") || hasPerm(sender, "fd.trap.remove.all");
	}

	public boolean hasWorldManagementPerm(CommandSender sender) {
		return hasPerm(sender, "fd.manage.world");
	}

	public void sendPermissionsMessage(CommandSender sender) {
		sender.sendMessage(Prefix.getChatPrefix() + ChatColor.RED + " You don't have permission to do that.");
		fd.getLog().warning(sender.getName() + " was denied access to a command.");
	}

}
