package event;

import java.util.EventListener;

public interface ActionListener extends EventListener {

    void actionPerformed(ActionEvent event);

}