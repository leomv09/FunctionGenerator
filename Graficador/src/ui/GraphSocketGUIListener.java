package ui;

import logic.NewDataRecievedEvent;
import logic.NewDataRecievedListener;

/**
 * La clase GraphSocketGUIListener implementa los métodos necesarios para actualizar
 * la interfaz de usuario con los datos provenitentes del socket.
 */
public class GraphSocketGUIListener implements NewDataRecievedListener {

    private GraphPanel panel;
    
    /**
     * Crea un nuevo objeto GraphSocketGUIListener
     * 
     * @param panel El objeto GraphPanel que se desea actualizar cuando lleguen datos del socket.
     */
    public GraphSocketGUIListener(GraphPanel panel)
    {
        this.panel = panel;
    }
    
    @Override
    public void handleEvent(NewDataRecievedEvent e) 
    {
        this.panel.repaint();
    }
    
}
