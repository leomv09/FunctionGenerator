package logic;

import java.util.Arrays;

/**
 * La clase GraphData implementa un contenedor para la información representada en un gráfico.
 */
public class GraphData {
    
    private int[] keys;
    private int[] values;
    private int range;
    private int functionType;
    
    /**
     * Crea un nuevo objeto GraphData.
     */
    public GraphData() {
        this.keys = new int[0];
        this.values = new int[0];
        this.range = -1;
        this.functionType = 0;
    }

    /**
     * Establece el rango en el que estarán los valores del gráfico.
     * 
     * @param ini Inicio del rango.
     * @param fin Final del rango.
     */
    public void setRange(int ini, int fin) {
        this.range = fin;
        this.keys = new int[fin-ini+1];
        this.values = new int[fin-ini+1];
        
        for (int i=0; i<=fin-ini; i++) {
            this.keys[i] = ini+i;
            this.values[i] = 0;
        }
    }
    
    
    /**
     * Establece el tipo de función que mostrará el gráfico.
     * 
     * @param type Tipo de función.
     */
    public void setFunctionType(int type)
    {
        this.functionType = type;
    }
    
    /**
     * Incrementa el peso almacenado para cierto valor.
     * 
     * @param key Valor a incrementar.
     */
    public void incrementCount(int key) {
        int index = Arrays.binarySearch(this.keys, key);
        if (index != -1) {
            this.values[index]++;
        }
    }
    
    /**
     * Obtiene el conjunto de valores.
     * 
     * @return El conjunto de valores.
     */
    public int[] getKeys() {
        return this.keys;
    }
    
    /**
     * Obtiene el conjunto de pesos de los valores.
     * 
     * @return El conjunto de pesos de los valores.
     */
    public int[] getValues() {
        return this.values;
    }
    
    /**
     * Obtiene el rango de valores del gráfico.
     * 
     * @return El rango de valores del gráfico.
     */
    public int getRange()
    {
        return this.range;
    }
    
    /**
     * Obtiene el tipo de función a mostrar en el gráfico.
     * 
     * @return El tipo de función a mostrar en el gráfico.
     */
    public int getFunctionType()
    {
        return this.functionType;
    }
}
