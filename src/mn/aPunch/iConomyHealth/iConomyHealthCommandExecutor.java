package mn.aPunch.iConomyHealth;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;

public class iConomyHealthCommandExecutor implements CommandExecutor {
	private iConomyHealth plugin;
	private iConomyHealthPermissions permissions;
	private String noPermissionsMessage = ChatColor.RED
			+ "[iConomyHealth] You do not have permission to use that command.";

	public iConomyHealthCommandExecutor(iConomyHealth instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		String commandName = command.getName();
		int healPrice = plugin.getHealPrice();
		int hurtPrice = plugin.getHurtPrice();
		String currency = iConomy.getBank().getCurrency();
		String notEnoughMoneyMessage = ChatColor.RED
				+ "[iConomyHealth] You do not have enough " + currency
				+ " to do that.";
		permissions = new iConomyHealthPermissions(plugin);
		if (sender instanceof Player) {
			Player player = (Player) sender;
			// /iHelp command
			if (commandName.equalsIgnoreCase("iHelp")) {
				if ((permissions.canHelp(player))
						|| (player.isOp())) {
					sendHelp(player);
				} else {
					player.sendMessage(noPermissionsMessage);
				}
				// /iList command
			} else if (commandName.equalsIgnoreCase("iList")) {
				if ((permissions.canList(player))
						|| (player.isOp())) {
					player.sendMessage(ChatColor.GREEN
							+ "===== iConomyHealth Prices =====");
					player.sendMessage(ChatColor.GOLD + "Healing -- "
							+ ChatColor.WHITE + plugin.getHealPrice() + " "
							+ currency);
					player.sendMessage(ChatColor.GOLD + "Hurting -- "
							+ ChatColor.WHITE + plugin.getHurtPrice() + " "
							+ currency);
				} else {
					player.sendMessage(noPermissionsMessage);
				}
				// /iHeal command
			} else if (commandName.equalsIgnoreCase("iHeal")) {
				if ((permissions.canHeal(player))
						|| (player.isOp())) {
					Account account = iConomy.getBank().getAccount(
							player.getName());
					double balance = account.getBalance();
					if (args.length == 2) {
						List<Player> players = plugin.getServer().matchPlayer(
								args[0]);
						Player receiver = players.get(0);
						int health = receiver.getHealth();
						String addHealth = args[1];
						int newHealth = Integer.parseInt(addHealth);
						if (players.size() == 1) {
							if (iConomy.getBank().hasAccount(player.getName())) {
								if (plugin.payPerHP() == true) {
									if (balance >= healPrice * newHealth) {
										account.subtract(healPrice * newHealth);
										receiver.setHealth(health + newHealth);
										player.sendMessage(ChatColor.GREEN
												+ "[iConomyHealth] You have healed "
												+ receiver.getName() + " for "
												+ healPrice * newHealth + " "
												+ currency + ".");
										receiver.sendMessage(ChatColor.GREEN
												+ "[iConomyHealth] You have been healed by "
												+ player.getName() + ".");
									}
								} else if (plugin.payPerHP() == false) {
									if (balance >= healPrice) {
										account.subtract(healPrice);
										receiver.setHealth(health + newHealth);
										player.sendMessage(ChatColor.GREEN
												+ "[iConomyHealth] You have healed "
												+ receiver.getName() + " for "
												+ healPrice + " " + currency
												+ ".");
										receiver.sendMessage(ChatColor.GREEN
												+ "[iConomyHealth] You have been healed by "
												+ player.getName() + ".");
									}
								}
							} else {
								player.sendMessage(ChatColor.RED
										+ "[iConomyHealth] You do not have an account, so how do you expect to do anything?!");
							}
						} else {
							player.sendMessage(ChatColor.RED
									+ "[iConomyHealth] You can only heal one player at a time.");
						}
					} else if (args.length == 1) {
						List<Player> players = plugin.getServer().matchPlayer(
								args[0]);
						Player receiver = players.get(0);
						int health = receiver.getHealth();
						int emptyHearts = 20 - health;
						if (players.size() == 1) {
							if (plugin.payPerHP() == true) {
								if (balance >= (healPrice * emptyHearts)) {
									receiver.setHealth(20);
									account.subtract(healPrice * emptyHearts);
									player.sendMessage(ChatColor.GREEN
											+ "[iConomyHealth] You have healed "
											+ receiver.getName() + " for "
											+ healPrice * emptyHearts + " "
											+ currency + ".");
									receiver.sendMessage(ChatColor.GREEN
											+ "[iConomyHealth] You have been healed by "
											+ player.getName() + ".");
								} else {
									player.sendMessage(notEnoughMoneyMessage);
								}
							} else if (plugin.payPerHP() == false) {
								if (balance >= healPrice) {
									receiver.setHealth(20);
									account.subtract(healPrice);
									player.sendMessage(ChatColor.GREEN
											+ "[iConomyHealth] You have healed "
											+ receiver.getName() + " for "
											+ healPrice + " " + currency + ".");
									receiver.sendMessage(ChatColor.GREEN
											+ "[iConomyHealth] You have been healed by "
											+ player.getName() + ".");
								} else {
									player.sendMessage(notEnoughMoneyMessage);
								}
							}
						} else if (players.size() > 1) {
							player.sendMessage(ChatColor.RED
									+ "[iConomyHealth] You can only heal one player at a time.");
						} else {
							player.sendMessage(ChatColor.RED
									+ "[iConomyHealth] You must specify a player.");
						}
					} else if (args.length == 0) {
						int health = player.getHealth();
						int emptyHearts = 20 - health;
						if (plugin.payPerHP() == true) {
							if (balance >= healPrice * emptyHearts) {
								account.subtract(healPrice * emptyHearts);
								player.setHealth(20);
								player.sendMessage(ChatColor.GREEN
										+ "[iConomyHealth] You have healed yourself for "
										+ healPrice * emptyHearts + " "
										+ currency + ".");
							} else {
								player.sendMessage(notEnoughMoneyMessage);
							}
						} else if (plugin.payPerHP() == false) {
							if (balance >= healPrice) {
								account.subtract(healPrice);
								player.setHealth(20);
								player.sendMessage(ChatColor.GREEN
										+ "[iConomyHealth] You have healed yourself for "
										+ healPrice + " " + currency + ".");
							} else {
								player.sendMessage(notEnoughMoneyMessage);
							}
						}
					}
				}
				// /iHurt command
			} else if (commandName.equalsIgnoreCase("iHurt")) {
				if ((permissions.canHurt(player))
						|| (player.isOp())) {
					Account account = iConomy.getBank().getAccount(
							player.getName());
					double balance = account.getBalance();
					if (args.length == 2) {
						List<Player> players = plugin.getServer().matchPlayer(
								args[0]);
						Player receiver = players.get(0);
						String subtractHealth = args[1];
						int newHealth = Integer.parseInt(subtractHealth);
						int health = receiver.getHealth();
						if (players.size() == 1) {
							if (iConomy.getBank().hasAccount(player.getName())) {
								if (plugin.payPerHP() == true) {
									if (balance >= hurtPrice * newHealth) {
										account.subtract(hurtPrice * newHealth);
										receiver.setHealth(health - newHealth);
										player.sendMessage(ChatColor.GREEN
												+ "[iConomyHealth] You have hurt "
												+ receiver.getName() + " for "
												+ hurtPrice * newHealth + " "
												+ currency + ".");
										receiver.sendMessage(ChatColor.RED
												+ "[iConomyHealth] You have been hurt by "
												+ player.getName() + ".");
									}
								} else if (plugin.payPerHP() == false) {
									if (balance >= hurtPrice) {
										account.subtract(hurtPrice);
										receiver.setHealth(health - newHealth);
										player.sendMessage(ChatColor.GREEN
												+ "[iConomyHealth] You have hurt "
												+ receiver.getName() + " for "
												+ healPrice + " " + currency
												+ ".");
										receiver.sendMessage(ChatColor.RED
												+ "[iConomyHealth] You have been hurt by "
												+ player.getName() + ".");
									}
								}
							} else {
								player.sendMessage(ChatColor.RED
										+ "[iConomyHealth] You do not have an account, so how do you expect to do anything?!");
							}
						} else {
							player.sendMessage(ChatColor.RED
									+ "[iConomyHealth] You can only hurt one player at a time.");
						}
					} else if (args.length == 1) {
						List<Player> players = plugin.getServer().matchPlayer(
								args[0]);
						Player receiver = players.get(0);
						int health = receiver.getHealth();
						if (players.size() == 1) {
							if (plugin.payPerHP() == true) {
								if (balance >= (hurtPrice * health)) {
									receiver.setHealth(0);
									account.subtract(hurtPrice * health);
									player.sendMessage(ChatColor.GREEN
											+ "[iConomyHealth] You have killed "
											+ receiver.getName() + " for "
											+ hurtPrice * health + " "
											+ currency + ".");
									receiver.sendMessage(ChatColor.RED
											+ "[iConomyHealth] You have been killed by "
											+ player.getName() + ".");
								} else {
									player.sendMessage(notEnoughMoneyMessage);
								}
							} else if (plugin.payPerHP() == false) {
								if (balance >= hurtPrice) {
									receiver.setHealth(0);
									account.subtract(hurtPrice);
									player.sendMessage(ChatColor.GREEN
											+ "[iConomyHealth] You have killed "
											+ receiver.getName() + " for "
											+ hurtPrice + " " + currency + ".");
									receiver.sendMessage(ChatColor.RED
											+ "[iConomyHealth] You have been killed by "
											+ player.getName() + ".");
								} else {
									player.sendMessage(notEnoughMoneyMessage);
								}
							}
						} else if (players.size() > 1) {
							player.sendMessage(ChatColor.RED
									+ "[iConomyHealth] You can only hurt one player at a time.");
						} else {
							player.sendMessage(ChatColor.RED
									+ "[iConomyHealth] You must specify a player to hurt.");
						}
					} else if (args.length == 0) {
						int health = player.getHealth();
						if (plugin.payPerHP() == true) {
							if (balance >= hurtPrice * health) {
								account.subtract(hurtPrice * health);
								player.setHealth(0);
								player.sendMessage(ChatColor.RED
										+ "[iConomyHealth] You have killed yourself for "
										+ hurtPrice * health + " " + currency
										+ ".");
							} else {
								player.sendMessage(notEnoughMoneyMessage);
							}
						} else if (plugin.payPerHP() == false) {
							if (balance >= hurtPrice) {
								account.subtract(hurtPrice);
								player.setHealth(0);
								player.sendMessage(ChatColor.RED
										+ "[iConomyHealth] You have killed yourself for "
										+ healPrice + " " + currency + ".");
							} else {
								player.sendMessage(notEnoughMoneyMessage);
							}
						}
					}
				}
			}
		}
		return true;
	}

	// help menu
	private void sendHelp(Player player) {
		player.sendMessage(ChatColor.GREEN + "===== iConomyHealth Help =====");
		player.sendMessage(ChatColor.GOLD
				+ "/iHeal [player] [hp (1-20)] - heal players");
		player.sendMessage(ChatColor.GOLD
				+ "/iHurt [player] [hp (1-20)] - damage players");
		player.sendMessage(ChatColor.GOLD
				+ "/iList - list the costs of healing/hurting");
		player.sendMessage(ChatColor.GREEN + "===== v0.3.2 by aPunch =====");
	}
}