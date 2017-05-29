package app.controller.test;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

public class Metier<T> {

	protected ListProperty<T> list = new SimpleListProperty<T>();

	public ObservableList getList() {
		return list.get();
	}

	public ListProperty<T> listProperty() {
		return list;
	}

	private void setList(ObservableList list) {
		this.list.set(list);
	}
}
