package com.nuclearw.donatorchat;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DonatorChat extends JavaPlugin {
	static String mainDirectory = "plugins" + File.separator + "DonatorChat";
	File versionFile = new File(this.getDataFolder() + File.separator + "VERSION");

	private final DonatorChatPlayerListener playerListener = new DonatorChatPlayerListener(this);
	
	public HashSet<Player> modeOn = new HashSet<Player>();
	
	Logger log = Logger.getLogger("Minecraft");
	Properties prop = new Properties();
	
	public void onEnable() {
		new File(mainDirectory).mkdir();
		
		if(!versionFile.exists()) {
			updateVersion();
		} else {
			String vnum = readVersion();
			if(vnum.equalsIgnoreCase("0.1")) updateVersion();
		}
		
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvents(playerListener, this);
		
		log.info("[DonatorChat] Version "+this.getDescription().getVersion()+" enabled.");
	}
	
	public void onDisable() {
		log.info("[DonatorChat] Version "+this.getDescription().getVersion()+" disabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("d")){
			// Console message?
			if(!(sender instanceof Player) && args.length >= 1) 
			{
				String aMessage = args[0];
				for (int i = 1; i <= args.length - 1; i++) {
					aMessage = aMessage + " " + args[i];
				}

				String aPrefix = ChatColor.GOLD + "[" + ChatColor.WHITE
				+ "*Console*" + ChatColor.GOLD + "] ";

				log.log(Level.INFO, "[D]<*Console*> "
						+ aMessage);

				for (Player herp : Bukkit.getServer().getOnlinePlayers()) {
					if (herp.hasPermission("donatorchat.use"))
						herp.sendMessage(aPrefix + aMessage);
				}
				return true;
			}
			
			Player player = (Player) sender;

			if(!player.hasPermission("donatorchat.use"))
			{
				player.sendMessage(ChatColor.DARK_RED+"You don't have Shatt's blessing to do that!");  
				return true;
			}

			// Not a toggle, a message

			if (args.length >= 1) {
				String aMessage = args[0];
				for (int i = 1; i <= args.length - 1; i++) {
					aMessage = aMessage + " " + args[i];
				}

				String aPrefix = ChatColor.GOLD + "[" + ChatColor.WHITE
				+ player.getName() + ChatColor.GOLD + "] ";
				log.log(Level.INFO, "[D]<" + player.getName() + "> "
						+ aMessage);
				for (Player herp : Bukkit.getServer().getOnlinePlayers()) {
					if (herp.hasPermission("donatorchat.use"))
						herp.sendMessage(aPrefix + aMessage);
				}
				return true;
			}

			if(modeOn.contains(player)) {
				modeOn.remove(player);
			} else {
				modeOn.add(player);
			}


			if(modeOn.contains(player))
			{
				player.sendMessage(ChatColor.GOLD + "Donator chat toggled " + ChatColor.GREEN + "On");  
			} else {
				player.sendMessage(ChatColor.GOLD + "Donator chat toggled " + ChatColor.RED + "Off");  
			}
		}
		return true;
	}
	
	public void updateVersion() {
		try {
			versionFile.createNewFile();
			BufferedWriter vout = new BufferedWriter(new FileWriter(versionFile));
			vout.write(this.getDescription().getVersion());
			vout.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		}
	}

	public String readVersion() {
		byte[] buffer = new byte[(int) versionFile.length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(versionFile));
			f.read(buffer);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (f != null) try { f.close(); } catch (IOException ignored) { }
		}
		
		return new String(buffer);
	}
	
    public boolean isPlayer(CommandSender sender) {
        return sender != null && sender instanceof Player;
    }
}
