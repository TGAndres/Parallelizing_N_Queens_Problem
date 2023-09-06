package BackTrackParalelo;

import Tablero.Tablero;
import java.util.ArrayList;

/**
 *
 * @author matm2
 */
public class HiloCasilla extends Thread{
    int n;
    Tablero tablero;
    //fila y columna donde se agrega la reina por este "camino"
    int fila, columna;
    //booleano de si se llega a una solución por este "camino"
    boolean seLogro = false;

    public HiloCasilla(Tablero tablero, int fila, int columna) {
        //Copiar el estado del tablero forzando creación de nuevas referencias
        this.tablero = new Tablero(tablero.n);
        this.n = this.tablero.n;
        for(int i = 0; i<n; i++)
            for(int j = 0; j<n; j++){
                this.tablero.casillas[i][j].ocupadoPor = tablero.casillas[i][j].ocupadoPor;
                for(int k = 0; k<tablero.casillas[i][j].bloqueadoPor.size(); k++)
                    this.tablero.casillas[i][j].bloqueadoPor.add(tablero.casillas[i][j].bloqueadoPor.get(k));
            }
        this.fila = fila;
        this.columna = columna;
    }
    
    @Override
    public void run(){
        tablero.agregar(fila,columna);
        //Para las versiones completamente paralelas y mixtas
        /*if(columna == n-1)
            seLogro = true;
        else
            seLogro = probarColumnaParalela(columna+1);*/
        
        //Solo paralelizando la primer columna 
        //(que resultó ser lo más rápido en la mayoría de casos)
        seLogro = !probarColumnaSecuencial(columna+1);
    }
    
    public boolean probarColumnaParalela(int columna){
        //activeCount define qué tan mixta 
        //(entre paralela100% y solo primer columna) es la solución
        if(columna<n && activeCount()<30){
            int i;
            HiloCasilla[] hilos = new HiloCasilla[n];
            ArrayList<HiloCasilla> hilosReales = new ArrayList<>();
            
            //Checada rápida
            boolean columnaLlena = true;
            for(i = 0; i<n ; i++)
                if(tablero.casillas[i][columna].bloqueadoPor.isEmpty())
                    columnaLlena = false;
            //Si la columna no tenía ninguna casilla disponible
            if(columnaLlena)
                return false;
        
            //Crear e iniciar los hilos
            for(i = 0; i<n ; i++)
                if(tablero.casillas[i][columna].bloqueadoPor.isEmpty()){
                    hilos[i] = new HiloCasilla(tablero,i,columna);
                    hilosReales.add(hilos[i]);
                    hilos[i].start(); 
                }

            //Buscar al primer hilo en resolverlo
            while(true){
                //Ningún hilo lo logró
                if(hilosReales.isEmpty())
                    return false;
                i = (i+1)%hilosReales.size();
                if(hilosReales.get(i).seLogro){
                    tablero = hilosReales.get(i).tablero;
                    return true;
                }
                if(!hilosReales.get(i).isAlive() && !hilosReales.get(i).seLogro)
                    hilosReales.remove(i);
            }
        }else if(columna<n){
            //Si ya se alcanzó la cantidad máxima de hilos, se continua secuencialmente
            return !probarColumnaSecuencial(columna);    
        }else{
            return false;
        }
    }
    
    //Igual al probarColumna de la versión secuencial
    public boolean probarColumnaSecuencial(int columna){
        int i;
            boolean columnaLlena = true;
            boolean bloqueoIndirecto;
            for(i = 0; i<n ; i++){
                if(tablero.agregar(i, columna)){
                    columnaLlena = false;
                    if(columna<n-1){
                        bloqueoIndirecto = probarColumnaSecuencial(columna+1);
                        if(bloqueoIndirecto){
                            tablero.quitar(i, columna);
                            columnaLlena = true;
                        }
                    }
                }
            }
            return columnaLlena;
    }
}