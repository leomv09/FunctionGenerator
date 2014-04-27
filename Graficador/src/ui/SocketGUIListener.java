package ui;

import logic.ClientConnectedEvent;
import logic.DataRecievedEvent;
import logic.SocketListener;

/**
 * La clase SocketGUIListener actualiza la interfaz gráfica con los datos que lleguen del Socket.
 */
public class SocketGUIListener implements SocketListener 
{
    private final DistributionGraph frame;
    private int dataRecievedCount;
    private int clientConnectedCount;
    
    /**
     * Crea un nuevo objeto SocketGUIListener.
     * 
     * @param frame El objeto DistributionGraph que se desea actualizar.
     */
    public SocketGUIListener(DistributionGraph frame)
    {
        this.frame = frame;
        this.dataRecievedCount = 0;
        this.clientConnectedCount = 0;
    }
    
    @Override
    public void handleDataRecieved(DataRecievedEvent e) 
    {
        try
        {
            double x = Double.valueOf(e.getData());
            this.frame.incrementValue(x);
            this.dataRecievedCount++;
        }
        catch (NumberFormatException ex) { }
    }

    @Override
    public void handleClientConnected(ClientConnectedEvent e)
    {
        this.clientConnectedCount++;
        this.dataRecievedCount = 0;
    }
}
