package iKguana.simpledialog;

import java.util.HashMap;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;

public class SimpleDialogListener implements Listener {
	public static HashMap<Integer, SimpleDialogData> queue = new HashMap<>();

	@EventHandler
	public void playerFormRespondedEvent(PlayerFormRespondedEvent event) {
		if (event.wasClosed())
			return;

		if (queue.containsKey(event.getFormID())) {
			SimpleDialogData data = queue.get(event.getFormID());
			queue.remove(event.getFormID());

			if (data.getClazz() == null || data.getFunction() == null)
				return;

			try {
				data.getClazz().getClass().getMethod(data.getFunction(), PlayerFormRespondedEvent.class, Object.class).invoke(data.getClazz(), event, data.getData());
			} catch (Exception e) {
				Server.getInstance().getLogger().alert("에러가 발생했습니다. (" + e.getClass().getSimpleName() + ")");
			}
		}
	}
}
