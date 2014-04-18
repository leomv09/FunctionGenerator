package logic;

import java.util.EventObject;

public class NewDataRecievedEvent extends EventObject {

    private int dataCount;
    private int data;
    
    public NewDataRecievedEvent(Object source, int dataCount, int data) {
        super(source);
        this.dataCount = dataCount;
        this.data = data;
    }
    
    public int getDataCount() {
        return this.dataCount;
    }
    
    public int getData() {
        return this.data;
    }
}