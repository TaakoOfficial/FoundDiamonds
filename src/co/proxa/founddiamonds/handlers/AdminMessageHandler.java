package co.proxa.founddiamonds.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.CrossServerListener;
import com.dthielke.herochat.Herochat;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import co.proxa.founddiamonds.FoundDiamonds;
import co.proxa.founddiamonds.file.Config;
import co.proxa.founddiamonds.util.Format;
import co.proxa.founddiamonds.util.Prefix;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class AdminMessageHandler {

    private FoundDiamonds fd;
    private HashSet<String> receivedAdminMessage = new HashSet<String>();

    public AdminMessageHandler(FoundDiamonds fd) {
        this.fd = fd;
    }

    public void sendAdminMessage(final Material mat, final int blockTotal, final Player player) {
    	 System.out.println("called adminmsg");
		String adminMessage = Prefix.getAdminPrefix() + " " + ChatColor.YELLOW + player.getName() + " on " + ChatColor.DARK_RED + fd.getConfig().getString(Config.BungeeCordServer) + ChatColor.YELLOW + " just found " + this.fd.getMapHandler().getAdminMessageBlocks().get(mat) + (blockTotal == 500 ? "over 500 " : String.valueOf(blockTotal)) + " " + Format.getFormattedName(mat, blockTotal);
        fd.getServer().getConsoleSender().sendMessage(adminMessage);
       

        if(Bukkit.getPluginManager().isPluginEnabled(Herochat.getPlugin().getName()) && fd.getConfig().getBoolean(Config.enableHerochat)) {
        	Channel reportChannel = Herochat.getChannelManager().getChannel(fd.getConfig().getString(Config.herochatChannel));
    		sendMessageToHerochat(adminMessage, reportChannel);
    	}else {
    		 for (Player y : fd.getServer().getOnlinePlayers()) {
    	        	if (fd.getPermissions().hasAdminMessagePerm(y) && y != player) {
    	                y.sendMessage(adminMessage);
    	                if (!receivedAdminMessage.contains(y.getName())) {
    	                    receivedAdminMessage.add(y.getName());
    	                }
    	            }
    	        }     
    	}   
        if(fd.getConfig().getBoolean(Config.useBungeeCord)){
        	List<String> bungeeadmins = (List<String>) fd.getConfig().getList(Config.BungeeCordAdminList);
        	if(bungeeadmins.size() > 0){
        		for(String admin : bungeeadmins){
        			if(Bukkit.getPlayer(admin) == null){
        				ByteArrayDataOutput out = ByteStreams.newDataOutput();
                  		out.writeUTF("Message");
                  		out.writeUTF(admin);                  		  
                  		out.writeUTF(adminMessage);
                  		player.sendPluginMessage(fd, "BungeeCord", out.toByteArray());             		
        			}            		       		  
            	}
        	}else{
        		System.out.println("[ERROR] Founddiamonds: Bungeecordsupport is enabled but no admins are defined. Can't send infos!");
        	}
        }
    }

    private void sendMessageToHerochat(String adminMessage,Channel reportChannel) {
    	String preformatedmsg = reportChannel.applyFormat(Herochat.getChannelManager().getStandardFormat(),"");       		
		preformatedmsg = preformatedmsg.replace("{prefix}", "");
		preformatedmsg = preformatedmsg.replace("{suffix}", "");
        preformatedmsg = preformatedmsg.replace("{group}", "");
        preformatedmsg = preformatedmsg.replace("{groupprefix}", "");
        preformatedmsg = preformatedmsg.replace("{groupsuffix}","");            
        preformatedmsg = preformatedmsg.replace("{sender}",fd.getConfig().getString(Config.herochatSender));
        preformatedmsg = preformatedmsg.replace("{msg}",adminMessage);    	            
        reportChannel.sendRawMessage(preformatedmsg);
        CrossServerListener.getInstance().sendChannelMessage(reportChannel, preformatedmsg);
    }
    
    public void clearReceivedAdminMessage() {
        receivedAdminMessage.clear();
    }

    public boolean receivedAdminMessage(Player player) {
        return receivedAdminMessage.contains(player.getName());
    }
}