package de.javabasti.fadedprison.autominer.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.javabasti.fadedprison.autominer.AutoMiner;

public class AutoMinerTime_Setup {

	public static void addAutoMinerTimeSeconds(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
		cfg.set("AutoMiner.User.timeLeft", timeLeftinSeconds + time);
		try {
			cfg.save(file);
		} catch (IOException e1) {
			e1.printStackTrace();
			p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
		}
	}

	public static void addAutoMinerTimeMinutes(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
		cfg.set("AutoMiner.User.timeLeft", timeLeftinSeconds + (time * 60));
		try {
			cfg.save(file);
		} catch (IOException e1) {
			e1.printStackTrace();
			p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
		}
	}

	public static void addAutoMinerTimeHours(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
		cfg.set("AutoMiner.User.timeLeft", timeLeftinSeconds + (time * 60) * 60);
		try {
			cfg.save(file);
		} catch (IOException e1) {
			e1.printStackTrace();
			p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
		}
	}

	public static void addAutoMinerTimeDays(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
		cfg.set("AutoMiner.User.timeLeft", timeLeftinSeconds + ((time * 60) * 60) * 24);
		try {
			cfg.save(file);
		} catch (IOException e1) {
			e1.printStackTrace();
			p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
		}
	}

	public static Integer getAutoMinerTime(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getInt("AutoMiner.User.timeLeft");
	}

	public static void setAutoMinerTimeSeconds(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("AutoMiner.User.timeLeft", time);
		try {
			cfg.save(file);
		} catch (IOException e1) {
			e1.printStackTrace();
			p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
		}
	}

	public static void setAutoMinerTimeMinutes(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("AutoMiner.User.timeLeft", time * 60);
		try {
			cfg.save(file);
		} catch (IOException e1) {
			e1.printStackTrace();
			p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
		}
	}

	public static void setAutoMinerTimeHours(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("AutoMiner.User.timeLeft", (time * 60) *60);
		try {
			cfg.save(file);
		} catch (IOException e1) {
			e1.printStackTrace();
			p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
		}
	}

	public static void setAutoMinerTimeDays(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("AutoMiner.User.timeLeft", ((time *60 ) * 60) * 24);
		try {
			cfg.save(file);
		} catch (IOException e1) {
			e1.printStackTrace();
			p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
		}
	}

	public static void removeAutoMinerTimeSeconds(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
		if (cfg.getInt("AutoMiner.User.timeLeft") > 0) {
			cfg.set("AutoMiner.User.timeLeft", timeLeftinSeconds - time);
			try {
				cfg.save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
				p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
			}
		}
	}

	public static void removeAutoMinerTimeMinutes(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
		if (cfg.getInt("AutoMiner.User.timeLeft") > 0) {
			cfg.set("AutoMiner.User.timeLeft", timeLeftinSeconds - (time * 60));
			try {
				cfg.save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
				p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
			}
		}
	}

	public static void removeAutoMinerTimeHours(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
		if (cfg.getInt("AutoMiner.User.timeLeft") > 0) {
			cfg.set("AutoMiner.User.timeLeft", timeLeftinSeconds - (time * 60) * 60);
			try {
				cfg.save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
				p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
			}
		}
	}

	public static void removeAutoMinerTimeDays(Player p, int time) {
		File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
				String.valueOf(p.getUniqueId().toString()) + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
		if (cfg.getInt("AutoMiner.User.timeLeft") > 0) {
			cfg.set("AutoMiner.User.timeLeft", timeLeftinSeconds - ((time * 60) * 60) * 24);
			try {
				cfg.save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
				p.sendMessage("§cThere was a error while saving your data, pls message an admin about it.");
			}
		}
	}

}
