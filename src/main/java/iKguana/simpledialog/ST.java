package iKguana.simpledialog;

import cn.nukkit.utils.Config;

public class ST {
	private static SimpleDialogPlugin $plugin;

	public ST(SimpleDialogPlugin plugin) {
		$plugin = plugin;
		plugin.getDataFolder().mkdirs();
		plugin.saveDefaultConfig();
	}

	public static Config getConfig() {
		return $plugin.getConfig();
	}

	public static String getMessage(String key) {
		return getMessage(key, new String[] {});
	}

	public static String getMessage(String key, String... strings) {
		String str = $plugin.getConfig().getString("messages." + key);
		for (int i = 0; i < strings.length; i++)
			str = str.replace("%" + (i + 1), strings[i]);
		return str;
	}
}
