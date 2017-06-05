package app.view.custom;

import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;

public class ScrollHandler implements EventHandler<ScrollEvent> {
	@Override
	public void handle(ScrollEvent event) {
		// deltaY == 40 by default
		GridPane source = (GridPane) event.getSource();
		ScrollPane scroll = (ScrollPane) source.getParent().getParent().getParent();
		double deltaY = event.getDeltaY();
		double height = scroll.getContent().getBoundsInLocal().getHeight();
		double vvalue = scroll.getVvalue();
		if (deltaY > 0) {
			scroll.setVvalue(vvalue - ((height / 100 * 20) / height)); // height / 100 * 20 / height = 20% of height
		} else {
			scroll.setVvalue(vvalue + ((height / 100 * 20) / height));
		}
	}
}
