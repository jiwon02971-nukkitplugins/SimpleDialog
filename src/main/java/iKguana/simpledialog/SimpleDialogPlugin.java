package iKguana.simpledialog;

import cn.nukkit.plugin.PluginBase;

public class SimpleDialogPlugin extends PluginBase {
	public void onEnable() {
		new ST(this);
		new SimpleDialog();
		getServer().getPluginManager().registerEvents(new SimpleDialogListener(), this);
	}
}
