package mn.aPunch.iConomyHealth;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;

public class iConomyHealthPermissions {
	public iConomyHealth plugin;
	public static Permissions permissionsPlugin;
	private static boolean permissionsEnabled = false;
	
	public iConomyHealthPermissions(iConomyHealth instance) {
		plugin = instance;
	}

	public static void initialize(Server server) {
		Plugin test = server.getPluginManager().getPlugin("Permissions");
		if (test != null) {
			Logger log = Logger.getLogger("Minecraft");
			permissionsPlugin = ((Permissions) test);
			permissionsEnabled = true;
			log.log(Level.INFO, "[iConomyHealth] Permissions enabled.");
		} else {
			Logger log = Logger.getLogger("Minecraft");
			log.log(Level.INFO,
					"[iConomyHealth] Permissions not found. Defaulting to Op.");
		}
	}

	private boolean permission(Player player, String string) {
		return Permissions.Security.permission(player, string);
	}

	public boolean canHelp(Player player) {
		if (permissionsEnabled) {
			return permission(player, "iConomyHealth.help");
		} else {
			return true;
		}
	}

	public boolean canHeal(Player player) {
		if (permissionsEnabled) {
			return permission(player, "iConomyHealth.heal");
		} else {
			return true;
		}
	}

	public boolean canHurt(Player player) {
		if (permissionsEnabled) {
			return permission(player, "iConomyHealth.hurt");
		} else {
			return true;
		}
	}

	public boolean canList(Player player) {
		if (permissionsEnabled) {
			return permission(player, "iConomyHealth.list");
		} else {
			return true;
		}
	}
}