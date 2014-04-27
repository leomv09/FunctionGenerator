package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

/**
 * La clase Socket proporciona un método para obtener datos provenientes de una conexión en red
 * por medio de Sockets.
 */
public class Socket extends Thread {
    
    private final List<SocketListener> listeners;
    private final int port;
    
    /**
     * Crea un nuevo objeto de tipo Socket.
     * 
     * @param port El puerto en que se va recibir la información.
     */
    public Socket(int port)
    {
        this.listeners = new LinkedList<>();
        this.port = port;
    }
    
    /**
     * Agrega un listener al conjunto de listeners.
     * 
     * @param listener El nuevo listener.
     */
    public synchronized void addEventListener(SocketListener listener)
    {
        listeners.add(listener);
    }
    
    /**
     * Elimina un listener del conjunto de listeners.
     * 
     * @param listener El listener a eliminar.
     */
    public synchronized void removeEventListener(SocketListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Dispara un evento a todos los listener avisando que hay un nuevo dato disponible.
     * 
     * @param data El último dato que llegó.
     */
    private synchronized void fireDataRecievedEvent(String data) 
    {
      DataRecievedEvent event = new DataRecievedEvent(this, data);
      
      for (SocketListener l : this.listeners)
      {
          l.handleDataRecieved(event);
      }
    }
    
    /**
     * Dispara un evento a todos los listener avisando que se conectó un nuevo cliente.
     * 
     * @param client El socket del nuevo cliente.
     */
    private synchronized void fireClientConnectedEvent(java.net.Socket client) 
    {
      ClientConnectedEvent event = new ClientConnectedEvent(this, client);
      
      for (SocketListener l : this.listeners)
      {
          l.handleClientConnected(event);
      }
    }
    
    /**
     * Método principal que espera una conexión y luego recibe los datos.
     */
    @Override
    public void run()
    {
        ServerSocket server;
        java.net.Socket client;
        BufferedReader in;
        BufferedWriter out;
        
        try
        {
            server = new ServerSocket(this.port);
            System.out.println(this + " Start listening.");
            String input;
            
            while ((client = server.accept()) != null)
            {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                this.fireClientConnectedEvent(client);
                
                while ((input = in.readLine()) != null)
                {
                   this.fireDataRecievedEvent(input);
                }
                
                out.close();
                in.close();
                client.close();
            }
            
            server.close();
            System.out.println(this + " Closed.");
        }
        catch(IOException e)
        {
           System.out.println(this + " Error: " + e.getMessage());
        }
    }
    
    @Override
    public String toString()
    {
        return "[" + "Socket localhost::" + this.port + "]";
    }
    
}
