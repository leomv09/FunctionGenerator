package logic;

public class GraphData {
    
    private int[] keys;
    private int[] values;
    
    public GraphData() {
        this.keys = new int[0];
        this.values = new int[0];
    }

    public void setRange(int ini, int fin){
        this.keys = new int[fin-ini+1];
        this.values = new int[fin-ini+1];
        
        int cont = 0;
        while (ini <= fin) {
            this.keys[cont] = ini++;
            this.values[cont++] = 0;
        }
    }
    
    public int getCount(int key)
    {
        int index = binarySearch(key);
        if (index != -1) {
            return this.values[index];
        }
        else {
            return -1;
        }
    }
    
    public void incrementCount(int key) {
        int index = binarySearch(key);
        if (index != -1) {
            this.values[index]++;
        }
    }
    
    public int[] getKeys() {
        return this.keys;
    }
    
    public int[] getValues() {
        return this.values;
    }
    
    private int binarySearch(int key) {
        int hi = this.keys.length - 1;
        int lo = 0;
        int index;
        
        while (hi >= lo) {
            index = lo + ((hi - lo) / 2);
            if (this.keys[index] > key) {
                hi = index - 1;
            } 
            else if (this.keys[index] < key) {
                lo = index + 1;
            }
            else {
                return index;
            }
        }
        return -1;
    }
    
}
