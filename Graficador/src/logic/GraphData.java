package logic;

import java.util.Arrays;

/**
 * La clase GraphData implementa un contenedor para la información representada en un gráfico.
 */
public class GraphData {
    
    private int[] keys;
    private int[] values;
    private int ini_range;
    private int fin_range;
    private int functionType;
    
    /**
     * Crea un nuevo objeto GraphData.
     */
    public GraphData() {
        this.keys = new int[0];
        this.values = new int[0];
        this.ini_range = 0;
        this.fin_range = 0;
        this.functionType = 0;
    }
    
    public void setIniRange(int ini)
    {
        this.ini_range = ini;
    }
    
    public void setFinRange(int fin)
    {
        this.fin_range = fin;
        
        this.keys = new int[this.fin_range - this.ini_range + 1];
        this.values = new int[this.fin_range - this.ini_range + 1];
        
        for (int i=0; i<this.keys.length; i++) {
            this.keys[i] = this.ini_range + i;
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
     * Obtiene el tipo de función a mostrar en el gráfico.
     * 
     * @return El tipo de función a mostrar en el gráfico.
     */
    public int getFunctionType() {
        return this.functionType;
    }
    
    public int getIniRange() {
        return this.ini_range;
    }
    
    public int getFinRange() {
        return this.fin_range;
    }
}
