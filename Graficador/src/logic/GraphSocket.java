package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * La clase GraphSocket proporciona un método para obtener datos provenientes de una conexión en red
 * por medio de Sockets.
 */
public class GraphSocket extends Thread {
    
    private List<NewDataRecievedListener> listeners;
    private ServerSocket server;
    private Socket client;
    private BufferedReader in;
    private BufferedWriter out;
    private int port;
    
    /**
     * Crea un nuevo objeto de tipo GraphSocket.
     * 
     * @param port El puerto en que se va recibir la información.
     */
    public GraphSocket(int port)
    {
        this.listeners = new LinkedList<>();
        this.port = port;
        this.server = null;
        this.client = null;
        this.in = null;
        this.out = null;
    }
    
    /**
     * Agrega un listener al conjunto de listeners.
     * 
     * @param listener El nuevo listener.
     */
    public synchronized void addEventListener(NewDataRecievedListener listener)
    {
        listeners.add(listener);
    }
    
    /**
     * Elimina un listener del conjunto de listeners.
     * 
     * @param listener El listener a eliminar.
     */
    public synchronized void removeEventListener(NewDataRecievedListener listener)
    {
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
     * Abre los puertos del socket y inicia los flujos de lectura y escritura.
     * 
     * @throws IOException Si no se pudo abrir correctamente un puerto.
     */
    private void connect() throws IOException
    {
        this.server = new ServerSocket(this.port);
        this.client = this.server.accept();
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    }
    
    /**
     * Cierra los flujos de lectura y escritura y los puertos del socket.
     * 
     * @throws IOException Si no se pudo cerrar correctamente un puerto o flujo.
     */
    private void close() throws IOException
    {
        this.out.close();
        this.in.close();
        this.client.close();
        this.server.close();
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
        try
        {
            this.connect();
            String input;

            while ((input = this.in.readLine()) != null)
            {
               this.fireEvent(input);
            }
            
            this.close();
        }
        catch(IOException e)
        {
           System.out.println(this + " Error: " + e.getMessage());
        }
    }
    
    @Override
    public String toString()
    {
        return "[" + "GraphSocketClient localhost::" + this.port + "]";
    }
    
}
