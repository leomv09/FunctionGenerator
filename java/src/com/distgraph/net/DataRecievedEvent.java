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

/**
 * La clase DataRecievedEvent sirve como contenedor de datos a la hora en que el socket envia
 a los listeners la señal de que ha llegado un nuevo dato.
 */
public class DataRecievedEvent extends EventObject {

    private final String data;
    
    /**
     * Crea un nuevo objeto DataRecievedEvent.
     * 
     * @param source Clase que disparó el evento.
     * @param data Nuevo dato.
     */
    public DataRecievedEvent(Object source, String data)
    {
        super(source);
        this.data = data;
    }
    
    /**
     * Obtiene el dato último dato que llegó.
     * 
     * @return El dato último dato que llegó.
     */
    public String getData()
    {
        return this.data;
    }
}