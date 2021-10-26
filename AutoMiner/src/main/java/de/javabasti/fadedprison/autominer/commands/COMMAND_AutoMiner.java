package de.javabasti.fadedprison.autominer.commands;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.javabasti.fadedprison.autominer.AutoMiner;
import de.javabasti.fadedprison.autominer.utils.AutoMinerTime_Setup;
import de.javabasti.fadedprison.autominer.utils.SetupConfig;
import de.javabasti.fadedprison.autominer.utils.SetupEnchantsettings;

public class COMMAND_AutoMiner implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		SetupConfig setupConfig = AutoMiner.getInstance().setupConfig;

		if (command.getName().equalsIgnoreCase("autominer")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;

				File file = new File(AutoMiner.instance.getDataFolder() + "/playerdata",
						String.valueOf(p.getUniqueId().toString()) + ".yml");
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("time")) {
						int timeLeftinSeconds = cfg.getInt("AutoMiner.User.timeLeft");
						if (timeLeftinSeconds < 60) {
							p.sendMessage(AutoMiner.getInstance()
									.colorize(setupConfig.getString("AutoMiner.AutoMinerTimeLeft")
											.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
											.replaceAll("%autominer_time_left%",
													calculateTimeForSeconds(timeLeftinSeconds))));
						} else if (timeLeftinSeconds >= 60 && timeLeftinSeconds < 3600) {
							p.sendMessage(AutoMiner.getInstance()
									.colorize(setupConfig.getString("AutoMiner.AutoMinerTimeLeft")
											.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
											.replaceAll("%autominer_time_left%",
													calculateTimeForMinutes(timeLeftinSeconds))));
						} else if (timeLeftinSeconds >= 3600 && timeLeftinSeconds < 86400) {
							p.sendMessage(AutoMiner.getInstance()
									.colorize(setupConfig.getString("AutoMiner.AutoMinerTimeLeft")
											.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
											.replaceAll("%autominer_time_left%",
													calculateTimeForHours(timeLeftinSeconds))));
						} else if (timeLeftinSeconds >= 86400) {
							p.sendMessage(AutoMiner.getInstance()
									.colorize(setupConfig.getString("AutoMiner.AutoMinerTimeLeft")
											.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
											.replaceAll("%autominer_time_left%", calculateTime(timeLeftinSeconds))));
						}
						return true;
					}
					if (args[0].equalsIgnoreCase("about")) {
						p.sendMessage(AutoMiner.getInstance().colorize("&f&l&m================"));
						p.sendMessage(AutoMiner.getInstance().colorize("&bAuthor: &3JavaBasti"));
						p.sendMessage(AutoMiner.getInstance().colorize("&bMade for &3Faded&bPrison."));
						p.sendMessage(AutoMiner.getInstance().colorize("&f&l&m================"));
						return true;
					}
					if (p.hasPermission(setupConfig.getString("AutoMiner.PermissionsToGiveTime"))) {
						if (args[0].equalsIgnoreCase("reload")) {
							p.sendMessage(
									AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.ReloadedAllFiles")
											.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
							SetupConfig setupConfig2 = AutoMiner.getInstance().setupConfig;
							SetupEnchantsettings setupEnchantsettings2 = AutoMiner.getInstance().setupEnchantsettings;
							YamlConfiguration.loadConfiguration(setupConfig2.getFile());
							YamlConfiguration.loadConfiguration(setupEnchantsettings2.getFile());
							return true;
						}
					} else {
						p.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.NoPermissions")
								.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
						return true;
					}
				} else if (args.length < 1
						&& !p.hasPermission(setupConfig.getString("AutoMiner.PermissionsToGiveTime"))) {
					p.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.Prefix")
							+ "&bCheck your AutoMiner-Time with &3/autominer time"));
					p.sendMessage(AutoMiner.getInstance()
							.colorize(setupConfig.getString("AutoMiner.Prefix") + "&bFor more infos /autominer about"));
					return true;
				}

				if (p.hasPermission(setupConfig.getString("AutoMiner.PermissionsToGiveTime"))) {
					if (args.length < 4) {
						p.sendMessage(AutoMiner.getInstance()
								.colorize(setupConfig.getString("AutoMiner.Prefix") + "&3Example: /autominer reload"));
						p.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.Prefix")
								+ "&b/autominer set/give/remove &7(&3player&7) (&3amount of time&7) &7(&3sec&7/&3min&7/&3h&7/&3day&7)"));
						p.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.Prefix")
								+ "&3Example: /autominer give Example1 10 min"));
						p.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.Prefix")
								+ "&3Example: /autominer remove Example1 10 min"));
						p.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.Prefix")
								+ "&3Example: /autominer set Example1 10 min"));
						p.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.Prefix")
								+ "&bSec = Second, Min = Minute, Hr = Hour, D = Day"));
						return true;
					}
					if (args.length == 4) {
						Player target = Bukkit.getPlayer(args[1]);
						int amountoftime = Integer.parseInt(args[2]);
						String timeVariable = args[3];
						if (args[0].equalsIgnoreCase("give")) {
							if (target != null) {
								if (amountoftime != 0) {
									if (timeVariable.equals("sec") || (timeVariable.equals("s")
											|| (timeVariable.equals("secs")) || (timeVariable.equals("seconds")))) {
										AutoMinerTime_Setup.addAutoMinerTimeSeconds(target, amountoftime);
										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeGivenSeconds")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeReceivedSeconds")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));

										return true;
									} else if (timeVariable.equals("min") || (timeVariable.equals("m"))
											|| (timeVariable.equals("mins")) || (timeVariable.equals("minutes"))) {
										AutoMinerTime_Setup.addAutoMinerTimeMinutes(target, amountoftime);

										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeGivenMinutes")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeReceivedMinutes")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));

										return true;
									} else if (timeVariable.equals("hr") || (timeVariable.equals("h")
											|| (timeVariable.equals("hour") || (timeVariable.equals("hours"))))) {
										AutoMinerTime_Setup.addAutoMinerTimeHours(target, amountoftime);
										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeGivenHours")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeReceivedHours")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));

										return true;
									} else if (timeVariable.equals("day") || (timeVariable.equals("days"))
											|| (timeVariable.equals("d"))) {
										AutoMinerTime_Setup.addAutoMinerTimeDays(target, amountoftime);
										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeGivenDays")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeReceivedDays")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));

										return true;
									} else {
										p.sendMessage(AutoMiner.getInstance()
												.colorize(setupConfig.getString("AutoMiner.WrongVariableUsed")
														.replaceAll("%prefix%",
																setupConfig.getString("AutoMiner.Prefix"))));
										return true;
									}

								} else {
									p.sendMessage(AutoMiner.getInstance().colorize(
											setupConfig.getString("AutoMiner.WrongNumberUsed").replaceAll("%prefix%",
													setupConfig.getString("AutoMiner.Prefix"))));
									return true;
								}
							} else {
								p.sendMessage(AutoMiner.getInstance()
										.colorize(setupConfig.getString("AutoMiner.PlayerNotAvailable")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
								return true;
							}
						} else if (args[0].equalsIgnoreCase("remove")) {
							if (target != null) {
								if (amountoftime != 0) {
									switch (timeVariable) {
									case "s":
									case "sec":
									case "secs":
									case "seconds":
										AutoMinerTime_Setup.removeAutoMinerTimeSeconds(target, amountoftime);
										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeRemovedSeconds")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeGotRemovedSeconds")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										return true;
									case "min":
									case "m":
									case "mins":
									case "minutes":
										AutoMinerTime_Setup.removeAutoMinerTimeMinutes(target, amountoftime);

										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeRemovedMinutes")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeGotRemovedMinutes")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										return true;
									case "hr":
									case "h":
									case "hour":
									case "hours":
										AutoMinerTime_Setup.removeAutoMinerTimeHours(target, amountoftime);
										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeRemovedHours")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeGotRemovedHours")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										return true;
									case "day":
									case "days":
									case "d":
										AutoMinerTime_Setup.removeAutoMinerTimeDays(target, amountoftime);
										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeRemovedDays")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeGotRemovedDays")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										return true;
									default:
										p.sendMessage(AutoMiner.getInstance()
												.colorize(setupConfig.getString("AutoMiner.WrongVariableUsed")
														.replaceAll("%prefix%",
																setupConfig.getString("AutoMiner.Prefix"))));
										return true;
									}

								} else {
									p.sendMessage(AutoMiner.getInstance().colorize(
											setupConfig.getString("AutoMiner.WrongNumberUsed").replaceAll("%prefix%",
													setupConfig.getString("AutoMiner.Prefix"))));
									return true;
								}
							} else {
								p.sendMessage(AutoMiner.getInstance()
										.colorize(setupConfig.getString("AutoMiner.PlayerNotAvailable")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
								return true;
							}
						} else if (args[0].equalsIgnoreCase("set")) {
							if (target != null) {
								if (amountoftime != 0) {
									switch (timeVariable) {
									case "s":
									case "sec":
									case "secs":
									case "seconds":
										AutoMinerTime_Setup.setAutoMinerTimeSeconds(target, amountoftime);
										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeSetSeconds")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeSetToSeconds")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										return true;
									case "min":
									case "m":
									case "mins":
									case "minutes":
										AutoMinerTime_Setup.setAutoMinerTimeMinutes(target, amountoftime);

										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeSetMinutes")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeSetToMinutes")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										return true;
									case "hr":
									case "h":
									case "hour":
									case "hours":
										AutoMinerTime_Setup.setAutoMinerTimeHours(target, amountoftime);
										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeSetHours")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeSetToHours")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										return true;
									case "day":
									case "days":
									case "d":
										AutoMinerTime_Setup.setAutoMinerTimeDays(target, amountoftime);
										p.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeSetDays")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										target.sendMessage(AutoMiner.getInstance().colorize(setupConfig
												.getString("AutoMiner.TimeSetToDays")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
												.replaceAll("%autominer_time%",
														String.valueOf(Integer.valueOf(amountoftime)))
												.replaceAll("%target_name%", target.getName())));
										return true;
									default:
										p.sendMessage(AutoMiner.getInstance()
												.colorize(setupConfig.getString("AutoMiner.WrongVariableUsed")
														.replaceAll("%prefix%",
																setupConfig.getString("AutoMiner.Prefix"))));
										return true;
									}

								} else {
									p.sendMessage(AutoMiner.getInstance().colorize(
											setupConfig.getString("AutoMiner.WrongNumberUsed").replaceAll("%prefix%",
													setupConfig.getString("AutoMiner.Prefix"))));
									return true;
								}
							} else {
								p.sendMessage(AutoMiner.getInstance()
										.colorize(setupConfig.getString("AutoMiner.PlayerNotAvailable")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
								return true;
							}
						}
					}
				} else {
					p.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.NoPermissions")
							.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
				}
			} else {
				if (args.length < 4) {
					sender.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.Prefix")
							+ "&b/autominer set/give/remove &7(&3player&7) (&3amount of time&7) &7(&3sec&7/&3min&7/&3h&7/&3day&7)"));
					sender.sendMessage(AutoMiner.getInstance().colorize(
							setupConfig.getString("AutoMiner.Prefix") + "&3Example: /autominer give Example1 10 min"));
					sender.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.Prefix")
							+ "&3Example: /autominer remove Example1 10 min"));
					sender.sendMessage(AutoMiner.getInstance().colorize(
							setupConfig.getString("AutoMiner.Prefix") + "&3Example: /autominer set Example1 10 min"));
					sender.sendMessage(AutoMiner.getInstance().colorize(setupConfig.getString("AutoMiner.Prefix")
							+ "&bSec = Second, Min = Minute, Hr = Hour, D = Day"));
					return true;
				}
				if (args.length == 4) {
					Player target = Bukkit.getPlayer(args[1]);
					int amountoftime = Integer.parseInt(args[2]);
					String timeVariable = args[3];
					if (args[0].equalsIgnoreCase("give")) {
						if (target != null) {
							if (amountoftime != 0) {
								if (timeVariable.equals("sec") || (timeVariable.equals("secs"))
										|| (timeVariable.equals("seconds"))) {
									AutoMinerTime_Setup.addAutoMinerTimeSeconds(target, amountoftime);
									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeGivenSeconds")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeReceivedSeconds")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));

									return true;
								} else if (timeVariable.equals("min") || (timeVariable.equals("m"))
										|| (timeVariable.equals("mins")) || (timeVariable.equals("minutes"))) {
									AutoMinerTime_Setup.addAutoMinerTimeMinutes(target, amountoftime);

									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeGivenMinutes")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeReceivedMinutes")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));

									return true;
								} else if (timeVariable.equals("hr") || (timeVariable.equals("h")
										|| (timeVariable.equals("hour") || (timeVariable.equals("hours"))))) {
									AutoMinerTime_Setup.addAutoMinerTimeHours(target, amountoftime);
									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeGivenHours")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeReceivedHours")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));

									return true;
								} else if (timeVariable.equals("day") || (timeVariable.equals("days"))
										|| (timeVariable.equals("d"))) {
									AutoMinerTime_Setup.addAutoMinerTimeDays(target, amountoftime);
									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeGivenDays")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeReceivedDays")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));

									return true;
								} else {
									sender.sendMessage(AutoMiner.getInstance().colorize(
											setupConfig.getString("AutoMiner.WrongVariableUsed").replaceAll("%prefix%",
													setupConfig.getString("AutoMiner.Prefix"))));
									return true;
								}

							} else {
								sender.sendMessage(AutoMiner.getInstance()
										.colorize(setupConfig.getString("AutoMiner.WrongNumberUsed")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
								return true;
							}
						} else {
							sender.sendMessage(AutoMiner.getInstance()
									.colorize(setupConfig.getString("AutoMiner.PlayerNotAvailable")
											.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
							return true;
						}
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (target != null) {
							if (amountoftime != 0) {
								switch (timeVariable) {
								case "sec":
								case "secs":
								case "seconds":
									AutoMinerTime_Setup.removeAutoMinerTimeSeconds(target, amountoftime);
									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeRemovedSeconds")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeGotRemovedSeconds")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									return true;
								case "min":
								case "m":
								case "mins":
								case "minutes":
									AutoMinerTime_Setup.removeAutoMinerTimeMinutes(target, amountoftime);

									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeRemovedMinutes")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeGotRemovedMinutes")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									return true;
								case "hr":
								case "h":
								case "hour":
								case "hours":
									AutoMinerTime_Setup.removeAutoMinerTimeHours(target, amountoftime);
									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeRemovedHours")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeGotRemovedHours")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									return true;
								case "day":
								case "days":
								case "d":
									AutoMinerTime_Setup.removeAutoMinerTimeDays(target, amountoftime);
									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeRemovedDays")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeGotRemovedDays")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									return true;
								default:
									sender.sendMessage(AutoMiner.getInstance().colorize(
											setupConfig.getString("AutoMiner.WrongVariableUsed").replaceAll("%prefix%",
													setupConfig.getString("AutoMiner.Prefix"))));
									return true;
								}

							} else {
								sender.sendMessage(AutoMiner.getInstance()
										.colorize(setupConfig.getString("AutoMiner.WrongNumberUsed")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
								return true;
							}
						} else {
							sender.sendMessage(AutoMiner.getInstance()
									.colorize(setupConfig.getString("AutoMiner.PlayerNotAvailable")
											.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
							return true;
						}
					} else if (args[0].equalsIgnoreCase("set")) {
						if (target != null) {
							if (amountoftime != 0) {
								switch (timeVariable) {
								case "sec":
								case "secs":
								case "seconds":
									AutoMinerTime_Setup.setAutoMinerTimeSeconds(target, amountoftime);
									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeSetSeconds")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeSetToSeconds")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									return true;
								case "min":
								case "m":
								case "mins":
								case "minutes":
									AutoMinerTime_Setup.setAutoMinerTimeMinutes(target, amountoftime);

									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeSetMinutes")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeSetToMinutes")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									return true;
								case "hr":
								case "h":
								case "hour":
								case "hours":
									AutoMinerTime_Setup.setAutoMinerTimeHours(target, amountoftime);
									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeSetHours")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeSetToHours")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									return true;
								case "day":
								case "days":
								case "d":
									AutoMinerTime_Setup.setAutoMinerTimeDays(target, amountoftime);
									sender.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeSetDays")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									target.sendMessage(AutoMiner.getInstance()
											.colorize(setupConfig.getString("AutoMiner.TimeSetToDays")
													.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))
													.replaceAll("%autominer_time%",
															String.valueOf(Integer.valueOf(amountoftime)))
													.replaceAll("%target_name%", target.getName())));
									return true;
								default:
									sender.sendMessage(AutoMiner.getInstance().colorize(
											setupConfig.getString("AutoMiner.WrongVariableUsed").replaceAll("%prefix%",
													setupConfig.getString("AutoMiner.Prefix"))));
									return true;
								}

							} else {
								sender.sendMessage(AutoMiner.getInstance()
										.colorize(setupConfig.getString("AutoMiner.WrongNumberUsed")
												.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
								return true;
							}
						} else {
							sender.sendMessage(AutoMiner.getInstance()
									.colorize(setupConfig.getString("AutoMiner.PlayerNotAvailable")
											.replaceAll("%prefix%", setupConfig.getString("AutoMiner.Prefix"))));
							return true;
						}
					}
				}
			}
		}
		return true;
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
