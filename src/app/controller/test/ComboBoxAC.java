package app.controller.test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;


public class ComboBoxAC<T> extends ComboBox<T> {

	private Metier<T> metier;


	public ComboBoxAC(){
		getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null && !newValue.isEmpty()) {
					System.out.println("newValue : " + newValue);

					getEditor().setText(newValue);
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
		selectionModelProperty().addListener(new ChangeListener<SingleSelectionModel<T>>() {
			@Override
			public void changed(ObservableValue<? extends SingleSelectionModel<T>> observable, SingleSelectionModel<T> oldValue, SingleSelectionModel<T> newValue) {
				System.out.println("selection :");
				System.out.println(oldValue);
				System.out.println(newValue);
			}
		});
	}

	public void setMetier(Metier<T> metier){this.metier = metier;}
}
