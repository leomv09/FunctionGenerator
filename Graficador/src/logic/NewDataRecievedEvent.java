package logic;

import java.util.EventObject;

/**
 * La clase NewDataRecievedEvent sirve como contenedor de datos a la hora en que el socket envia
 * a los listeners la señal de que ha llegado un nuevo dato.
 */
public class NewDataRecievedEvent extends EventObject {

    private int dataCount;
    private int data;
    
    /**
     * Crea un nuevo objeto NewDataRecievedEvent.
     * 
     * @param source Clase que disparó el evento.
     * @param dataCount Total que datos que han llegado hasta el momento.
     * @param data Nuevo dato.
     */
    public NewDataRecievedEvent(Object source, int dataCount, int data) {
        super(source);
        this.dataCount = dataCount;
        this.data = data;
    }
    
    /**
     * Obtiene el total que datos que han llegado hasta el momento.
     * 
     * @return El total que datos que han llegado hasta el momento.
     */
    public int getDataCount() {
        return this.dataCount;
    }
    
    /**
     * Obtiene el dato último dato que llegó.
     * 
     * @return El dato último dato que llegó.
     */
    public int getData() {
        return this.data;
    }
}