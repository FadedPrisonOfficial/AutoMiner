package de.javabasti.fadedprison.autominer.events;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.javabasti.fadedprison.autominer.AutoMiner;

public class EVENT_Join implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e2) {
				e2.printStackTrace();
				p.sendMessage("§cThere was a error while creating your data, pls message an admin about it.");
			}
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			cfg.set("AutoMiner.User.Name", p.getName());
			cfg.set("AutoMiner.User.UUID", p.getUniqueId().toString());
			cfg.set("AutoMiner.User.timeLeft", 0);
			try {
				cfg.save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
				p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
			}
		} else {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			try {
				cfg.save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
				p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
			}
		}
	}
}
