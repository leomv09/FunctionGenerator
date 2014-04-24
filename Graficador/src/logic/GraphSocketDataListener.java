package logic;

/**
 * La clase GraphSocketDataListener implementa los métodos necesarios para procesar la información proveniente del socket.
 * Los datos provenientes son almacenados en un objeto GraphData.
 */
public class GraphSocketDataListener implements NewDataRecievedListener
{
    private GraphData data;
    private int dataRecievedCount;
    
    /**
     * Crea un nuevo objeto GraphSocketListener.
     * 
     * @param data Objeto de tipo GraphData en donde se guardará la información proveniente del socket.
     */
    public GraphSocketDataListener(GraphData data)
    {
        this.data = data;
        dataRecievedCount = 0;
    }
    
    @Override
    public void handleEvent(NewDataRecievedEvent e) {
        try
        {
            int input = Integer.valueOf( e.getData() );
            
            switch (this.dataRecievedCount)
            {
                case 0:
                    try 
                    {
                        this.data.setFunctionType(input);
                        this.dataRecievedCount++;
                    }
                    catch (Exception ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 1:
                    try 
                    {
                        this.data.setIniRange(input);
                        this.dataRecievedCount++;
                    } 
                    catch (Exception ex) 
                    {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 2:
                    try 
                    {
                        this.data.setFinRange(input);
                        this.dataRecievedCount++;
                    } 
                    catch (Exception ex) 
                    {
                        System.out.println(ex.getMessage());
                    }
                    break;
                default:
                    try 
                    {
                        this.data.incrementCount(input);
                        this.dataRecievedCount++;
                    } 
                    catch (Exception ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                    break;
            }
        }
        catch (NumberFormatException ex)
        {
            System.out.println("El dato recibido no es un número.");
        }
    }
}
