package logic;

import java.net.*;
import java.io.*;

public class GraphSocket extends Thread{
    
    private ServerSocket serverSocket;//Socket que espera las conexiones.
    private Socket clientSocket;//El socket que se conecta.
    private BufferedReader in;// Buffer de lectura.
    public GraphData data;// Controlador de datos para el gráfico.
    private int dataStart;// Inicio del rango de valores  para el gráfico.
    private int dataEnd;// Final del rango de valores para el gráfico
    private int dataCount;// Contador de datos recibidos.
    
    /**
     * Crea un nuevo objeto de tipo GraphSocket.
     */
    public GraphSocket() {
        this.data = new GraphData();
        this.dataCount = 0;
        this.dataStart = 0;
        this.dataEnd = 0;
        try
        {
            this.serverSocket = new ServerSocket(2020);
        }
        catch(IOException e)
        {
           System.out.println("Error al intentar escuchar al puerto ");
           System.out.println("Error: " + e);
        }   
    }
    
    /**
     * Establece el objeto encargado de manejar los datos para el gráfico
     * 
     * @param Data nuevo objeto que maneja los datos del gráfico.
     */
    public void setData(GraphData Data)
    {
        this.data = Data;
    }
    
    /**
     * Obtiene el controlador de datos del gráfico.
     * 
     * @return El controlador de datos del gráfico.
     */
    public GraphData getData()
    {
        return this.data;
    }
    
    
    /*
     Método principal que espera una conexión y luego recibe los datos y los procesa.
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

                   if(input != -1)
                   {
                       switch(dataCount)
                       {
                           case 0: this.data.setFunctionType(input);
                                   this.dataCount++;
                               break;
                           case 1: this.dataStart = input;
                                   this.dataCount++;
                               break;
                           case 2: this.dataEnd = input;
                                   this.data.setRange(this.dataStart, this.dataEnd);
                                   this.dataCount++;
                               break;
                           default: this.data.incrementCount(input);
                                    this.dataCount++;
                               break;
                       }
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
