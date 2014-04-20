package logic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.LinkedList;

/**
 * La clase GraphSocket proporciona un método para obtener datos provenientes de una conexión en red
 * por medio de Sockets.
 */
public class GraphSocket extends Thread {
    
    private List<NewDataRecievedListener> listeners; // Conjunto de listeners del socket.
    private int port; // El puerto a escuchar.
    private ServerSocket serverSocket;// Socket que espera las conexiones.
    private Socket clientSocket;// El socket que se conecta.
    
    /**
     * Crea un nuevo objeto de tipo GraphSocket.
     * 
     * @param port El puerto en que se va recibir la información.
     */
    public GraphSocket(int port)
    {
        this.listeners = new LinkedList<>();
        this.port = port;
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
     * @param data El último dato que llegó.
     */
    private synchronized void fireEvent(String data) 
    {
      NewDataRecievedEvent event = new NewDataRecievedEvent(this, data);
      
      for (NewDataRecievedListener l : this.listeners)
      {
          l.handleEvent(event);
      }
    }
    
    /**
     * Método principal que espera una conexión y luego recibe los datos.
     * Se dispara un evento de tipo NewDataRecievedEvent a cada listener cada vez
     * que una nueva línea de texto a sido leída.
     * Cada texto enviado debe terminar en un salto de línea.
     */
    @Override
    public void run()
    {
        BufferedReader reader;
        try
        {
            this.serverSocket = new ServerSocket(this.port);
            this.clientSocket = this.serverSocket.accept();
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String input;

            while (this.clientSocket.isConnected() && (input = reader.readLine()) != null)
            {
               this.fireEvent(input);
            }

            reader.close();
            this.serverSocket.close();
        }
        catch(IOException e)
        {
           System.out.println("Error al intentar escuchar al puerto.");
           System.out.println("Error: " + e.getMessage());
        }
    }
    
}
