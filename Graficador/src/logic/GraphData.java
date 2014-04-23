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
    
    /**
     * Establece el inicio del rango del gráfico.
     * 
     * @param ini El inicio del rango.
     */
    public void setIniRange(int ini) throws Exception
    {
        this.ini_range = ini;
    }
    
    /**
     * Establece el final del rango del gráfico.
     * 
     * @param fin El final del rango.
     * @throws java.lang.Exception Si el final del rango es menor que el inicio. 
     */
    public void setFinRange(int fin) throws Exception
    {
        if (fin >= this.ini_range)
        {
            this.fin_range = fin;
            this.keys = new int[this.fin_range - this.ini_range + 1];
            this.values = new int[this.fin_range - this.ini_range + 1];

            for (int i=0; i<this.keys.length; i++)
            {
                this.keys[i] = this.ini_range + i;
                this.values[i] = 0;
            }
        }
        else
        {
            throw new Exception("El final del rango debe ser mayor que el inicio del mismo.");
        }
    }
    
    /**
     * Establece el tipo de función que mostrará el gráfico.
     * 
     * @param type Tipo de la función. DISCRETA = 0, CONTINUA = 1.
     * @throws java.lang.Exception Si el tipo no es válido.
     */
    public void setFunctionType(int type) throws Exception
    {
        if (type == 0 || type == 1 || type == 2)
        {
            this.functionType = type;
        }
        else
        {
            throw new Exception("La funcion debe ser 0 = Discreta o 1 = Continua o 2 = Uniforme.");
        }
    }
    
    /**
     * Incrementa el peso almacenado para cierto valor.
     * 
     * @param key Valor a incrementar.
     * @throws java.lang.Exception Si el valor a incrementar no se encuentra entre el rango definido.
     */
    public void incrementCount(int key) throws Exception
    {
        int index = Arrays.binarySearch(this.keys, key);
        
        if (index >= 0)
        {
            this.values[index]++;
        }
        else
        {
            throw new Exception("El valor a incrementar debe estar entre el rango definido.");
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
    
    /**
     * Obtiene el inicio del rango del gráfico.
     * 
     * @return El inicio del rango.
     */
    public int getIniRange() {
        return this.ini_range;
    }
    
    /**
     * Obtiene el final del rango del gráfico.
     * 
     * @return El final del rango.
     */
    public int getFinRange() {
        return this.fin_range;
    }
}
