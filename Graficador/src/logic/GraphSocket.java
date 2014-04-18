package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase GraphSocket proporciona un método para obtener datos provenientes de una conexión en red
 * por medio de Sockets.
 */
public class GraphSocket extends Thread {
    
    private List listeners; // Conjunto de listeners del socket.
    private ServerSocket serverSocket;// Socket que espera las conexiones.
    private Socket clientSocket;// El socket que se conecta.
    private BufferedReader in;// Buffer de lectura.
    private int dataCount;// Contador de datos recibidos.
    
    /**
     * Crea un nuevo objeto de tipo GraphSocket.
     * 
     * @param port El puerto en que se va recibir la información.
     */
    public GraphSocket(int port) {
        this.listeners = new ArrayList();
        this.dataCount = 0;
        
        try
        {
            this.serverSocket = new ServerSocket(port);
        }
        catch(IOException e)
        {
           System.out.println("Error al intentar escuchar al puerto ");
           System.out.println("Error: " + e);
        }   
    }
    
    /**
     * Agrega un listener al conjunto de listeners.
     * 
     * @param listener El nuevo listener.
     */
    public synchronized void addEventListener(NewDataRecievedListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Elimina un listener del conjunto de listeners.
     * 
     * @param listener El listener a eliminar.
     */
    public synchronized void removeEventListener(NewDataRecievedListener listener) {
        listeners.remove(listener);
    }

    /**
     * Dispara un evento a todos los listener avisando que hay un nuevo dato disponible.
     * 
     * @param dataCount El número de datos que han llegado hasta el momento.
     * @param data El último dato que llegó.
     */
    private synchronized void fireEvent(int dataCount, int data) {
      NewDataRecievedEvent event = new NewDataRecievedEvent(this, dataCount, data);
      java.util.Iterator i = listeners.iterator();
      while (i.hasNext()) {
        ((NewDataRecievedListener) i.next()).handleEvent(event);
      }
    }
    
    /**
     * Método principal que espera una conexión y luego recibe los datos.
     */
    @Override
    public void run()
    {
        try
         {
             this.clientSocket = this.serverSocket.accept();
             
             while(this.clientSocket.isConnected())
             {
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                int input = in.read();

                if (input != -1)
                {
                    this.fireEvent(this.dataCount++, input);
                }
             }

            }
            catch(IOException e)
            {
               System.out.println("Error al intentar escuchar al puerto.");
               System.out.println("Error: " + e);
            } 
    }
    
}
