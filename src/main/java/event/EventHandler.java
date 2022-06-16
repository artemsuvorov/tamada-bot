package event;

import java.util.ArrayList;

public class EventHandler {

    private final ArrayList<ActionListener> listeners;

    public EventHandler() {
        this.listeners = new ArrayList<>();
    }

    public void invoke(Object sender, ActionEvent event) {
        for (var listener : listeners) {
            listener.actionPerformed(event);
        }
    }

    public void addListener(ActionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ActionListener listener) {
        listeners.remove(listener);
    }

}