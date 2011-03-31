package mn.aPunch.iConomyHealth;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;

public class iConomyHealthPermissions {
	private static Permissions permissionsPlugin;
	private static boolean permissionsEnabled = false;

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

	@SuppressWarnings("static-access")
	private static boolean permission(Player player, String string) {
		return permissionsPlugin.Security.permission(player, string);
	}

	public static boolean canHelp(Player player) {
		if (permissionsEnabled) {
			return permission(player, "iConomyHealth.help");
		} else {
			return true;
		}
	}

	public static boolean canHeal(Player player) {
		if (permissionsEnabled) {
			return permission(player, "iConomyHealth.heal");
		} else {
			return true;
		}
	}

	public static boolean canHurt(Player player) {
		if (permissionsEnabled) {
			return permission(player, "iConomyHealth.damage");
		} else {
			return true;
		}
	}
}