package app.controller.test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;


public class ComboBoxAC<T> extends ComboBox<T> {

	private Metier<T> metier;


	public ComboBoxAC(){
		getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println("on est la");
				if (newValue != null && !newValue.isEmpty()) {
					ObservableList collect = FXCollections.observableArrayList();
					metier.getList().stream()
							.filter(o -> o.toString().contains(newValue))
							.forEach(collect::add);
					itemsProperty().unbind();
					itemsProperty().bind(new SimpleListProperty<T>(
							collect));
					show();
				} else {
					itemsProperty().unbind();
					itemsProperty().bind(metier.listProperty());
				}
			}
		});
	}

	@Override
	public ObjectProperty<ObservableList<T>> itemsProperty() {
		return super.itemsProperty();
	}

	public void setMetier(Metier<T> metier){this.metier = metier;}
}
