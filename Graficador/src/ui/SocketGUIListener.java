package ui;

import logic.NewDataRecievedEvent;
import logic.SocketListener;

public class SocketGUIListener implements SocketListener 
{
    private final DistributionGraph frame;
    private int dataRecievedCount;
    
    public SocketGUIListener(DistributionGraph frame)
    {
        this.frame = frame;
        this.dataRecievedCount = 0;
    }
    
    @Override
    public void handleNewDataRecieved(NewDataRecievedEvent e) 
    {
        try
        {
            double x = Double.valueOf(e.getData());
            this.frame.incrementValue(x);
            this.dataRecievedCount++;
        }
        catch (NumberFormatException ex) { }
    }
}
