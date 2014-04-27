package com.distgraph.net;

/**
 * Interfaz que proporciona la comunicación entre listeners y eventos disparados por el Socket.
 */
public interface SocketListener {
    
    /**
     * Procesa la información cuando el socket recibe un nuevo dato.
     * 
     * @param e Evento a procesar.
     */
    public void handleDataRecieved(DataRecievedEvent e);
    
    /**
     * Procesa la información cuando un nuevo cliente se conecta al socket.
     * 
     * @param e Evento a procesar.
     */
    public void handleClientConnected(ClientConnectedEvent e);
    
}