package app.controller.test;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

public class Metier<T> {

	protected ListProperty<T> list = new SimpleListProperty<T>();

	public ObservableList<T> getList() {
		return list.get();
	}

	public ListProperty<T> listProperty() {
		return list;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void setList(ObservableList<T> list) {
		this.list.set((ObservableList<T>) list);
	}
}
