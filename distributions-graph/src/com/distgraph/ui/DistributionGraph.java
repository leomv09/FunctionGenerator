/*
 Instituto Tecnológico de Costa Rica.
 IC-4700 Lenguajes de Programación.
 II Proyecto Programado.
 Integrantes:
    José Andrés García Sáenz <jags9415@gmail.com>.
    Leonardo Madrigal Valverde <lmadrigal09@gmail.com>.
 I Semestre 2014.
*/

package com.distgraph.ui;

import com.distgraph.net.Socket;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DistributionGraph extends JFrame {
  
  /**
    * Panel en donde se dibujara el gráfico.
    * 
  */
  private ChartPanel panel;
  
  /**
    * Botón para limpiar el gáfico.
    * 
  */
  private JButton clearButton;
  
  /**
    * Gráfico.
  */
  private JFreeChart chart;
  
  /**
    * Objecto en donde se dibujan pares de datos (x, y).
  */
  private XYPlot plot;
  
  /**
    * Objecto en donde se almacena cada serie de datos del gráfico.
  */
  private XYSeriesCollection data;
  
  /**
    * Serie actual del gráfico.
  */
  private XYSeries currentSerie;
  
  /**
    * Socket que recibe los datos.
  */
  private final Socket socket;
  
  /**
    * Indica si la serie actual posee un rango definido.
  */
  private boolean haveRange;
  
    /**
     * Crea un nuevo objeto de tipo DistributionGraph.
     * 
     * @param title Título de la ventana.
     */
    public DistributionGraph(String title)
    {
      this.setTitle(title);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setSize(700, 700);
      this.setLocationRelativeTo(null);

      this.socket = new Socket();
      this.socket.addEventListener(new UISocketHandler(this));
      
      this.haveRange = false;
      this.initializeChart();
    }
  
    /**
     * Inicia todos los valores necesarios para mostrar el gráfico.
    */
    private void initializeChart()
    {
        this.data = new XYSeriesCollection();

        this.chart = ChartFactory.createXYLineChart(
                this.getTitle(),
                "",
                "",
                this.data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        this.plot = (XYPlot) chart.getPlot();
        this.plot.setBackgroundPaint(Color.WHITE);
        this.plot.setRangeGridlinePaint(Color.BLACK);
        this.plot.setDomainGridlinePaint(Color.BLACK);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer()
        {
            @Override
            public Stroke getItemStroke(int row, int column)
            {
                return new BasicStroke(1.5f);
            }
        };
        
        renderer.setBaseShapesVisible(false);
        plot.setRenderer(renderer);

        this.panel = new ChartPanel(this.chart);
        this.clearButton = new JButton("Borrar Datos");
        this.clearButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
            {
                data.removeAllSeries();
            }
            
        });
        
        this.getContentPane().add(this.panel, BorderLayout.CENTER);
        this.getContentPane().add(this.clearButton, BorderLayout.SOUTH);
    }

    /**
     * Conecta el socket al puerto ingresado.
     * Este método solo tendrá efecto la primera vez que se llamé. Ya que el hilo para el socket no se puede reiniciar.
     * 
     * @param port El puerto en que se desea escuchar.
     */
    public void startListening(int port)
    {
        if (this.socket.getState() == Thread.State.NEW)
        {
            this.socket.setPort(port);
            this.socket.start();
        }
    }
    
    /**
     * Agrega una nueva serie al gráfico.
     * La serie es agregada con una llave aleatoria para provenir que hallan dos o más series con la misma llave.
     */
    public void createNewSerie()
    {
        Random r = new Random();
        this.currentSerie = new XYSeries(r.nextDouble());
        this.data.addSeries( this.currentSerie );
    }
  
    /**
     * Establece el nombre a mostrar en el gráfico para la serie actual.
     * 
     * @param key El nombre de la serie actual.
     */
    public void renameSerie(Comparable key)
    {
        this.currentSerie.setKey(key);
    }
  
    /**
     * Inicializa la serie actual en intervalos.
     * El rango es divido en intervalos de tamaño interval y cada valor es agregado a la serie actual con un peso de 0.
     * Para los parámetros ini=0 fin=0.5 interval=0.1 se agregará al gráfico los valores {0.0, 0.1, 0.2, 0.3, 0.4, 0.5}.
     * 
     * @param ini El inicio del rango [incluyente].
     * @param fin El final del rango [incluyente].
     * @param interval El intervalo a utilizar.
     */
    public void setRange(double ini, double fin, double interval)
    {
        this.haveRange = ini != fin;
        
        if (this.haveRange)
        {
            while (ini <= fin)
            {
                this.currentSerie.add(ini, 0);
                ini += interval;
            }   
        }
    }
  
    /**
     * Incrementa el valor en el gráfico para cierto número.
     * Si el número no existe se agrega con un peso de 1.
     * 
     * @param x El número a incrementar.
     */
    public void incrementValue(double x)
    {
      // Indice del valor a incrementar.
      int index = getIndexFromSerie(x);

      // Si ya existe el valor entonces el indice es positivo. Se incrementa su peso en 1 unidad.
      if (index >= 0)
      {
          int y = this.currentSerie.getY(index).intValue();
          this.currentSerie.updateByIndex(index, y+1);
      }
      // Si no, se agrega el valor a la serie actual.
      else
      {
          this.currentSerie.add(x, 1);
      }
    }
  
    /**
     * Obtiene el índice de un valor en la serie actual de datos.
     * Si la función actual es uniforme discreta retorna el índice exacto del valor.
     * Si no, retorna el indice del intervalo en el cual se encuetra el valor.
     * 
     * @param x El valor a buscar.
     * @return El índice. Negativo si no se encontró.
     */
    private int getIndexFromSerie(double x)
    {
      if (this.haveRange)
      {
          return getIndexFromIntervals(x);
          
      }
      else
      {
          return this.currentSerie.indexOf(x);
      }
    }

    /**
     * Retorna el índice del intervalo en el cual se encuentra cierto valor.
     * El índice retornado corresponde al extremo superior del intervalo en que se encuentra el valor.
     * Si se desea buscar el valor 0.65 y el rango definido es {0.0, 0.4, 0.7, 1.0} entonces la función
     * retornará 2 porque 0.65 se encuentre en el intervalo [0.4, 0.7].
     * 
     * @param x El valor a buscar
     * @return El índice del valor. Negativo si no se encontró.
     */
    private int getIndexFromIntervals(double x)
    {
        int i = 0;
        int count = this.currentSerie.getItemCount();
        
        while (i < count)
        {
           if (this.currentSerie.getX(i).doubleValue() >= x)
           {
               return i;
           }
           i++;
        }
        
        return -1;   
    }
          
    /**
     * Comienza la ejecución del programa.
     * 
     * @param args Argumentos del programa.
     */
    public static void main(String[] args)
    {
      DistributionGraph frame = new DistributionGraph("Gráfico de Distribución");
      frame.startListening(2020);
      frame.setVisible(true);
    }
}