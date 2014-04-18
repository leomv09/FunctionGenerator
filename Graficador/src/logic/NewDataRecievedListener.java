package logic;

/**
 * Interfaz que proporciona la comunicación entre listeners y eventos del tipo NewDataRecieved.
 */
public interface NewDataRecievedListener {
    
    /**
     * Procesa la información del evento.
     * 
     * @param e Evento a procesar.
     */
    public void handleEvent(NewDataRecievedEvent e);
    
}