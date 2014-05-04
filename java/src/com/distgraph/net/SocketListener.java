/*
 Instituto Tecnológico de Costa Rica.
 IC-4700 Lenguajes de Programación.
 II Proyecto Programado.
 Integrantes:
    José Andrés García Sáenz <jags9415@gmail.com>.
    Leonardo Madrigal Valverde <lmadrigal09@gmail.com>.
 I Semestre 2014.
*/

package com.distgraph.net;

/**
 * Interfaz que proporciona la comunicación entre los eventos disparados por el Socket y los manejadores de eventos.
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