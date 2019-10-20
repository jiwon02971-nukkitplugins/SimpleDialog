package iKguana.simpledialog;

import java.util.ArrayList;
import java.util.Collection;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;

public class SimpleDialog {
	public static int formId = 0;

	public static enum Type {
		ONLY_TEXT, FILTERING, FILTERING_SKIP
	}

	public static void sendDialog(Object clz, String name, Player player, FormWindow window) {
		sendDialog(clz, name, player, window, null);
	}

	public static void sendDialog(Object clz, String name, Player player, FormWindow window, Object data) {
		if (player instanceof Player) {
			SimpleDialogListener.queue.put(formId, new SimpleDialogData(clz, name, data));
			player.showFormWindow(window, formId++);
		}
	}

	public static void sendDialog(Object clz, String name, Player player, Type type) {
		sendDialog(clz, name, player, type, null);
	}

	@SuppressWarnings("unchecked")
	public static void sendDialog(Object clz, String name, Player player, Type type, Object data) {
		if (player instanceof Player) {
			if (type == Type.ONLY_TEXT) {
				FormWindowSimple window = new FormWindowSimple(ST.getMessage("only_text.title"), (String) data);
				sendDialog(null, null, player, window);
			} else if (type == Type.FILTERING) {
				ArrayList<String> list = (data instanceof ArrayList<?> ? (ArrayList<String>) data : toStringList(Server.getInstance().getOnlinePlayers().values()));
				if (list.size() <= 10) {
					sendDialog(clz, name, player, Type.FILTERING_SKIP, data);
				} else {
					FormWindowModal window = new FormWindowModal(ST.getMessage("filtering.title"), ST.getMessage("filtering.ask_use_filter"), ST.getMessage("filtering.b_yes"), ST.getMessage("filtering.b_no"));
					if (list.size() == 0)
						window.setContent(window.getContent() + "\n\n" + ST.getMessage("filtering.no_element"));
					
					sendDialog(receiver, "form_filter_player", player, window, new Object[] { clz, name, list });
				}
			} else if (type == Type.FILTERING_SKIP) {
				ArrayList<String> list = (data instanceof ArrayList<?> ? (ArrayList<String>) data : toStringList(Server.getInstance().getOnlinePlayers().values()));
				FormWindowSimple window = new FormWindowSimple(ST.getMessage("filtering.title"), ST.getMessage("filtering.press_element"));
				if (list.size() == 0)
					window.setContent(window.getContent() + "\n\n" + ST.getMessage("filtering.no_element"));
				
				for (String element : list)
					window.addButton(new ElementButton(element));
				SimpleDialog.sendDialog(clz, name, player, window);
			}
		}
	}

	static SimpleDialog receiver = new SimpleDialog();

	@SuppressWarnings("unchecked")
	public void form_filter_player(PlayerFormRespondedEvent event, Object data) {
		Object[] arr = (Object[]) data;
		if (event.getWindow() instanceof FormWindowModal) {
			if (((FormResponseModal) event.getResponse()).getClickedButtonId() == 0) {
				FormWindowCustom window = new FormWindowCustom(ST.getMessage("filtering.title"));
				window.addElement(new ElementInput(ST.getMessage("filtering.input_name")));
				SimpleDialog.sendDialog(this, "form_filter_player", event.getPlayer(), window, data);
			} else {
				FormWindowSimple window = new FormWindowSimple(ST.getMessage("filtering.title"), ST.getMessage("filtering.press_element"));
				for (String element : (ArrayList<String>) arr[2])
					window.addButton(new ElementButton(element));
				SimpleDialog.sendDialog(arr[0], (String) arr[1], event.getPlayer(), window);
			}
		} else if (event.getWindow() instanceof FormWindowCustom) {
			String str = ((FormResponseCustom) event.getResponse()).getInputResponse(0);
			FormWindowSimple window = new FormWindowSimple(ST.getMessage("filtering.title"), ST.getMessage("filtering.press_element"));
			for (String element : (ArrayList<String>) arr[2])
				if (element.toLowerCase().startsWith(str.toLowerCase()))
					window.addButton(new ElementButton(element));
			SimpleDialog.sendDialog(arr[0], (String) arr[1], event.getPlayer(), window);
		}
	}

	public static ArrayList<String> toStringList(Collection<Player> players) {
		ArrayList<String> arr = new ArrayList<>();
		for (Player player : players)
			arr.add(player.getName());
		return arr;
	}
}
