package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import logic.Socket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DistributionGraph extends JFrame {
  
  private static final long serialVersionUID = 1L;
  
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
    * Objecto en donde se almacena cada serie de datos del gráfico.
  */
  private XYSeriesCollection data;
  
  /**
    * Serie actual del gráfico.
  */
  private XYSeries serie;
  
  /**
    * Socket que recibe los datos.
  */
  private final Socket socket;
  
  public DistributionGraph()
  {
    this.createChart();
    this.createPanel();
    
    this.setTitle("Gráfico de Distribución");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(650, 650);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    
    this.socket = new Socket(2020);
    this.socket.addEventListener(new SocketGUIListener(this));
    
    this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowOpened(WindowEvent evt)
        {
            socket.start();
        }
    });
  }
          
          
  private void createChart()
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
    
    this.createNewSerie();
  }
  
  private void createPanel()
  {
    this.panel = new ChartPanel(this.chart);
    this.getContentPane().add(this.panel, BorderLayout.CENTER);
  }
  
  public void createNewSerie()
  {
      this.serie = new XYSeries("");
      this.data.addSeries( this.serie );
      this.plot.getRenderer().setSeriesStroke(this.data.getSeriesCount()-1, new BasicStroke(2));
  }
  
  public void setSerieKey(Comparable key)
  {
      this.serie.setKey(key);
  }
  
  public void setRange(double ini, double fin, double interval)
  {
      throw new UnsupportedOperationException("Not implemented");
  }
  
  public void incrementValue(double x)
  {
    int index = this.serie.indexOf(x);

    if (index >= 0)
    {
        int y = this.serie.getY(index).intValue();
        this.serie.updateByIndex(index, y+1);
    }
    else
    {
        this.serie.add(x, 1);
    }
  }
          
  public static void main(String[] args)
  {
    DistributionGraph frame = new DistributionGraph();
  }
}