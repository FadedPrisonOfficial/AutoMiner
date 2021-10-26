package de.javabasti.fadedprison.autominer;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import de.javabasti.fadedprison.autominer.commands.COMMAND_AutoMiner;
import de.javabasti.fadedprison.autominer.events.EVENT_EnterAutoMiner;
import de.javabasti.fadedprison.autominer.events.EVENT_Join;
import de.javabasti.fadedprison.autominer.utils.SetupConfig;
import de.javabasti.fadedprison.autominer.utils.SetupEnchantsettings;

public final class AutoMiner extends JavaPlugin {

	public static AutoMiner instance;
	public HashMap<String, BukkitTask> Autominelist = new HashMap<String, BukkitTask>();
	public SetupConfig setupConfig;
	public SetupEnchantsettings setupEnchantsettings;;

	public static AutoMiner getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		this.setupConfig = new SetupConfig();
		this.setupEnchantsettings = new SetupEnchantsettings();
		registerEvents();
		registerCMDS();
		
		System.out.println(AutoMiner.getInstance().colorize("================"));
		System.out.println(AutoMiner.getInstance().colorize("Author: JavaBasti"));
		System.out.println(AutoMiner.getInstance().colorize("Made for FadedPrison."));
		System.out.println(AutoMiner.getInstance().colorize("AutoMiner successfully started."));
		System.out.println(AutoMiner.getInstance().colorize("================"));

	}

	@Override
	public void onDisable() {
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(Autominelist.containsKey(all.getUniqueId().toString())) {
				Autominelist.get(all.getUniqueId().toString()).cancel();
				Autominelist.remove(all.getUniqueId().toString());
			}
		}
		System.out.println(AutoMiner.getInstance().colorize("================"));
		System.out.println(AutoMiner.getInstance().colorize("Author: JavaBasti"));
		System.out.println(AutoMiner.getInstance().colorize("Made for FadedPrison."));
		System.out.println(AutoMiner.getInstance().colorize("AutoMiner successfully stopped."));
		System.out.println(AutoMiner.getInstance().colorize("================"));
	}

	private void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new EVENT_Join(), this);
		pm.registerEvents(new EVENT_EnterAutoMiner(), this);
	}

	private void registerCMDS() {
		Bukkit.getPluginCommand("autominer").setExecutor(new COMMAND_AutoMiner());
	}

	public String colorize(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

}
