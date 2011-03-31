package mn.aPunch.iConomyHealth;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.Server;

import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;

public class iConomyHealth extends JavaPlugin {
	public Logger log = Logger.getLogger("Minecraft");
	private static iConomy iConomy = null;
	private static Server Server = null;
	Configuration config;

	public static int healPrice = 10;
	public static int hurtPrice = 25;
	public static boolean payPerHP = true;

	private void loadConfig() {
		config.load();
		healPrice = config.getInt("heal-price", healPrice);
		hurtPrice = config.getInt("hurt-price", hurtPrice);
		payPerHP = config.getBoolean("pay-per-healthpoint", payPerHP);
	}

	private void defaultConfig() {
		config.setProperty("heal-price", healPrice);
		config.setProperty("hurt-price", hurtPrice);
		config.setProperty("pay-per-healthpoint", payPerHP);
		config.save();
	}

	public static Server getBukkitServer() {
		return Server;
	}

	public static iConomy getiConomy() {
		return iConomy;
	}

	public static boolean setiConomy(iConomy plugin) {
		if (iConomy == null) {
			iConomy = plugin;
		} else {
			return false;
		}
		return true;
	}

	private void sendHelp(Player player) {
		player.sendMessage(ChatColor.GREEN + "===== iConomyHealth v0.1.1 =====");
		player.sendMessage(ChatColor.GOLD
				+ "/iHeal [player] [hp (1-20)] - heal players");
		player.sendMessage(ChatColor.GOLD
				+ "/iHurt [player] [hp (1-20)] - damage players");
		player.sendMessage(ChatColor.GREEN + "===== created by aPunch =====");
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		String commandName = command.getName();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (commandName.equalsIgnoreCase("iHelp")) {
				if (iConomyHealthPermissions.canHelp(player) || (player.isOp())) {
					sendHelp(player);
					return true;
				} else {
					player.sendMessage(ChatColor.RED
							+ "[iConomyHealth] You do not have permission to use that command.");
				}
			}
			try {
				if (commandName.equalsIgnoreCase("iHeal")) {
					if (iConomyHealthPermissions.canHeal(player)
							|| (player.isOp())) {
						List<Player> players = getServer().matchPlayer(args[0]);
						Player receiver = players.get(0);
						int health = receiver.getHealth();
						String addHealth = args[1];
						int newHealth = Integer.parseInt(addHealth);
						String currency = com.nijiko.coelho.iConomy.iConomy
								.getBank().getCurrency();
						if (args.length >= 2) {
							if (players.size() >= 1) {
								if (com.nijiko.coelho.iConomy.iConomy.getBank()
										.hasAccount(player.getName())) {
									Account account = com.nijiko.coelho.iConomy.iConomy
											.getBank().getAccount(
													player.getName());
									double balance = account.getBalance();
									int healPrice = config.getInt("heal-price",
											10);
									boolean payPerHP = config.getBoolean(
											"pay-per-healthpoint", true);
									if (balance >= healPrice) {
										if (payPerHP == true) {
											account.subtract(healPrice
													* newHealth);
											receiver.setHealth(health
													+ newHealth);
											player.sendMessage(ChatColor.GREEN
													+ "[iConomyHealth] You have healed "
													+ receiver.getName()
													+ " for " + healPrice
													* newHealth + " "
													+ currency + ".");
											receiver.sendMessage(ChatColor.GREEN
													+ "[iConomyHealth] You have been healed by "
													+ player.getName() + ".");

										} else {
											account.subtract(healPrice);
											receiver.setHealth(health
													+ newHealth);
											player.sendMessage(ChatColor.GREEN
													+ "[iConomyHealth] You have healed "
													+ receiver.getName()
													+ " for " + healPrice + " "
													+ currency + ".");
											receiver.sendMessage(ChatColor.GREEN
													+ "[iConomyHealth] You have been healed by "
													+ player.getName() + ".");
										}
									} else {
										player.sendMessage(ChatColor.RED
												+ "[iConomyHealth] You do not have enough "
												+ currency + " to heal "
												+ receiver.getName() + ".");
									}

								} else {
									player.sendMessage(ChatColor.RED
											+ "[iConomyHealth] You do not have an account, so how do you expect to buy anything?!");
								}

							} else {
								player.setHealth(health + newHealth);
								player.sendMessage(ChatColor.GREEN
										+ "[iConomyHealth] You have healed yourself for "
										+ healPrice + currency + ".");
							}
						}
					}
				} else {
					player.sendMessage(ChatColor.RED
							+ "[iConomyHealth] You do not have permission to use that command.");
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				player.sendMessage(ChatColor.RED
						+ "[iConomyHealth] You must specify a player and an amount of health-points.");
			}
			try {
				if (commandName.equalsIgnoreCase("iHurt")) {
					if (iConomyHealthPermissions.canHurt(player)
							|| (player.isOp())) {
						List<Player> players = getServer().matchPlayer(args[0]);
						Player receiver = players.get(0);
						int health = receiver.getHealth();
						String subtractHealth = args[1];
						int newHealth = Integer.parseInt(subtractHealth);
						String currency = com.nijiko.coelho.iConomy.iConomy
								.getBank().getCurrency();
						if (args.length >= 2) {
							if (players.size() >= 1) {
								if (com.nijiko.coelho.iConomy.iConomy.getBank()
										.hasAccount(player.getName())) {
									Account account = com.nijiko.coelho.iConomy.iConomy
											.getBank().getAccount(
													player.getName());
									double balance = account.getBalance();
									int hurtPrice = config.getInt("hurt-price",
											25);
									boolean payPerHP = config.getBoolean("pay-per-healthpoint", true);
									if (balance >= hurtPrice) {
										if(payPerHP == true){
											account.subtract(hurtPrice + newHealth);
											receiver.setHealth(health - newHealth);
											player.sendMessage(ChatColor.GREEN
													+ "[iConomyHealth] You have hurt "
													+ receiver.getName() + " for "
													+ hurtPrice * newHealth + " " + currency
													+ ".");
											receiver.sendMessage(ChatColor.GREEN
													+ "[iConomyHealth] You have been hurt by "
													+ player.getName() + ".");
										}else{
										account.subtract(hurtPrice);
										receiver.setHealth(health - newHealth);
										player.sendMessage(ChatColor.GREEN
												+ "[iConomyHealth] You have hurt "
												+ receiver.getName() + " for "
												+ hurtPrice + " " + currency
												+ ".");
										receiver.sendMessage(ChatColor.GREEN
												+ "[iConomyHealth] You have been hurt by "
												+ player.getName() + ".");
										}

									} else {
										player.sendMessage(ChatColor.RED
												+ "[iConomyHealth] You do not have enough "
												+ currency + " to hurt "
												+ receiver.getName() + ".");
									}
								} else {
									player.sendMessage(ChatColor.RED
											+ "[iConomyHealth] You do not have an account, so how do you expect to buy anything?!");
								}
							} else {
								player.setHealth(health - newHealth);
								player.sendMessage(ChatColor.GREEN
										+ "[iConomyHealth] You have damaged yourself for "
										+ hurtPrice + currency + ".");
							}
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "[iConomyHealth] You do not have permission to use that command.");
					}
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				player.sendMessage(ChatColor.RED
						+ "[iConomyHealth] You must specify a player and an amount of health-points.");
			}
		}
		return true;
	}

	public void onEnable() {
		Server = getServer();
		iConomyHealthPermissions.initialize(getServer());
		config = getConfiguration();
		if (!new File(getDataFolder(), "config.yml").exists()) {
			defaultConfig();
		}
		loadConfig();
		PluginDescriptionFile pdfFile = getDescription();
		System.out.println("[" + pdfFile.getName() + "]" + " version "
				+ pdfFile.getVersion() + " is enabled! ");
	}

	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		System.out.println("[" + pdfFile.getName() + "]" + " version "
				+ pdfFile.getVersion() + " is disabled! ");
	}
}
