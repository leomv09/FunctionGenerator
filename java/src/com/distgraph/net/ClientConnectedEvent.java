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

import java.util.EventObject;
import java.util.Date;

/**
 * La clase ClientConnectedEvent como contenedor de los datos del nuevo cliente que se ha conectado al Socket.
 */
public class ClientConnectedEvent extends EventObject {
    
    private final java.net.Socket client;
    private final Date connectionTime;
    
    /**
     * Crea un nuevo objeto ClientConnectedEvent.
     * 
     * @param source Clase que disparó el evento.
     * @param client Nuevo cliente.
     */
    public ClientConnectedEvent(Object source, java.net.Socket client)
    {
        super(source);
        this.client = client;
        this.connectionTime = new Date();
    }

    /**
     * Obtiene el socket cliente.
     * 
     * @return El socket cliente.
     */
    public java.net.Socket getClient()
    {
        return this.client;
    }
    
    /**
     * Obtiene la fecha y hora en que se conectó el cliente.
     * 
     * @return La fecha y hora en que se conectó el cliente.
     */
    public Date getConnectionTime()
    {
        return this.connectionTime;
    }
    
}
