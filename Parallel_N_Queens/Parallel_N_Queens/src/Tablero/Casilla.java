package Tablero;

import java.util.ArrayList;

/**
 *
 * @author matm2
 */
public class Casilla {
    public int ocupadoPor = -1;
    public ArrayList<Integer> bloqueadoPor;
    
    public Casilla() {
        bloqueadoPor = new ArrayList<>();
    }
}
