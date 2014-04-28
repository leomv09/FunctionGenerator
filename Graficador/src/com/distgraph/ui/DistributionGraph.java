package com.distgraph.ui;

import com.distgraph.net.Socket;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DistributionGraph extends JFrame {
  
  /**
    * Panel en donde se dibujara el gráfico.
    * 
  */
  private ChartPanel panel;
  
  /**
    * Gráfico.
  */
  private JFreeChart chart;
  
  /**
    * Objecto que dibuja pared de datos (x, y).
  */
  private XYPlot plot;
  
  /**
    * Objecto en donde se almacena cada currentSerie de datos del gráfico.
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
     * Crea un nuevo objeto de tipo DistributionGraph.
     */
    public DistributionGraph()
    {
      this.initializeChart();

      this.setTitle("Gráfico de Distribución");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setSize(650, 650);
      this.setLocationRelativeTo(null);
      this.setVisible(true);

      this.socket = new Socket(2020);
      this.socket.addEventListener(new SocketGUIListener(this));
      this.socket.start();
    }
  
    /**
     * Inicia todos los valores necesarios para mostrar el gráfico.
    */
    private void initializeChart()
    {
      this.data = new XYSeriesCollection();

      this.chart = ChartFactory.createXYLineChart(
              "Gráfico de Distribución",
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

      this.panel = new ChartPanel(this.chart);
      this.getContentPane().add(this.panel, BorderLayout.CENTER);
    }
  
    /**
     * Agrega una nueva currentSerie al gráfico.
     */
    public void createNewSerie()
    {
        // Crear la serie.
        this.currentSerie = new XYSeries("");
        // Agregarla a la colección de series.
        this.data.addSeries( this.currentSerie );
        // Dibujarla con una línea de grueso #2.
        this.plot.getRenderer().setSeriesStroke(this.data.getSeriesCount()-1, new BasicStroke(2));
    }
  
    /**
     * Establece el nombre a mostrar en el gráfico para la currentSerie actual.
     * 
     * @param key El nombre de la currentSerie.
     */
    public void renameSerie(Comparable key)
    {
        this.currentSerie.setKey(key);
    }
  
    /**
     * Inicializa la currentSerie en intervalos.
     * El rango es divido en intervalos de tamaño interval y cada valor es agregado a la serie actual con un peso de 0.
     * 
     * @param ini El inicio del rango [incluyente].
     * @param fin El final del rango [incluyente].
     * @param interval El intervalo a utilizar.
     */
    public void setRange(double ini, double fin, double interval)
    {
        throw new UnsupportedOperationException("Not implemented");
    }
  
    /**
     * Incrementa el valor en el gráfico para cierto número.
     * Si el número no existe se agrega.
     * 
     * @param x El número a incrementar.
     */
    public void incrementValue(double x)
    {
      // Indice del valor a incrementar.
      int index = getIndexFromSerie(x);

      // Si ya existe el valor entonces el indice es positivo. Se incrementa su valor Y en 1 unidad.
      if (index >= 0)
      {
          int y = this.currentSerie.getY(index).intValue();
          this.currentSerie.updateByIndex(index, y+1);
      }
      // Si no, se agrega el valor a la currentSerie.
      else
      {
          this.currentSerie.add(x, 1);
      }
    }
  
    /**
     * Obtiene el índice de un valor en la currentSerie de datos.
     * Si la función actual es uniforme discreta retorna el índice exacto del valor.
     * Si no, retorna el indice del intervalo en el cual se encuetra el valor.
     * 
     * @param x El valor a buscar.
     * @return El índice. Negativo si no se encontró.
     */
    public int getIndexFromSerie(double x)
    {
      String key = String.valueOf(this.currentSerie.getKey());

      if (key.endsWith("UNIFORME-DISC"))
      {
          return this.currentSerie.indexOf(x);
      }
      else
      {
          return getIndexFromIntervals(x);
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
    public int getIndexFromIntervals(double x)
    {
      throw new UnsupportedOperationException("Not implemented");
    }
          
    public static void main(String[] args)
    {
      DistributionGraph frame = new DistributionGraph();
    }
}