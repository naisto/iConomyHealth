package mn.aPunch.iConomyHealth;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.Server;

import com.nijiko.coelho.iConomy.iConomy;

public class iConomyHealth extends JavaPlugin {
	public Logger log = Logger.getLogger("Minecraft");
	iConomyHealthCommandExecutor commandExecutor = new iConomyHealthCommandExecutor(this);
	private static iConomy iConomy = null;
	private static Server server = null;
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
		return server;
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

	public void onEnable() {
		server = getServer();
		// register commands
		getCommand("iHelp").setExecutor(commandExecutor);
		getCommand("iList").setExecutor(commandExecutor);
		getCommand("iHeal").setExecutor(commandExecutor);
		getCommand("iHurt").setExecutor(commandExecutor);

		// set up Permissions
		iConomyHealthPermissions.initialize(getServer());
		// check if iConomy is detected
		Plugin test = getServer().getPluginManager().getPlugin("iConomy");
		if (test != null) {
			iConomy = ((iConomy) test);
		} else {
			log.info("[iConomyHealth] iConomy not detected. Disabling plugin.");
			getServer().getPluginManager().disablePlugin(this);
		}
		// check if config.yml is there...if so, load it; if not, create it
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