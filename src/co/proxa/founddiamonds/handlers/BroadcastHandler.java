package co.proxa.founddiamonds.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.CrossServerListener;
import com.dthielke.herochat.Herochat;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import co.proxa.founddiamonds.FoundDiamonds;
import co.proxa.founddiamonds.file.Config;
import co.proxa.founddiamonds.util.Format;
import co.proxa.founddiamonds.util.PluginUtils;
import co.proxa.founddiamonds.util.Prefix;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class BroadcastHandler {

    private FoundDiamonds fd;

    public BroadcastHandler(FoundDiamonds fd) {
        this.fd = fd;
    }

    public void handleBroadcast(final Material mat,final int blockTotal, final Player player, final int lightLevel) {
        broadcastFoundBlock(player, mat, blockTotal, lightLevel);
        if (mat== Material.DIAMOND_ORE) {
            if (fd.getConfig().getBoolean(Config.potionsForFindingDiamonds)) {
                fd.getPotionHandler().handlePotions(player);
            }
            if (fd.getConfig().getBoolean(Config.itemsForFindingDiamonds)) {
                fd.getItemHandler().handleRandomItems(player);
            }
        }
    }

    private void broadcastFoundBlock(final Player player, final Material mat, final int blockTotal, final int lightLevel) {    	
        String matName = Format.getFormattedName(mat, blockTotal);
        ChatColor color = fd.getMapHandler().getBroadcastedBlocks().get(mat);
        double lightPercent = ((double)lightLevel / 15) * 100;
        DecimalFormat df = new DecimalFormat("##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String formattedPercent = df.format(lightPercent);
        String message = fd.getConfig().getString(Config.bcMessage).replace("@Prefix@", Prefix.getChatPrefix() + color).replace("@Player@",
                getBroadcastName(player) + (fd.getConfig().getBoolean(Config.useOreColors) ? color : "")).replace("@Number@",
                (blockTotal) == 500 ? "over 500" :String.valueOf(blockTotal)).replace("@BlockName@", matName).replace(
                "@LightLevel@", String.valueOf(lightLevel)).replace("@LightPercent@", formattedPercent + "%").replace("@Server@", fd.getConfig().getString(Config.herochatServerName));
        
        String formatted = PluginUtils.customTranslateAlternateColorCodes('&', message);
        fd.getServer().getConsoleSender().sendMessage(formatted);
       
        if(fd.getConfig().getBoolean(Config.useBungeeCord)){
        	List<String> bungeeadmins = (List<String>) fd.getConfig().getList(Config.BungeeCordAdminList);
        	if(bungeeadmins.size() > 0){
        		for(String admin : bungeeadmins){
        			if(Bukkit.getPlayer(admin) == null){
        				ByteArrayDataOutput out = ByteStreams.newDataOutput();
                  		out.writeUTF("Message");
                  		out.writeUTF(admin);                  		  
                  		out.writeUTF(formatted);
                  		player.sendPluginMessage(fd, "BungeeCord", out.toByteArray());        		
        			}            		       		  
            	}
        	}else{
        		System.out.println("[ERROR] Founddiamonds: Bungeecordsupport is enabled but no admins are defined. Can't send infos!");
        	}
        }
        if(Bukkit.getPluginManager().isPluginEnabled(Herochat.getPlugin().getName()) && fd.getConfig().getBoolean(Config.enableHerochat)) {
    		Channel reportChannel = Herochat.getChannelManager().getChannel(fd.getConfig().getString(Config.herochatChannel));    		
    		sendMessageToHerochat(formatted, reportChannel);
    	}else {
    		for (Player x : fd.getServer().getOnlinePlayers()) {
    			if (fd.getPermissions().hasBroadcastPerm(x) && fd.getWorldHandler().isEnabledWorld(x) && !fd.getAdminMessageHandler().receivedAdminMessage(x)) {
    				x.sendMessage(formatted);
    			}
    		}  
    	}            
        if (fd.getConfig().getBoolean(Config.cleanLog)) {
            fd.getLoggingHandler().writeToCleanLog(matName, blockTotal, player.getName());
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
    
    private String getBroadcastName(Player player) {
        return (fd.getConfig().getBoolean(Config.useNick) ? player.getDisplayName() : player.getName());
    }
}
