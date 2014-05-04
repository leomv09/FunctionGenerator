/*
 Instituto Tecnológico de Costa Rica.
 IC-4700 Lenguajes de Programación.
 II Proyecto Programado.
 Integrantes:
    José Andrés García Sáenz <jags9415@gmail.com>.
    Leonardo Madrigal Valverde <lmadrigal09@gmail.com>.
 I Semestre 2014.
*/

package com.distgraph.net;

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
    private int port;
    
    /**
     * Crea un nuevo objeto de tipo Socket.
     */
    public Socket()
    {
        super("Socket Thread");
        this.listeners = new LinkedList<>();
        this.port = 0;
    }
    
    /**
     * Crea un nuevo objeto de tipo Socket.
     * 
     * @param port El puerto en que se va recibir la información.
     */
    public Socket(int port)
    {
        super("Socket Thread");
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
     * Establece el puerto del socket.
     * 
     * @param port El nuevo puerto.
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Obtiene el puerto del socket.
     * 
     * @return El puerto.
     */
    public int getPort()
    {
        return this.port;
    }
    
    /**
     * Método principal que espera conexiones y recibe los datos.
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
            this.port = server.getLocalPort();
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
            
        }
        catch (IOException e)
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
