package logic;

/**
 * Interfaz que proporciona la comunicación entre listeners y eventos disparados por el Socket.
 */
public interface SocketListener {
    
    /**
     * Procesa la información cuando el socket recibe un nuevo dato.
     * 
     * @param e Evento a procesar.
     */
    public void handleNewDataRecieved(NewDataRecievedEvent e);
    
}