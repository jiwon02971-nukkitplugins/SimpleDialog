package iKguana.simpledialog;

public class SimpleDialogData {
	private Object clazz;
	private String fc;
	private Object data;

	public SimpleDialogData(Object clazz, String fc, Object data) {
		this.clazz = clazz;
		this.fc = fc;
		this.data = data;
	}

	public Object getClazz() {
		return clazz;
	}

	public String getFunction() {
		return fc;
	}

	public Object getData() {
		return data;
	}
}
