package com.nuclearw.donatorchat;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class DonatorChatPlayerListener implements Listener {
	public static DonatorChat plugin;

	public DonatorChatPlayerListener(DonatorChat instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChat(PlayerChatEvent event) 
	{
		Player player = event.getPlayer();

		String y = ChatColor.GOLD + "[" + ChatColor.WHITE + player.getName() + ChatColor.GOLD + "] ";

		if(player.hasPermission("donatorchat.use") && plugin.modeOn.contains(player))
		{
			plugin.log.log(Level.INFO, "[D]"+"<"+player.getName()+"> "+event.getMessage());
			event.setCancelled(true);
			for(Player herp : plugin.getServer().getOnlinePlayers()){
				if(herp.hasPermission("donatorchat.use")){
					herp.sendMessage(y+event.getMessage());
				}
			}
			return;
		}
	}
}
