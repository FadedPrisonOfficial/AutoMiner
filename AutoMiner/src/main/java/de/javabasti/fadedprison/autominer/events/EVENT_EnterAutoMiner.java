package de.javabasti.fadedprison.autominer.events;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;

import de.javabasti.fadedprison.autominer.AutoMiner;
import de.javabasti.fadedprison.autominer.utils.SetupConfig;
import de.javabasti.fadedprison.autominer.utils.SetupEnchantsettings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EVENT_EnterAutoMiner implements Listener {

	private HashMap<String, Long> blockRejoin = new HashMap<String, Long>();
	private HashMap<String, Integer> timeWhenJoin = new HashMap<String, Integer>();;
	private HashMap<String, Double> tokensWhenJoin = new HashMap<String, Double>();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onRegionEnter(RegionEnterEvent e) {
		Player p = e.getPlayer();
		if (e.getRegion().getId().equalsIgnoreCase("autominer") && e.isCancellable()) {
			SetupConfig setupConfig = AutoMiner.getInstance().setupConfig;
			SetupEnchantsettings setupEnchantsettings = AutoMiner.getInstance().setupEnchantsettings;
			YamlConfiguration cfgConfig = YamlConfiguration.loadConfiguration(setupConfig.getFile());
			YamlConfiguration cfgSettings = YamlConfiguration.loadConfiguration(setupEnchantsettings.getFile());
			File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
					String.valueOf(p.getUniqueId().toString()) + ".yml");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
			if (timeLeftinSeconds > 0) {
				if (blockRejoin.containsKey(p.getUniqueId().toString())) {
					int blockRejoincooldown = cfgSettings.getInt("AutoMiner.JoinCooldown.Seconds");
					long timeLeftToRejoin = ((blockRejoin.get(p.getUniqueId().toString()) / 1000) + blockRejoincooldown)
							- (System.currentTimeMillis() / 1000);
					if (timeLeftToRejoin > 0) {
						p.sendMessage(AutoMiner.getInstance().colorize(cfgConfig.getString("AutoMiner.Prefix")
								+ "§cYou can't enter the AutoMiner for another §6" + timeLeftToRejoin + " seconds."));
					} else {
						blockRejoin.remove(p.getUniqueId().toString());
					}
					e.setCancelled(true);
				} else {
					for (ItemStack item2 : p.getInventory().getContents()) {
						if (item2 != null && (item2.getType() != Material.AIR)) {
							if (item2.getType().equals(Material.DIAMOND_PICKAXE)) {
								ItemMeta itemmeta = item2.getItemMeta();
								if (itemmeta != null) {
									if (itemmeta.getLore() != null) {
										TokenEnchantAPI tAPI = TokenEnchantAPI.getInstance();
										this.timeWhenJoin.put(p.getUniqueId().toString(), timeLeftinSeconds);
										double tokensOnJoin = tAPI.getTokens(p);
										this.tokensWhenJoin.put(p.getUniqueId().toString(), tokensOnJoin);
										List<String> lore = itemmeta.getLore();
										Map<String, Long> enchantMap = new HashMap<>();
										Map<String, Long> TokenMiner = new HashMap<>();
										Map<String, Long> TokenImpact = new HashMap<>();

										for (String line : lore) {
											String[] enchantment = line.split(" ");
											String enchantName = enchantment[0];
											String enchantLvl = enchantment[1];
											switch (enchantLvl) {
											case "I":
												long l1 = 1;
												enchantMap.put(enchantName, l1);
												break;
											case "II":
												long l2 = 2;
												enchantMap.put(enchantName, l2);
												break;
											case "III":
												long l3 = 3;
												enchantMap.put(enchantName, l3);
												break;
											case "IV":
												long l4 = 4;
												enchantMap.put(enchantName, l4);
												break;
											case "V":
												long l5 = 5;
												enchantMap.put(enchantName, l5);
												break;
											case "VI":
												long l6 = 6;
												enchantMap.put(enchantName, l6);
												break;
											case "VII":
												long l7 = 7;
												enchantMap.put(enchantName, l7);
												break;
											case "VIII":
												long l8 = 8;
												enchantMap.put(enchantName, l8);
												break;
											case "IX":
												long l9 = 9;
												enchantMap.put(enchantName, l9);
												break;
											case "X":
												long l10 = 10;
												enchantMap.put(enchantName, l10);
												break;
											default:
												long level = Long.parseLong(enchantment[1]);
												enchantMap.put(enchantName, level);
												break;
											}

											if (enchantName.contains("TokenMiner")) {
												long lvl = enchantMap.get(enchantName);
												TokenMiner.put("TokenMiner", lvl);
											}
											if (enchantName.contains("TokenImpact")) {
												long lvl = enchantMap.get(enchantName);
												TokenImpact.put("TokenImpact", lvl);
											}
										}

										if (TokenMiner.get("TokenMiner") != null
												&& (TokenImpact.get("TokenImpact") != null)) {
											p.sendMessage(AutoMiner.getInstance()
													.colorize(cfgConfig.getString("AutoMiner.EnteredAutoMiner")
															.replaceAll("%prefix%",
																	cfgConfig.getString("AutoMiner.Prefix"))));
											startAMwithTMandTI(p);
										} else if (TokenImpact.get("TokenImpact") == null) {
											p.sendMessage(AutoMiner.getInstance()
													.colorize(cfgConfig.getString("AutoMiner.EnteredAutoMiner")
															.replaceAll("%prefix%",
																	cfgConfig.getString("AutoMiner.Prefix"))));
											startAMwTMwithoutTI(p);
										}
									} else {
										e.setCancelled(true);
									}
								} else {
									p.sendMessage(
											"§cThere was an error while joining the autominer, pls report this to an admin.");
									e.setCancelled(true);
								}
								break;
							}
						}
					}
				}
			} else {
				e.setCancelled(true);
				p.sendMessage(
						AutoMiner.getInstance().colorize(cfgConfig.getString("AutoMiner.NoAutoMinerTimeLeftOnEnter")
								.replaceAll("%prefix%", cfgConfig.getString("AutoMiner.Prefix"))));
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onRegionLeave(RegionLeaveEvent e) {
		Player p = e.getPlayer();
		SetupConfig setupConfig = AutoMiner.getInstance().setupConfig;
		YamlConfiguration cfgConfig = YamlConfiguration.loadConfiguration(setupConfig.getFile());
		if (e.getRegion().getId().equalsIgnoreCase("autominer") && e.isCancellable()) {
			if (AutoMiner.getInstance().Autominelist.containsKey(p.getUniqueId().toString())) {
				p.sendMessage(AutoMiner.getInstance().colorize(cfgConfig.getString("AutoMiner.LeftAutoMiner")
						.replaceAll("%prefix%", cfgConfig.getString("AutoMiner.Prefix"))));
				TokenEnchantAPI tAPI = TokenEnchantAPI.getInstance();
				double tokensBefore = this.tokensWhenJoin.get(p.getUniqueId().toString());
				double tokensAfter = tAPI.getTokens(p);
				double tokensMade = (tokensAfter - tokensBefore);

				File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
						String.valueOf(p.getUniqueId().toString()) + ".yml");
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

				int timeBefore = this.timeWhenJoin.get(p.getUniqueId().toString());
				int timeAfter = cfg.getInt("AutoMiner.User.timeLeft");
				int timeUsed = (timeBefore - timeAfter);
				if (timeUsed < 60) {
					if (timeAfter < 60) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForSeconds(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForSeconds(timeUsed)))));
					}else if (timeAfter >= 60 && timeAfter < 3600) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForMinutes(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForSeconds(timeUsed)))));
					}else if (timeAfter >= 3600 && timeAfter < 86400) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForHours(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForSeconds(timeUsed)))));
					}else if (timeAfter >= 86400) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTime(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForSeconds(timeUsed)))));
					}
				} else if (timeUsed >= 60 && timeUsed < 3600) {
					if (timeAfter < 60) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForSeconds(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForMinutes(timeUsed)))));
					}else if (timeAfter >= 60 && timeAfter < 3600) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForMinutes(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForMinutes(timeUsed)))));
					}else if (timeAfter >= 3600 && timeAfter < 86400) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForHours(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForMinutes(timeUsed)))));
					}else if (timeAfter >= 86400) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTime(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForMinutes(timeUsed)))));
					}
				} else if (timeUsed >= 3600 && timeUsed < 86400) {
					if (timeAfter < 60) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForSeconds(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForHours(timeUsed)))));
					}else if (timeAfter >= 60 && timeAfter < 3600) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForMinutes(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForHours(timeUsed)))));
					}else if (timeAfter >= 3600 && timeAfter < 86400) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForHours(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForHours(timeUsed)))));
					}else if (timeAfter >= 86400) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTime(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTimeForHours(timeUsed)))));
					}
				} else if (timeUsed >= 86400) {
					if (timeAfter < 60) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForSeconds(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTime(timeUsed)))));
					}else if (timeAfter >= 60 && timeAfter < 3600) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForMinutes(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTime(timeUsed)))));
					}else if (timeAfter >= 3600 && timeAfter < 86400) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTimeForHours(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTime(timeUsed)))));
					}else if (timeAfter >= 86400) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(cfgConfig.getString("AutoMiner.SummarizeTokens")
										.replaceAll("%playerautominertimeleft%",
												String.valueOf(calculateTime(timeAfter)))
										.replaceAll("%tokensmade%", String.valueOf(withSuffix(tokensMade)))
										.replaceAll("%timeused%", String.valueOf(calculateTime(timeUsed)))));
					}
				}
				this.tokensWhenJoin.remove(p.getUniqueId().toString());
				this.timeWhenJoin.remove(p.getUniqueId().toString());
				stopAMtask(p.getUniqueId().toString());
				blockRejoin.put(p.getUniqueId().toString(), System.currentTimeMillis());
			}
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if (AutoMiner.getInstance().Autominelist.containsKey(p.getUniqueId().toString())) {
			stopAMtask(p.getUniqueId().toString());
			this.timeWhenJoin.remove(p.getUniqueId().toString());
			this.tokensWhenJoin.remove(p.getUniqueId().toString());
		}
	}

	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		SetupConfig setupConfig = AutoMiner.getInstance().setupConfig;
		YamlConfiguration cfgConfig = YamlConfiguration.loadConfiguration(setupConfig.getFile());
		if (AutoMiner.getInstance().Autominelist.containsKey(p.getUniqueId().toString())) {
			p.sendMessage(AutoMiner.getInstance().colorize(cfgConfig.getString("AutoMiner.LeftAutoMiner")
					.replaceAll("%prefix%", cfgConfig.getString("AutoMiner.Prefix"))));
			stopAMtask(p.getUniqueId().toString());
			this.timeWhenJoin.remove(p.getUniqueId().toString());
			this.tokensWhenJoin.remove(p.getUniqueId().toString());
		}
	}

	private void startAMwithTMandTI(Player p) {
		SetupConfig setupConfig = AutoMiner.getInstance().setupConfig;
		SetupEnchantsettings setupEnchantsettings = AutoMiner.getInstance().setupEnchantsettings;
		YamlConfiguration cfgSettings = YamlConfiguration.loadConfiguration(setupEnchantsettings.getFile());

		for (ItemStack item2 : p.getInventory().getContents()) {
			if (item2 != null && (item2.getType() != Material.AIR)) {
				if (item2.getType().equals(Material.DIAMOND_PICKAXE)) {
					ItemMeta itemmeta = item2.getItemMeta();
					if (itemmeta != null) {
						if (itemmeta.getLore() != null) {
							int seconds = cfgSettings.getInt("AutoMiner.TimeBetweenProcs.Seconds");
							BukkitTask autoMinerTask = new BukkitRunnable() {

								@SuppressWarnings("deprecation")
								@Override
								public void run() {
									File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
											String.valueOf(p.getUniqueId().toString()) + ".yml");
									YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
									YamlConfiguration cfgConfig = YamlConfiguration
											.loadConfiguration(setupConfig.getFile());
									YamlConfiguration cfgSettings = YamlConfiguration
											.loadConfiguration(setupEnchantsettings.getFile());
									int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
									if (timeLeftinSeconds > 0) {
										for (ItemStack item2 : p.getInventory().getContents()) {
											if (item2 != null && (item2.getType() != Material.AIR)) {
												if (item2.getType().equals(Material.DIAMOND_PICKAXE)) {
													ItemMeta itemmeta = item2.getItemMeta();
													List<String> lore = itemmeta.getLore();
													Map<String, Long> enchantMap = new HashMap<>();
													Map<String, Long> TokenMiner = new HashMap<>();
													Map<String, Long> TokenImpact = new HashMap<>();

													for (String line : lore) {
														String[] enchantment = line.split(" ");
														String enchantName = enchantment[0];
														String enchantLvl = enchantment[1];
														switch (enchantLvl) {
														case "I":
															long l1 = 1;
															enchantMap.put(enchantName, l1);
															break;
														case "II":
															long l2 = 2;
															enchantMap.put(enchantName, l2);
															break;
														case "III":
															long l3 = 3;
															enchantMap.put(enchantName, l3);
															break;
														case "IV":
															long l4 = 4;
															enchantMap.put(enchantName, l4);
															break;
														case "V":
															long l5 = 5;
															enchantMap.put(enchantName, l5);
															break;
														case "VI":
															long l6 = 6;
															enchantMap.put(enchantName, l6);
															break;
														case "VII":
															long l7 = 7;
															enchantMap.put(enchantName, l7);
															break;
														case "VIII":
															long l8 = 8;
															enchantMap.put(enchantName, l8);
															break;
														case "IX":
															long l9 = 9;
															enchantMap.put(enchantName, l9);
															break;
														case "X":
															long l10 = 10;
															enchantMap.put(enchantName, l10);
															break;
														default:
															long level = Long.parseLong(enchantment[1]);
															enchantMap.put(enchantName, level);
															break;
														}

														if (enchantName.contains("TokenMiner")) {
															long lvl = enchantMap.get(enchantName);
															TokenMiner.put("TokenMiner", lvl);
														}
														if (enchantName.contains("TokenImpact")) {
															long lvl = enchantMap.get(enchantName);
															TokenImpact.put("TokenImpact", lvl);
														}
													}
													if (TokenMiner.get("TokenMiner") != null) {
														if (TokenImpact.get("TokenImpact") != null) {
															long tokenMinerlvl = TokenMiner.get("TokenMiner");
															long tokenImpactlvl = TokenImpact.get("TokenImpact");

															long TokenMiner_proc = cfgSettings
																	.getInt("AutoMiner.Enchants.TokenMiner");
															double TokenImpact_boost = cfgSettings
																	.getDouble("AutoMiner.Enchants.TokenImpact");

															double tokenstoget = (TokenMiner_proc * tokenMinerlvl
																	+ TokenMiner_proc * tokenMinerlvl * tokenImpactlvl
																			* TokenImpact_boost);

															String tokensReceived = AutoMiner.getInstance()
																	.colorize(cfgConfig.getString(
																			"AutoMiner.ActionBar_ReceivedTokens")
																			.replaceAll("%tokenamount%",
																					withSuffix((long) tokenstoget))
																			.replaceAll("%prefix%", cfgConfig
																					.getString("AutoMiner.Prefix")));

															TokenEnchantAPI tAPI = TokenEnchantAPI.getInstance();
															tAPI.addTokens(p, tokenstoget);
															p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
																	TextComponent.fromLegacyText(tokensReceived));
															if (timeLeftinSeconds >= seconds) {
																cfg.set("AutoMiner.User.timeLeft",
																		(cfg.getInt("AutoMiner.User.timeLeft")
																				- seconds));
																try {
																	cfg.save(file);
																} catch (IOException e1) {
																	e1.printStackTrace();
																	p.sendMessage(
																			"§cThere was a error while saving your data, pls message an admin about it.");
																}
															} else if (timeLeftinSeconds < seconds
																	&& timeLeftinSeconds > 0) {
																cfg.set("AutoMiner.User.timeLeft",
																		(cfg.getInt("AutoMiner.User.timeLeft")
																				- timeLeftinSeconds));
																try {
																	cfg.save(file);
																} catch (IOException e1) {
																	e1.printStackTrace();
																	p.sendMessage(
																			"§cThere was a error while saving your data, pls message an admin about it.");
																}
															}
														} else {
															stopAMtask(p.getUniqueId().toString());
															startAMwTMwithoutTI(p);
														}
													} else {
														stopAMtask(p.getUniqueId().toString());
														p.sendMessage(AutoMiner.getInstance().colorize(cfgConfig
																.getString("AutoMiner.LeftAutoMiner")
																.replaceAll("%prefix%",
																		cfgConfig.getString("AutoMiner.Prefix"))));
													}

												} else if (timeLeftinSeconds == 0) {
													p.sendMessage(AutoMiner.getInstance()
															.colorize(cfgConfig
																	.getString("AutoMiner.AutoMinerOutOfTime")
																	.replaceAll("%prefix%",
																			cfgConfig.getString("AutoMiner.Prefix"))));
													stopAMtask(p.getUniqueId().toString());
												}
											}
										}
									}
								}
							}.runTaskTimer(AutoMiner.getInstance(), 0, 20 * seconds);
							AutoMiner.getInstance().Autominelist.put(p.getUniqueId().toString(), autoMinerTask);

						}
					}
				}
			}
		}
	}

	private void startAMwTMwithoutTI(Player p) {
		SetupConfig setupConfig = AutoMiner.getInstance().setupConfig;
		SetupEnchantsettings setupEnchantsettings = AutoMiner.getInstance().setupEnchantsettings;
		YamlConfiguration cfgSettings = YamlConfiguration.loadConfiguration(setupEnchantsettings.getFile());

		for (ItemStack item2 : p.getInventory().getContents()) {
			if (item2 != null && (item2.getType() != Material.AIR)) {
				if (item2.getType().equals(Material.DIAMOND_PICKAXE)) {
					ItemMeta itemmeta = item2.getItemMeta();
					if (itemmeta != null) {
						if (itemmeta.getLore() != null) {

							int seconds = cfgSettings.getInt("AutoMiner.TimeBetweenProcs.Seconds");

							BukkitTask autoMinerTask = new BukkitRunnable() {

								@SuppressWarnings("deprecation")
								@Override
								public void run() {

									File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
											String.valueOf(p.getUniqueId().toString()) + ".yml");
									YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
									YamlConfiguration cfgConfig = YamlConfiguration
											.loadConfiguration(setupConfig.getFile());
									YamlConfiguration cfgSettings = YamlConfiguration
											.loadConfiguration(setupEnchantsettings.getFile());
									int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");

									if (timeLeftinSeconds > 0) {
										for (ItemStack item2 : p.getInventory().getContents()) {
											if (item2 != null && (item2.getType() != Material.AIR)) {
												if (item2.getType().equals(Material.DIAMOND_PICKAXE)) {
													ItemMeta itemmeta = item2.getItemMeta();
													List<String> lore = itemmeta.getLore();
													Map<String, Long> enchantMap = new HashMap<>();
													Map<String, Long> TokenMiner = new HashMap<>();
													Map<String, Long> TokenImpact = new HashMap<>();
													for (String line : lore) {
														String[] enchantment = line.split(" ");
														String enchantName = enchantment[0];
														String enchantLvl = enchantment[1];
														switch (enchantLvl) {
														case "I":
															long l1 = 1;
															enchantMap.put(enchantName, l1);
															break;
														case "II":
															long l2 = 2;
															enchantMap.put(enchantName, l2);
															break;
														case "III":
															long l3 = 3;
															enchantMap.put(enchantName, l3);
															break;
														case "IV":
															long l4 = 4;
															enchantMap.put(enchantName, l4);
															break;
														case "V":
															long l5 = 5;
															enchantMap.put(enchantName, l5);
															break;
														case "VI":
															long l6 = 6;
															enchantMap.put(enchantName, l6);
															break;
														case "VII":
															long l7 = 7;
															enchantMap.put(enchantName, l7);
															break;
														case "VIII":
															long l8 = 8;
															enchantMap.put(enchantName, l8);
															break;
														case "IX":
															long l9 = 9;
															enchantMap.put(enchantName, l9);
															break;
														case "X":
															long l10 = 10;
															enchantMap.put(enchantName, l10);
															break;
														default:
															long level = Long.parseLong(enchantment[1]);
															enchantMap.put(enchantName, level);
															break;
														}

														if (enchantName.contains("TokenMiner")) {
															long lvl = enchantMap.get(enchantName);
															TokenMiner.put("TokenMiner", lvl);
														}
														if (enchantName.contains("TokenImpact")) {
															long lvl = enchantMap.get(enchantName);
															TokenImpact.put("TokenImpact", lvl);
														}
													}
													if (TokenImpact.get("TokenImpact") != null) {
														stopAMtask(p.getUniqueId().toString());

														startAMwithTMandTI(p);
													}
													if (TokenMiner.get("TokenMiner") != null) {
														long tokenMinerlvl = TokenMiner.get("TokenMiner");

														long TokenMiner_proc = cfgSettings
																.getInt("AutoMiner.Enchants.TokenMiner");

														double tokenstoget = (long) (TokenMiner_proc * tokenMinerlvl);
														String tokensReceived = AutoMiner.getInstance()
																.colorize(cfgConfig
																		.getString("AutoMiner.ActionBar_ReceivedTokens")
																		.replaceAll("%tokenamount%",
																				withSuffix((long) tokenstoget))
																		.replaceAll("%prefix%", cfgConfig
																				.getString("AutoMiner.Prefix")));
														TokenEnchantAPI tAPI = TokenEnchantAPI.getInstance();
														tAPI.addTokens(p, tokenstoget);
														p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
																TextComponent.fromLegacyText(tokensReceived));
														if (timeLeftinSeconds >= seconds) {
															cfg.set("AutoMiner.User.timeLeft",
																	(cfg.getInt("AutoMiner.User.timeLeft") - seconds));
															try {
																cfg.save(file);
															} catch (IOException e1) {
																e1.printStackTrace();
																p.sendMessage(
																		"§cThere was a error while saving your data, pls message an admin about it.");
															}
														} else if (timeLeftinSeconds < seconds
																&& timeLeftinSeconds > 0) {
															cfg.set("AutoMiner.User.timeLeft",
																	(cfg.getInt("AutoMiner.User.timeLeft")
																			- timeLeftinSeconds));
															try {
																cfg.save(file);
															} catch (IOException e1) {
																e1.printStackTrace();
																p.sendMessage(
																		"§cThere was a error while saving your data, pls message an admin about it.");
															}
														}
													} else {
														p.sendMessage(AutoMiner.getInstance().colorize(cfgConfig
																.getString("AutoMiner.LeftAutoMiner")
																.replaceAll("%prefix%",
																		cfgConfig.getString("AutoMiner.Prefix"))));
														stopAMtask(p.getUniqueId().toString());
													}
												} else if (timeLeftinSeconds == 0) {
													p.sendMessage(AutoMiner.getInstance()
															.colorize(cfgConfig
																	.getString("AutoMiner.AutoMinerOutOfTime")
																	.replaceAll("%prefix%",
																			cfgConfig.getString("AutoMiner.Prefix"))));
													stopAMtask(p.getUniqueId().toString());

												}
											}
										}
									}
								}
							}.runTaskTimer(AutoMiner.getInstance(), 0, 20 * seconds);
							AutoMiner.getInstance().Autominelist.put(p.getUniqueId().toString(), autoMinerTask);
						}
					}
				}
			}
		}
	}

	private void stopAMtask(String uuid) {
		if (AutoMiner.getInstance().Autominelist.containsKey(uuid)) {
			AutoMiner.getInstance().Autominelist.get(uuid).cancel();
			AutoMiner.getInstance().Autominelist.remove(uuid);
		}
	}

	public static String withSuffix(double d) {
		if (d < 1000.0D)
			return String.valueOf(d);
		int exp = (int) (Math.log(d) / Math.log(1000.0D));
		return String.format("%.0f%c", new Object[] { Double.valueOf(d / Math.pow(1000.0D, exp)),
				Character.valueOf("kMBTqQsSONdUDtPpEeonVu".charAt(exp - 1)) });
	}

	public static String calculateTimeForSeconds(long seconds) {
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

		return second + " second(s)";
	}

	public static String calculateTimeForMinutes(long seconds) {
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

		return minute + " minute(s) " + second + " second(s)";
	}

	public static String calculateTimeForHours(long seconds) {
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24L);
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

		return hours + " hour(s) " + minute + " minute(s) " + second + " second(s)";
	}

	public static String calculateTime(long seconds) {
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24L);
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

		return day + " day(s) " + hours + " hour(s) " + minute + " minute(s) " + second + " second(s)";
	}
}
