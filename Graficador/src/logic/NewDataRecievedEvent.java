package logic;

import java.util.EventObject;

/**
 * La clase NewDataRecievedEvent sirve como contenedor de datos a la hora en que el socket envia
 * a los listeners la señal de que ha llegado un nuevo dato.
 */
public class NewDataRecievedEvent extends EventObject {

    private final String data;
    
    /**
     * Crea un nuevo objeto NewDataRecievedEvent.
     * 
     * @param source Clase que disparó el evento.
     * @param data Nuevo dato.
     */
    public NewDataRecievedEvent(Object source, String data)
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