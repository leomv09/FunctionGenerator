package logic;

public class GraphSocketListener implements NewDataRecievedListener
{
    private GraphData data;
    
    public GraphSocketListener(GraphData data)
    {
        this.data = data;
    }
    
    @Override
    public void handleEvent(NewDataRecievedEvent e) {
        System.out.println("Data: " + e.getDataCount() + " " + e.getData());
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
