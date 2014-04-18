package logic;

/**
 * La clase GraphSocketListener implementa los métodos necesarios para procesar la información proveniente del socket.
 */
public class GraphSocketListener implements NewDataRecievedListener
{
    private GraphData data;
    
    /**
     * Crea un nuevo objeto GraphSocketListener.
     * 
     * @param data Objeto de tipo GraphData en donde se guardará la información proveniente del socket.
     */
    public GraphSocketListener(GraphData data)
    {
        this.data = data;
    }
    
    @Override
    public void handleEvent(NewDataRecievedEvent e) {
        switch(e.getDataCount())
        {
            case 0:
                this.data.setFunctionType(e.getData());
                break;
            case 1:
                this.data.setIniRange(e.getData());
                break;
            case 2:
                this.data.setFinRange(e.getData());
                break;
            default:
                this.data.incrementCount(e.getData());
                break;
        }
    }
}
